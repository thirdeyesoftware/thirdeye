package com.iobeam.portal.model.gateway;


import javax.sql.*;
import java.sql.*;
import java.util.*;
import java.net.InetAddress;
import com.iobeam.util.MACAddress;
import com.iobeam.portal.util.*;
import com.iobeam.portal.model.venue.*;


/**
*/
public class GatewayDAO  {

	static final String SELECT_BY_ID =
			"select " +
			"gatewayid, " +
			"venueid, " +
			"ipaddress, " +
			"notify_port, " +
			"macaddress, " +
			"privateipaddress, " +
			"timestamp " +
			"from gateway " +
			"where gatewayid = ?";

	static final String SELECT_BY_IP =
			"select " +
			"gatewayid, " +
			"venueid, " +
			"ipaddress, " +
			"notify_port, " +
			"macaddress, " +
			"privateipaddress, " +
			"timestamp " +
			"from gateway " +
			"where ipaddress = ?";

	static final String SELECT_BY_MAC =
			"select " +
			"gatewayid, " +
			"venueid, " +
			"ipaddress, " +
			"notify_port, " +
			"macaddress, " +
			"privateipaddress, " +
			"timestamp " +
			"from gateway " +
			"where macaddress = ?";

	static final String SELECT_BY_VENUE_ID =
			"select " +
			"gatewayid, " +
			"venueid, " +
			"ipaddress, " +
			"notify_port, " +
			"macaddress, " +
			"privateipaddress, " +
			"timestamp " +
			"from gateway " +
			"where venueid = ?";


	static final String DELETE_BY_ID =
			"delete from gateway where " +
			"gatewayid = ?";


	static final String DELETE_BY_VENUE_ID =
			"delete from gateway where " +
			"venueid = ?";


	static final String INSERT =
			"insert into gateway (" +
			"gatewayid, " +
			"venueid, " +
			"ipaddress, " +
			"notify_port, " +
			"macaddress, " +
			"privateipaddress, " +
			"timestamp" +
			") values " +
			"(?, ?, ?, ?, ?, ?,?)";


	static final String UPDATE =
			"update gateway set " +
			"venueid=?, " +
			"ipaddress=?, " +
			"notify_port=?, " +
			"macaddress=?, " +
			"privateipaddress=?, " +
			"timestamp=? " +
			"where gatewayid = ?";


	/**
	* Returns the Gateway for the specified IPAddress,
	* or null if there is none.
	*/
	public static Gateway select(InetAddress address)
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;
		Gateway gateway = null;
		ResultSet rs = null;

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(SELECT_BY_IP);
			ps.setString(1, address.getHostAddress());

			rs = ps.executeQuery();

