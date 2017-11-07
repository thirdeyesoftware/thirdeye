package com.iobeam.portal.task.billing.invoice.createinvoice;

import javax.ejb.EJBObject;
import java.rmi.*;
import java.util.Collection;
import com.iobeam.portal.model.invoice.*;
import com.iobeam.portal.model.account.*;
import com.iobeam.portal.model.billing.BillingException;
import com.iobeam.portal.model.billing.BillingPeriod;

public interface CreateInvoice extends EJBObject {

	public Invoice createInvoice(Account account, Collection accountEntries)
			throws BillingException, RemoteException;
	

	/**
	 * create invoices for the specified billing period.
	 */
	public Collection createInvoices(BillingPeriod period) 
			throws BillingException, RemoteException;

}


