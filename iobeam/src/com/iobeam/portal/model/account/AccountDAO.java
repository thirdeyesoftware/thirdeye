package com.iobeam.portal.model.account;

import com.iobeam.portal.util.DataAccessException;
import com.iobeam.portal.util.DataNotFoundException;
import com.iobeam.portal.util.DBHelper;
import com.iobeam.portal.util.BlindKey;
import com.iobeam.portal.util.Money;
import com.iobeam.portal.util.ReadableNumberGenerator;

import com.iobeam.portal.model.customer.*;
import com.iobeam.portal.model.billablecustomer.*;
import com.iobeam.portal.model.invoice.*;
import com.iobeam.portal.model.subscription.*;
import com.iobeam.portal.model.product.ProductPK;
import com.iobeam.portal.model.billing.PaymentInstrumentType;
import com.iobeam.portal.model.billing.PaymentInstrument;

import java.util.logging.*;
import java.util.*;
import java.sql.*;
import javax.sql.*;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.EJBException;

public class AccountDAO  {

	public static void checkPrimaryKey(AccountPK pk) 
			throws ObjectNotFoundException, DataAccessException {
		String sql = "SELECT AccountID FROM ACCOUNT";
		Statement stmt = null;
		ResultSet rs = null;
		Connection c = null;
		try {
			c = DBHelper.getConnection();
			stmt = c.createStatement();
			rs = stmt.executeQuery(sql);
			if (!rs.next()) {
				throw new ObjectNotFoundException(
					"Could not find account " + pk.getID());
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"unable to retrieve account", sqle, sql, pk.toString());
		}
		finally {
			try {
				rs.close();
				stmt.close();
				c.close();
			}
			catch (Exception teze) {
			}
		}
	}
			
	public static AccountData create(CustomerPK customerPK, 
			BillableCustomerPK billableCustomerPK)	throws DataAccessException {
		String accountNumber = null;
		StringBuffer sb = new StringBuffer(0);
		sb.append(" INSERT INTO ACCOUNT (AccountID, CustomerID, ");
		sb.append("	BillableCustomerID, AccountStatusID, AccountNumber)");
		sb.append(" VALUES(?,?,?,?,?)");
		Connection c = null;
		PreparedStatement ps = null;
		long key = 0;
		AccountPK pk = null;
		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(sb.toString());
			ps.setLong(1, key = BlindKey.getNextKey("ACCOUNT_SEQ"));
			ps.setLong(2, customerPK.getID());

			if (billableCustomerPK == null) {
				ps.setNull(3, Types.NUMERIC);
			} else {
				ps.setLong(3, billableCustomerPK.getID());
			}

			ps.setLong(4, AccountStatus.OPEN.getID());
			ps.setString(5, 
				accountNumber = ReadableNumberGenerator.getReadableNumber(key));
			if (ps.executeUpdate() < 1) {
				throw new DataAccessException(
					"could not create account", sb.toString(),
					customerPK.toString() + "," + 
					billableCustomerPK.toString());
			}
			pk = new AccountPK(key);
		
		}
		catch (SQLException sqle) {
				throw new DataAccessException(
					"could not create account", sqle, sb.toString(), 
					customerPK.toString() + "," + 
					billableCustomerPK.toString());
		}
		finally {
			try {
				ps.close();
				c.close();
			}
			catch (Exception eee) { }
		}

