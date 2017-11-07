package com.iobeam.portal.task.actor.gateway.register;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;
import java.rmi.RemoteException;

public interface RegisterGatewayHome extends EJBHome {
	public static String JNDI_NAME = "iobeam.portal.RegisterGatewayHome";

	public RegisterGateway create() throws CreateException, RemoteException;

}

