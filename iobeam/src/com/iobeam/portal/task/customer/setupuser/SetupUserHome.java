package com.iobeam.portal.task.customer.setupuser;


import java.rmi.*;
import javax.ejb.*;


public interface SetupUserHome extends EJBHome {

	public static final String JNDI_NAME = "iobeam.portal.SetupUserHome";


	public SetupUser create() throws CreateException, RemoteException;

}


