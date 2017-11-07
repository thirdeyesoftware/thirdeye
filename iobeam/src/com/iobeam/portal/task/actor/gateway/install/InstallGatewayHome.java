package com.iobeam.portal.task.actor.gateway.install;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import java.rmi.RemoteException;

public interface InstallGatewayHome extends EJBHome {

	public InstallGateway create() 
			throws CreateException, RemoteException;
}

