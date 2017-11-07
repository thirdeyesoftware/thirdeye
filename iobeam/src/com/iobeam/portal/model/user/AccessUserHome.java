package com.iobeam.portal.model.user;


import java.rmi.RemoteException;
import javax.ejb.*;


public interface AccessUserHome extends EJBHome {

	public static final String JNDI_NAME = "iobeam.portal.AccessUserHome";

	public AccessUser create()
			throws CreateException, RemoteException;
}
