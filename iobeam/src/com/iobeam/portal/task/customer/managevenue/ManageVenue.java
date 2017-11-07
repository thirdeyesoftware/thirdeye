package com.iobeam.portal.task.customer.managevenue;


import java.util.*;
import java.rmi.RemoteException;
import javax.ejb.*;
import com.iobeam.portal.model.gateway.Gateway;
import com.iobeam.portal.model.subscription.*;
import com.iobeam.portal.model.customercontact.CustomerContact;
import com.iobeam.portal.security.User;
import com.iobeam.portal.security.NoSuchVenueException;
import com.iobeam.portal.model.venue.*;


public interface ManageVenue extends EJBObject {

	public Venue createVenue(String venueName, 
		VenueType venueType, CustomerContact contact)
				throws CreateException, RemoteException;


	public User createVenueOperator(Venue venue, String uname, char[] pwd,
			CustomerContact contact) throws CreateException, RemoteException;

	
	public void removeVenueOperator(Venue venue, User venueOperator) 
			throws RemoteException;
	

	/**
	* Sets Gateway information describing the Gateway hardware
	* in use at the Venue referenced by the specified Gateway instance.
	*/
	public void setGateway(Gateway gw) throws RemoteException;


	/**
	* Removes the specified Venue and all unreferenced dependencies
	* from the system.
	*/
	public void removeVenue(VenuePK venuePK) throws RemoteException;


	/**
	* Returns a Map of VenuePKs by respective venueName.
	*/
	public Map getVenueNameMap() throws RemoteException;


	/**
	* Returns a Collection of all active-status Subscriptions for the
	* specified SubscriptionType and Venue.
	*/
	public Collection getActiveSubscriptions(VenuePK venuePK,
			SubscriptionType subscriptionType)
			throws RemoteException;


	/**
	* Returns a Collection of all created-status Subscriptions for the
	* specified SubscriptionType and Venue.
	*/
	public Collection getCreatedSubscriptions(VenuePK venuePK,
			SubscriptionType subscriptionType)
			throws RemoteException;


	/**
	* Generates count Subscriptions in the Account enclosing the
	* specified generative Venue Subscription.
	*
	* @exception SubscriptionException the specified Subscription is
	* not generative.
	*
	* @exception ExhaustedGeneratorSubscriptionException the specified
	* Subscription has no remaining generation capacity to satisfy the
	* request.
	*/
	public void generateSubscriptions(SubscriptionPK generator, int count)
			throws SubscriptionException, RemoteException;


	/**
	* Activates the specified Gateway as the Gateway for its Venue.
	* This is a one-time operation for a new Gateway being installed
	* at a venue.
	*
	* @throws NoSuchVenueException if the Gateway's venuePK is not
	* known to the system.
	*/
	public void installGateway(Gateway gateway)
			throws NoSuchVenueException, RemoteException;
}
