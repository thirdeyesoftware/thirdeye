package com.iobeam.portal.model.invoice;

import javax.sql.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import javax.ejb.*;
import javax.naming.*;
import java.rmi.RemoteException;
import com.iobeam.portal.util.*;
import com.iobeam.portal.model.account.*;
import com.iobeam.portal.model.billing.BillingPeriod;
import com.iobeam.portal.model.customer.CustomerPK;
import com.iobeam.portal.model.billablecustomer.BillableCustomerPK;


public class AccessInvoiceDAO {

	private static final String SELECT_INVOICE = 
		"select i.invoiceid, i.accountid, i.invoicedate, " + 
		" i.invoicestatusid, i.invoicenumber,"+
		" i.amountdue from invoice i, Account a " + 
		" WHERE a.accountid = i.accountid";
	
	private static final String SELECT_ITEMS = 
		"select InvoiceLineItemID, InvoiceID, AccountEntryID " + 
		" from INVOICE_LINE_ITEM WHERE InvoiceID = ?"; 

	private static final String INSERT_INVOICE = 
		"insert into INVOICE (InvoiceID, InvoiceNumber, AccountID, " +
		"InvoiceDate, AmountDue, InvoiceStatusId) values (?,?,?,?,?,?)";

	private static final String INSERT_ITEM = 
		"insert into INVOICE_LINE_ITEM (InvoiceLineItemID, InvoiceID, " + 
		"AccountEntryID) values(?,?,?)";

	private static final String DELETE_INVOICE = 	
		"delete from INVOICE where InvoviceID = ?";
	
	private static final String DELETE_ITEMS = 
		"delete from INVOICE_LINE_ITEM where InvoiceID = ?";

	private static final String UPDATE_INVOICE = 
		"update INVOICE set InvoiceNumber = ?, AccountID = ?, InvoiceDate = ?, " +
		"AmountDue = ?, InvoiceStatusId = ? where InvoiceID = ?";
	
	private static final String UPDATE_ITEM = 
		"update INVOICE_LINE_ITEM set AccountEntryID = ? where " + 
		"InvoiceLineItemID = ?";
	
