package com.iobeam.portal.task.billing.statement.processstatements;

import java.rmi.RemoteException;
import javax.ejb.EJBObject;

import com.iobeam.portal.model.billing.BillingPeriod;
import com.iobeam.portal.model.billing.BillingException;

public interface ProcessStatements extends EJBObject {

	/**
	 * processes statemets for accounts with activity during the
	 * specified billing period.
	 */
	public void process(BillingPeriod period) 
			throws BillingException, RemoteException;
}

