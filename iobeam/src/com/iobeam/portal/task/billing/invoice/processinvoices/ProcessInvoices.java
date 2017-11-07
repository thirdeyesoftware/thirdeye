package com.iobeam.portal.task.billing.invoice.processinvoices;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

import com.iobeam.portal.util.Money;
import com.iobeam.portal.model.billing.BillingException;
import com.iobeam.portal.model.billing.*;
import com.iobeam.portal.model.billablecustomer.BillableCustomerPK;

public interface ProcessInvoices extends EJBObject {

	public void processInvoices(BillingPeriod period)
			throws BillingException, RemoteException;

}


