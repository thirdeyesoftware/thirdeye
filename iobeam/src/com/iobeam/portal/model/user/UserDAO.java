package com.iobeam.portal.model.user;


import javax.sql.*;
import java.sql.*;
import java.util.*;
import com.iobeam.portal.util.*;
import com.iobeam.portal.model.customer.CustomerPK;


/**
*/
public class UserDAO  {

	static final String SELECT_BY_ID =
			"select " +
			"userid, " +
			"username, " +
			"passwordhash, " +
			"passwordsalt, " +
			"customerid, " +
			"passwordreminderanswer " +
			"from users " +
			"where userid = ?";

	static final String SELECT_BY_NAME =
			"select " +
			"userid, " +
			"username, " +
			"passwordhash, " +
			"passwordsalt, " +
			"customerid, " +
			"passwordreminderanswer " +
			"from users " +
			"where username = ?";


	static final String DELETE_BY_ID =
			"delete from users where " +
			"userid = ?";


	static final String INSERT =
			"insert into users (" +
			"userid, " +
			"username, " +
			"passwordhash, passwordsalt, customerid, passwordreminderanswer" +
			") values " +
			"(?, ?, ?, ?, ?, ?)";


	static final String UPDATE =
			"update users set " +
			"username=?, " + 
			"passwordhash=?, " +
			"passwordsalt=?, " +
			"customerid=?, " + 
			"passwordreminderanswer=? " +
			"where userid = ?";


	static final String SELECT_USER_BY_CUSTOMER_ID =
			"select " +
			"userid, " +
			"username, " +
			"passwordhash, " +
			"passwordsalt, " +
			"customerid, " +
			"passwordreminderanswer " +
			"from users " +
			"where customerid = ?";


	static final String SELECT_USER_BY_REMINDER_INFO = 
			"select " + 
			"u.userid, " +
			"u.username, " +
			"u.passwordhash, " +
			"u.passwordsalt, " +
			"u.customerid, " +
			"u.passwordreminderanswer " +
			"from users u, customer c, customer_contact cc, address a " +
			"where u.customerid = c.customerid and c.contactid = " + 
			"cc.customercontactid and cc.addressid = a.addressid and " + 
			"lower(a.city) = ? and lower(a.state) = ? and a.zipcode = ? and " + 
			"lower(cc.emailaddress) = ? and lower(u.passwordreminderanswer) = ?";


	/**
	* Returns the UserData for the specified UserPK,
	* or null if there is none.
	*/
	public static UserData select(UserPK pk)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;
		UserData userData = null;
		ResultSet rs = null;

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(SELECT_BY_ID);
			ps.setLong(1, pk.getID());

			rs = ps.executeQuery();

