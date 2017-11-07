package com.iobeam.portal.task.actor.gateway.register;

import javax.ejb.*;
import java.net.InetAddress;
import java.util.*;
import java.util.logging.Logger;
import javax.naming.*;

import com.iobeam.portal.util.BusinessLogicError;
import com.iobeam.portal.model.venue.*;
import com.iobeam.portal.model.customer.*;
import com.iobeam.portal.model.subscription.*;
import com.iobeam.portal.security.*;
import com.iobeam.portal.model.gateway.*;
import com.iobeam.util.MACAddress;

public class RegisterGatewayBean implements SessionBean {
	
	private SessionContext mContext;

	public void ejbCreate() throws CreateException {

	}
	public void setSessionContext(SessionContext ctx) {
		mContext = ctx;
	}

	public void ejbPostCreate() {

	}

	public void ejbRemove() {

	}

	public void ejbActivate() {

	}

	public void ejbPassivate() {

	}


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
			IllegalGatewayException {
		
		AccessVenue av = null;

		Logger logger = Logger.getLogger(
				"com.iobeam.portal.task.actor.gateway.register");

		try {
			av = getAccessVenue();

			logger.info("registering " + venuePK.toString());	

			try {
				Venue venue = av.findByPrimaryKey(venuePK);
			}
			catch (FinderException fe) {
				logger.info("could not find venue");
				throw new NoSuchVenueException(venuePK);
			}

			AccessGateway ag = getAccessGateway();
			Gateway gateway = null;
			try {
				gateway = ag.findByVenuePK(venuePK);
			}
			catch (FinderException fe) {
				logger.info("could not find gateway.");
				throw new NoSuchGatewayException(venuePK);
			}

			if (!gateway.getMACAddress().equals(gatewayMAC)) {
				throw new IllegalGatewayException(venuePK, gatewayMAC);
			}

			gateway.setPublicIPAddress(publicIP);
			gateway.setNotifyPort(notifyPort);
			gateway.setPrivateIPAddress(privateIP);

			ag.update(gateway);

			logger.info("registration complete.");
		} 
		catch (NoSuchGatewayException nsge) {
			throw nsge;
		}
		catch (NoSuchVenueException nsve) {
			throw nsve;
		}
		catch (IllegalGatewayException ige) {
			throw ige;
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}

	public Collection getRoutingRules(VenuePK venuePK) 
			throws NoSuchVenueException {
	
		throw new UnsupportedOperationException("no impl yet.");
	}


	/**
	* Returns the venue Subscription for the spcecified VenuePK.
	*/
	public Subscription getVenueSubscription(VenuePK venuePK)
			throws NoSuchVenueException, SubscriptionException {

		try {
			Venue venue = getAccessVenue().findByPrimaryKey(venuePK);

			Customer venueCustomer = getCustomer(venue.getCustomerPK());
			SubscriptionType st;

			if (venue.getVenueType().equals(VenueType.PUBLIC)) {
				st = SubscriptionType.PUBLIC_VENUE;
			} else
			if (venue.getVenueType().equals(VenueType.PRIVATE)) {
				st = SubscriptionType.PRIVATE_VENUE;
			} else {
				throw new UnsupportedOperationException(
						venue.getVenueType().toString());
			}

			List s = venueCustomer.getActiveSubscriptions(st);

			if (s.size() == 0) {
				throw new SubscriptionUnavailableException(st);
			}

			if (s.size() > 1) {
				throw new BusinessLogicError("Venue " + venuePK +
						" has more that one " + st);
			}

			return (Subscription) s.get(0);
		}
		catch (FinderException fe) {
			throw new NoSuchVenueException(venuePK);
		}
		catch (SubscriptionException se) {
			throw se;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


	private AccessVenue getAccessVenue() {
		try {
			InitialContext ic = new InitialContext();

			AccessVenueHome h = (AccessVenueHome)
					ic.lookup(AccessVenueHome.JNDI_NAME);

			return h.create();
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


	private AccessGateway getAccessGateway() {
		try {
			InitialContext ic = new InitialContext();

			AccessGatewayHome h = (AccessGatewayHome)
					ic.lookup(AccessGatewayHome.JNDI_NAME);

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
			fe.printStackTrace();
			throw fe;
		}
		catch (EJBException ejbe) {
			ejbe.printStackTrace();
			throw ejbe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}
}
