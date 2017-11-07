package com.iobeam.portal.model.customernotice;

import javax.sql.*;
import java.sql.*;
import java.util.*;
import javax.naming.*;

import com.iobeam.portal.util.*;

public class CustomerNoticeDAO {

	private static final String SELECT_ALL = 
		"select * from customer_notice " + 
		" order by orderbit asc";

	private static final String SELECT_ALL_ACTIVE = 
		"select * from customer_notice " + 
		"where active = 'Y' order by orderbit asc";

	private static final String SELECT_ACTIVE_BY_VENUE_ID = 
		"select * from customer_notice " + 
		" where active = 'Y' and venueid = ? " + 
		" order by orderbit ASC";
	
	private static final String SELECT_BY_ID = 
		"select * from customer_notice where id = ? order by orderbit asc";

	public static CustomerNotice selectById(long id) 
			throws DataAccessException {

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		CustomerNotice notice = null;

		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(SELECT_BY_ID);
			ps.setLong(1, id);

			rs = ps.executeQuery();

			if (rs.next()) {
				notice = createFromRS(rs);
			}
		}
		catch (SQLException e) {
			throw new DataAccessException("could not find notice.", e,
					SELECT_BY_ID, Long.toString(id));
		}
		finally {
			DBHelper.close(c, ps, rs);
		}
		return notice;
	}
	
	public static Collection selectAll() throws DataAccessException {
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List list = new Vector();

		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(SELECT_ALL);

			rs = ps.executeQuery();

			while (rs.next()) {
				list.add(createFromRS(rs));
			}
		}
		catch (SQLException e) {
			throw new DataAccessException("could not find all notices", e,
					SELECT_ALL, null);
		}
		finally {
			DBHelper.close(c, ps, rs);
		}

		return list;
	}

	public static Collection selectAllActive() throws DataAccessException {
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List list = new Vector();

		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(SELECT_ALL_ACTIVE);

			rs = ps.executeQuery();

			while (rs.next()) {
				list.add(createFromRS(rs));
			}
		}
		catch (SQLException e) {
			throw new DataAccessException("could not find active notices", e,
					SELECT_ALL_ACTIVE, null);
		}
		finally {
			DBHelper.close(c, ps, rs);
		}

		return list;

	}


	public static Collection selectActiveByVenueId(long venueid) 
			throws DataAccessException {
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List list = new Vector();

		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(SELECT_ACTIVE_BY_VENUE_ID);
			ps.setLong(1, venueid);
			rs = ps.executeQuery();

			while (rs.next()) {
				list.add(createFromRS(rs));
			}
		}
		catch (SQLException e) {
			throw new DataAccessException("could not find active notices", e,
					SELECT_ACTIVE_BY_VENUE_ID, null);
		}
		finally {
			DBHelper.close(c, ps, rs);
		}

		return list;

	}

	public static void delete(CustomerNotice notice) 
			throws DataAccessException {
		throw new UnsupportedOperationException("no impl yet.");
	}

	public static void update(CustomerNotice notice) 
			throws DataAccessException {
		throw new UnsupportedOperationException("no impl yet.");
	}
		
	private static CustomerNotice createFromRS(ResultSet rs)
			throws SQLException {

		return new CustomerNotice(
				rs.getLong("customernoticeid"),
				rs.getLong("venueid"),
				rs.getString("notice"),
				rs.getString("active").equals("Y"),
				rs.getInt("orderbit"));

	}

}
