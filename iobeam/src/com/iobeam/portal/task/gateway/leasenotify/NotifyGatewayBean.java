package com.iobeam.portal.task.gateway.leasenotify;


import java.rmi.RemoteException;
import javax.ejb.*;
import javax.naming.*;
import java.net.InetAddress;
import java.util.Date;
import java.util.logging.Logger;
import com.iobeam.portal.security.*;
import com.iobeam.portal.service.contact.*;
import com.iobeam.portal.util.BusinessLogicError;
import com.iobeam.portal.model.gateway.*;
import com.iobeam.portal.model.gateway.usercontact.*;


public class NotifyGatewayBean implements SessionBean {

	private SessionContext mSessionContext;


	public static final String LOGGER =
		"com.iobeam.portal.task.gateway.leasenotify";

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
	* Enables the specified User for internet access
	* at the Gateway where the User is currently located.
	*
	* @param expirationDate the date and time at which the access
	* rights must expire on the Gateway, or null if there is no
	* expiration date.
	*/
	public void enableUser(User user, Date expirationDate) {
		UserContact userContact = null;
		Gateway gateway = null;

		Logger logger = Logger.getLogger(LOGGER);

		try {
			userContact = getAccessUserContact().findByUserPK(user.getUserPK());
			
			logger.info("userContact=" + userContact.toString());

			gateway = getAccessGateway().findByVenuePK(
					userContact.getVenuePK());
	

			logger.info("gateway=" + gateway.toString());

			ContactService.enableContact(gateway, userContact, expirationDate);

		}
		catch (FinderException fe) {
			BusinessLogicError ble = new BusinessLogicError(fe);

				logger.throwing(
					NotifyGatewayBean.class.getName(),
					"enableUser", ble);

			throw ble;
		}
		catch (ContactServiceException cse) {
			cse.printStackTrace();
			EJBException ejbe = new EJBException(cse);

			Logger.getLogger("com.iobeam.portal.task.gateway").throwing(
					NotifyGatewayBean.class.getName(),
					"enableUser", ejbe);

			throw ejbe;
		}
		catch (RemoteException re) {
			throw new EJBException(re);
		}
	}


	/**
	* Enables the member associated with the specified contact
	* for internet access at the Gateway where the contact
	* was established.
	*
	* @param expirationDate the date and time at which the access
	* rights must expire on the Gateway, or null if there is no
	* expiration date.
	*/
	public void enableAnonymousContact(ContactID contactID,
			Date expirationDate) {
		UserContact userContact = null;
		Gateway gateway = null;

		try {
			userContact = getAccessUserContact().findByContactID(contactID);

			gateway = getAccessGateway().findByVenuePK(
					userContact.getVenuePK());

			ContactService.enableContact(gateway, userContact, expirationDate);
		}
		catch (FinderException fe) {
			BusinessLogicError ble = new BusinessLogicError(fe);

			Logger.getLogger("com.iobeam.portal.task.gateway").throwing(
					NotifyGatewayBean.class.getName(),
					"enableAnonymousContact", ble);

			throw ble;
		}
		catch (ContactServiceException cse) {
			EJBException ejbe = new EJBException(cse);

			Logger.getLogger("com.iobeam.portal.task.gateway").throwing(
					NotifyGatewayBean.class.getName(),
					"enableAnonymousContact", ejbe);

			throw ejbe;
		}
		catch (RemoteException re) {
			throw new EJBException(re);
		}
	}


	/**
	* Disables the specified User's internet connection
	* at the Gateway where the User is currently located.
	*/
	public void disableUser(User user) {
		UserContact userContact = null;
		Gateway gateway = null;

		try {
			userContact = getAccessUserContact().findByUserPK(user.getUserPK());

			gateway = getAccessGateway().findByVenuePK(
					userContact.getVenuePK());

			ContactService.disableContact(gateway, userContact);
		}
		catch (FinderException fe) {
			BusinessLogicError ble = new BusinessLogicError(fe);

			Logger.getLogger("com.iobeam.portal.task.gateway").throwing(
					NotifyGatewayBean.class.getName(),
					"enableUser", ble);

			throw ble;
		}
		catch (ContactServiceException cse) {
			EJBException ejbe = new EJBException(cse);

			Logger.getLogger("com.iobeam.portal.task.gateway").throwing(
					NotifyGatewayBean.class.getName(),
					"enableUser", ejbe);

			throw ejbe;
		}
		catch (RemoteException re) {
			throw new EJBException(re);
		}
	}


	/**
	* Disables the internet connection of the member associated
	* with the specified contact at the Gateway where the contact
	* was established.
	*/
	public void disableAnonymousContact(ContactID contactID) {
		throw new UnsupportedOperationException("not impl yet");
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
}
