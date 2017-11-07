package com.iobeam.portal.security.rdbms.authenticator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.naming.*;
import java.util.logging.Logger;

import weblogic.management.utils.NotFoundException;

import com.iobeam.portal.security.*;
import com.iobeam.portal.util.DBHelper;

public final class PortalAuthenticatorDAO {

	private static Logger cLogger = 
			Logger.getLogger("com.iobeam.portal.security.rdbms.authenticator");
	
	private static final String SELECT_USER_BY_USER_NAME = 
		"select * from security_user where username = ? ";

	private static final String SELECT_USER_BY_CREDENTIALS = 
		"select * from security_user where username = ? and password = ?";

	private static final String SELECT_ALL_USERS =
		"select * from security_user ";

	private static final String INSERT_USER = 
		"insert into security_user (username, password) values(?,?)";

	private static final String INSERT_USER_GROUP = 
		"insert into security_user_group (username, groupname) values (?,?)";

	private static final String DELETE_USER_BY_USER_NAME = 
		"delete from security_user where username = ?";

	private static final String SELECT_ALL_GROUP_NAMES = 
		"select DISTINCT groupname from security_group";

	private static final String SELECT_GROUPS_BY_GROUP_NAME =
		"select * from security_group where parentgroupname = ?";

	private static final String DELETE_GROUP_FROM_GROUP = 
		"delete from security_group where groupname = ? and " + 
		"parentgroupname = ?";

	private static final String INSERT_GROUP = 
		"insert into security_group (" + 
		"securitygroupid, groupname, parentgroupname) values(" + 
		"security_group_seq.nextval, ?,?)";

	private static final String DELETE_GROUP_BY_GROUP_NAME = 
		"delete from security_group where groupname = ?";

	private static final String SELECT_GROUPS_BY_USER_NAME = 
		"select * from security_user_group where username = ?";

	public synchronized static boolean authenticate( String user, String password )
	 		throws NotFoundException {

      boolean exists = false;
      Connection con = null;
			PreparedStatement ps = null;
			ResultSet rs = null;

      try {
      	// Get a connection 
      	con = DBHelper.getConnection();
			ps = con.prepareStatement(SELECT_USER_BY_CREDENTIALS);
			ps.setString(1, user);
			ps.setString(2, password);

			rs = ps.executeQuery();

			if (rs.next()) {
				exists = true;
			}

      } catch ( Exception e ) {

         cLogger.info( "Error authenticating user: " + e);

      } finally {

			try {
				rs.close();
				ps.close();
	        	con.close();
			}
			catch (Exception e) { }

		}

		cLogger.info("Authenticating user " + user + " => " +
   		( exists ? "success" : "failure" ) );

    return exists;
   }

   /**
    *  Returns the security groups for the specified user.
    *
    */
	public synchronized static Enumeration selectUserGroups( String user ) {
		checkVal( user );
		Vector v = new Vector();
		cLogger.info("PortalAuthenticatorDAO.selectUserGroups()");

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

      try {
      	con = DBHelper.getConnection();
         
      	ps = con.prepareStatement(SELECT_GROUPS_BY_USER_NAME);

      	ps.setString(1, user);
				rs = ps.executeQuery();

         while ( rs.next() ) {
            v.add( rs.getString("groupname") );
         }

      } catch ( SQLException e ) {
         cLogger.info( "Error accessing database: " + e );
      } catch ( Exception ex ) {
         cLogger.info( "Error accessing database: " + ex );
      }
		finally {
			try {
				rs.close();
				ps.close();
				con.close();
			}
			catch (Exception e) { }
		}

      // Return an enumeration for the vector of groups
      return v.elements();
   }


   /**
    *  Determine if a specific user exists in the database
    *
    *@param  user  The name of the user to check for
    *@return       True, if the user exists
    */
   public synchronized static boolean userExists( String user ) {
      cLogger.info( "Checking for existance of user '" + user + "'." );

      Connection con = null;
      boolean exists = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
      try {

      	con = DBHelper.getConnection();

      	ps = con.prepareStatement(SELECT_USER_BY_USER_NAME);
				ps.setString(1, user);
      	rs = ps.executeQuery();
      	
			exists = rs.next();

      } catch ( SQLException e ) {
         cLogger.info( "Error checking for user existance: " + e);

      } catch ( Exception ex ) {
         cLogger.info( "Error checking for user existance: " + ex);
      
		} finally {
      	try {
				rs.close();
				ps.close();
				con.close();
			}
			catch (Exception ee) { }
		}

      return exists;
   }

