package com.iobeam.portal.util;


import javax.sql.*;
import java.sql.*;
import javax.ejb.*;
import javax.rmi.*;
import javax.naming.*;
import java.util.*;
import java.util.logging.*;


/**
* DBHelper contains utility methods to operate on the database, retrieve
* connections and free connections.
* @author	jblau
*/
public class DBHelper {

	public static final String SQL_DUPLICATE_KEY = "23000";
	
	public static final int INSERT_ACTION = 0;
	public static final int UPDATE_ACTION = 1;
	public static final int DELETE_ACTION = 2;

	
	public static InitialContext getInitialContext(String host, int port, String user,
			String password) throws Exception {
		Properties p = new Properties();
		p.setProperty(Context.INITIAL_CONTEXT_FACTORY,
				"weblogic.jndi.WLInitialContextFactory");
		p.setProperty(Context.PROVIDER_URL, "t3://" + host + ":" + port);

		p.setProperty("java.naming.security.principal", user);
		p.setProperty("java.naming.security.credentials", password);

		InitialContext ic = new InitialContext(p);

		return ic;
	}

	public static Connection getConnection(String host, int port, 
			String username, String password, String dsname) throws SQLException {

		InitialContext ic = null;
		try {
	  	ic =	getInitialContext(host, port, username, password);
		}
		catch (Exception ee) {
			ee.printStackTrace();
			return null;
		}

		return getConnection(ic,dsname);

	}

	public static Connection getConnection() throws SQLException {
		InitialContext ic = null;
		try {
			ic = new InitialContext();
		}
		catch (Exception ee) {
			throw new EJBException(ee);
		}
		return getConnection(ic, DataSources.PRIMARY_NAME);
	}

	private static Connection getConnection(InitialContext ic,
			String dsname) throws SQLException {
 		DataSource dataSource;

 		try {
 			dataSource = (DataSource)ic.lookup(dsname);
 		}
 		catch (NamingException ne) {
 			throw new EJBException(ne);
 		}
 		finally {
 			try {
 				if (ic!= null) {
 					ic.close();
 				}
 			}
 			catch (Exception e) {
 				throw new EJBException(e);
 			}
 		}
 		return dataSource.getConnection();
	}


	public static void freeConnection(Connection pconnection) {
		if (pconnection != null) {
			try {
				if (!pconnection.isClosed()) pconnection.close();
			}
			catch (SQLException sqle) {
				throw new EJBException(sqle.getMessage());
			}
		}
	}


	public static void cleanup(CallableStatement stmt, ResultSet rs) {
		try {
			stmt.close();
			rs.close();
		}
		catch (Exception e) {}
	}


	public static void close(Connection conn, Statement s, ResultSet rs) {
		Throwable throwable = null;

		if (rs != null) {
			try {
				rs.close();
			}
			catch (Throwable t) {
				Logger l = Logger.getLogger("com.iobeam.portal.util");
				l.throwing(DBHelper.class.getName(), "close resultset", t);

				throwable = t;
			}
		}

		if (s != null) {
			try {
				s.close();
			}
			catch (Throwable t) {
				Logger l = Logger.getLogger("com.iobeam.portal.util");
				l.throwing(DBHelper.class.getName(), "close statement", t);

				if (throwable == null) {
					throwable = t;
				}
			}
		}

		if (conn != null) {
			try {
				conn.close();
			}
			catch (Throwable t) {
				Logger l = Logger.getLogger("com.iobeam.portal.util");
				l.throwing(DBHelper.class.getName(), "close connection", t);

				if (throwable == null) {
					throwable = t;
				}
			}
		}

		if (throwable != null) {
			throw new RuntimeException(throwable);
		}
	}
}
