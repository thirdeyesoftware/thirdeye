package com.iobeam.portal.model.venue;


import javax.sql.*;
import java.sql.*;
import java.util.*;
import com.iobeam.portal.util.*;
import com.iobeam.portal.model.customer.CustomerPK;


/**
*/
public class VenueDAO  {

	static final String SELECT_BY_ID =
			"select " +
			"venueid, " +
			"venuename, " +
			"site_secure_random, " +
			"venuetypeid, " +
			"customerid, " +
			"redirecturl " +
			"from venue " +
			"where venueid = ?";

	static final String SELECT_BY_CUSTOMER =
			"select " +
			"venueid, " +
			"venuename, " +
			"site_secure_random, " +
			"venuetypeid, " +
			"customerid, " +
			"redirecturl " +
			"from venue " +
			"where customerid = ?";


	static final String SELECT_ALL_ID_NAME=
			"select " +
			"venueid, " +
			"venuename " +
			"from venue";


	static final String DELETE_BY_ID =
			"delete from venue where " +
			"venueid = ?";


	static final String INSERT =
			"insert into venue (" +
			"venueid, " +
			"venuename, " +
			"site_secure_random, venuetypeid, customerid" +
			") values " +
			"(?, ?, ?, ?, ?)";


	static final String UPDATE =
			"update venue set " +
			"venuename=?, " +
			"site_secure_random=?, " +
			"venuetypeid=?, " +
			"customerid=?, " +
			"redirecturl =? " +
			"where venueid = ?";


	static final String SELECT_VENUE_BY_CUSTOMER_ID =
			"select " +
			"venueid " +
			"from venue " +
			"where customerid = ?";


	static final String SELECT_VENUE_BY_NAME =
			"select " +
			"venueid " +
			"from venue " +
			"where venuename = ?";



	/**
	* Returns the VenueData for the specified VenuePK,
	* or null if there is none.
	*/
	public static VenueData select(VenuePK pk)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;
		VenueData venueData = null;
		ResultSet rs = null;

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(SELECT_BY_ID);
			ps.setLong(1, pk.getID());

			rs = ps.executeQuery();

