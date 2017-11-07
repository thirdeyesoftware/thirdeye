package com.iobeam.portal.model.address;


import javax.sql.*;
import java.sql.*;
import java.util.*;
import com.iobeam.portal.util.*;
import com.iobeam.portal.model.country.*;


/**
*/
public class AddressDAO  {

	static final String SELECT_BY_ID =
			"select " +
			"a.addressid, a.address1, a.address2, a.city, a.state, " +
			"a.zipcode, " +
			"a.countryid, b.country " +
			"from address a, country b " +
			"where " +
			"a.addressid = ? and " +
			"a.countryid = b.countryid";


	static final String DELETE_BY_ID =
			"delete from address where " +
			"addressid = ?";


	static final String INSERT =
			"insert into address (" +
			"addressid, address1, address2, city, state, " +
			"zipcode, countryid " +
			") values " +
			"(?, ?, ?, ?, ?, ?, ?)";

	static final String UPDATE = 
			"update address set address1 = ?, " +
			"address2 = ?, city = ?, state = ?, zipcode = ?, " + 
			"countryid = ? where addressid = ?";


	/**
	* Returns the Address for the specified AddressPK, or null
	* if there is none.
	*/
	public static Address select(AddressPK pk)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;
		Address address = null;
		ResultSet rs = null;

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(SELECT_BY_ID);
			ps.setLong(1, pk.getID());

			rs = ps.executeQuery();

			if (rs.next()) {
				address = createFromRS(rs, 1);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"select address by id failed", sqle,
				SELECT_BY_ID, pk);
		}
		finally {
			DBHelper.close(conn, ps, rs);
		}

		return address;
	}

	/**
	 * updates address record.
	 */
	public static void update(Address address) throws DataAccessException {
		
		PreparedStatement ps = null;
		Connection conn = null;
		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(UPDATE);
			ps.setString(1, address.getMailingAddress().getLine1());
			ps.setString(2, address.getMailingAddress().getLine2());
			ps.setString(3, address.getMailingAddress().getCity());
			ps.setString(4, address.getMailingAddress().getState());
			ps.setString(5, address.getMailingAddress().getZipcode());
			ps.setLong(6, address.getCountry().getPK().getID());
			ps.setLong(7, address.getPK().getID());
			if (ps.executeUpdate() == 0) { 
				throw new DataAccessException("unable to update address",
					UPDATE, address);
			}

		}
		catch (SQLException sqle) { 
			throw new DataAccessException("unable to update address",
				sqle, UPDATE, address);
		}
		finally {
			try {
				ps.close();
				conn.close();
			}
			catch (Exception ee) { }
		}
	}

	static public Address createFromRS(ResultSet rs, int columnOffset)
			throws SQLException {

		int i = columnOffset;

		AddressPK pk = new AddressPK(rs.getLong(i++));
		String line1 = rs.getString(i++);
		String line2 = rs.getString(i++);
		String city = rs.getString(i++);
		String state = rs.getString(i++);
		String zip = rs.getString(i++);

		Country country = CountryDAO.createFromRS(rs, i);

		return new Address(pk,
				new MailingAddress(line1, line2, city, state, zip),
				country);
	}


	/**
	* Delete the specified Address, so long as it is unreferenced.
	* Does nothing if an integrity constraint violation occurs.
	*/
	public static void delete(AddressPK pk)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(DELETE_BY_ID);
			ps.setLong(1, pk.getID());

			int count = ps.executeUpdate();

			if (count == 0) {
				throw new DataNotFoundException(
					"delete address by id failed",
					DELETE_BY_ID, pk);
			}
		}
		catch (SQLException sqle) {
			// Ignore integrity constraint violation, and
			// just do nothing.
			//
			if (sqle.getErrorCode() != 2291) {
				throw new DataAccessException(
					"delete address by id failed", sqle,
					DELETE_BY_ID, pk);
			}
		}
		finally {
			DBHelper.close(conn, ps, null);
		}
	}


	/**
	* Creates an Address from the specified Address.
	*
	* Returns an Address instance with a valid AddressPK.
	*
	* Throws IllegalStateException if the Address already has
	* an AddressPK.
	*/
	public static Address create(Address address)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;

		if (address.getPK() != null) {
			throw new IllegalStateException("address already has pk " +
					address.getPK());
		}

		long key = BlindKey.getNextKey("ADDRESS_SEQ");
		address.setPK(new AddressPK(key));

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(INSERT);

			int i = 1;

			ps.setLong(i++, address.getPK().getID());
			ps.setString(i++, address.getMailingAddress().getLine1());
			ps.setString(i++, address.getMailingAddress().getLine2());
			ps.setString(i++, address.getMailingAddress().getCity());
			ps.setString(i++, address.getMailingAddress().getState());
			ps.setString(i++, address.getMailingAddress().getZipcode());
			ps.setLong(i++, address.getCountry().getPK().getID());

			int count = ps.executeUpdate();

			if (count <= 0) {
				throw new DataAccessException(
					"create address failed",
					INSERT, address);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"create address failed", sqle,
				INSERT, address);
		}
		finally {
			DBHelper.close(conn, ps, null);
		}

		return address;
	}
}
