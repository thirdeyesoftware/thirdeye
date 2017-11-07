package com.iobeam.portal.task.billing.account.processaccounts;

import com.iobeam.portal.model.billing.BillingException;
import com.iobeam.portal.model.billing.BillingPeriod;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

public interface ProcessAccounts extends EJBObject {

	/**
	 * process accounts to create account entries for vendibles such
	 * as subscriptions.
	 */
	public void processAccounts(BillingPeriod period) 
			throws BillingException, RemoteException;
	
}


