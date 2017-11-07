package com.iobeam.portal.model.invoice;

import com.iobeam.portal.model.EntityAccessor;
import com.iobeam.portal.model.billing.BillingPeriod;
import com.iobeam.portal.model.billablecustomer.BillableCustomerPK;
import com.iobeam.portal.model.account.AccountPK;

import javax.ejb.*;
import java.rmi.RemoteException;
import java.util.Collection;

public interface AccessInvoice extends EntityAccessor, EJBObject {

	public Invoice findByPrimaryKey(InvoicePK pk) 
			throws FinderException, RemoteException;

	/**
	 * find invoice by specified invoice number.
	 */
	public Invoice findByInvoiceNumber(InvoiceNumber number) 
			throws FinderException, RemoteException;

	/**
	 * find invoices by specified billable customer pk and 
	 * billing period.
	 */
	public Collection findByBillableCustomerPK(BillableCustomerPK custPK,
				BillingPeriod period) 
			throws FinderException, RemoteException;

	/**
	 * find all invoices by billable customer pk.
	 */
	public Collection findAllByBillableCustomerPK(BillableCustomerPK custPK)
			throws FinderException, RemoteException;
		
	/**
	 * find all invoices for specified account pk.
	 */
	public Collection findByAccountPK(AccountPK pk) 
			throws FinderException, RemoteException;

	/**
	 * find all invoices for specified billing period.
	 */
	public Collection findAllByBillingPeriod(BillingPeriod period) 
			throws FinderException, RemoteException;

	/**
	 * find invoice for specified account and billing period.
	 */
	public Invoice findByBillingPeriod(AccountPK pk, BillingPeriod period) 
			throws FinderException, RemoteException;

	/**
	 * create invoice.
	 */
	public Invoice create(AccountPK acctPK, 
			Collection accountEntries) throws CreateException, RemoteException;

	/**
	 * update invoice.
	 */
	public void update(Invoice invoice)
		throws RemoteException;

	/**
	 * delete invoice using specified invoice pk.
	 */
	public void delete(InvoicePK pk) 
		throws RemoteException;

}
