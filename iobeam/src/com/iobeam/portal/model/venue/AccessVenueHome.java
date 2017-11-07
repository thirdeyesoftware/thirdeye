package com.iobeam.portal.model.venue;


import java.rmi.RemoteException;
import javax.ejb.*;


public interface AccessVenueHome extends EJBHome {

	public static final String JNDI_NAME = "iobeam.portal.AccessVenueHome";

	public AccessVenue create()
			throws CreateException, RemoteException;
}
