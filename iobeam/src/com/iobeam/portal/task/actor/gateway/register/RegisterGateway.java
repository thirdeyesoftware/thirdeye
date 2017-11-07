package com.iobeam.portal.task.actor.gateway.register;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;
import java.net.InetAddress;
import java.util.Collection;

import com.iobeam.portal.model.venue.VenuePK;
import com.iobeam.portal.model.subscription.*;
import com.iobeam.portal.security.*;
import com.iobeam.util.MACAddress;

public interface RegisterGateway extends EJBObject {

	/**
	* Registers the gateway described by the specified MAC
	* as Gateway for the specified venue.
	*
	* @param venuePK the VenuePK for the Venue where the Gateway is
	* located.
	*
	* @param gatewayMAC the MACAddress of the nic facing the Gateway's
	* backhaul.
	*
	* @param publicIP the InetAddress of the public (backhaul) interface
	* at the Gateway.
	*
	* @param notifyPort the Gateway's listen port for lease notification
	* from the Portal.
	*
	* @param privateIP the InetAddress of the private (wireless) interface
	* at the Gateway.
	*
	* @exception NoSuchGatewayException the specified Venue does not
	* have an installed Gateway known to the system.
	*
	* @exception NoSuchVenueException the specified Venue does not exist.
	*
	* @exception IllegalGatewayException the specified Gateway MAC does
	* not match the installed Gateway MAC.
	*/
	public void register(VenuePK venuePK, MACAddress gatewayMAC,
			InetAddress publicIP, int notifyPort, InetAddress privateIP)
			throws NoSuchGatewayException, NoSuchVenueException,
			IllegalGatewayException, RemoteException;


	public Collection getRoutingRules(VenuePK venuePK)
			throws NoSuchVenueException, RemoteException;


	/**
	* Returns the venue Subscription for the spcecified VenuePK.
	*
	* @exception NoSuchVenueException the specified Venue does not exist.
	*
	* @exception SubscriptionUnavailableException this Venue has no
	* Subscription matching its VenueType.
	*/
	public Subscription getVenueSubscription(VenuePK venuePK)
			throws NoSuchVenueException,
			SubscriptionException, RemoteException;
}

