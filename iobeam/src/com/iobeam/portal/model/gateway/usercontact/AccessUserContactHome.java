package com.iobeam.portal.model.gateway.usercontact;


import java.rmi.RemoteException;
import javax.ejb.*;


public interface AccessUserContactHome extends EJBHome {

	public static final String JNDI_NAME =
			"iobeam.portal.AccessUserContactHome";

	public AccessUserContact create()
			throws CreateException, RemoteException;
}