		return new AccountData(pk, 
				customerPK,
				billableCustomerPK,
				new AccountNumber(accountNumber),
				AccountStatus.OPEN);

	}

	public static void delete(AccountPK pk) throws DataAccessException {
		
		StringBuffer sb = new StringBuffer(0);
		sb.append(" DELETE FROM ACCOUNT WHERE AccountID = ?");
		Connection c = null;
		PreparedStatement ps = null;
		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(sb.toString());
			ps.setLong(1, pk.getID());
			if (ps.executeUpdate() < 1) {
				throw new DataAccessException(
					"could not remove account", sb.toString(), pk.toString());
			}
		}
		catch (SQLException sqle) {
				throw new DataAccessException(
					"could not remove account", sqle, sb.toString(), pk.toString());
		}
		finally {
			try {
				ps.close();
				c.close();
			}
			catch (Exception eze) { }
		}

	}

	public static void update(AccountData data) throws DataAccessException {
			StringBuffer sb = new StringBuffer(0);
		sb.append(" UPDATE ACCOUNT SET AccountNumber = ?, CustomerID = ?, ");
		sb.append("	BillableCustomerID = ?, AccountStatusID = ? WHERE ");
		sb.append(" AccountID = ?");
		Connection c = null;
		PreparedStatement ps = null;
		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(sb.toString());
			ps.setString(1, data.getAccountNumber().getNumber());
			ps.setLong(2, data.getCustomerPK().getID());
			ps.setLong(3, data.getBillableCustomerPK().getID());
			ps.setLong(4, data.getAccountStatus().getID());
			if (ps.executeUpdate() < 1) {
				throw new DataAccessException(
					"could not update account", sb.toString(), data.toString());
			}
		}
		catch (SQLException sqle) {
				throw new DataAccessException(
					"could not update account", sqle, sb.toString(), data.toString());
		}
		finally {
			try {
				ps.close();
				c.close(); 
			}
			catch (Exception ee) { }
		}

	}

	public static AccountData load(AccountData data)
			throws DataAccessException {
		String sql = "SELECT * FROM ACCOUNT WHERE AccountID = ?";
		PreparedStatement ps = null;
		Connection c = null;
		ResultSet rs = null;
		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(sql);
			ps.setLong(1, data.getPK().getID());
			rs = ps.executeQuery();
			if (rs.next()) {
				data = createFromRS(rs);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"could not retrieve account", sqle,sql, data.toString());
		}
		finally {
			try {
				rs.close();
				ps.close();
				c.close();
			}
			catch (Exception eze) { }
		}

		return data;
	}


	public static Collection selectByActivity(java.util.Date startDate, 
			java.util.Date endDate) throws DataAccessException {

			StringBuffer sb = new StringBuffer(0);
			sb.append("SELECT A.* FROM ACCOUNT A, ACCOUNT_ENTRY AE WHERE ");
			sb.append(" A.ACCOUNTID = AE.ACCOUNTID AND (AE.ACCOUNTENTRYDATE >= ? ");
			sb.append(" AND AE.ACCOUNTENTRYDATE <= ?)");
			Connection c = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			Vector v = new Vector();
			Logger.getLogger("com.iobeam.portal.model.account").info(
				"start date = " + startDate + ", end date = " + endDate);

			try {
				c = DBHelper.getConnection();
				ps = c.prepareStatement(sb.toString());
				ps.setTimestamp(1, new Timestamp(startDate.getTime()));
				ps.setTimestamp(2, new Timestamp(endDate.getTime()));
				rs = ps.executeQuery();
				while (rs.next()) {
					AccountPK pk = new AccountPK(
						rs.getLong("ACCOUNTID"));
					if (!v.contains(pk)) {
						v.addElement(pk);
					}
				}
			}
			catch (SQLException sqle) {
				throw new DataAccessException(
					"unable to retrieve accounts", sqle, sb.toString(), 
					startDate.toString() + "," + endDate.toString());
			}
			finally {
				try {
					rs.close();
					ps.close();
					c.close();
				}
				catch (Exception eze) { }
			}
			return v;
	
	}

	public static AccountPK selectByInvoicePK(InvoicePK invoicePK) 
			throws DataAccessException {
		StringBuffer sb = new StringBuffer(0);
		sb.append("SELECT ACCOUNT.ACCOUNTID FROM ACCOUNT, INVOICE I WHERE ");
		sb.append(" ACCOUNT.ACCOUNTID = I.ACCOUNTID AND I.INVOICEID = ?");
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		AccountPK accountPK = null;
		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(sb.toString());
			ps.setLong(1, invoicePK.getID());
			rs = ps.executeQuery();
			if (rs.next()) {
				accountPK = new AccountPK(
					rs.getLong("ACCOUNTID"));
			}
			else {
				throw new DataAccessException(
					"unable to retrieve account", sb.toString(),
					invoicePK.toString());
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"unable to retrienve account", sqle, sb.toString(), 
				invoicePK.toString());
		}
		finally {
			try {
				rs.close();
				ps.close();
				c.close();
			}
			catch (Exception e) { }
		}
		return accountPK;
	}


	public static AccountPK selectByAccountNumber(AccountNumber accountNumber) 
			throws DataAccessException {

		StringBuffer sb = new StringBuffer(0);
		sb.append("SELECT ACCOUNTID FROM ACCOUNT WHERE ");
		sb.append("ACCOUNTNUMBER = ?");
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		AccountPK accountPK = null;
		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(sb.toString());
			ps.setString(1, accountNumber.getNumber());
			rs = ps.executeQuery();
			if (rs.next()) {
				accountPK = new AccountPK(
					rs.getLong("ACCOUNTID"));
			}
			else {
				throw new DataNotFoundException(
					"unable to retrieve account", sb.toString(),
					accountNumber);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"unable to retrienve account", sqle, sb.toString(), 
				accountNumber);
		}
		finally {
			try {
				rs.close();
				ps.close();
				c.close();
			}
			catch (Exception e) { }
		}
		return accountPK;
				
	}


	/**
	 * returns the current balacne on a given account.
	 * does not include balance forward entry types.
	 */
	public static Money getCurrentBalance(AccountPK accountPK) 
			throws DataAccessException {
		StringBuffer sb = new StringBuffer(0);
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection c = null;
		Money money = null;
		sb.append("SELECT SUM(AMOUNT) Balance FROM ACCOUNT_ENTRY WHERE ");
		sb.append(" AccountID = ? AND AccountEntryTypeID != ?");
		
		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(sb.toString());
			ps.setLong(1, accountPK.getID());
			ps.setInt(2, AccountEntryType.BALANCE_FORWARD.getID());

			rs = ps.executeQuery();
			if (rs.next()) {
				money = new Money(rs.getDouble("Balance"));
			} else {
				money = new Money(0d);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"unable to retrieve acct balance",
				sqle, sb.toString(), accountPK.toString());
		}
		finally {
			try {
				rs.close();
				ps.close();
				c.close();
			}
			catch (Exception ezee) { }
		}
		return money;

	}

	public static AccountEntry selectAccountEntry(long acctEntryID)
			throws DataAccessException {
		String sql = "SELECT * FROM Account_Entry WHERE AccountEntryID = ?";
		Connection c = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		AccountEntry entry = null;

		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(sql);
			ps.setLong(1, acctEntryID);
			rs = ps.executeQuery();
			if (rs.next()) {
				AccountEntryType type = 
					AccountEntryType.getInstanceFor(rs.getInt("AccountEntryTypeID"));
				entry = createAccountEntryFromRS(rs, type);
			
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"unable to find account entry.",sqle, sql, "" + acctEntryID);
		}
		finally {
			try {
				rs.close();
				ps.close();
				c.close();
			}
			catch (Exception sleazy) { }
		}
		return entry;
	}

	public static SortedSet selectAccountEntries(AccountPK pk, 
				AccountEntryType type, java.util.Date startDate, 
				java.util.Date endDate) throws DataAccessException {

		if (startDate == null) throw new NullPointerException("start date");
		if (endDate == null) throw new NullPointerException("end date");
		
		TreeSet ts = new TreeSet();
		StringBuffer sb = new StringBuffer(0);
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection c = null;
		sb.append("SELECT ACCOUNT_ENTRY.* FROM ACCOUNT_ENTRY WHERE ");
		sb.append("AccountID = ? AND AccountEntryTypeID = ? AND ");
		sb.append("(AccountEntryDate >= ? AND ");
		sb.append("AccountEntryDate <= ?) ORDER BY AccountEntryDate ASC");
			
		Logger.getLogger("com.iobeam.portal.model.account").info(
				"start date = " + startDate + ", end date = " + endDate);
		
		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(sb.toString());
			ps.setLong(1, pk.getID());
			ps.setInt(2, type.getID());
			ps.setTimestamp(3, new Timestamp(startDate.getTime()));
			ps.setTimestamp(4, new Timestamp(endDate.getTime()));
			rs = ps.executeQuery();
			while (rs.next()) {
				AccountEntry entry = createAccountEntryFromRS(rs, type);
				ts.add(entry);
				
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"unable to retrieve account entries.",
				sqle,sb.toString(), startDate.toString() + "," + endDate.toString());
		}
		finally {
			try {
				rs.close();
				ps.close();
				c.close();
			}
			catch (Exception ezeee) { }
		}
		return ts;
	}
	
	public static SortedSet selectAccountEntries(AccountPK pk, 
			java.util.Date startDate, java.util.Date endDate) 
				throws DataAccessException {

		if (startDate == null) throw new NullPointerException("start date");
		if (endDate == null) throw new NullPointerException("end date");
		
		TreeSet ts = new TreeSet();
		StringBuffer sb = new StringBuffer(0);
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection c = null;
		sb.append("SELECT ACCOUNT_ENTRY.* FROM ACCOUNT_ENTRY WHERE ");
		sb.append("AccountID = ? AND AccountEntryDate >= ? AND ");
		sb.append("AccountEntryDate <= ? ORDER BY accountentrytypeid, ");
		sb.append(" AccountEntryDate ASC");
		
		Logger.getLogger("com.iobeam.portal.model.account").info(
			"for account = " + pk);
		
		Logger.getLogger("com.iobeam.portal.model.account").info(
				"start date = " + startDate + ", end date = " + endDate);
		Logger.getLogger("com.iobeam.portal.model.account").info(
			sb.toString());

		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(sb.toString());
			ps.setLong(1, pk.getID());
			ps.setTimestamp(2, new Timestamp(startDate.getTime()));
			ps.setTimestamp(3, new Timestamp(endDate.getTime()));
			rs = ps.executeQuery();
			while (rs.next()) {
				AccountEntryType type = 
					AccountEntryType.getInstanceFor(rs.getInt("AccountEntryTypeID"));

				AccountEntry entry = createAccountEntryFromRS(rs, type);
				ts.add(entry);
				
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"unable to retrieve account entries.",
				sqle,sb.toString(), startDate.toString() + "," + endDate.toString());
		}
		finally {
			try {
				rs.close();
				ps.close();
				c.close();
			}
			catch (Exception ezeee) { }
		}
		return ts;
	}
	
	private static AccountEntry createAccountEntryFromRS(ResultSet rs, 
			AccountEntryType type) throws SQLException {
		
		if (type.equals(AccountEntryType.PAYMENT)) {
			Payment payment = new Payment(rs.getLong("AccountEntryId"),
						new Money(rs.getDouble("Amount")),
						new java.util.Date(rs.getTimestamp("AccountEntryDate").getTime()), 
						PaymentInstrumentType.getInstanceFor(rs.getInt("paymentinstrumenttypeid")),
						rs.getString("memo"));
			return payment;
		}
		else if (type.equals(AccountEntryType.PURCHASE)) {
			long subscriptionid = rs.getLong("SubscriptionID");
			long productid = rs.getLong("ProductID");
			Purchase purchase;
			if (subscriptionid > 0) {
				purchase = new Purchase(rs.getLong("AccountEntryID"),
					new Money(rs.getDouble("Amount")),
					new java.util.Date(rs.getTimestamp("AccountEntryDate").getTime()),
					new SubscriptionPK(subscriptionid));
				return purchase;
			}
			else if (productid > 0) {
				purchase = new Purchase(rs.getLong("AccountEntryID"),
					new Money(rs.getDouble("Amount")),
					new java.util.Date(rs.getTimestamp("AccountEntryDate").getTime()),
					new ProductPK(productid));
				return purchase;
			} else {
				throw new EJBException("Unknown purchase");
			}
		}
		else if (type.equals(AccountEntryType.BALANCE_FORWARD)) {
			BalanceForward bf = new 
				BalanceForward(rs.getLong("accountentryid"),
						new Money(rs.getDouble("amount")),
						new java.util.Date(rs.getTimestamp("accountentrydate").getTime()));
			return bf;
		}
		else if (type.equals(AccountEntryType.SALES_TAX)) {
			SalesTax tax = new SalesTax(
				rs.getLong("accountentryid"),
				new Money(rs.getDouble("amount")),
				new java.util.Date(rs.getTimestamp("accountentrydate").getTime()));
			return tax;
		}
		else {
			throw new EJBException("could not create account entry. unknown type.");
		}

	}

	/**
	 * create payment account entry for given account and amount. 
	 */
	public static Payment createPayment(AccountPK accountpk, 
				PaymentInstrument instrument, Money amount, String memo)
			throws DataAccessException {
		Payment payment = 
			new Payment(BlindKey.getNextKey("ACCOUNT_ENTRY_SEQ"), amount, 
			 new java.util.Date(), instrument.getType(), instrument.toString());

		StringBuffer sb = new StringBuffer(0);
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection c = null;
		sb.append("INSERT INTO ACCOUNT_ENTRY (AccountEntryID, AccountID, Amount,");
		sb.append("AccountEntryTypeID, AccountEntryDate, PaymentInstrumentTypeID, ");
		sb.append("memo)  VALUES (?,?,?,?,?,?,?) ");
		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(sb.toString());
			ps.setLong(1, payment.getID());
			ps.setLong(2, accountpk.getID());
			double amt = amount.getAmount();
			if (amt >= 0) {
				amt = -1 * amt;
			}
			ps.setDouble(3, amt);
			ps.setInt(4, AccountEntryType.PAYMENT.getID());
			ps.setTimestamp(5, new Timestamp((new java.util.Date()).getTime()));
			ps.setInt(6, instrument.getType().getID());
			ps.setString(7, memo);

			if (ps.executeUpdate() < 1) {
				throw new DataAccessException(
					"unable to create payment.", sb.toString(), 
					accountpk.toString() + instrument.toString() + amount.toString());
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"unable to create payment.", sqle, sb.toString(), 
					accountpk.toString() + instrument.toString() + amount.toString());
		}
		finally {
			try {
				ps.close();
				c.close();
			}
			catch (Exception eze) { }
		}
		return payment;

	}

	/**
	 * creates a balance forward account entry with the given amount.
	 */
	public static BalanceForward createBalanceForward(AccountPK accountpk, Money amount) 
			throws DataAccessException {
		BalanceForward bf = new BalanceForward(
			BlindKey.getNextKey("ACCOUNT_ENTRY_SEQ"), amount, new java.util.Date());
		createAccountEntry(accountpk, bf);
		return bf;
	}
	
	/**
	 * creates a sales tax account entry with the given amount.
	 */
	public static SalesTax createSalesTax(AccountPK accountpk, Money amount) 
			throws DataAccessException {
		SalesTax st = new SalesTax (
			BlindKey.getNextKey("ACCOUNT_ENTRY_SEQ"), amount, new java.util.Date());
		createAccountEntry(accountpk, st);
		
		return st;
	}

	/**
	 * create purchase account entry for Product.
	 */
	public static Purchase createPurchase(AccountPK accountpk, Money amount,
			ProductPK productPK, String memo) throws DataAccessException {
		Purchase purchase = new Purchase(
				BlindKey.getNextKey("ACCOUNT_ENTRY_SEQ"), amount, 
				new java.util.Date(), productPK);

		purchase.setMemo(memo);

		createAccountEntry(accountpk, purchase);
		return purchase;
	}

	/**
	 * create purchase account entry for subscription.
	 */
	public static Purchase createPurchase(AccountPK accountpk, Money amount,
			SubscriptionPK subPK, String memo) throws DataAccessException {
		Purchase purchase = new Purchase(
				BlindKey.getNextKey("ACCOUNT_ENTRY_SEQ"), amount, 
				new java.util.Date(), subPK);
		
		purchase.setMemo(memo);
		createAccountEntry(accountpk, purchase);

		return purchase;
	}

	/** 
	 * create account entry in database for account and given account 
	 * entry.
	 */
	private static void createAccountEntry(AccountPK accountPK, 
			AccountEntry entry) throws DataAccessException {
		StringBuffer sb = new StringBuffer(0);
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection c = null;
		sb.append("INSERT INTO ACCOUNT_ENTRY (AccountEntryID, AccountID, Amount,");
		sb.append("AccountEntryTypeID, AccountEntryDate, ProductID, SubscriptionID, ");
		sb.append("memo) ");
		sb.append(" VALUES (?,?,?,?,?,?,?,?) ");
		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(sb.toString());
			ps.setLong(1, entry.getID());
			ps.setLong(2, accountPK.getID());
			ps.setDouble(3, entry.getAmount().getAmount());
			ps.setInt(4, entry.getAccountEntryType().getID());
			ps.setTimestamp(5, new Timestamp(entry.getPostDate().getTime()));
			ps.setLong(6, (entry.getProductPK()==null ? 0 :
				entry.getProductPK().getID()));
			ps.setLong(7, (entry.getSubscriptionPK() == null ? 0 : 
					entry.getSubscriptionPK().getID()));

			if (entry.getMemo() == null) {
				ps.setNull(8, Types.VARCHAR);
			}
			else {
				ps.setString(8, entry.getMemo());
			}


			if (ps.executeUpdate() < 1) {
				throw new DataAccessException(
					"unable to create account entry.", sb.toString(), entry.toString());
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"unable to create entry.", sqle, sb.toString(), entry.toString());
		}
		finally {
			try {
				ps.close();
				c.close();
			}
			catch (Exception eze) { }
		}

	}

	/**
	 * find subscriptions for account, given a subscription type and a
	 * subscription status
	 * @param accountPK account to lookup
	 * @param stype SubscriptionType
	 * @param status SubscriptionStatus, use null for all status
	 */
	public static Collection selectSubscriptions(AccountPK accountPK,
			SubscriptionType stype, SubscriptionStatus status) 
				throws DataAccessException {
		Vector v = new Vector();
		StringBuffer sb = new StringBuffer(0);
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection c = null;
		sb.append("SELECT SUBSCRIPTIONID FROM SUBSCRIPTION WHERE ");
		sb.append(" SubscriptionTypeID = ? AND AccountID = ? ");
		if (status != null) {
			sb.append(" AND SubscriptionStatusID = ?");
		}
		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(sb.toString());
			ps.setLong(1, stype.getID());
			ps.setLong(2, accountPK.getID());
			if (status != null) ps.setLong(3, status.getID());
			rs = ps.executeQuery();
			while (rs.next()) {
				SubscriptionPK pk = new SubscriptionPK(rs.getLong("SubscriptionID"));
				v.addElement(pk);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"could not retrieve subscriptions",
				sqle,sb.toString(),
				accountPK.toString() + "," + stype.toString() + "," + 
				status != null ? status.toString() : "<null>");
		}
		finally {
			try {
				rs.close();
				ps.close();
				c.close();
			}
			catch (Exception slezy) { }
		}
		return v;
	}

	/**
	 * find subscriptions for account with a given subscription type.
	 */
	public static Collection selectSubscriptions(AccountPK accountPK,
			SubscriptionType stype) 
				throws DataAccessException {
		return selectSubscriptions(accountPK, stype, null);
	}

	/**
	 * find all subscriptions for this account.
	 */
	public static Collection selectAllSubscriptions(
			AccountPK accountPK) throws DataAccessException {
		Vector v = new Vector();
		StringBuffer sb = new StringBuffer(0);
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection c = null;
		sb.append("SELECT SUBSCRIPTIONID FROM SUBSCRIPTION WHERE ");
		sb.append(" AccountID = ? ");

		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(sb.toString());
			ps.setLong(1, accountPK.getID());
			rs = ps.executeQuery();
			while (rs.next()) {
				SubscriptionPK pk = new SubscriptionPK(rs.getLong("SubscriptionID"));
				v.addElement(pk);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"could not retrieve subscriptions",
				sqle,sb.toString(),
				accountPK.toString());
		}
		finally {
			try {
				rs.close();
				ps.close();
				c.close();
			}
			catch (Exception slezy) { }
		}
		return v;
	}
					
	public static AccountData createFromRS(ResultSet rs) 
			throws SQLException {
		AccountPK pk = new AccountPK(rs.getLong("AccountID"));
		CustomerPK custPK = 	
			new CustomerPK(rs.getLong("CustomerID"));
		BillableCustomerPK billablePK = 	
			new BillableCustomerPK(
				new CustomerPK(rs.getLong("BillableCustomerID")));
		AccountStatus status = 
			AccountStatus.getInstanceFor(rs.getLong("AccountStatusID"));
		AccountNumber number = 
				new AccountNumber(rs.getString("AccountNumber"));
		return new AccountData(pk, custPK, billablePK, number, status);

	}
	
	
}
