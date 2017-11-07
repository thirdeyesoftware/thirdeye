package com.iobeam.portal.task.vendible.managesubscription;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import java.rmi.RemoteException;

public interface ManageSubscriptionHome extends EJBHome {

	public static final String JNDI_NAME = 
		"iobeam.portal.ManageSubscriptionHome";

	public ManageSubscription create() throws CreateException, RemoteException;

}

