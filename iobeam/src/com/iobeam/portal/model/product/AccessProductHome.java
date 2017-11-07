package com.iobeam.portal.model.product;


import java.rmi.RemoteException;
import javax.ejb.*;

/**
*/
public interface AccessProductHome extends EJBHome {

	public static final String JNDI_NAME = "iobeam.portal.AccessProductHome";

	public AccessProduct create()
			throws CreateException, RemoteException;


}
