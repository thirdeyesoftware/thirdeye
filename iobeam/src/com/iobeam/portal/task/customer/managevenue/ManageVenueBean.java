package com.iobeam.portal.task.customer.managevenue;


import javax.ejb.*;
import javax.naming.*;
import java.util.*;
import java.util.logging.Logger;
import java.rmi.RemoteException;
import com.iobeam.portal.model.gateway.*;
import com.iobeam.portal.model.customercontact.CustomerContact;
import com.iobeam.portal.model.customer.*;
import com.iobeam.portal.model.subscription.*;
import com.iobeam.portal.model.venue.*;
import com.iobeam.portal.security.*;
import com.iobeam.portal.task.customer.managecustomer.*;
import com.iobeam.portal.task.vendible.managesubscription.*;


public class ManageVenueBean implements SessionBean {
	private SessionContext mContext;

	public void ejbCreate() throws CreateException {
	}

	public void ejbActivate() {

	}
	public void ejbPassivate() {
	}
	
	public void ejbRemove() {
	}
	
	public void setSessionContext(SessionContext context) {
		mContext = context;
	}


	private SessionContext getSessionContext() {
		return mContext;
	}


	public Venue createVenue(String venueName, VenueType type, 
		CustomerContact contact) throws CreateException {

		try {
			Customer customer = getManageCustomer().createCustomer(contact);

			return getAccessVenue().createVenue(venueName, type, customer);
		}
		catch (RemoteException re) {
			Logger logger = Logger.getLogger("com.iobeam.portal.task");
			logger.throwing(ManageVenue.class.getName(), "createVenue", 
					(Throwable)re);
			throw new EJBException(re);
		}
	}


	public User createVenueOperator(Venue venue, String username, 
			char[] password, CustomerContact contact) throws CreateException {

		throw new UnsupportedOperationException("no impl yet");
	}


	public void removeVenueOperator(Venue venue, User operator) {
		
		throw new UnsupportedOperationException("no impl yet");
	}


	/**
	* Sets Gateway information describing the Gateway hardware
	* in use at the Venue referenced by the specified Gateway instance.
	*/
	public void setGateway(Gateway gateway) {
		
		try {
			getAccessGateway().create(gateway);
		}
		catch (RemoteException re) {
			throw new EJBException(re);
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


	/**
	* Removes the specified Venue and all unreferenced dependencies
	* from the system.
	*/
	public void removeVenue(VenuePK venuePK) {
		try {
			Venue venue = getAccessVenue().findByPrimaryKey(venuePK);

			getManageCustomer().removeCustomer(venue.getCustomerPK());

			getAccessVenue().removeVenue(venue);
		}
		catch (RemoteException re) {
			throw new EJBException(re);
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


	/**
	* Returns a Map of VenuePKs by respective venueName.
	*/
	public Map getVenueNameMap() {
		try {
			return getAccessVenue().getVenueNameMap();
		}
		catch (RemoteException re) {
			throw new EJBException(re);
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


	/**
	* Returns a Collection of all active-status Subscriptions for the
	* specified SubscriptionType and Venue.
	*/
	public Collection getActiveSubscriptions(VenuePK venuePK,
			SubscriptionType subscriptionType) {

		try {
			Venue venue = getAccessVenue().findByPrimaryKey(venuePK);

			Customer customer = getCustomer(venue.getCustomerPK());

			return customer.getActiveSubscriptions(subscriptionType);
		}
		catch (RemoteException re) {
			throw new EJBException(re);
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


	/**
	* Returns a Collection of all created-status Subscriptions for the
	* specified SubscriptionType and Venue.
	*/
	public Collection getCreatedSubscriptions(VenuePK venuePK,
			SubscriptionType subscriptionType) {

		try {
			Venue venue = getAccessVenue().findByPrimaryKey(venuePK);

			Customer customer = getCustomer(venue.getCustomerPK());

			return customer.getCreatedSubscriptions(subscriptionType);
		}
		catch (RemoteException re) {
			throw new EJBException(re);
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


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
	public void generateSubscriptions(SubscriptionPK generatorPK, int count)
			throws SubscriptionException {

		try {
			getManageSubscription().generateSubscriptions(generatorPK,
					count);
		}
		catch (SubscriptionException se) {
			getSessionContext().setRollbackOnly();
			throw se;
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


	/**
	* Activates the specified Gateway as the Gateway for its Venue.
	* This is a one-time operation for a new Gateway being installed
	* at a venue.
	*
	* @exception NoSuchVenueException the Gateway's venuePK is not
	* known to the system.
	*/
	public void installGateway(Gateway gateway) throws NoSuchVenueException {

		try {
			getAccessVenue().installGateway(gateway);
		}
		catch (NoSuchVenueException nsve) {
			throw nsve;
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


	private ManageSubscription getManageSubscription() {
		try {
			InitialContext ic = new InitialContext();
			ManageSubscriptionHome h = 
				(ManageSubscriptionHome)ic.lookup(
						ManageSubscriptionHome.JNDI_NAME);
			return h.create();
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


	private ManageCustomer getManageCustomer() {
		try {
			InitialContext ic = new InitialContext();
			ManageCustomerHome mcHome = 
				(ManageCustomerHome)ic.lookup(ManageCustomerHome.JNDI_NAME);
			return mcHome.create();
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (Exception e) {
			throw new EJBException(e.toString());
		}
	}


	private Customer getCustomer(CustomerPK customerPK)
			throws FinderException {

		try {
			InitialContext ic = new InitialContext();

			CustomerHome h = (CustomerHome)
					ic.lookup(CustomerHome.JNDI_NAME);

			return h.findByPrimaryKey(customerPK);
		}
		catch (FinderException fe) {
			throw fe;
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


	private AccessVenue getAccessVenue() {

		try {
			InitialContext ic = new InitialContext();

			AccessVenueHome avHome = 
				(AccessVenueHome)ic.lookup(AccessVenueHome.JNDI_NAME);

			return avHome.create();
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


	private AccessGateway getAccessGateway() {

		try {
			InitialContext ic = new InitialContext();

			AccessGatewayHome h = 
				(AccessGatewayHome)ic.lookup(AccessGatewayHome.JNDI_NAME);

			return h.create();
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}
}
