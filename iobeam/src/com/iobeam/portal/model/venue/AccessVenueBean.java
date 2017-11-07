package com.iobeam.portal.model.venue;


import java.util.Map;
import java.util.Collection;
import java.util.Iterator;
import java.rmi.RemoteException;
import javax.ejb.*;
import javax.naming.*;
import com.iobeam.portal.security.User;
import com.iobeam.portal.util.*;
import com.iobeam.portal.model.customer.*;
import com.iobeam.portal.model.user.*;
import com.iobeam.portal.model.gateway.*;
import com.iobeam.portal.model.subscription.*;
import com.iobeam.portal.security.NoSuchVenueException;

public class AccessVenueBean implements SessionBean {

	private SessionContext mSessionContext;


	public void setSessionContext(SessionContext context) {
		mSessionContext = context;
	}


	public void ejbCreate() throws CreateException {
	}


	public void ejbActivate() {
	}


	public void ejbPassivate() {
	}


	public void ejbRemove() {
	}


	/**
	* Returns the Venue with the specified pk.
	* Throws FinderException if there is none.
	*/
	public Venue findByPrimaryKey(VenuePK venuePK)
			throws FinderException {

		if (venuePK == null) {
			throw new NullPointerException("venuePK");
		}

		VenueData venueData = null;

		try {
			venueData = VenueDAO.select(venuePK);

			if (venueData == null) {
				throw new ObjectNotFoundException(venuePK.toString());
			}
		}
		catch (FinderException fe) {
			throw fe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		return venueData;
	}


	/**
	* Returns the Venue with the specified CustomerPK.
	* Throws FinderException if there is none.
	*/
	public Venue findByCustomerPK(CustomerPK customerPK)
			throws FinderException {

		if (customerPK == null) {
			throw new NullPointerException("customerPK");
		}

		VenueData venueData = null;

		try {
			venueData = VenueDAO.select(customerPK);

			if (venueData == null) {
				throw new ObjectNotFoundException(customerPK.toString());
			}
		}
		catch (FinderException fe) {
			throw fe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		return venueData;
	}


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
			Customer customer) throws CreateException {

		VenueData venueData = null;

		try {
			CustomerPK customerPK = (CustomerPK) customer.getPrimaryKey();

			if (VenueDAO.venueExists(customerPK)) {
				throw new BusinessLogicError("venue exists for " +
						customerPK);
			}
			if (UserDAO.userExists(customerPK)) {
				throw new BusinessLogicError("user exists for " +
						customerPK);
			}

			if (VenueDAO.venueExists(venueName)) {
				throw new DuplicateKeyException("venue exists with name " +
						venueName);
			}

			venueData = new VenueData(venueName, customerPK, venueType);
			venueData = VenueDAO.create(venueData);
		}
		catch (DuplicateKeyException dke) {
			throw dke;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		return venueData;
	}


	/**
	* Removes the specified Venue from the system.
	*/
	public void removeVenue(Venue venue)
			throws RemoveException {

		try {
			VenueDAO.delete((VenueData) venue);
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


	/**
	* Returns a Map of VenuePKs keyed on respective venueNames.
	*/
	public Map getVenueNameMap() {
		try {
			return VenueDAO.selectNameMap();
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


	/**
	* Returns a Collection of VenueOperators for the specified
	* Venue.
	*/
	public Collection findVenueOperators(VenuePK venuePK)
			throws FinderException {
		throw new UnsupportedOperationException("not impl yet");
	}


	/**
	* Returns true if the specified Venue has a VenueOperators
	* with the specified userName.
	*/
	public boolean hasVenueOperator(User user) {
		throw new UnsupportedOperationException("not impl yet");
	}


	/**
	* Adds the specified User as a VenueOperator on the specified Venue.
	* Throws SubscriptionException if there are no more available slots
	* for VenueOperators.
	*/
	public void addVenueOperator(VenuePK venuePK, User user)
			throws SubscriptionException {
		throw new UnsupportedOperationException("not impl yet");
	}


	/**
	* Sets the specified Gateway as the Gateway instance for its Venue.
	*
	* Throws NoSuchVenueException if the Venue does not exist.
	*/
	public void installGateway(Gateway gateway) 
			throws NoSuchVenueException {
	
		try {
			Venue venue = findByPrimaryKey(gateway.getVenuePK());

			if (getAccessGateway().hasGateway(gateway.getVenuePK())) {
				throw new BusinessLogicError(gateway.getVenuePK() +
						" already has an installed Gateway");
			}

			getAccessGateway().create(gateway);
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (FinderException fe) {
			throw new NoSuchVenueException(gateway.getVenuePK());
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


	private AccessGateway getAccessGateway() {
		try {
			InitialContext ic = new InitialContext();
			AccessGatewayHome h =
					(AccessGatewayHome) ic.lookup(AccessGatewayHome.JNDI_NAME);

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
