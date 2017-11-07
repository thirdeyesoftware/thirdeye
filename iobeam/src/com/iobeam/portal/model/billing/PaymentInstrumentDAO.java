package com.iobeam.portal.model.billing;


import javax.sql.*;
import java.sql.*;
import java.util.*;
import com.iobeam.portal.util.*;
import com.iobeam.portal.model.address.*;


/**
*/
public class PaymentInstrumentDAO  {

	static final String SELECT_BY_ID =
			"select " +
			"paymentinstrumentid, " +
			"paymentinstrumenttypeid, " +
			"accountnumber, " +
			"securitycode, " +
			"expirationdate, " +
			"routingnumber, " +
			"checknumber, " +
			"bankname, " +
			"accountholdername, " +
			"billingaddressid " +
			"from payment_instrument " +
			"where " +
			"paymentinstrumentid = ?";


	static final String DELETE_BY_ID =
			"delete from payment_instrument where " +
			"paymentinstrumentid = ?";


	static final String INSERT =
			"insert into payment_instrument (" +
			"paymentinstrumentid, " +
			"paymentinstrumenttypeid, " +
			"accountnumber, " +
			"securitycode, " +
			"expirationdate, " +
			"routingnumber, " +
			"checknumber, " +
			"bankname, " +
			"accountholdername, " +
			"billingaddressid " +
			") values " +
			"(?, ?, ?, ?, ?,   ?, ?, ?, ?, ?)";


	static final String UPDATE_CC =
			"update payment_instrument set " +
			"expirationdate=?, " +
			"accountnumber=?, " + 
			"accountholdername=?, " + 
			"securitycode=?," + 
			"billingaddressid=? " +
			"where paymentinstrumentid=?";

	static final String UPDATE_CHECK = 
		"update payment_instrument set " + 
			"routingnumber = ?," + 
			"checknumber =  ?, " + 
			"bankname = ?, " +
			"accountholdername = ?, " + 
			"accountnumber = ? " + 
			"where paymentinstrumentid = ?";

	/**
	* Returns the PaymentInstrument for the specified PaymentInstrumentPK,
	* or null if there is none.
	*/
	public static PaymentInstrument select(PaymentInstrumentPK pk)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;
		PaymentInstrument paymentInstrument = null;
		ResultSet rs = null;

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(SELECT_BY_ID);
			ps.setLong(1, pk.getID());

			rs = ps.executeQuery();

