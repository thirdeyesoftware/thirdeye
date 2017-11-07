package com.iobeam.portal.model.country;


import java.rmi.RemoteException;
import javax.ejb.*;


public interface AccessCountryHome extends EJBHome {

	public static final String JNDI_NAME =
			"iobeam.portal.AccessCountryHome";

	public AccessCountry create()
			throws CreateException, RemoteException;
}
