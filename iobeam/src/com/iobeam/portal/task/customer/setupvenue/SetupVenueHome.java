package com.iobeam.portal.task.customer.setupvenue;


import java.rmi.*;
import javax.ejb.*;


public interface SetupVenueHome extends EJBHome {

	public static final String JNDI_NAME = "iobeam.portal.SetupVenueHome";


	public SetupVenue create() throws CreateException, RemoteException;

}