   /**
    *  Check that the specified value is valid.
    *
    *@param  val  The value to be checked
    */
   private static void checkVal( String val ) {
      if ( val == null ) {
         throw new IllegalArgumentException( "value must not be null" );
      }
      if ( val.indexOf( "," ) >= 0 ) {
         throw new IllegalArgumentException( "value must not contain \",\":\"" + val + "\"" );
      }
      if ( val.length() != val.trim().length() ) {
         throw new IllegalArgumentException( "value must not begin or end with whitespace:\"" + val + "\"" );
      }
      if ( val.length() == 0 ) {
         throw new IllegalArgumentException( "value must not be empty" );
      }
   }

   /**
    *  This method will create a new group in the database
    *
    *@param  group         The name of the new group to add
    *@param  parentGroups  A list of groups that this group is a member of
    */
   private void createGroup( String group, String[] parentGroups ) {

      Connection c = null;
		PreparedStatement ps = null;
      try {

      	c = DBHelper.getConnection(); 
			ps = c.prepareStatement(INSERT_GROUP);
			ps.setString(1, group);
			ps.setNull(2, Types.VARCHAR);
			ps.executeUpdate();
			
			for (int i = 0; i < parentGroups.length; i++) { 
				ps.setString(1, group);
				ps.setString(2, parentGroups[i]);
	
				ps.executeUpdate();
			}

		}
		catch (SQLException sqle) {
			cLogger.info("error in creategroup: " + sqle.toString());
		}
		finally {
			try {
				ps.close();
				c.close();
			}
			catch (Exception e) { }
		}

   }

	/**
	 * delete a group by the specified name.
	 */
	public void deleteGroup(String groupname) {
		Connection c = null;
		PreparedStatement ps = null;
		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(DELETE_GROUP_BY_GROUP_NAME);
			ps.setString(1, groupname);
			ps.executeUpdate();
		}
		catch (SQLException sqle) { 
			cLogger.info("could not delete group: " + sqle);
		}
		finally {
			try {
				ps.close();
				c.close();
			}
			catch (Exception e) { }
		}
	}

   /**
    *  Create a user in the database
    *
    *@param  user          The username of the user to create
    *@param  password      The password for the new user
    *@param  parentGroups  A list of groups that the user should be a member of
    */
   public void createUser( String user, String password, String[] parentGroups ) {

		Connection c = null;
		PreparedStatement ps = null;

      try {

         // Retrieve a connection 
      	c = DBHelper.getConnection();
			ps = c.prepareStatement(INSERT_USER);
			ps.setString(1, user);
			ps.setString(2, password);
			ps.executeUpdate();

			ps.close();
			ps = c.prepareStatement(INSERT_USER_GROUP);
			
			if (parentGroups != null) { 
				for (int i = 0; i < parentGroups.length; i++) { 
					ps.setString(1, user);
					ps.setString(2, parentGroups[i]);
					ps.executeUpdate();
				}
			}

      } catch ( SQLException e ) {
         cLogger.info( "Error creating user: " + e);
      } catch ( Exception ex ) {
         cLogger.info( "Error creating user: " + ex);

      } finally {
         try {
				ps.close();
				c.close();
			}
			catch (Exception e) { }
      }

   }

	/**
	 * deletes a user.
	 */
	public void deleteUser(String username) {
		Connection c = null;
		PreparedStatement ps = null;
		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(DELETE_USER_BY_USER_NAME);
			ps.setString(1, username);
			ps.executeUpdate();
		}
		catch (SQLException sqle) { 
			cLogger.info("could not delete user: " + sqle);
		}
		finally {
			try {
				ps.close();
				c.close();
			}
			catch (Exception e) { }
		}
	}

	/**
	 * adds a list of groups to the specified user.
	 */
	public void addGroupToUser(String username, String[] groups) {
		Connection c = null;
		PreparedStatement ps = null;
		checkVal(username);
		if (groups == null ||
				groups.length == 0) {
			throw new IllegalArgumentException("groups");
		}
		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(INSERT_USER_GROUP);
			for (int i = 0; i < groups.length; i++) {
				ps.setString(1, username);
				ps.setString(2, groups[i]);
				ps.executeUpdate();
			}
		}
		catch (SQLException sqle) {
			cLogger.info("could not add group for user: " + sqle);
		}
		finally {
			try {
				ps.close();
				c.close();
			}
			catch (Exception e) { }
		}
	}

}   
