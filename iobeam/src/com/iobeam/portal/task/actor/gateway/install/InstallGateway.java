package com.iobeam.portal.task.actor.gateway.install;


import javax.ejb.EJBObject;
import java.rmi.RemoteException;

import com.iobeam.portal.model.gateway.Gateway;
import com.iobeam.portal.security.NoSuchVenueException;


public interface InstallGateway extends EJBObject {

	/**
	* Activates the specified Gateway as the Gateway for its Venue.
	* This is a one-time operation for a new Gateway being installed
	* at a venue.
	*
	* @throws NoSuchVenueException if the Gateway's venuePK is not
	* known to the system.
	*/
	public void install(Gateway gateway) throws RemoteException,
			NoSuchVenueException;
}

