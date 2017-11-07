package com.iobeam.portal.task.gateway.leasenotify;


import javax.ejb.EJBHome;
import javax.ejb.CreateException;
import java.rmi.RemoteException;


public interface NotifyGatewayHome extends EJBHome {

	public static final String JNDI_NAME =
			"iobeam.portal.NotifyGatewayHome";

	/**
	*/
	public NotifyGateway create() throws CreateException, RemoteException;
	
}
