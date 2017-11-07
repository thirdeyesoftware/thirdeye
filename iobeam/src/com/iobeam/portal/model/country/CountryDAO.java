package com.iobeam.portal.model.country;


import javax.sql.*;
import java.sql.*;
import java.util.*;
import com.iobeam.portal.util.*;


/**
*/
public class CountryDAO  {

	static final String SELECT_BY_ID =
			"select countryid, country from country " +
			"where countryid = ?";

	static final String SELECT_ALL =
			"select countryid, country from country";


	/**
	* Returns the Country for the specified CountryPK, or null
	* if there is none.
	*/
	public static Country select(CountryPK pk)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;
		Country country = null;
		ResultSet rs = null;

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(SELECT_BY_ID);
			ps.setLong(1, pk.getID());

			rs = ps.executeQuery();

			if (rs.next()) {
				country = createFromRS(rs, 1);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"select country by id failed", sqle,
				SELECT_BY_ID, pk);
		}
		finally {
			DBHelper.close(conn, ps, rs);
		}

		return country;
	}


	/**
	* Returns a Collection of all Country instances known to system.
	*/
	public static Collection selectAll()  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;
		ResultSet rs = null;

		Collection c = new ArrayList();

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(SELECT_ALL);

			rs = ps.executeQuery();

			while (rs.next()) {
				c.add(createFromRS(rs, 1));
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"select country ALL failed", sqle,
				SELECT_ALL, null);
		}
		finally {
			DBHelper.close(conn, ps, rs);
		}

		return c;
	}


	static public Country createFromRS(ResultSet rs, int columnOffset)
			throws SQLException {

		int i = columnOffset;

		CountryPK pk = new CountryPK(rs.getLong(i++));
		String countryName = rs.getString(i++);

		return new Country(pk, countryName);
	}
}