			if (rs.next()) {
				paymentInstrument = createFromRS(rs, 1);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"select payment_instrument by id failed", sqle,
				SELECT_BY_ID, pk);
		}
		finally {
			DBHelper.close(conn, ps, rs);
		}

		return paymentInstrument;
	}


	static private PaymentInstrument createFromRS(ResultSet rs,
			int columnOffset)
			throws SQLException, DataAccessException {

		int i = columnOffset;

		PaymentInstrumentPK pk = new PaymentInstrumentPK(rs.getLong(i++));
		PaymentInstrumentType piType =
				PaymentInstrumentType.getInstanceFor(rs.getInt(i++));
		String accountNumber = rs.getString(i++);
		String securityCode = rs.getString(i++);
		java.util.Date expirationDate = rs.getDate(i++);
		String routingNumber = rs.getString(i++);
		String checkNumber = rs.getString(i++);
		String bankName = rs.getString(i++);
		String accountHolderName = rs.getString(i++);
		AddressPK billingAddressPK = new AddressPK(rs.getLong(i++));
		

		PaymentInstrument paymentInstrument = null;

		if (piType.equals(PaymentInstrumentType.CREDIT_CARD)) {

			Address billingAddress = AddressDAO.select(billingAddressPK);

			paymentInstrument = new CreditCard(pk,
					accountNumber, securityCode, accountHolderName,
					billingAddress, expirationDate);
		} else
		if (piType.equals(PaymentInstrumentType.CHECK)) {
			paymentInstrument = new Check(pk, checkNumber);
		} else {
			throw new UnsupportedOperationException(piType.toString());
		}


		return paymentInstrument;
	}


	/**
	* Delete the specified PaymentInstrument, so long as it is unreferenced.
	* Does nothing if an integrity constraint violation occurs.
	*/
	public static void delete(PaymentInstrument paymentInstrument)  
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;

		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(DELETE_BY_ID);
			ps.setLong(1, paymentInstrument.getPK().getID());

			int count = ps.executeUpdate();

			if (count == 0) {
				throw new DataNotFoundException(
					"delete payment_instrument by id failed",
					DELETE_BY_ID, paymentInstrument.getPK());
			}

			if (paymentInstrument.getType().equals(
					PaymentInstrumentType.CREDIT_CARD)) {
				CreditCard creditCard = (CreditCard) paymentInstrument;

				AddressDAO.delete(creditCard.getCardHolderAddress().getPK());

				creditCard.resetDeletedAddressPKs();
			}

		}
		catch (SQLException sqle) {
			// Ignore integrity constraint violation, and
			// just do nothing.
			//
			if (sqle.getErrorCode() != 2291) {
				throw new DataAccessException(
					"delete payment_instrument by id failed", sqle,
					DELETE_BY_ID, paymentInstrument.getPK());
			}
		}
		finally {
			DBHelper.close(conn, ps, null);
		}
	}


	/**
	* Creates a PaymentInstrument from the specified PaymentInstrument.
	*
	* Returns a PaymentInstrument instance with a valid PaymentInstrumentPK.
	*
	* Throws IllegalStateException if the PaymentInstrument already has
	* a PaymentInstrumentPK.
	*/
	public static PaymentInstrument create(PaymentInstrument paymentInstrument)
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;

		if (paymentInstrument.getPK() != null) {
			throw new IllegalStateException("paymentInstrument already " +
					"has pk " + paymentInstrument.getPK());
		}

		long key = BlindKey.getNextKey("PAYMENT_INSTRUMENT_SEQ");
		PaymentInstrumentPK pk = new PaymentInstrumentPK(key);
		PaymentInstrumentType piType = paymentInstrument.getType();


		CreditCard cc = null;
		Check check = null;

		if (piType == null) {
			throw new NullPointerException(paymentInstrument.toString());
		}

		if (piType.equals(PaymentInstrumentType.CREDIT_CARD)) {
			cc = (CreditCard) paymentInstrument;
			cc.setPK(new PaymentInstrumentPK(key));

			if (cc.getCardHolderAddress().getPK() == null) {
				Address address = AddressDAO.create(
						cc.getCardHolderAddress());
				cc.setCardHolderAddress(address);
			}
			cc.resetDeletedAddressPKs();
		} else
		if (piType.equals(PaymentInstrumentType.CHECK)) {
			check = (Check) paymentInstrument;
			check.setPK(new PaymentInstrumentPK(key));
		} else {
			throw new UnsupportedOperationException(piType.toString());
		}



		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(INSERT);

			int i = 1;

			ps.setLong(i++, paymentInstrument.getPK().getID());
			ps.setInt(i++, paymentInstrument.getType().getID());

			if (piType.equals(PaymentInstrumentType.CREDIT_CARD)) {
				ps.setString(i++, cc.getCreditCardNumber());
				ps.setString(i++, cc.getSecurityCode());
				ps.setDate(i++,
						new java.sql.Date(cc.getExpirationDate().getTime()));

				ps.setNull(i++, Types.VARCHAR);
				ps.setNull(i++, Types.VARCHAR);
				ps.setNull(i++, Types.VARCHAR);

				ps.setString(i++, cc.getCardHolderName());
				ps.setLong(i++, cc.getCardHolderAddress().getPK().getID());
			} else
			if (piType.equals(PaymentInstrumentType.CHECK)) {
				ps.setNull(i++, Types.VARCHAR);
				ps.setNull(i++, Types.VARCHAR);
				ps.setNull(i++, Types.VARCHAR);

				ps.setNull(i++, Types.VARCHAR);
				ps.setString(i++, check.getCheckNumber());
				ps.setNull(i++, Types.VARCHAR);

				ps.setNull(i++, Types.VARCHAR);
				ps.setNull(i++, Types.NUMERIC);
			}

			int count = ps.executeUpdate();

			if (count <= 0) {
				throw new DataAccessException(
					"create paymentInstrument failed",
					INSERT, paymentInstrument);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"create paymentInstrument failed", sqle,
				INSERT, paymentInstrument);
		}
		finally {
			DBHelper.close(conn, ps, null);
		}

		return paymentInstrument;
	}



	/**
	* Updates the specified PaymentInstrument in the database.
	* A new Address record is created as needed (if the specified
	* PaymentInstrument uses Address and the Address does not have a pk).
	* Any previously used Address is deleted if unreferenced.
	*
	* Returns a PaymentInstrument with valid PKs in dependent objects.
	*
	* Throws IllegalStateException if the specified PaymentInstrument
	* does not have a PaymentInstrumentPK.
	*/
	public static PaymentInstrument update(PaymentInstrument paymentInstrument)
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;

		if (paymentInstrument.getPK() == null) {
			throw new IllegalStateException("paymentInstrument has no pk " +
					paymentInstrument.getPK());
		}


		PaymentInstrumentType piType = paymentInstrument.getType();


		CreditCard cc = null;
		Check check = null;

		if (piType.equals(PaymentInstrumentType.CREDIT_CARD)) {
			cc = (CreditCard) paymentInstrument;

			if (cc.getCardHolderAddress().getPK() == null) {
				Address address = AddressDAO.create(
						cc.getCardHolderAddress());
				cc.setCardHolderAddress(address);
			} else {
				AddressDAO.update(cc.getCardHolderAddress());
			}

			for (Iterator it = cc.getDeletedAddressPKs().iterator();
					it.hasNext(); ) {
				AddressPK addressPK = (AddressPK) it.next();
				AddressDAO.delete(addressPK);
			}

			cc.resetDeletedAddressPKs();

			updateCC(cc);

		} else
		if (piType.equals(PaymentInstrumentType.CHECK)) {
			check = (Check) paymentInstrument;
			updateCheck(check);
		} else {
			throw new UnsupportedOperationException(piType.toString());
		}

		return paymentInstrument;

	}

	private static void updateCC(CreditCard card) 
			throws DataAccessException {
		PreparedStatement ps = null;
		Connection conn = null;
		try {

			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(UPDATE_CC);
			int i = 1;
			ps.setTimestamp(i++,
				new Timestamp(card.getExpirationDate().getTime()));
			ps.setString(i++,
				card.getCreditCardNumber());
			ps.setString(i++,
				card.getCardHolderName());
			ps.setString(i++,
				card.getSecurityCode());
			ps.setLong(i++,
				card.getCardHolderAddress().getPK().getID());
			ps.setLong(i++, card.getPK().getID());
	
			if (ps.executeUpdate() <= 0) {
				throw new DataAccessException("could not update cc", UPDATE_CC, card);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException("could not update cc", sqle, UPDATE_CC,
			card);
		}
		finally {
			try {
				ps.close();
				conn.close();
			}
			catch (Exception ee) { }
		}	
	}

	private static void updateCheck(Check check) 
			throws DataAccessException {

		PreparedStatement ps = null;
		Connection conn = null;
		try {
			conn = DBHelper.getConnection();
			ps = conn.prepareStatement(UPDATE_CHECK);
			int i = 1;
			ps.setString(i++, check.getRoutingNumber());
			ps.setString(i++, check.getCheckNumber());
			ps.setString(i++, check.getBankName());
			ps.setString(i++, check.getAccountHolderName());
			ps.setString(i++, check.getAccountNumber());
			ps.setLong(i++, check.getPK().getID());
		
			int count = ps.executeUpdate();

			if (count <= 0) {
				throw new DataAccessException(
					"update paymentInstrument failed",
					UPDATE_CHECK, check);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException("could not update check.",
				sqle, UPDATE_CC, check);
		}
		finally {
			try {
				ps.close();
				conn.close();
			}
			catch (Exception eee) { }
		}
	}
}
