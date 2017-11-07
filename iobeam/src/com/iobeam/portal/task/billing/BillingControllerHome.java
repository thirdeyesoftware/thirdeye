package com.iobeam.portal.task.billing;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;
import java.rmi.RemoteException;

public interface BillingControllerHome extends EJBHome {
	public static final String JNDI_NAME = "iobeam.portal.BillingControllerHome";

	public BillingController create() throws CreateException, RemoteException;
}

