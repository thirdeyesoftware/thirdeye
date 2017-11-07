package com.iobeam.portal.task.billing.invoice.processinvoices;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;
import java.rmi.RemoteException;

public interface ProcessInvoicesHome extends EJBHome {

	public static final String JNDI_NAME = 
		"iobeam.portal.ProcessInvoicesHome";

	public ProcessInvoices create() 
			throws CreateException, RemoteException;
}

