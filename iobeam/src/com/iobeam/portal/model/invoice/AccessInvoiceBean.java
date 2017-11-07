package com.iobeam.portal.model.invoice;

import javax.ejb.*;
import javax.naming.*;
import java.util.*;
import com.iobeam.portal.util.DataAccessException;
import com.iobeam.portal.model.billing.BillingPeriod;
import com.iobeam.portal.model.billablecustomer.BillableCustomerPK;
import com.iobeam.portal.model.account.AccountPK;

public class AccessInvoiceBean implements SessionBean  {

	private SessionContext mContext;

	public void setSessionContext(SessionContext context) {
		mContext = context;
	}
	public void unsetSessionContext() {
		mContext = null;
	}

	private SessionContext getSessionContext() {
		return mContext;
	}
	public void ejbCreate() throws CreateException {

	}

	public void ejbActivate() {
	
	}

	public void ejbPassivate() {

	}
	
	public void ejbRemove() {

	}

	public Invoice findByPrimaryKey(InvoicePK pk) 
			throws FinderException {
		Invoice invoice = null;	
		try {
			invoice = AccessInvoiceDAO.selectByPrimaryKey(pk);
		}
		catch (DataAccessException dae) {
			throw new FinderException(dae.toString());
		}
		return invoice;

	}

	/**
	 * find invoice by readable invoice number.
	 */
	public Invoice findByInvoiceNumber(InvoiceNumber number) 
			throws FinderException {
		try {
			return AccessInvoiceDAO.selectByInvoiceNumber(number);
		}
		catch (DataAccessException dae) {
			throw new FinderException(dae.toString());
		}
	}

	/**
	 * find all invoices specified by billing period
	 */
	public Collection findAllByBillingPeriod(BillingPeriod period) 
			throws FinderException {
		try {
			return AccessInvoiceDAO.selectAllByBillingPeriod(period);
		}
		catch (DataAccessException dae) {
			throw new FinderException(dae.toString());
		}
	}

	/**
	 * find invoice by specified account and billing period.
	 */
	public Invoice findByBillingPeriod(AccountPK pk, BillingPeriod period)
			throws FinderException {
		try {
			return AccessInvoiceDAO.selectByBillingPeriod(pk, period);
		}
		catch (DataAccessException dae) {
			throw new FinderException(dae.toString());
		}
	}

	public Collection findByBillableCustomerPK(BillableCustomerPK custPK, 
			BillingPeriod period) throws FinderException {
		Collection c;
		try {
			c = AccessInvoiceDAO.selectByBillableCustomerPK(
					custPK, period);
		}
		catch (Exception dae) {
			throw new FinderException(dae.toString());
		}
		return c;

	}

	public Collection findAllByBillableCustomerPK(BillableCustomerPK custPK)
			throws FinderException {
		Collection c;
		try {
			c = AccessInvoiceDAO.selectAllByBillableCustomerPK(custPK);
		}
		catch (Exception dae) {
			throw new FinderException(dae.toString());
		}
		return c;

	}

	public Collection findByAccountPK(AccountPK acctPK) 
			throws FinderException {
		Collection c;
		try {
			c = AccessInvoiceDAO.selectByAccountPK(acctPK);
		}
		catch (Exception dae) {
			throw new FinderException(dae.toString());
		}
		return c;
	}

	public Invoice create(AccountPK accountPK, Collection entries) 
			throws CreateException {
		Invoice newInvoice;
		try {
			newInvoice = 
				AccessInvoiceDAO.create(accountPK, entries);
		}
		catch (DataAccessException dae) {
			throw new CreateException(dae.toString());
		}
		return newInvoice;
	}

	public void update(Invoice invoice) {
		try {
			AccessInvoiceDAO.update(invoice);
		}
		catch (DataAccessException dae) {
			throw new EJBException(dae.toString());
		}
	}

	public void delete(InvoicePK pk) {
		try {
			AccessInvoiceDAO.delete(pk);
		}
		catch (DataAccessException dae) {
			throw new EJBException(dae.toString());
		}
	}

		

}
