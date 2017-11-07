package com.iobeam.portal.task.customer.manageuser;


import java.rmi.*;
import javax.ejb.*;


public interface ManageUserHome extends EJBHome {

	public static final String JNDI_NAME = "iobeam.portal.ManageUserHome";


	public ManageUser create() throws CreateException, RemoteException;
}


