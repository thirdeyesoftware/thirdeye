package com.iobeam.portal.model.customercontact;


import javax.sql.*;
import java.sql.*;
import java.util.*;
import java.util.logging.Logger;
import com.iobeam.portal.util.*;
import com.iobeam.portal.model.address.*;
import com.iobeam.portal.model.billing.BillingDeliveryType;


/**
*/
public class CustomerContactDAO  {

	static final String SELECT_BY_ID =
			"select " +
			"a.customercontactid, " +
			"a.first_name, a.middle_initial, a.last_name, " +
			"a.phonenumber, a.faxnumber, a.emailaddress, " +
			"a.primarybillingdeliveryid, " +
			"a.addressid, b.address1, b.address2, b.city, b.state, " +
			"b.zipcode, " +
			"b.countryid, c.country " +
			"from customer_contact a, address b, country c " +
			"where " +
			"a.customercontactid = ? and " +
			"a.addressid = b.addressid and " +
			"b.countryid = c.countryid";


	static final String DELETE_BY_ID =
			"delete from customer_contact where " +
			"customercontactid = ?";


	static final String INSERT =
			"insert into customer_contact (" +
			"customercontactid, " +
			"first_name, middle_initial, last_name, phonenumber, faxnumber, " +
			"emailaddress, addressid, primarybillingdeliveryid " +
			") values " +
			"(?, ?, ?, ?, ?, ?, ?, ?, ?)";


	static final String UPDATE =
			"update customer_contact set " +
			"first_name=?, " +
			"middle_initial=?, " +
			"last_name=?, " +
			"phonenumber=?, " +
			"faxnumber=?, " +
			"emailaddress=?, " +
			"addressid=?, " +
			"primarybillingdeliveryid = ? " + 
			"where customercontactid = ?";


	/**
	* Returns the CustomerContact for the specified CustomerContactPK,
	* or null if there is none.
	*/
	public static CustomerContact select(CustomerContactPK pk)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;
		CustomerContact customerContact = null;
		ResultSet rs = null;

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(SELECT_BY_ID);
			ps.setLong(1, pk.getID());

			rs = ps.executeQuery();

