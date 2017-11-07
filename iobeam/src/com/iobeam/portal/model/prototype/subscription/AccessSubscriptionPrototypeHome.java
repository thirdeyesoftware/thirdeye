package com.iobeam.portal.model.prototype.subscription;


import java.rmi.RemoteException;
import javax.ejb.*;


public interface AccessSubscriptionPrototypeHome extends EJBHome {

	public static final String JNDI_NAME = 
			"iobeam.portal.AccessSubscriptionPrototypeHome";


	public AccessSubscriptionPrototype create()
			throws CreateException, RemoteException;
}
