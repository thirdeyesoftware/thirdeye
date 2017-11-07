package com.iobeam.portal.model.gateway.usercontact;


import javax.sql.*;
import java.sql.*;
import java.util.*;
import java.net.InetAddress;
import com.iobeam.util.MACAddress;
import com.iobeam.portal.util.*;
import com.iobeam.portal.model.user.*;
import com.iobeam.portal.model.venue.*;


/**
*/
public class UserContactDAO  {

	static final String SELECT_BY_ID =
			"select " +
			"usercontactid, " +
			"userid, " +
			"user_contact.venueid, " +
			"ipaddress, " +
			"macaddress, " +
			"anonymous, " +
			"timestamp, " +
			"venuename " +
			"from user_contact, venue " +
			"where usercontactid = ? and " + 
			"venue.venueid = user_contact.venueid";

	static final String SELECT_BY_USER_ID_BY_DATE =
			"select " +
			"usercontactid, " +
			"userid, " +
			"user_contact.venueid, " +
			"ipaddress, " +
			"macaddress, " +
			"anonymous, " +
			"timestamp, " +
			"venuename " +
			"from user_contact, venue " +
			"where userid = ? and " +
			"(timestamp >= ? and timestamp < ?) and " +
			"venue.venueid = user_contact.venueid " + 
			"order by timestamp desc";

	static final String SELECT_BY_VENUE_ID_BY_DATE =
			"select " +
			"usercontactid, " +
			"userid, " +
			"user_contact.venueid, " +
			"ipaddress, " +
			"macaddress, " +
			"anonymous, " +
			"timestamp, " +
			"venuename " +
			"from user_contact, venue " +
			"where user_contact.venueid = ? and " +
			"(timestamp >= ? and timestamp < ?) and " +
			"venue.venueid = user_contact.venueid " + 
			"order by timestamp desc";

	static final String SELECT_ROLLUP_BY_VENUE_ID =
			"select " +
			"venueid, " +
			"to_char(timestamp, 'MM/dd/yyyy') dt, " +
			"count(macaddress) thecount from " + 
			"from user_contact " +
			"where venueid = ? " + 
			"group by to_char(timestamp, 'MM/dd/yyyy') " + 
			"order by to_char(timestamp, 'MM/dd/yyyy') asc";

	static final String SELECT_BY_USER_ID =
			"select " +
			"usercontactid, " +
			"userid, " +
			"user_contact.venueid, " +
			"ipaddress, " +
			"macaddress, " +
			"anonymous, " +
			"timestamp, " +
			"venuename " + 
			"from user_contact, venue " +
			"where userid = ? and " +
			"venue.venueid = user_contact.venueid " +
			"order by timestamp desc";

	static final String SELECT_ALL =
			"select " +
			"usercontactid, " +
			"userid, " +
			"user_contact.venueid, " +
			"ipaddress, " +
			"macaddress, " +
			"anonymous, " +
			"timestamp, " +
			"venuename " + 
			"from user_contact, venue where " +
			"venue.venueid = user_contact.venueid " +
			"order by timestamp desc";

	static final String SELECT_USAGE_MONTHS_BY_USER = 
			"select " +
			"distinct to_date(to_char(timestamp, 'Month, YYYY'), " + 
			"'Month, YYYY') month " +
			"from user_contact " + 
			"where userid = ? order by month desc";

	static final String DELETE_BY_ID =
			"delete from user_contact where " +
			"usercontactid = ?";


	static final String INSERT =
			"insert into user_contact (" +
			"usercontactid, " +
			"userid, " +
			"venueid, " +
			"ipaddress, " +
			"macaddress, " +
			"anonymous, " +
			"timestamp" +
			") values " +
			"(?, ?, ?, ?, ?, ?, ?)";


	static final String UPDATE =
			"update user_contact set " +
			"userid=?, " +
			"venueid=?, " +
			"ipaddress=?, " +
			"macaddress=?, " +
			"anonymous=?, " +
			"timestamp=? " +
			"where usercontactid = ?";



	/**
	* Returns the UserContact for the specified ContactID,
	* or null if there is none.
	*/
	public static UserContact select(ContactID contactID)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;
		UserContact userContact = null;
		ResultSet rs = null;

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(SELECT_BY_ID);
			ps.setLong(1, contactID.getID());

			rs = ps.executeQuery();

