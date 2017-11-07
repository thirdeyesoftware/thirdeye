package com.iobeam.portal.model.account;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;
import java.rmi.RemoteException;


public interface AccessAccountHome extends EJBHome {

	public static final String JNDI_NAME = "iobeam.portal.AccessAccountHome";

	public AccessAccount create() throws CreateException, RemoteException;
	
}
