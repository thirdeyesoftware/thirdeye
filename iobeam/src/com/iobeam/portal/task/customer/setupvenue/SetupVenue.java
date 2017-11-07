package com.iobeam.portal.task.customer.setupvenue;


import java.util.Collection;
import java.rmi.RemoteException;
import javax.ejb.*;
import com.iobeam.portal.model.customercontact.CustomerContact;
import com.iobeam.portal.model.venue.Venue;
import com.iobeam.portal.model.billing.PaymentInstrument;
import com.iobeam.portal.model.prototype.subscription.*;


public interface SetupVenue extends EJBObject {


	/**
	* Returns a Collection of all available SubscriptionPrototypes for
	* private Venues.
	*/
	public Collection getPrivateVenueSubscriptionPrototypes()
			throws RemoteException;


	/**
	* Returns a Collection of all available SubscriptionPrototypes for
	* public Venues.
	*/
	public Collection getPublicVenueSubscriptionPrototypes()
			throws RemoteException;


	/**
	* Sets up a new private Venue according to the specified information.
	*/
	public Venue setupPrivateVenue(String venueName,
			CustomerContact customerContact,
			PaymentInstrument paymentInstrument,
			PrivateVenueSubscriptionPrototype subscriptionPrototype)
			throws RemoteException;


	/**
	* Sets up a new public Venue according to the specified information.
	*/
	public Venue setupPublicVenue(String venueName,
			CustomerContact customerContact,
			PaymentInstrument paymentInstrument,
			PublicVenueSubscriptionPrototype subscriptionPrototype)
			throws RemoteException;
}