			if (rs.next()) {
				gateway = createFromRS(rs, 1);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"select gateway by id failed", sqle,
				SELECT_BY_ID, address);
		}
		finally {
			DBHelper.close(conn, ps, rs);
		}

		return gateway;
	}

	/**
	* Returns the Gateway for the specified mac addr,
	* or null if there is none.
	*/
	public static Gateway select(MACAddress mac)
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;
		Gateway gateway = null;
		ResultSet rs = null;

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(SELECT_BY_MAC);
			ps.setString(1, mac.toString());

			rs = ps.executeQuery();

			if (rs.next()) {
				gateway = createFromRS(rs, 1);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"select gateway by mac failed", sqle,
				SELECT_BY_MAC, mac);
		}
		finally {
			DBHelper.close(conn, ps, rs);
		}

		return gateway;
	}

	/**
	* Returns the Gateway for the specified GatewayPK,
	* or null if there is none.
	*/
	public static Gateway select(GatewayPK pk)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;
		Gateway gateway = null;
		ResultSet rs = null;

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(SELECT_BY_ID);
			ps.setLong(1, pk.getID());

			rs = ps.executeQuery();

			if (rs.next()) {
				gateway = createFromRS(rs, 1);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"select gateway by id failed", sqle,
				SELECT_BY_ID, pk);
		}
		finally {
			DBHelper.close(conn, ps, rs);
		}

		return gateway;
	}

	

	/**
	* Returns the Gateway for the specified VenuePK,
	* or null if there is none.
	*/
	public static Gateway select(VenuePK venuePK)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;
		Gateway gateway = null;
		ResultSet rs = null;

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(SELECT_BY_VENUE_ID);
			ps.setLong(1, venuePK.getID());

			rs = ps.executeQuery();

			if (rs.next()) {
				gateway = createFromRS(rs, 1);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"select gateway by venueid failed", sqle,
				SELECT_BY_VENUE_ID, venuePK);
		}
		finally {
			DBHelper.close(conn, ps, rs);
		}

		return gateway;
	}



	static private Gateway createFromRS(ResultSet rs, int columnOffset)
			throws SQLException, DataAccessException {

		int i = columnOffset;

		GatewayPK pk = new GatewayPK(rs.getLong(i++));
		VenuePK venuePK = new VenuePK(rs.getLong(i++));

		InetAddress ipAddress = null;
		String ip = rs.getString(i++);
		if (ip != null) {
			try {
				ipAddress = InetAddress.getByName(ip);
			}
			catch (Exception e) {
				throw new Error(e);
			}
		}
		int notifyPort = rs.getInt(i++);

		MACAddress macAddress = new MACAddress(rs.getString(i++));

		InetAddress privateIPAddress = null;
		ip = rs.getString(i++);
		if (ip != null) {
			try {
				privateIPAddress = InetAddress.getByName(ip);
			}
			catch (Exception e) {
				throw new Error(e);
			}
		}
		java.util.Date timestamp = rs.getTimestamp(i++);

		Gateway gateway = new Gateway(pk, venuePK, macAddress,
				ipAddress, privateIPAddress, notifyPort, timestamp);

		return gateway;
	}


	/**
	* Delete the specified Gateway.
	*/
	public static void delete(GatewayPK gatewayPK)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(DELETE_BY_ID);
			ps.setLong(1, gatewayPK.getID());

			int count = ps.executeUpdate();

			if (count == 0) {
				throw new DataNotFoundException(
					"delete gateway by id failed",
					DELETE_BY_ID, gatewayPK);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"delete gateway by id failed", sqle,
				DELETE_BY_ID, gatewayPK);
		}
		finally {
			DBHelper.close(conn, ps, null);
		}
	}


	/**
	* Delete the Gateway or Gateways associated with the specified Venue.
	*/
	public static void delete(VenuePK venuePK)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(DELETE_BY_VENUE_ID);
			ps.setLong(1, venuePK.getID());

			int count = ps.executeUpdate();

			if (count == 0) {
				throw new DataNotFoundException(
					"delete gateway by id failed",
					DELETE_BY_VENUE_ID, venuePK);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"delete gateway by id failed", sqle,
				DELETE_BY_VENUE_ID, venuePK);
		}
		finally {
			DBHelper.close(conn, ps, null);
		}
	}


	/**
	* Creates a Gateway record from the specified Gateway.
	*
	* Returns a Gateway instance with a valid GatewayPK.
	*
	* Throws IllegalStateException if the Gateway already has
	* a GatewayPK.
	*/
	public static Gateway create(Gateway gateway)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;

		if (gateway.getPK() != null) {
			throw new IllegalStateException("gateway already has pk " +
					gateway.getPK());
		}

		long key = BlindKey.getNextKey("GATEWAY_SEQ");
		gateway.setPK(new GatewayPK(key));

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(INSERT);

			int i = 1;

			ps.setLong(i++, gateway.getPK().getID());
			ps.setLong(i++, gateway.getVenuePK().getID());
			if (gateway.getPublicIPAddress() != null) {
				ps.setString(i++,
						gateway.getPublicIPAddress().getHostAddress());
			} else {
				ps.setNull(i++, Types.VARCHAR);
			}
			ps.setInt(i++, gateway.getNotifyPort());
			ps.setString(i++, gateway.getMACAddress().toString());
			if (gateway.getPrivateIPAddress() != null) {
				ps.setString(i++,
						gateway.getPrivateIPAddress().getHostAddress());
			} else {
				ps.setNull(i++, Types.VARCHAR);
			}
			ps.setTimestamp(i++,
					new java.sql.Timestamp(gateway.getTimestamp().getTime()));

			int count = ps.executeUpdate();

			if (count <= 0) {
				throw new DataAccessException(
					"create gateway failed",
					INSERT, gateway);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"create gateway failed", sqle,
				INSERT, gateway);
		}
		finally {
			DBHelper.close(conn, ps, null);
		}

		return gateway;
	}



	/**
	* Updates the specified Gateway record in the database.
	*
	* Throws IllegalStateException if the specified Gateway
	* does not have a GatewayPK.
	*/
	public static Gateway update(Gateway gateway)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;

		if (gateway.getPK() == null) {
			throw new IllegalStateException("gateway has no pk " +
					gateway.getPK());
		}

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(UPDATE);

			int i = 1;

			ps.setLong(i++, gateway.getVenuePK().getID());
			if (gateway.getPublicIPAddress() != null) {
				ps.setString(i++,
						gateway.getPublicIPAddress().getHostAddress());
			} else {
				ps.setNull(i++, Types.VARCHAR);
			}
			ps.setInt(i++, gateway.getNotifyPort());
			ps.setString(i++, gateway.getMACAddress().toString());
			if (gateway.getPrivateIPAddress() != null) {
				ps.setString(i++,
						gateway.getPrivateIPAddress().getHostAddress());
			} else {
				ps.setNull(i++, Types.VARCHAR);
			}
			ps.setTimestamp(i++,
					new java.sql.Timestamp(gateway.getTimestamp().getTime()));

			ps.setLong(i++, gateway.getPK().getID());

			int count = ps.executeUpdate();

			if (count <= 0) {
				throw new DataAccessException(
					"update gateway failed",
					UPDATE, gateway);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"update gateway failed", sqle,
				UPDATE, gateway);
		}
		finally {
			DBHelper.close(conn, ps, null);
		}

		return gateway;
	}


	/**
	* Returns true if a Gateway exists with the specified VenuePK.
	*/
	public static boolean gatewayExists(VenuePK venuePK)
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;
		ResultSet rs = null;

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(SELECT_BY_VENUE_ID);

			int i = 1;
			ps.setLong(i++, venuePK.getID());

			rs = ps.executeQuery();

			return rs.next();
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"select gateway failed", sqle,
				SELECT_BY_VENUE_ID, venuePK);
		}
		finally {
			DBHelper.close(conn, ps, rs);
		}
	}
}