			if (rs.next()) {
				userContact = createFromRS(rs, 1);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"select usercontact by id failed", sqle,
				SELECT_BY_ID, contactID);
		}
		finally {
			DBHelper.close(conn, ps, rs);
		}

		return userContact;
	}


	/**
	* Returns the most recent UserContact for the specified UserPK,
	* or null if there is none.
	*/
	public static UserContact select(UserPK userPK)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;
		UserContact userContact = null;
		ResultSet rs = null;

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(SELECT_BY_USER_ID);
			ps.setLong(1, userPK.getID());

			rs = ps.executeQuery();

			if (rs.next()) {
				userContact = createFromRS(rs, 1);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"select usercontact by userid failed", sqle,
				SELECT_BY_USER_ID, userPK);
		}
		finally {
			DBHelper.close(conn, ps, rs);
		}

		return userContact;
	}

	public static Collection selectAllByVenueByDate(VenuePK venuePK,
			java.util.Date startdate, java.util.Date enddate) throws DataAccessException {
		
		PreparedStatement ps = null;
		Connection conn = null;
		UserContact userContact = null;
		ResultSet rs = null;
		Vector results = new Vector();

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(SELECT_BY_VENUE_ID_BY_DATE);
			ps.setLong(1, venuePK.getID());
			ps.setTimestamp(2, new Timestamp(startdate.getTime()));
			ps.setTimestamp(3, new Timestamp(enddate.getTime()));
			
			rs = ps.executeQuery();

			while (rs.next()) {
				userContact = createFromRS(rs, 1);
				results.addElement(userContact);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"select all usercontact by userid failed", sqle,
				SELECT_BY_VENUE_ID_BY_DATE, venuePK);
		}
		finally {
			DBHelper.close(conn, ps, rs);
		}

		return results;
	}

	public static Collection selectAllByDate(UserPK userPK, 
			java.util.Date startdate, java.util.Date enddate) throws DataAccessException {
		
		PreparedStatement ps = null;
		Connection conn = null;
		UserContact userContact = null;
		ResultSet rs = null;
		Vector results = new Vector();

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(SELECT_BY_USER_ID_BY_DATE);
			ps.setLong(1, userPK.getID());
			ps.setTimestamp(2, new Timestamp(startdate.getTime()));
			ps.setTimestamp(3, new Timestamp(enddate.getTime()));
			
			rs = ps.executeQuery();

			while (rs.next()) {
				userContact = createFromRS(rs, 1);
				results.addElement(userContact);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"select all usercontact by userid failed", sqle,
				SELECT_BY_USER_ID, userPK);
		}
		finally {
			DBHelper.close(conn, ps, rs);
		}

		return results;
	}

	public static Hashtable selectRollupByVenuePK(VenuePK pk)  
			throws DataAccessException {
		PreparedStatement ps = null;
		Connection conn = null;
		Hashtable hash = new Hashtable();
		ResultSet rs = null;

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(SELECT_ROLLUP_BY_VENUE_ID);
			ps.setLong(1, pk.getID());
			rs = ps.executeQuery();
			while (rs.next()) {
				hash.put(rs.getString("dt"), 
					Integer.valueOf(rs.getString("thecount")));
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException("could not retrieve rollup", sqle,
				SELECT_ROLLUP_BY_VENUE_ID, pk);
		}
		finally {
			DBHelper.close(conn, ps, rs);
		}
		return hash;

	}

	public static Collection selectAll(UserPK userPK) 
			throws DataAccessException {
		
		PreparedStatement ps = null;
		Connection conn = null;
		UserContact userContact = null;
		ResultSet rs = null;
		Vector results = new Vector();

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(SELECT_BY_USER_ID);
			ps.setLong(1, userPK.getID());

			rs = ps.executeQuery();

			while (rs.next()) {
				userContact = createFromRS(rs, 1);
				results.addElement(userContact);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"select all usercontact by userid failed", sqle,
				SELECT_BY_USER_ID, userPK);
		}
		finally {
			DBHelper.close(conn, ps, rs);
		}

		return results;
	}


	public static Collection selectAll() 
			throws DataAccessException {
		PreparedStatement ps = null;
		Connection conn = null;
		UserContact userContact = null;
		ResultSet rs = null;
		Vector results = new Vector();

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(SELECT_ALL);

			rs = ps.executeQuery();

			while (rs.next()) {
				userContact = createFromRS(rs, 1);
				results.addElement(userContact);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"select all usercontact failed", sqle,
				SELECT_ALL);
		}
		finally {
			DBHelper.close(conn, ps, rs);
		}

		return results;
	}

	/**
	 * returns a collection of distinct months for usage by 
	 * specified user.
	 */
	public static Collection selectUsageMonthsByUserPK(UserPK userpk) 
			throws DataAccessException {
		PreparedStatement ps = null;
		Connection conn = null;
		ResultSet rs = null;
		Vector results = new Vector();

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(SELECT_USAGE_MONTHS_BY_USER);
			ps.setLong(1, userpk.getID());
			rs = ps.executeQuery();

			while (rs.next()) {
				
				results.addElement(new
					java.util.Date(rs.getTimestamp("month").getTime()));

			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"select months by user usercontact failed", sqle,
				SELECT_USAGE_MONTHS_BY_USER);
		}
		finally {
			DBHelper.close(conn, ps, rs);
		}

		return results;
	}

	static private UserContact createFromRS(ResultSet rs, int columnOffset)
			throws SQLException, DataAccessException {

		int i = columnOffset;
		String venueName;

		ContactID contactID = new ContactID(rs.getLong(i++));
		UserPK userPK = new UserPK(rs.getLong(i++));
		if (rs.wasNull()) {
			userPK = null;
		}
		VenuePK venuePK = new VenuePK(rs.getLong(i++));

		InetAddress ipAddress = null;
		try {
			ipAddress = InetAddress.getByName(rs.getString(i++));
		}
		catch (Exception e) {
			throw new Error(e);
		}

		MACAddress macAddress = new MACAddress(rs.getString(i++));
		boolean isAnonymous = "Y".equalsIgnoreCase(rs.getString(i++));
		java.util.Date timestamp = rs.getTimestamp(i++);

		venueName = rs.getString(i++);

		UserContact userContact = new UserContact(contactID, venuePK,
				ipAddress, macAddress, isAnonymous, timestamp, userPK, venueName);

		return userContact;
	}


	/**
	* Delete the specified UserContact.
	*/
	public static void delete(UserContact userContact)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(DELETE_BY_ID);
			ps.setLong(1, userContact.getContactID().getID());

			int count = ps.executeUpdate();

			if (count == 0) {
				throw new DataNotFoundException(
					"delete usercontact by id failed",
					DELETE_BY_ID, userContact.getContactID());
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"delete usercontact by id failed", sqle,
				DELETE_BY_ID, userContact.getContactID());
		}
		finally {
			DBHelper.close(conn, ps, null);
		}
	}


	/**
	* Creates a UserContact record from the specified UserContact.
	*
	* Returns a UserContact instance with a valid ContactID.
	*
	* Throws IllegalStateException if the UserContact already has
	* a ContactID.
	*/
	public static UserContact create(UserContact userContact)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;

		if (userContact.getContactID() != null) {
			throw new IllegalStateException("userContact already has id " +
					userContact.getContactID());
		}

		long key = BlindKey.getNextKey("USER_CONTACT_SEQ");
		userContact.setContactID(new ContactID(key));

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(INSERT);

			int i = 1;

			ps.setLong(i++, userContact.getContactID().getID());
			if (!userContact.isAnonymous() &&
					userContact.getUserPK() != null) {
				ps.setLong(i++, userContact.getUserPK().getID());
			} else {
				ps.setNull(i++, Types.NUMERIC);
			}
			ps.setLong(i++, userContact.getVenuePK().getID());
			ps.setString(i++, userContact.getUserIPAddress().getHostAddress());
			ps.setString(i++, userContact.getUserMACAddress().toString());
			ps.setString(i++, userContact.isAnonymous() ? "Y" : "N");
			ps.setTimestamp(i++,
					new Timestamp(userContact.getTimestamp().getTime()));

			int count = ps.executeUpdate();

			if (count <= 0) {
				throw new DataAccessException(
					"create usercontact failed",
					INSERT, userContact);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"create usercontact failed", sqle,
				INSERT, userContact);
		}
		finally {
			DBHelper.close(conn, ps, null);
		}

		return userContact;
	}



	/**
	* Updates the specified UserContact record in the database.
	*
	* Throws IllegalStateException if the specified UserContact
	* does not have a ContactID.
	*/
	public static UserContact update(UserContact userContact)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;

		if (userContact.getContactID() == null) {
			throw new IllegalStateException("userContact has no ContactID " +
					userContact);
		}

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(UPDATE);

			int i = 1;

			if (!userContact.isAnonymous() &&
					userContact.getUserPK() != null) {
				ps.setLong(i++, userContact.getUserPK().getID());
			} else {
				ps.setNull(i++, Types.NUMERIC);
			}
			ps.setLong(i++, userContact.getVenuePK().getID());
			ps.setString(i++, userContact.getUserIPAddress().getHostAddress());
			ps.setString(i++, userContact.getUserMACAddress().toString());
			ps.setString(i++, userContact.isAnonymous() ? "Y" : "N");
			ps.setTimestamp(i++,
					new Timestamp(userContact.getTimestamp().getTime()));

			ps.setLong(i++, userContact.getContactID().getID());

			int count = ps.executeUpdate();

			if (count <= 0) {
				throw new DataAccessException(
					"update usercontact failed",
					UPDATE, userContact);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"update usercontact failed", sqle,
				UPDATE, userContact);
		}
		finally {
			DBHelper.close(conn, ps, null);
		}

		return userContact;
	}
}