			if (rs.next()) {
				customerContact = createFromRS(rs, 1);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"select customerContact by id failed", sqle,
				SELECT_BY_ID, pk);
		}
		finally {
			DBHelper.close(conn, ps, rs);
		}

		return customerContact;
	}


	static private CustomerContact createFromRS(ResultSet rs, int columnOffset)
			throws SQLException {

		int i = columnOffset;

		CustomerContactPK pk = new CustomerContactPK(rs.getLong(i++));
		String firstName = rs.getString(i++);
		String middleInitial = rs.getString(i++);
		String lastName = rs.getString(i++);
		String phoneNumber = rs.getString(i++);
		String faxNumber = rs.getString(i++);
		String emailAddress = rs.getString(i++);
		BillingDeliveryType deliveryType =
				BillingDeliveryType.getInstanceFor(rs.getInt(i++));
		
		Address address = AddressDAO.createFromRS(rs, i);

		CustomerContact customerContact = null;
		try {
			customerContact = new CustomerContact(pk,
					new ContactName(firstName, middleInitial, lastName),
					address, phoneNumber, faxNumber, emailAddress, deliveryType);
		}
		catch (javax.mail.internet.ParseException pe) {
			throw new Error(pe);
		}

		return customerContact;
	}


	/**
	* Delete the specified CustomerContact, so long as it is unreferenced.
	* Does nothing if an integrity constraint violation occurs.
	*/
	public static void delete(CustomerContact customerContact)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(DELETE_BY_ID);
			ps.setLong(1, customerContact.getPK().getID());

			int count = ps.executeUpdate();

			if (count == 0) {
				throw new DataNotFoundException(
					"delete customerContact by id failed",
					DELETE_BY_ID, customerContact.getPK());
			}

			AddressDAO.delete(customerContact.getAddress().getPK());

			customerContact.resetDeletedAddressPKs();
		}
		catch (SQLException sqle) {
			// Ignore integrity constraint violation, and
			// just do nothing.
			//
			if (sqle.getErrorCode() != 2291) {
				throw new DataAccessException(
					"delete customerContact by id failed", sqle,
					DELETE_BY_ID, customerContact.getPK());
			}
		}
		finally {
			DBHelper.close(conn, ps, null);
		}
	}


	/**
	* Creates a CustomerContact from the specified CustomerContact.
	*
	* Returns a CustomerContact instance with a valid CustomerContactPK.
	*
	* Throws IllegalStateException if the CustomerContact already has
	* a CustomerContactPK.
	*/
	public static CustomerContact create(CustomerContact customerContact)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;

		if (customerContact.getPK() != null) {
			throw new IllegalStateException("customerContact already has pk " +
					customerContact.getPK());
		}

		long key = BlindKey.getNextKey("CUSTOMER_CONTACT_SEQ");
		customerContact.setPK(new CustomerContactPK(key));

		if (customerContact.getAddress().getPK() == null) {
			Address address = AddressDAO.create(
					customerContact.getAddress());
			customerContact.setAddress(address);
		}
		customerContact.resetDeletedAddressPKs();


		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(INSERT);

			int i = 1;

			ps.setLong(i++, customerContact.getPK().getID());
			ContactName contactName = customerContact.getContactName();
			ps.setString(i++, contactName.getFirstName());
			ps.setString(i++, contactName.getMiddleInitial());
			ps.setString(i++, contactName.getLastName());
			ps.setString(i++, customerContact.getPhoneNumber());
			ps.setString(i++, customerContact.getFaxNumber());
			ps.setString(i++, customerContact.getEmailAddress());
			ps.setLong(i++, customerContact.getAddress().getPK().getID());
			ps.setInt(i++, customerContact.getBillingDeliveryType().getID());

			int count = ps.executeUpdate();

			if (count <= 0) {
				throw new DataAccessException(
					"create customerContact failed",
					INSERT, customerContact);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"create customerContact failed", sqle,
				INSERT, customerContact);
		}
		finally {
			DBHelper.close(conn, ps, null);
		}

		return customerContact;
	}



	/**
	* Updates the specified CustomerContact in the database.
	* A new Address record is created as needed (if the specified
	* Address does not have a pk).  Any previously used Address
	* is deleted if unreferenced.
	*
	* Returns a CustomerContact with valid PKs in dependent objects.
	*
	* Throws IllegalStateException if the specified CustomerContact
	* does not have a CustomerContactPK.
	*/
	public static CustomerContact update(CustomerContact customerContact)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;

		if (customerContact.getPK() == null) {
			throw new IllegalStateException("customerContact has no pk " +
					customerContact.getPK());
		}

		if (customerContact.getAddress().getPK() == null) {
			Address address = AddressDAO.create(
					customerContact.getAddress());
			customerContact.setAddress(address);

		} else {
			AddressDAO.update(customerContact.getAddress());
		}

		for (Iterator it = customerContact.getDeletedAddressPKs().iterator();
				it.hasNext(); ) {
			AddressPK addressPK = (AddressPK) it.next();
			AddressDAO.delete(addressPK);
		}
		customerContact.resetDeletedAddressPKs();

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(UPDATE);

			int i = 1;

			ContactName contactName = customerContact.getContactName();
			ps.setString(i++, contactName.getFirstName());
			ps.setString(i++, contactName.getMiddleInitial());
			ps.setString(i++, contactName.getLastName());
			ps.setString(i++, customerContact.getPhoneNumber());
			ps.setString(i++, customerContact.getFaxNumber());
			ps.setString(i++, customerContact.getEmailAddress());
			ps.setLong(i++, customerContact.getAddress().getPK().getID());
			ps.setInt(i++, customerContact.getBillingDeliveryType().getID());
			ps.setLong(i++, customerContact.getPK().getID());

			int count = ps.executeUpdate();

			if (count <= 0) {
				throw new DataAccessException(
					"update customerContact failed",
					UPDATE, customerContact);
			}

		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"update customerContact failed", sqle,
				UPDATE, customerContact);
		}
		finally {
			DBHelper.close(conn, ps, null);
		}

		return customerContact;
	}
}
