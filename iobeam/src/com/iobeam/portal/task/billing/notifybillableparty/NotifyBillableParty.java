package com.iobeam.portal.task.billing.notifybillableparty;

import com.iobeam.portal.model.billing.Statement;
import com.iobeam.portal.model.billing.BillingPeriod;
import com.iobeam.portal.model.invoice.Invoice;
import com.iobeam.portal.task.billing.BillingNotificationException;

import javax.ejb.*;
import java.rmi.*;

public interface NotifyBillableParty extends EJBObject {

	public void notify(Invoice invoice) 
			throws BillingNotificationException, RemoteException;

	public void notify(Statement statement)
			throws BillingNotificationException, RemoteException;
	
	public void notifyInvoices(BillingPeriod period) 
			throws BillingNotificationException, RemoteException;
	
}

	