			if (rs.next()) {
				userData = createFromRS(rs, 1);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"select user by id failed", sqle,
				SELECT_BY_ID, pk);
		}
		finally {
			DBHelper.close(conn, ps, rs);
		}

		return userData;
	}


	/**
	* Returns the UserData for the specified CustomerPK,
	* or null if there is none.
	*/
	public static UserData select(CustomerPK customerPK)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;
		UserData userData = null;
		ResultSet rs = null;

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(SELECT_USER_BY_CUSTOMER_ID);
			ps.setLong(1, customerPK.getID());

			rs = ps.executeQuery();

			if (rs.next()) {
				userData = createFromRS(rs, 1);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"select user by customerID failed", sqle,
				SELECT_USER_BY_CUSTOMER_ID, customerPK);
		}
		finally {
			DBHelper.close(conn, ps, rs);
		}

		return userData;
	}


	/**
	* Returns the UserData for the specified user name,
	* or null if there is none.
	*/
	public static UserData select(String userName)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;
		UserData userData = null;
		ResultSet rs = null;

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(SELECT_BY_NAME);
			ps.setString(1, userName);

			rs = ps.executeQuery();

			if (rs.next()) {
				userData = createFromRS(rs, 1);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"select user by name failed", sqle,
				SELECT_BY_NAME, userName);
		}
		finally {
			DBHelper.close(conn, ps, rs);
		}

		return userData;
	}

	/**
	* Returns the UserData for the specified email address, 
	* city, state, zipcode and reminder answer.
	* or null if there is none.
	*/
	public static UserData selectByReminderInfo(String email,
			String city, String state, String zipcode,
			String answer) throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;
		UserData userData = null;
		ResultSet rs = null;

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(SELECT_USER_BY_REMINDER_INFO);
			ps.setString(1, city);
			ps.setString(2, state);
			ps.setString(3, zipcode);
			ps.setString(4, email);
			ps.setString(5, answer);

			rs = ps.executeQuery();

			if (rs.next()) {
				userData = createFromRS(rs, 1);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"select user by name failed", sqle,
				SELECT_USER_BY_REMINDER_INFO, email);
		}
		finally {
			DBHelper.close(conn, ps, rs);
		}

		return userData;
	}


	static private UserData createFromRS(ResultSet rs, int columnOffset)
			throws SQLException, DataAccessException {

		int i = columnOffset;

		UserPK pk = new UserPK(rs.getLong(i++));
		String userName = rs.getString(i++);
		String passwordHash = rs.getString(i++);
		long salt = rs.getLong(i++);

		CustomerPK customerPK = new CustomerPK(rs.getLong(i++));
		
		String answer = rs.getString(i++);

		UserData userData = new UserData(pk,
				userName, passwordHash, salt, customerPK,
				answer);

		return userData;
	}


	/**
	* Delete the specified User.
	*/
	public static void delete(UserData userData)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(DELETE_BY_ID);
			ps.setLong(1, userData.getPK().getID());

			int count = ps.executeUpdate();

			if (count == 0) {
				throw new DataNotFoundException(
					"delete user by id failed",
					DELETE_BY_ID, userData.getPK());
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"delete user by id failed", sqle,
				DELETE_BY_ID, userData.getPK());
		}
		finally {
			DBHelper.close(conn, ps, null);
		}
	}


	/**
	* Creates a User record from the specified UserData.
	*
	* Returns a UserData instance with a valid UserPK.
	*
	* Throws IllegalStateException if the UserData already has
	* a UserPK.
	*/
	public static UserData create(UserData userData)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;

		if (userData.getPK() != null) {
			throw new IllegalStateException("userData already has pk " +
					userData.getPK());
		}

		long key = BlindKey.getNextKey("USER_SEQ");
		userData.setPK(new UserPK(key));

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(INSERT);

			int i = 1;

			ps.setLong(i++, userData.getPK().getID());
			ps.setString(i++, userData.getUserName());
			ps.setString(i++, userData.getPasswordHash());
			ps.setLong(i++, userData.getPasswordSalt());
			ps.setLong(i++, userData.getCustomerPK().getID());
			ps.setString(i++, userData.getPasswordReminderAnswer());
			int count = ps.executeUpdate();

			if (count <= 0) {
				throw new DataAccessException(
					"create user failed",
					INSERT, userData);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"create user failed", sqle,
				INSERT, userData);
		}
		finally {
			DBHelper.close(conn, ps, null);
		}

		return userData;
	}



	/**
	* Updates the specified User record in the database.
	*
	* Throws IllegalStateException if the specified UserData
	* does not have a UserPK.
	*/
	public static UserData update(UserData userData)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;

		if (userData.getPK() == null) {
			throw new IllegalStateException("userData has no pk " +
					userData.getPK());
		}

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(UPDATE);

			int i = 1;

			ps.setString(i++, userData.getUserName());
			ps.setString(i++, userData.getPasswordHash());
			ps.setLong(i++, userData.getPasswordSalt());
			ps.setLong(i++, userData.getCustomerPK().getID());
			ps.setString(i++, userData.getPasswordReminderAnswer());
			ps.setLong(i++, userData.getPK().getID());

			int count = ps.executeUpdate();

			if (count <= 0) {
				throw new DataAccessException(
					"update user failed",
					UPDATE, userData);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"update user failed", sqle,
				UPDATE, userData);
		}
		finally {
			DBHelper.close(conn, ps, null);
		}

		return userData;
	}


	/**
	* Returns true if a User exists with the specified CustomerPK.
	*/
	public static boolean userExists(CustomerPK customerPK)
			throws DataAccessException {
		PreparedStatement ps = null;
		Connection conn = null;
		ResultSet rs = null;

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(SELECT_USER_BY_CUSTOMER_ID);

			int i = 1;
			ps.setLong(i++, customerPK.getID());

			rs = ps.executeQuery();

			return rs.next();
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"select user failed", sqle,
				SELECT_USER_BY_CUSTOMER_ID, customerPK);
		}
		finally {
			DBHelper.close(conn, ps, rs);
		}
	}


	/**
	* Returns true if a User exists with the specified userName.
	*/
	public static boolean userExists(String userName)
			throws DataAccessException {
		PreparedStatement ps = null;
		Connection conn = null;
		ResultSet rs = null;

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(SELECT_BY_NAME);

			int i = 1;
			ps.setString(i++, userName);

			rs = ps.executeQuery();

			return rs.next();
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"select user failed", sqle,
				SELECT_BY_NAME, userName);
		}
		finally {
			DBHelper.close(conn, ps, rs);
		}
	}
}
