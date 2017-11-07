package com.iobeam.portal.model.venue;


import java.util.Map;
import java.util.Collection;
import java.rmi.RemoteException;
import javax.ejb.*;
import com.iobeam.portal.model.EntityAccessor;
import com.iobeam.portal.security.User;
import com.iobeam.portal.model.customer.*;
import com.iobeam.portal.model.gateway.*;
import com.iobeam.portal.model.subscription.SubscriptionException;
import com.iobeam.portal.security.NoSuchVenueException;

public interface AccessVenue extends EntityAccessor, EJBObject {

	/**
	* Returns the Venue with the specified pk.
	* Throws FinderException if there is none.
	*/
	public Venue findByPrimaryKey(VenuePK venuePK)
			throws FinderException, RemoteException;


	/**
	* Returns the Venue with the specified CustomerPK.
	* Throws FinderException if there is none.
	*/
	public Venue findByCustomerPK(CustomerPK customerPK)
			throws FinderException, RemoteException;


	/**
	* Creates a new Venue with the specified venueName, linked to the
	* specified Customer.
	*
	* Throws DuplicateKeyException if the venueName already
	* exists.
	*
	* Throws BusinessLogicError if there is another Venue or
	* User with the same CustomerPK.
	*/
	public Venue createVenue(String venueName, VenueType venueType,
			Customer customer) throws CreateException, RemoteException;


	/**
	* Removes the specified Venue from the system.
	*/
	public void removeVenue(Venue venue)
			throws RemoteException, RemoveException;

	/**
	* Returns a Collection of VenueOperators for the specified
	* Venue.
	*/
	public Collection findVenueOperators(VenuePK venuePK)
			throws FinderException, RemoteException;


	/**
	* Returns true if the specified Venue has a VenueOperator
	* with the specified userName.
	*/
	public boolean hasVenueOperator(User user)
			throws RemoteException;


	/**
	* Adds the specified User as a VenueOperator on the specified Venue.
	* Throws SubscriptionException if there are no more available slots
	* for VenueOperators.
	*/
	public void addVenueOperator(VenuePK venuePK, User user)
			throws SubscriptionException, RemoteException;


	/**
	* Sets the specified Gateway as the Gateway instance for its Venue.
	*
	* Throws NoSuchVenueException if the Venue does not exist.
	*/
	public void installGateway(Gateway gateway)
			throws NoSuchVenueException, RemoteException;


	/**
	* Returns a Map of VenuePKs keyed on respective venueNames.
	*/
	public Map getVenueNameMap() throws RemoteException;
}