			if (rs.next()) {
				venueData = createFromRS(rs, 1);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"select venue by id failed", sqle,
				SELECT_BY_ID, pk);
		}
		finally {
			DBHelper.close(conn, ps, rs);
		}

		return venueData;
	}


	/**
	* Returns the VenueData for the specified CustomerPK,
	* or null if there is none.
	*/
	public static VenueData select(CustomerPK pk)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;
		VenueData venueData = null;
		ResultSet rs = null;

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(SELECT_BY_CUSTOMER);
			ps.setLong(1, pk.getID());

			rs = ps.executeQuery();

			if (rs.next()) {
				venueData = createFromRS(rs, 1);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"select venue by customer failed", sqle,
				SELECT_BY_CUSTOMER, pk);
		}
		finally {
			DBHelper.close(conn, ps, rs);
		}

		return venueData;
	}



	static private VenueData createFromRS(ResultSet rs, int columnOffset)
			throws SQLException, DataAccessException {

		int i = columnOffset;

		VenuePK pk = new VenuePK(rs.getLong(i++));
		String venueName = rs.getString(i++);
		long siteKey = rs.getLong(i++);
		VenueType venueType = VenueType.getVenueType(rs.getInt(i++));
		CustomerPK customerPK = new CustomerPK(rs.getLong(i++));
		String url = rs.getString(i++);

		VenueData venueData = new VenueData(pk,
				venueName, customerPK, venueType, siteKey);
		venueData.setRedirectUrl(url);

		return venueData;
	}


	/**
	* Delete the specified Venue.
	*/
	public static void delete(VenueData venueData)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(DELETE_BY_ID);
			ps.setLong(1, venueData.getPK().getID());

			int count = ps.executeUpdate();

			if (count == 0) {
				throw new DataNotFoundException(
					"delete venue by id failed",
					DELETE_BY_ID, venueData.getPK());
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"delete venue by id failed", sqle,
				DELETE_BY_ID, venueData.getPK());
		}
		finally {
			DBHelper.close(conn, ps, null);
		}
	}


	/**
	* Creates a Venue record from the specified VenueData.
	*
	* Returns a VenueData instance with a valid VenuePK.
	*
	* Throws IllegalStateException if the VenueData already has
	* a VenuePK.
	*/
	public static VenueData create(VenueData venueData)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;

		if (venueData.getPK() != null) {
			throw new IllegalStateException("venueData already has pk " +
					venueData.getPK());
		}

		long key = BlindKey.getNextKey("VENUE_SEQ");
		venueData.setPK(new VenuePK(key));

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(INSERT);

			int i = 1;

			ps.setLong(i++, venueData.getPK().getID());
			ps.setString(i++, venueData.getVenueName());
			ps.setLong(i++, venueData.getSiteKey());
			ps.setInt(i++, venueData.getVenueType().getID());
			ps.setLong(i++, venueData.getCustomerPK().getID());

			int count = ps.executeUpdate();

			if (count <= 0) {
				throw new DataAccessException(
					"create venue failed",
					INSERT, venueData);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"create venue failed", sqle,
				INSERT, venueData);
		}
		finally {
			DBHelper.close(conn, ps, null);
		}

		return venueData;
	}



	/**
	* Updates the specified Venue record in the database.
	*
	* Throws IllegalStateException if the specified VenueData
	* does not have a VenuePK.
	*/
	public static VenueData update(VenueData venueData)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;

		if (venueData.getPK() == null) {
			throw new IllegalStateException("venueData has no pk " +
					venueData.getPK());
		}

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(UPDATE);

			int i = 1;

			ps.setString(i++, venueData.getVenueName());
			ps.setLong(i++, venueData.getSiteKey());
			ps.setInt(i++, venueData.getVenueType().getID());
			ps.setLong(i++, venueData.getCustomerPK().getID());
			ps.setLong(i++, venueData.getPK().getID());

			int count = ps.executeUpdate();

			if (count <= 0) {
				throw new DataAccessException(
					"update venue failed",
					UPDATE, venueData);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"update venue failed", sqle,
				UPDATE, venueData);
		}
		finally {
			DBHelper.close(conn, ps, null);
		}

		return venueData;
	}


	/**
	* Returns true if a Venue exists with the specified CustomerPK.
	*/
	public static boolean venueExists(CustomerPK customerPK)
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;
		ResultSet rs = null;

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(SELECT_VENUE_BY_CUSTOMER_ID);

			int i = 1;
			ps.setLong(i++, customerPK.getID());

			rs = ps.executeQuery();

			return rs.next();
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"select venue failed", sqle,
				SELECT_VENUE_BY_CUSTOMER_ID, customerPK);
		}
		finally {
			DBHelper.close(conn, ps, rs);
		}
	}


	/**
	* Returns true if a Venue exists with the specified venue name.
	*/
	public static boolean venueExists(String venueName)
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;
		ResultSet rs = null;

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(SELECT_VENUE_BY_NAME);

			int i = 1;
			ps.setString(i++, venueName);

			rs = ps.executeQuery();

			return rs.next();
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"select venue failed", sqle,
				SELECT_VENUE_BY_NAME, venueName);
		}
		finally {
			DBHelper.close(conn, ps, rs);
		}
	}


	/**
	* Returns a Map of VenuePKs by respective venueNames.
	*/
	public static Map selectNameMap()  throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;
		ResultSet rs = null;
		Map map = new Hashtable();

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(SELECT_ALL_ID_NAME);

			rs = ps.executeQuery();

			while (rs.next()) {
				int i = 1;
				VenuePK pk = new VenuePK(rs.getLong(i++));
				String venueName = rs.getString(i++);
				map.put(venueName, pk);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"select name map failed", sqle,
				SELECT_ALL_ID_NAME);
		}
		finally {
			DBHelper.close(conn, ps, rs);
		}

		return map;
	}
}
