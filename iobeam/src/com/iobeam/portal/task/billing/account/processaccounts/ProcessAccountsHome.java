package com.iobeam.portal.task.billing.account.processaccounts;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;
import java.rmi.RemoteException;

public interface ProcessAccountsHome extends EJBHome {

	public static final String JNDI_NAME = "iobeam.portal.ProcessAccountsHome";

	public ProcessAccounts create() 
			throws CreateException, RemoteException;
}

