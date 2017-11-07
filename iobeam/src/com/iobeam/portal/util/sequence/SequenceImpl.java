package com.iobeam.portal.util.sequence;

import javax.ejb.*;
import java.rmi.*;
import javax.naming.*;
import java.sql.*;
import javax.sql.*;

import com.iobeam.portal.util.*;

public class SequenceImpl implements SessionBean {
	SessionContext mContext;

	private static final String GENERIC_SEQUENCE_TABLE = 
			"generic_sequence";

	private static final String DB_TYPE_PROP = 
			"iobeam.portal.db.type";

	public void setSessionContext( SessionContext context ) {
		mContext = context;
	}
	public void unsetSessionContext() {
		mContext = null;
	}

	public long getNextSequenceNumber(String seq) {
		String dbtype = System.getProperty(DB_TYPE_PROP);

		return getNextSequenceNumber(seq, dbtype);
	}

	public long getNextSequenceNumber(String targetSequence,
			String dbtype) {
		String sql = null;

		if (dbtype.equals(Sequence.DB_TYPE_ORACLE)) {
			sql = "select " + targetSequence + ".nextval from dual";
			return getSequenceNumber(targetSequence, sql);
		}
		else if (dbtype.equals(Sequence.DB_TYPE_PGSQL)) {
			sql = "select nextval('" + targetSequence + "')";
			return getSequenceNumber(targetSequence, sql);
		}
		else {
			return getNextGenericSequenceNumber();
		}
	}

	public long getNextGenericSequenceNumber() {
		
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		long seqNum = 0;
		String sql = "select currval from " + GENERIC_SEQUENCE_TABLE  + 
				" for update";

		try {

			connection = DBHelper.getConnection();

			ps = connection.prepareStatement(sql);

			ps.executeQuery();

			rs = ps.getResultSet();
			if (rs.next()) {
				seqNum = rs.getLong(1);
				ps.close();
				sql = "update " + GENERIC_SEQUENCE_TABLE + 
						" set currval = currval + 1";
				ps = connection.prepareStatement(sql);
				ps.executeUpdate();
			} else {
				throw new EJBException("no generic sequence number available.");
			}
		}
		catch (SQLException sqle) {
			throw new EJBException(sqle);
		}
		finally {
			DBHelper.close(connection, ps, rs);
		}
		return seqNum;

	}
	private long getSequenceNumber(
			String targetSequence, String sql) {
		
		Connection connection = null;
		PreparedStatement ps = null;
		PreparedStatement psUpdate = null;

		long seqNum = 0;

		try {

			connection = DBHelper.getConnection();

			ps = connection.prepareStatement(sql);

			ps.executeQuery();

			ResultSet rs = ps.getResultSet();
			if (rs.next()) {
				seqNum = rs.getLong(1);
			} else {
				throw new EJBException("no sequence number for " + targetSequence);
			}

			rs.close();
		}
		catch (SQLException sqle) {
			throw new EJBException(sqle);
		}
		finally {
			DBHelper.freeConnection( connection );
		}
		return seqNum;

	}

	

	public void ejbCreate() throws CreateException {
	}

	public void ejbActivate() {}
	public void ejbPassivate() {}
	public void ejbRemove() {}

}
