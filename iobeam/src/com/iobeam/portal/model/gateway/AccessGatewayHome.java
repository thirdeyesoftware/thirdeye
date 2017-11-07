package com.iobeam.portal.model.gateway;


import java.rmi.RemoteException;
import javax.ejb.*;


public interface AccessGatewayHome extends EJBHome {

	public static final String JNDI_NAME =
			"iobeam.portal.AccessGatewayHome";

	public AccessGateway create()
			throws CreateException, RemoteException;
}
