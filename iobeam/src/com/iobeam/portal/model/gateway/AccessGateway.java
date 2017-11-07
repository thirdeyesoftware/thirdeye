package com.iobeam.portal.model.gateway;


import java.rmi.RemoteException;
import javax.ejb.*;
import java.net.InetAddress;

import com.iobeam.util.MACAddress;
import com.iobeam.portal.model.EntityAccessor;
import com.iobeam.portal.model.venue.VenuePK;


public interface AccessGateway extends EntityAccessor, EJBObject {

	/**
	* Returns the Gateway instance for the specified Venue.
	* Venues that are not fully online may not yet have a Gateway.
	*/
	public Gateway findByVenuePK(VenuePK venuePK)
			throws FinderException, RemoteException;


	/**
	* Returns true if the specified Venue has an installed Gateway.
	*/
	public boolean hasGateway(VenuePK venuePK) throws RemoteException;


	/**
	* returns Gateway using the specified ipaddress.
	*/
	public Gateway findByIPAddress(InetAddress address) 
			throws FinderException, RemoteException;

	/**
	* returns Gateway using the specified mac addr.
	*/
	public Gateway findByMACAddress(MACAddress mac) 
			throws FinderException, RemoteException;

	/**
	* Throws DuplicateKeyException if the Venue already
	* has a Gateway instance.
	*/
	public Gateway create(Gateway gateway)
			throws CreateException, RemoteException;


	/**
	* Removes any Gateway associated with the specified Venue.
	*/
	public void remove(VenuePK venuePK) throws RemoteException;


	/**
	* Replaces an existing Gateway with the specified Gateway data.
	* Throws BusinessLogicError if there is no Gateway with the
	* specified venueName.
	*/
	public void update(Gateway gateway) throws RemoteException;
}