	public static Invoice selectByPrimaryKey(InvoicePK pk) 
			throws DataAccessException {
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = SELECT_INVOICE + " and InvoiceID = ?";

		Invoice invoice = null;
		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(sql);
			ps.setLong(1, pk.getID());
			rs = ps.executeQuery();
			if (rs.next()) {
				invoice = createFromRS(rs);
				invoice.setInvoiceLineItems(selectInvoiceLineItems(c, invoice.getPK()));
			}
			else {
				throw new DataNotFoundException (
					"no data found for key.", sql, pk.toString());
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"unable to retrieve invoice.", sqle, sql, pk.toString());
		}
		finally {
			try {
				rs.close();
				ps.close();
				c.close();
			}
			catch (Exception eee) { }
		}
		return invoice;
	}

	public static Invoice selectByInvoiceNumber(InvoiceNumber number) 
			throws DataAccessException {
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = SELECT_INVOICE + " and InvoiceNumber = ?";

		Invoice invoice = null;
		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(sql);
			ps.setString(1, number.getInvoiceNumber());
			rs = ps.executeQuery();
			if (rs.next()) {
				invoice = createFromRS(rs);
				invoice.setInvoiceLineItems(selectInvoiceLineItems(c, invoice.getPK()));
			}
			else {
				throw new DataNotFoundException (
					"no data found for key.", sql, number.toString());
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"unable to retrieve invoice.", sqle, sql, number.toString());
		}
		finally {
			try {
				rs.close();
				ps.close();
				c.close();
			}
			catch (Exception eee) { }
		}
		return invoice;
	}

	public static Collection selectByAccountPK(AccountPK pk) 
			throws DataAccessException {
		Vector v = new Vector();
		PreparedStatement ps = null;
		Connection c = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer(0);
		sb.append(SELECT_INVOICE).append(" and A.AccountID = ?");
		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(sb.toString());
			ps.setLong(1, pk.getID());
			rs = ps.executeQuery();
			while (rs.next()) {
				Invoice invoice = createFromRS(rs);
				invoice.setInvoiceLineItems(selectInvoiceLineItems(c, invoice.getPK()));
				v.addElement(invoice);
			}
		}
		catch (SQLException sqle) { 
			throw new DataAccessException(
				"unable to retrieve invoices.", sqle, sb.toString(), pk.toString());
		}
		finally {
			try {
				rs.close();
				ps.close();
				c.close();
			}
			catch (Exception ee) { }
		}
	
		return v;
	}

	public static Collection selectAllByBillingPeriod(BillingPeriod period) 
			throws DataAccessException {
		Vector v = new Vector();
		PreparedStatement ps = null;
		Connection c = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer(0);
		sb.append(SELECT_INVOICE).append(" and ");
		sb.append("(i.invoicedate >= ? and i.invoicedate <= ?)");
		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(sb.toString());
			ps.setTimestamp(1, new Timestamp(period.getStartDate().getTime()));
			ps.setTimestamp(2, new Timestamp(period.getEndDate().getTime()));

			rs = ps.executeQuery();
			while (rs.next()) {
				Invoice invoice = createFromRS(rs);
				invoice.setInvoiceLineItems(selectInvoiceLineItems(c, invoice.getPK()));
				Logger.getLogger("com.iobeam.portal.model.invoice").info(
					"invoice = " + invoice.toString());

				v.addElement(invoice);
			}
		}
		catch (SQLException sqle) { 
			throw new DataAccessException(
				"unable to retrieve invoices.", sqle, sb.toString(), period.toString());
		}
		finally {
			try {
				rs.close();
				ps.close();
				c.close();
			}
			catch (Exception ee) { }
		}
	
		return v;
	}

	public static Invoice selectByBillingPeriod(AccountPK pk, BillingPeriod period) 
			throws DataAccessException {
		Invoice invoice = null;
		PreparedStatement ps = null;
		Connection c = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer(0);
		sb.append(SELECT_INVOICE).append(" and A.AccountID = ? AND ");
		sb.append("(invoicedate >= ? and invoicedate <= ?)");
		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(sb.toString());
			ps.setLong(1, pk.getID());
			ps.setTimestamp(2, new Timestamp(period.getStartDate().getTime()));
			ps.setTimestamp(3, new Timestamp(period.getEndDate().getTime()));

			rs = ps.executeQuery();
			if (rs.next()) {
				invoice = createFromRS(rs);
				invoice.setInvoiceLineItems(selectInvoiceLineItems(c, invoice.getPK()));
			}
		}
		catch (SQLException sqle) { 
			throw new DataAccessException(
				"unable to retrieve invoices.", sqle, sb.toString(), pk.toString());
		}
		finally {
			try {
				rs.close();
				ps.close();
				c.close();
			}
			catch (Exception ee) { }
		}
	
		return invoice;
	}

	public static Collection selectAllByBillableCustomerPK(BillableCustomerPK pk) 
			throws DataAccessException {
		Vector v = new Vector();
		PreparedStatement ps = null;
		Connection c = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer(0);

		sb.append(SELECT_INVOICE).append(" and A.BillableCustomerID = ?");
		
		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(sb.toString());
			ps.setLong(1, pk.getID());
			rs = ps.executeQuery();
			while (rs.next()) {
				Invoice invoice = createFromRS(rs);
				invoice.setInvoiceLineItems(selectInvoiceLineItems(c, invoice.getPK()));
				v.addElement(invoice);
			}
		}
		catch (SQLException sqle) { 
			throw new DataAccessException(
				"unable to retrieve invoices.", sqle, sb.toString(), pk.toString());
		}
		finally {
			DBHelper.close(c, ps, rs);
		}
	
		return v;
	}
	
	public static Collection selectByBillableCustomerPK(BillableCustomerPK pk,
			BillingPeriod period) throws DataAccessException {
		Vector v = new Vector();
		PreparedStatement ps = null;
		Connection c = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer(0);
		sb.append(SELECT_INVOICE).append(" and A.BillableCustomerID = ? and ");
		sb.append(" I.InvoiceDate >= ? AND I.InvoiceDate < ?");
		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(sb.toString());
			ps.setLong(1, pk.getID());
			ps.setTimestamp(2, new Timestamp(period.getStartDate().getTime()));
			ps.setTimestamp(3, new Timestamp(period.getEndDate().getTime()));
			rs = ps.executeQuery();
			while (rs.next()) {
				Invoice invoice = createFromRS(rs);
				invoice.setInvoiceLineItems(selectInvoiceLineItems(c, invoice.getPK()));
				v.addElement(invoice);
			}
		}
		catch (SQLException sqle) { 
			throw new DataAccessException(
				"unable to retrieve invoices.", sqle, sb.toString(), pk.toString());
		}
		finally {
			DBHelper.close(c, ps, rs);
		}
	
		return v;
	}

	private static Collection selectInvoiceLineItems(Connection c, InvoicePK pk) 
			throws SQLException {
		String sql = SELECT_ITEMS;
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setLong(1, pk.getID());
		ResultSet rs = ps.executeQuery();
		Vector v = new Vector();
		AccessAccountHome aaHome = null;
		AccessAccount aa = null;
		InitialContext ic = null;
		try {
			ic = new InitialContext();
			aaHome = (AccessAccountHome)ic.lookup(AccessAccountHome.JNDI_NAME);
			aa = aaHome.create();
			
			while (rs.next()) {
				InvoiceLineItem item = new InvoiceLineItem(
					rs.getLong("InvoiceLineItemID"),
					aa.findAccountEntry(rs.getLong("AccountEntryID")));
			  	
				v.addElement(item);
			}
		}
		catch (FinderException fe) {
			throw new EJBException(fe.toString());
		}
		catch (CreateException ce) {
			throw new EJBException(ce.toString());
		}
		catch (NamingException e) {
			throw new EJBException(e.toString());
		}
		catch (RemoteException re) {
			throw new EJBException(re.toString());
		}
		return v;
	}

	/**
	 * creates a new invoice
	 */
	public static Invoice create(AccountPK accountPK, Collection acctEntries) 
			throws DataAccessException {
		
		String sql = INSERT_INVOICE;
		PreparedStatement ps = null;
		Connection c = null;
		InvoicePK invoicePK = null;
		java.util.Date invoiceDate = new java.util.Date();
		long key = BlindKey.getNextKey("INVOICE_SEQ");
		InvoiceNumber invoiceNumber = new InvoiceNumber(
			ReadableNumberGenerator.getReadableNumber(key));
		double totalDue = 0;

		for (Iterator it = acctEntries.iterator();it.hasNext();) {

			AccountEntry entry = (AccountEntry)it.next();
			totalDue = totalDue + entry.getAmount().getAmount();
			Logger.getLogger("com.iobeam.portal.model.invoice").info(
				"line item " + entry.getID() + " amount = " + entry.getAmount().getAmount());

		}

		Logger.getLogger("com.iobeam.portal.model.invoice").info(
			"invoice " + invoiceNumber + " = " + totalDue);

		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(sql);
			ps.setLong(1, key);
			ps.setString(2, invoiceNumber.getInvoiceNumber());
			ps.setLong(3, accountPK.getID());
			ps.setTimestamp(4, new Timestamp(invoiceDate.getTime()));
			ps.setDouble(5, totalDue);

			Logger.getLogger("com.iobeam.portal.model.invoice").info(
				"" + Double.toString(totalDue));

			ps.setInt(6, InvoiceStatus.INVOICE_PENDING_PROCESS.getID());
			if (ps.executeUpdate() < 1) {
				throw new DataAccessException(
					"could not create invoice.", sql, accountPK.toString());
			}
			invoicePK = new InvoicePK(key);
			createInvoiceLineItems(invoicePK, acctEntries);
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"could not create invoice.", sqle, sql, accountPK.toString());
		}
		finally {
			try {
				ps.close();
				c.close();
			}
			catch (Exception eze) { }
		}
		return new Invoice(
			invoicePK, invoiceNumber, invoiceDate, accountPK, new Money(totalDue), 
			InvoiceStatus.INVOICE_PENDING_PROCESS);
	}

	/**
	 * creates invoice line items from account entries and returns
	 * collection of invoice line items.
	 */
	private static Collection createInvoiceLineItems(InvoicePK pk,
			Collection accountEntries) throws DataAccessException {

		if (pk == null) throw new NullPointerException("Invoice PK");
		String sql = INSERT_ITEM;
		Connection c = null;
		PreparedStatement ps = null;
		Vector items = new Vector();
		long key = 0;
		InvoiceLineItem invoiceLineItem = null;
		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(sql);
			AccountEntry entry = null;
			for (Iterator it = accountEntries.iterator(); it.hasNext();) {
				entry = (AccountEntry)it.next();
				ps.setLong(1, key = BlindKey.getNextKey("INVOICE_LINE_ITEM_SEQ"));
				ps.setLong(2, pk.getID());
				ps.setLong(3, entry.getID());
				if (ps.executeUpdate() < 1) {
					throw new DataAccessException(
						"could not create invoice line item from account entry",
						sql, entry.toString());
				}
				invoiceLineItem = new InvoiceLineItem(key, entry);
				items.addElement(invoiceLineItem);
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"could not create invoice line item.", sqle, sql, pk.toString());
		}
		finally {
			try {
				ps.close();
				c.close();
			}
			catch (Exception ee) { }
		}
		return items;
	}

	public static void update(Invoice invoice) throws DataAccessException {
		PreparedStatement ps = null;
		Connection c = null;
		String sql = UPDATE_INVOICE;
		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(sql);
			ps.setString(1, invoice.getInvoiceNumber().getInvoiceNumber());
			ps.setLong(2, invoice.getAccountPK().getID());
			ps.setTimestamp(3, new Timestamp(invoice.getInvoiceDate().getTime()));
			ps.setDouble(4, invoice.getTotalDue().getAmount());
			ps.setInt(5, invoice.getInvoiceStatus().getID());
			ps.setLong(6, invoice.getPK().getID());

			if (ps.executeUpdate() < 1) {
				throw new DataAccessException(
					"could not update invoice.", sql, invoice.toString());
			}

			updateInvoiceLineItems(c, invoice.getInvoiceLineItems());
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"could not update invoice", sqle, sql, invoice.toString());
		}
		finally {
			try {
				ps.close();
				c.close();
			}
			catch (Exception ze) { }
		}
	
	}

	private static void updateInvoiceLineItems(Connection c, Collection items)
			throws DataAccessException {
		String sql = UPDATE_ITEM;
		try {
			PreparedStatement ps = c.prepareStatement(sql);
			for (Iterator it = items.iterator();it.hasNext();) {
				InvoiceLineItem item = (InvoiceLineItem)it.next();
				ps.setLong(1, item.getAccountEntry().getID());
				ps.setLong(2, item.getID());
				if (ps.executeUpdate() < 1) {
					throw new DataAccessException(
						"could not update line item.", sql, item.toString());
				}
			}
			ps.close();
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"could not update line items.", sqle, sql);
		}

	}

	public static void delete(InvoicePK pk) throws DataAccessException {

		String sql = DELETE_ITEMS;
		PreparedStatement ps = null;
		Connection c = null;
		try {
			c = DBHelper.getConnection();
			ps = c.prepareStatement(sql);
			ps.setLong(1, pk.getID());
			if (ps.executeUpdate() < 1) {
				throw new DataAccessException(
					"could not delete invoice items.",sql, pk.toString());
			}
			ps.close();
			sql = DELETE_INVOICE;
			ps = c.prepareStatement(sql);
			ps.setLong(1, pk.getID());
			if (ps.executeUpdate() < 1) {
				throw new DataAccessException(
					"could not delete invoice.", sql, pk.toString());
			}
		}
		catch (SQLException sqle) {
			throw new DataAccessException(
				"could not delete invoice.", sqle, sql, pk.toString());
		}
		finally {
			try {
				ps.close();
				c.close();
			}
			catch (Exception eze) { }
		}
	}

	private static Invoice createFromRS(ResultSet rs)
				 throws SQLException {
		InvoicePK pk = new InvoicePK(rs.getLong("InvoiceID"));
		InvoiceNumber number = 
			new InvoiceNumber( rs.getString("InvoiceNumber"));
		java.util.Date date = new java.util.Date(
				rs.getTimestamp("InvoiceDate").getTime());
		AccountPK acctPK = new AccountPK(rs.getLong("AccountID"));

		Money amountDue = new Money(rs.getDouble("AmountDue"));
		InvoiceStatus status = InvoiceStatus.getInstanceFor(
				rs.getInt("InvoiceStatusID"));

		return new Invoice(pk, number, date, acctPK, amountDue, status);

	}

}



