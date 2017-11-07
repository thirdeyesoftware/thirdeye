package com.iobeam.portal.task.customer.setupvenue;


import javax.ejb.*;
import javax.naming.*;
import java.util.*;
import java.util.logging.Logger;
import java.rmi.RemoteException;
import com.iobeam.portal.model.customercontact.CustomerContact;
import com.iobeam.portal.model.venue.*;
import com.iobeam.portal.model.account.*;
import com.iobeam.portal.model.subscription.*;
import com.iobeam.portal.model.customer.*;
import com.iobeam.portal.model.billablecustomer.*;
import com.iobeam.portal.model.prototype.subscription.*;
import com.iobeam.portal.model.billing.PaymentInstrument;
import com.iobeam.portal.task.customer.managevenue.*;
import com.iobeam.portal.task.customer.managecustomer.*;


public class SetupVenueBean implements SessionBean {
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


	/**
	* Returns a Collection of all available SubscriptionPrototypes for
	* private Venues.
	*/
	public Collection getPrivateVenueSubscriptionPrototypes() {
		
		try {
			return getAccessSubscriptionPrototype().findBySubscriptionType(
					SubscriptionType.PRIVATE_VENUE);
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


	/**
	* Returns a Collection of all available SubscriptionPrototypes for
	* public Venues.
	*/
	public Collection getPublicVenueSubscriptionPrototypes() {

		try {
			return getAccessSubscriptionPrototype().findBySubscriptionType(
					SubscriptionType.PUBLIC_VENUE);
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


	/**
	* Sets up a new private Venue according to the specified information.
	*/
	public Venue setupPrivateVenue(String venueName,
			CustomerContact customerContact,
			PaymentInstrument paymentInstrument,
			PrivateVenueSubscriptionPrototype subscriptionPrototype) {

		Venue venue = null;

		try {
			venue = getManageVenue().createVenue(venueName,
					VenueType.PRIVATE, customerContact);

			Customer venueCustomer = getCustomer(venue.getCustomerPK());

			BillableCustomer billableParty =
					getManageCustomer().createBillableCustomer(
					venueCustomer, paymentInstrument);

			Account account =
					getAccountHome().create(venueCustomer, billableParty);

			SubscriptionBuilder builder = SubscriptionBuilderFactory.
					getFactory().getSubscriptionBuilder();

			Subscription venueSubscription = builder.createSubscription(
					subscriptionPrototype, new Date());

			account.addSubscription(venueSubscription);

			Subscription venueStaffSubscription =
					builder.createDefaultSubscription(
					SubscriptionType.VENUE_STAFF, new Date());

			account.addSubscription(venueStaffSubscription);
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		return venue;
	}


	/**
	* Sets up a new public Venue according to the specified information.
	*/
	public Venue setupPublicVenue(String venueName,
			CustomerContact customerContact,
			PaymentInstrument paymentInstrument,
			PublicVenueSubscriptionPrototype subscriptionPrototype) {

		Venue venue = null;

		try {
			venue = getManageVenue().createVenue(venueName,
					VenueType.PUBLIC, customerContact);

			Customer venueCustomer = getCustomer(venue.getCustomerPK());

			BillableCustomer billableParty =
					getManageCustomer().createBillableCustomer(
					venueCustomer, paymentInstrument);

			Account account =
					getAccountHome().create(venueCustomer, billableParty);

			SubscriptionBuilder builder = SubscriptionBuilderFactory.
					getFactory().getSubscriptionBuilder();

			Subscription venueSubscription = builder.createSubscription(
					subscriptionPrototype, new Date());

			account.addSubscription(venueSubscription);

			Subscription venueStaffSubscription =
					builder.createDefaultSubscription(
					SubscriptionType.VENUE_STAFF, new Date());

			account.addSubscription(venueStaffSubscription);
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		return venue;
	}


	private AccessSubscriptionPrototype getAccessSubscriptionPrototype() {

		try {
			InitialContext ic = new InitialContext();

			AccessSubscriptionPrototypeHome h = 
					(AccessSubscriptionPrototypeHome) ic.lookup(
					AccessSubscriptionPrototypeHome.JNDI_NAME);

			return h.create();
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


	private ManageVenue getManageVenue() {

		try {
			InitialContext ic = new InitialContext();

			ManageVenueHome h = 
					(ManageVenueHome) ic.lookup(
					ManageVenueHome.JNDI_NAME);

			return h.create();
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


	private ManageCustomer getManageCustomer() {

		try {
			InitialContext ic = new InitialContext();

			ManageCustomerHome h = 
					(ManageCustomerHome) ic.lookup(
					ManageCustomerHome.JNDI_NAME);

			return h.create();
		}
		catch (Exception e) {
			throw new EJBException(e);
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


	private AccountHome getAccountHome() {

		try {
			InitialContext ic = new InitialContext();

			AccountHome h = 
					(AccountHome) ic.lookup(
					AccountHome.JNDI_NAME);

			return h;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}
}
