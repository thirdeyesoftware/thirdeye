package com.iobeam.portal.task.gateway.manageusercontact;


import javax.ejb.EJBHome;
import javax.ejb.CreateException;
import java.rmi.RemoteException;


public interface ManageUserContactHome extends EJBHome {

	public static final String JNDI_NAME =
			"iobeam.portal.ManageUserContactHome";

	/**
	*/
	public ManageUserContact create() throws CreateException, RemoteException;
	
}
