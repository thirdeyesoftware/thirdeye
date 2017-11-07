package com.iobeam.portal.task.actor.user.signon;


import javax.ejb.EJBHome;
import javax.ejb.CreateException;
import java.rmi.RemoteException;


public interface SignonUserHome extends EJBHome {

	public static final String JNDI_NAME = "iobeam.portal.SignonUserHome";

	/**
	*/
	public SignonUser create() throws CreateException, RemoteException;
	
}
