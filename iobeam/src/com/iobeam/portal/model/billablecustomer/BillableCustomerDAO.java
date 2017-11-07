package com.iobeam.portal.model.billablecustomer;


import javax.sql.*;
import java.sql.*;
import java.util.*;
import com.iobeam.portal.util.*;
import com.iobeam.portal.model.customercontact.*;
import com.iobeam.portal.model.billing.*;
import com.iobeam.portal.model.customer.CustomerPK;


/**
*/
public class BillableCustomerDAO  {

	static final String SELECT_BY_ID =
			"select " +
			"customerid, " +
			"parentcustomerid, " +
			"billingcontactid, " +
			"paymentinstrumentid, " +
			"active " +
			"from customer " +
			"where customerid = ?";


	static final String DELETE_BY_ID =
			"delete from customer where " +
			"customerid = ?";


	static final String INSERT =
			"insert into customer (" +
			"customerid, " +
			"parentcustomerid, " +
			"billingcontactid, " +
			"paymentinstrumentid, " +
			"active" +
			") values " +
			"(?, ?, ?, ?, ?)";


	static final String UPDATE =
			"update customer set " +
			"parentcustomerid=?, " +
			"billingcontactid=?, " +
			"paymentinstrumentid=?, " +
			"active=? " +
			"where customerid = ?";


	/**
	* Returns the BillableCustomerData for the specified BillableCustomerPK,
	* or null if there is none.
	*/
	public static BillableCustomerData select(BillableCustomerPK pk)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;
		BillableCustomerData customerData = null;
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
				"select billable customer by id failed", sqle,
				SELECT_BY_ID, pk);
		}
		finally {
			DBHelper.close(conn, ps, rs);
		}

		return customerData;
	}


	public static boolean pkExists(BillableCustomerPK pk)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;
		BillableCustomerData customerData = null;
		ResultSet rs = null;

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(SELECT_BY_ID);
			ps.setLong(1, pk.getID());

			rs = ps.executeQuery();

			if (rs.next()) {
				customerData = createFromRS(rs, 1);

				if (customerData.getPaymentInstrument() != null) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"select billable customer by id failed", sqle,
				SELECT_BY_ID, pk);
		}
		finally {
			DBHelper.close(conn, ps, rs);
		}
	}


	static private BillableCustomerData createFromRS(ResultSet rs,
			int columnOffset)
			throws SQLException, DataAccessException {

		int i = columnOffset;

		BillableCustomerPK pk = new BillableCustomerPK(rs.getLong(i++));
		CustomerPK parentCustomerPK = null;
		long parentCustomerID = rs.getLong(i++);
		if (!rs.wasNull()) {
			parentCustomerPK = new CustomerPK(parentCustomerID);
		}
		CustomerContactPK customerContactPK =
				new CustomerContactPK(rs.getLong(i++));

		PaymentInstrumentPK paymentInstrumentPK =
				new PaymentInstrumentPK(rs.getLong(i++));
		boolean isActive = "Y".equalsIgnoreCase(rs.getString(i++));

		CustomerContact customerContact =
				CustomerContactDAO.select(customerContactPK);

		PaymentInstrument paymentInstrument =
				PaymentInstrumentDAO.select(paymentInstrumentPK);

		BillableCustomerData customerData = new BillableCustomerData(pk,
				parentCustomerPK, customerContact,
				paymentInstrument, isActive);

		return customerData;
	}


	/**
	* Delete the specified BillableCustomer.
	*/
	public static void delete(BillableCustomerData customerData)  
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
					"delete billable customer by id failed",
					DELETE_BY_ID, customerData.getPK());
			}

			// This is insufficient.  We must delete the
			// other customer contact information associated
			// with the underlying customer.
			//
			CustomerContactDAO.delete(
					customerData.getCustomerContact());

			PaymentInstrumentDAO.delete(
					customerData.getPaymentInstrument());
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"delete billable customer by id failed", sqle,
				DELETE_BY_ID, customerData.getPK());
		}
		finally {
			DBHelper.close(conn, ps, null);
		}
	}


	/**
	* Creates a BillableCustomer record from the specified
	* BillbaleCustomerData.
	*
	* Returns a BillableCustomerData instance with a valid
	* BillableCustomerPK.
	*
	* Throws IllegalStateException if the BillableCustomerData already has
	* a BillableCustomerPK.
	*/
	public static BillableCustomerData create(BillableCustomerData customerData)
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;

		if (customerData.getPK() != null) {
			throw new IllegalStateException("customerData already has pk " +
					customerData.getPK());
		}

		long key = BlindKey.getNextKey("CUSTOMER_SEQ");
		customerData.setPK(new BillableCustomerPK(key));


		if (customerData.getCustomerContact().getPK() == null) {
			CustomerContact customerContact = CustomerContactDAO.create(
					customerData.getCustomerContact());
			customerData.setCustomerContact(customerContact);
		}

		if (customerData.getPaymentInstrument().getPK() == null) {
			PaymentInstrument paymentInstrument = PaymentInstrumentDAO.create(
					customerData.getPaymentInstrument());
			customerData.setPaymentInstrument(paymentInstrument);
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
			ps.setLong(i++,
					customerData.getPaymentInstrument().getPK().getID());
			ps.setString(i++,
					customerData.isActive() ? "Y" : "N");

			int count = ps.executeUpdate();

			if (count <= 0) {
				throw new DataAccessException(
					"create billable customer failed",
					INSERT, customerData);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"create billable customer failed", sqle,
				INSERT, customerData);
		}
		finally {
			DBHelper.close(conn, ps, null);
		}

		return customerData;
	}



	/**
	* Updates the specified BillableCustomer record in the database.
	* A new CustomerContact record is created as needed (if the specified
	* CustomerContact does not have a pk).  Any previously used CustomerContact
	* is deleted if unreferenced.
	*
	* Returns a BillableCustomerData with valid PKs in dependent objects.
	*
	* Throws IllegalStateException if the specified BillableCustomerData
	* does not have a BillableCustomerPK.
	*/
	public static BillableCustomerData update(BillableCustomerData customerData)
			throws DataAccessException {

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
		}

		for (Iterator it = customerData.getDeletedCustomerContacts().iterator();
				it.hasNext(); ) {
			CustomerContact customerContact = (CustomerContact) it.next();
			CustomerContactDAO.delete(customerContact);
		}
		customerData.resetDeletedCustomerContacts();


		if (customerData.getPaymentInstrument().getPK() == null) {
			PaymentInstrument paymentInstrument = PaymentInstrumentDAO.create(
					customerData.getPaymentInstrument());
			customerData.setPaymentInstrument(paymentInstrument);
		}

		for (Iterator it =
				customerData.getDeletedPaymentInstruments().iterator();
				it.hasNext(); ) {
			PaymentInstrument paymentInstrument = (PaymentInstrument) it.next();
			PaymentInstrumentDAO.delete(paymentInstrument);
		}
		customerData.resetDeletedPaymentInstruments();



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
			ps.setLong(i++,
					customerData.getPaymentInstrument().getPK().getID());
			ps.setString(i++,
					customerData.isActive() ? "Y" : "N");
			ps.setLong(i++, customerData.getPK().getID());

			int count = ps.executeUpdate();

			if (count <= 0) {
				throw new DataAccessException(
					"update billable customer failed",
					UPDATE, customerData);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"update billable customer failed", sqle,
				UPDATE, customerData);
		}
		finally {
			DBHelper.close(conn, ps, null);
		}

		return customerData;
	}
}
