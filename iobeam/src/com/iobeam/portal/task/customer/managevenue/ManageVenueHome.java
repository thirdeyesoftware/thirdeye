package com.iobeam.portal.task.customer.managevenue;


import java.rmi.*;
import javax.ejb.*;


public interface ManageVenueHome extends EJBHome {

	public static final String JNDI_NAME = "iobeam.portal.ManageVenueHome";


	public ManageVenue create() throws CreateException, RemoteException;
}


