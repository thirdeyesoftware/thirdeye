package com.iobeam.portal.model.subscription;


import java.rmi.RemoteException;
import javax.ejb.*;


public interface AccessSubscriptionHome extends EJBHome {
	public static final String JNDI_NAME = 
			"iobeam.portal.AccessSubscriptionHome";
			
	public AccessSubscription create()
			throws CreateException, RemoteException;
}
