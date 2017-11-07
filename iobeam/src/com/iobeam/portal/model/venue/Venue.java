package com.iobeam.portal.model.venue;


import com.iobeam.portal.model.customer.CustomerPK;


/**
* Describes a location with a Gateway for providing connection
* services to Users.
*/
public interface Venue {

	public VenuePK getPK();


	public String getVenueName();

	public String getRedirectUrl();

	/**
	* Returns the pk for the Customer instance associated with this
	* Venue.  All Venues are also Customers.
	*/
	public CustomerPK getCustomerPK();


	/**
	*/
	public VenueType getVenueType();


	/**
	* Returns the shared secret used for secure communication with
	* this Venue and its associated Gateway.
	*/
	public long getSiteKey();
}
