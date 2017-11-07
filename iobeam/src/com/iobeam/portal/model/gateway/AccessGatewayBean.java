package com.iobeam.portal.model.gateway;


import java.util.logging.Logger;
import javax.ejb.*;
import java.net.InetAddress;

import com.iobeam.util.MACAddress;
import com.iobeam.portal.util.DataNotFoundException;
import com.iobeam.portal.model.venue.VenuePK;


public class AccessGatewayBean implements SessionBean {

	public static final String LOGGER = "com.iobeam.portal.model.gateway";

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
	* Returns the Gateway instance for the specified Venue.
	* Venues that are not fully online may not yet have a Gateway.
	*/
	public Gateway findByVenuePK(VenuePK venuePK)
			throws FinderException {

		Gateway gateway = null;

		try {
			gateway = GatewayDAO.select(venuePK);

			if (gateway == null) {
				throw new ObjectNotFoundException(venuePK.toString());
			}
		}
		catch (FinderException fe) {
			throw fe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		return gateway;
	}


	/**
	* Returns true if the specified Venue has an installed Gateway.
	*/
	public boolean hasGateway(VenuePK venuePK)  {
		Gateway gateway = null;

		try {
			gateway = GatewayDAO.select(venuePK);

			return gateway != null;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


	/**
	* returns Gateway using the specified ipaddress.
	*/
	public Gateway findByIPAddress(InetAddress address) 
			throws FinderException {
		Gateway gateway = null;
		try {
			gateway = GatewayDAO.select(address);

			if (gateway == null) {
				throw new ObjectNotFoundException(address.toString());
			}
		}
		catch (FinderException fe) {
			throw fe;
		}
		catch (Exception e) { 
			throw new EJBException(e);
		}
		return gateway;
	}

	/**
	* returns Gateway using the specified mac address
	*/
	public Gateway findByMACAddress(MACAddress mac) 
			throws FinderException {
		Gateway gateway = null;
		try {
			gateway = GatewayDAO.select(mac);

			if (gateway == null) {
				throw new ObjectNotFoundException(mac.toString());
			}
		}
		catch (FinderException fe) {
			throw fe;
		}
		catch (Exception e) { 
			throw new EJBException(e);
		}
		return gateway;
	}

	/**
	* Creates a new Gateway according to the specified Gateway instance.
	*/
	public Gateway create(Gateway gateway) throws CreateException {

		try {
			gateway = GatewayDAO.create(gateway);
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		return gateway;
	}


	/**
	* Removes any Gateway or Gateways associated with the specified Venue.
	*/
	public void remove(VenuePK venuePK) {
		try {
			GatewayDAO.delete(venuePK);
		}
		catch (DataNotFoundException dnfe) {
			throw new NoSuchEntityException(dnfe);
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


	/**
	* Replaces an existing Gateway with the specified Gateway data.
	*/
	public void update(Gateway gateway) {
		try {
			gateway.updateTimestamp();

			Logger.getLogger(LOGGER).info(gateway.toString());
			gateway = GatewayDAO.update(gateway);
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}
}
