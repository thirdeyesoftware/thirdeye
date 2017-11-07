package com.iobeam.portal.task.gateway.manageusercontact;


import java.rmi.RemoteException;
import javax.ejb.*;
import javax.naming.*;
import java.net.InetAddress;
import java.util.logging.Logger;
import com.iobeam.util.MACAddress;
import com.iobeam.portal.util.BusinessLogicError;
import com.iobeam.portal.security.NoSuchVenueException;
import com.iobeam.portal.security.GatewayException;
import com.iobeam.portal.security.NoSuchGatewayException;
import com.iobeam.portal.model.gateway.*;
import com.iobeam.portal.model.gateway.usercontact.*;
import com.iobeam.portal.model.user.*;
import com.iobeam.portal.model.venue.*;


public class ManageUserContactBean implements SessionBean {

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
	* Records a user-contact at the specified Venue.  The resulting
	* user-contact describes an unknown client machine making network
	* contact with a Gateway.
	*/
	public ContactID logInitialContact(VenuePK venuePK,
			InetAddress venueIP, InetAddress userIP,
			MACAddress userMAC, long siteKey)
			throws NoSuchVenueException, GatewayException {

		Logger logger = Logger.getLogger("com.iobeam.portal.task");
		logger.entering(ManageUserContactBean.class.getName(),
				"logInitialContact");

		Venue venue = null;
		Gateway gateway = null;
		UserContact userContact;

		try {
			try {
				venue = getAccessVenue().findByPrimaryKey(venuePK);
			}
			catch (FinderException fe) {
				throw new NoSuchVenueException(venuePK);
			}

			gateway = getAccessGateway().findByVenuePK(venuePK);

			if (siteKey != venue.getSiteKey()) {
				logger.warning("contact at " + venuePK +
						" has incorrect siteKey " + siteKey + " != " +
						venue.getSiteKey());

				throw new GatewayException("Contact at " +
						venuePK + " " + venueIP + " has incorrect siteKey");
			}

			if (!venueIP.equals(gateway.getPublicIPAddress())) {
				gateway.setPublicIPAddress(venueIP);
				logger.info("update publicIP " + gateway);
				getAccessGateway().update(gateway);
			}

			userContact = getAccessUserContact().create(venuePK,
					userIP, userMAC);
			
		}
		catch (CreateException ce) {
			throw new EJBException(ce);
		}
		catch (FinderException fe) {
			throw new NoSuchGatewayException(venuePK, fe);
		}
		catch (RemoteException re) {
			throw new EJBException(re);
		}

		return userContact.getContactID();
	}


	/**
	* Associates the specified User with the specified Contact.
	* This association occurs at User signon time.
	*/
	public void bindUser(UserPK userPK, ContactID contactID) {
		UserContact userContact = null;
		
		try {
			AccessUserContact auc = getAccessUserContact();

			userContact = auc.findByContactID(contactID);
			userContact.setUserPK(userPK);

			auc.update(userContact);
		}
		catch (FinderException fe) {
			BusinessLogicError ble = new BusinessLogicError(fe);

			Logger.getLogger("com.iobeam.portal.task.gateway").throwing(
					ManageUserContactBean.class.getName(),
					"bindUser", ble);

			throw ble;
		}
		catch (RemoteException re) {
			throw new EJBException(re);
		}
	}


	/**
	* Associates the specified UserContact with an anonymous signon.
	* This association occurs at user signon time.
	*/
	public void bindAnonymous(ContactID contactID) {
		UserContact userContact = null;
		
		try {
			AccessUserContact auc = getAccessUserContact();

			userContact = auc.findByContactID(contactID);
			userContact.setAnonymous(true);

			auc.update(userContact);
		}
		catch (FinderException fe) {
			BusinessLogicError ble = new BusinessLogicError(fe);

			Logger.getLogger("com.iobeam.portal.task.gateway").throwing(
					ManageUserContactBean.class.getName(),
					"bindAnonymous", ble);

			throw ble;
		}
		catch (RemoteException re) {
			throw new EJBException(re);
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


	private AccessUserContact getAccessUserContact() {
		try {
			InitialContext ic = new InitialContext();

			AccessUserContactHome h = (AccessUserContactHome)
					ic.lookup(AccessUserContactHome.JNDI_NAME);

			return h.create();
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}
}
