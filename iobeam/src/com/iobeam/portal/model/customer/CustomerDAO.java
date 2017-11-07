package com.iobeam.portal.model.customer;


import javax.sql.*;
import java.sql.*;
import java.util.logging.Logger;
import java.util.*;
import com.iobeam.portal.util.*;
import com.iobeam.portal.model.customercontact.*;


/**
*/
public class CustomerDAO  {

	static final String SELECT_BY_ID =
			"select " +
			"customerid, " +
			"parentcustomerid, " +
			"contactid, " +
			"active," + 
			"createdate " +
			"from customer " +
			"where customerid = ?";


	static final String DELETE_BY_ID =
			"delete from customer where " +
			"customerid = ?";


	static final String INSERT =
			"insert into customer (" +
			"customerid, " +
			"parentcustomerid, " +
			"contactid, active, createdate" +
			") values " +
			"(?, ?, ?, ?, ?)";


	static final String UPDATE =
			"update customer set " +
			"parentcustomerid=?, " +
			"contactid=?, " +
			"active=?, createdate = ? " +
			"where customerid = ?";


	/**
	* Returns the CustomerData for the specified CustomerPK,
	* or null if there is none.
	*/
	public static CustomerData select(CustomerPK pk)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;
		CustomerData customerData = null;
		ResultSet rs = null;

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(SELECT_BY_ID);
			ps.setLong(1, pk.getID());

			rs = ps.executeQuery();

			if (rs.next()) {
				customerData = createFromRS(rs, 1);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"select customer by id failed", sqle,
				SELECT_BY_ID, pk);
		}
		finally {
			DBHelper.close(conn, ps, rs);
		}

		return customerData;
	}


	public static boolean pkExists(CustomerPK pk)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;
		ResultSet rs = null;

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(SELECT_BY_ID);
			ps.setLong(1, pk.getID());

			rs = ps.executeQuery();

			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"select customer by id failed", sqle,
				SELECT_BY_ID, pk);
		}
		finally {
			DBHelper.close(conn, ps, rs);
		}
	}


	static private CustomerData createFromRS(ResultSet rs, int columnOffset)
			throws SQLException, DataAccessException {

		int i = columnOffset;

		CustomerPK pk = new CustomerPK(rs.getLong(i++));
		CustomerPK parentCustomerPK = null;
		long parentCustomerID = rs.getLong(i++);
		if (!rs.wasNull()) {
			parentCustomerPK = new CustomerPK(parentCustomerID);
		}
		CustomerContactPK customerContactPK =
				new CustomerContactPK(rs.getLong(i++));
		boolean isActive = "Y".equalsIgnoreCase(rs.getString(i++));

		java.util.Date createDate = new
			java.util.Date(rs.getTimestamp(i++).getTime());

		CustomerContact customerContact =
				CustomerContactDAO.select(customerContactPK);

		CustomerData customerData = new CustomerData(pk,
				parentCustomerPK, customerContact, isActive, createDate);

		return customerData;
	}


	/**
	* Delete the specified Customer.
	*/
	public static void delete(CustomerData customerData)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(DELETE_BY_ID);
			ps.setLong(1, customerData.getPK().getID());

			int count = ps.executeUpdate();

			if (count == 0) {
				throw new DataNotFoundException(
					"delete customer by id failed",
					DELETE_BY_ID, customerData.getPK());
			}

			CustomerContactDAO.delete(
					customerData.getCustomerContact());
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"delete customer by id failed", sqle,
				DELETE_BY_ID, customerData.getPK());
		}
		finally {
			DBHelper.close(conn, ps, null);
		}
	}


	/**
	* Creates a Customer record from the specified CustomerData.
	*
	* Returns a CustomerData instance with a valid CustomerPK.
	*
	* Throws IllegalStateException if the CustomerData already has
	* a CustomerPK.
	*/
	public static CustomerData create(CustomerData customerData)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;

		if (customerData.getPK() != null) {
			throw new IllegalStateException("customerData already has pk " +
					customerData.getPK());
		}

		long key = BlindKey.getNextKey("CUSTOMER_SEQ");
		customerData.setPK(new CustomerPK(key));


		if (customerData.getCustomerContact().getPK() == null) {
			CustomerContact customerContact = CustomerContactDAO.create(
					customerData.getCustomerContact());
			customerData.setCustomerContact(customerContact);
		}


		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(INSERT);

			int i = 1;

			ps.setLong(i++, customerData.getPK().getID());
			CustomerPK parentCustomerPK = customerData.getParentCustomerPK();
			if (parentCustomerPK != null) {
				ps.setLong(i++, parentCustomerPK.getID());
			} else {
				ps.setNull(i++, Types.NUMERIC);
			}
			ps.setLong(i++,
					customerData.getCustomerContact().getPK().getID());
			ps.setString(i++,
					customerData.isActive() ? "Y" : "N");

			java.util.Date date = new java.util.Date();
			ps.setTimestamp(i++,
					new Timestamp(date.getTime()));
			customerData.setCreateDate(date);
			
			int count = ps.executeUpdate();

			if (count <= 0) {
				throw new DataAccessException(
					"create customer failed",
					INSERT, customerData);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"create customer failed", sqle,
				INSERT, customerData);
		}
		finally {
			DBHelper.close(conn, ps, null);
		}

		return customerData;
	}



	/**
	* Updates the specified Customer record in the database.
	* A new CustomerContact record is created as needed (if the specified
	* CustomerContact does not have a pk).  Any previously used CustomerContact
	* is deleted if unreferenced.
	*
	* Returns a CustomerData with valid PKs in dependent objects.
	*
	* Throws IllegalStateException if the specified CustomerData
	* does not have a CustomerPK.
	*/
	public static CustomerData update(CustomerData customerData)  
			throws DataAccessException {

		Logger l = Logger.getLogger("com.iobeam.portal.model.customer");
		l.info("CustomerDAO.update():");
		PreparedStatement ps = null;
		Connection conn = null;

		if (customerData.getPK() == null) {
			throw new IllegalStateException("customerData has no pk " +
					customerData.getPK());
		}

		if (customerData.getCustomerContact().getPK() == null) {
			CustomerContact customerContact = CustomerContactDAO.create(
					customerData.getCustomerContact());
			customerData.setCustomerContact(customerContact);
		} else {
			CustomerContactDAO.update(customerData.getCustomerContact());
		}

		for (Iterator it = customerData.getDeletedCustomerContacts().iterator();
				it.hasNext(); ) {
			CustomerContact customerContact = (CustomerContact) it.next();
			CustomerContactDAO.delete(customerContact);
		}
		customerData.resetDeletedCustomerContacts();


		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(UPDATE);

			int i = 1;

			CustomerPK parentCustomerPK = customerData.getParentCustomerPK();
			if (parentCustomerPK != null) {
				ps.setLong(i++, parentCustomerPK.getID());
			} else {
				ps.setNull(i++, Types.NUMERIC);
			}
			ps.setLong(i++, customerData.getCustomerContact().getPK().getID());
			ps.setString(i++,
					customerData.isActive() ? "Y" : "N");

			ps.setTimestamp(i++, new
				Timestamp(customerData.getCreateDate().getTime()));

			ps.setLong(i++, customerData.getPK().getID());

			int count = ps.executeUpdate();

			if (count <= 0) {
				throw new DataAccessException(
					"update customer failed",
					UPDATE, customerData);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"update customer failed", sqle,
				UPDATE, customerData);
		}
		finally {
			DBHelper.close(conn, ps, null);
		}

		return customerData;
	}
}
