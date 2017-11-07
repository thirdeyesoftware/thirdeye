package com.iobeam.portal.task.actor.user.signon;


import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.*;
import javax.ejb.*;
import javax.naming.*;
import com.iobeam.portal.util.*;
import com.iobeam.portal.security.*;
import com.iobeam.portal.model.user.*;
import com.iobeam.portal.model.venue.*;
import com.iobeam.portal.model.customer.*;
import com.iobeam.portal.model.subscription.*;
import com.iobeam.portal.task.actor.user.usersession.*;
import com.iobeam.portal.task.gateway.manageusercontact.*;
import com.iobeam.portal.task.gateway.leasenotify.*;


public class SignonUserBean implements SessionBean {

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
	* Sign on the named user via the Portal UI.
	*
	* @exception IllegalStateException the PortalUserSession is
	* already signed on.
	*
	* @exception NoSuchUserException the specified user name is not
	* known to the system.
	*
	* @exception InvalidPasswordException the user is known but the
	* password is incorrect.
	*/
	public void signOnPortalUser(UserSession userSession,
			String userName, char[] password)
			throws NoSuchUserException,
			InvalidPasswordException {

		try {
			
			
			if (userSession.isSignedOn()) {
				throw new IllegalStateException("session already signed on");
			}
			User user = getValidUser(userName, password);
			Customer customer = getCustomer(user.getCustomerPK());

			Logger.getLogger("com.iobeam.portal.task.user.signon").
				info("signOn()");
			userSession.signOn(user, customer);

		}
		catch (IllegalStateException ise) {
			throw ise;
		}
		catch (NoSuchUserException nsue) {
			throw nsue;
		}
		catch (InvalidPasswordException ipe) {
			throw ipe;
		}
		catch (EJBException ejbe) {
			ejbe.printStackTrace();
			throw ejbe;
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e);
		}
	}

	/**
	 * notifies gateway of system's intention of signing off user.
	 * throws IllegalStateException if UserSession is not signed on.
	 */
	public void signOffUser(UserSession session) {
		try {
			if (session == null || !session.isSignedOn()) {
				throw new IllegalStateException("user is not signed on.");
			}
			User user = session.getUser();
			
			InitialContext ic = new InitialContext();

			NotifyGatewayHome ngHome = 
				(NotifyGatewayHome)ic.lookup(NotifyGatewayHome.JNDI_NAME);
		  
			NotifyGateway nc = ngHome.create();
			nc.disableUser(user);
		}
		catch (IllegalStateException ise) {
			throw ise;
		}
		catch(Exception e) { 
			throw new EJBException(e);
		}
	}

	/**
	* Sign on the named user via the venue described in the
	* specified UserSession.
	*
	* @exception IllegalStateException the UserSession is
	* already signed on.
	*
	* @exception NoSuchUserException the specified user name is not
	* known to the system.
	*
	* @exception InvalidPasswordException the user is known but the
	* password is incorrect.
	*
	* @exception SubscriptionUnavailableException there is no
	* anonymous access for the Venue associated with the
	* specified UserSession.
	*/
	public void signOnUser(UserSession userSession,
			String userName, char[] password)
			throws NoSuchUserException,
			InvalidPasswordException, SubscriptionException {

		try {
			Logger l =
					Logger.getLogger("com.iobeam.portal.task.user.signonuser");
			
			if (userSession.isSignedOn()) {
					l.info("user" + userSession.getUser().toString() + 
						" is already signed on.");	
					throw new IllegalStateException(
							"session already signed on");
			}

			User user = getValidUser(userName, password);
			
			Customer customer = getCustomer(user.getCustomerPK());

			Subscription subscription = getUserAccessSubscription(user,
					userSession.getVenue());

			userSession.signOn(user, customer, subscription);

			getManageUserContact().bindUser(user.getUserPK(),
					userSession.getContactID());

			// notify gateway

			InitialContext ic = new InitialContext();

			NotifyGatewayHome ngHome = 
				(NotifyGatewayHome)ic.lookup(NotifyGatewayHome.JNDI_NAME);
		  
			NotifyGateway nc = ngHome.create();

			nc.enableUser(user, subscription.getExpirationDate());
		}
		catch (IllegalStateException ise) {
			throw ise;
		}
		catch (NoSuchUserException nsue) {
			throw nsue;
		}
		catch (InvalidPasswordException ipe) {
			throw ipe;
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (SubscriptionException se) {
			throw se;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


	/**
	* Sign on an anonymous user via the venue described in the
	* specified UserSession.  The venue must have a Subscription
	* that allows anonymous (no member subscription) signons.
	*
	* @exception IllegalStateException the UserSession is already
	* signed on.
	*
	* @exception SubscriptionUnavailableException there is no
	* anonymous access for the Venue associated with the
	* specified UserSession.
	*/
	public void signOnAnonymousUser(UserSession userSession)
			throws SubscriptionException {

		try {
			if (userSession.isSignedOn()) {
				throw new IllegalStateException("session already signed on");
			}

			Subscription s = getAnonymousAccessSubscription(
					userSession.getVenue());

			userSession.signOnAnonymously(s);

			getManageUserContact().bindAnonymous(userSession.getContactID());
			InitialContext ic = new InitialContext();

			// notify gateway
			NotifyGatewayHome ngHome = 
				(NotifyGatewayHome)ic.lookup(NotifyGatewayHome.JNDI_NAME);
		  
			NotifyGateway nc = ngHome.create();

			nc.enableAnonymousContact(userSession.getContactID(), null);

		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}



	/**
	* Returns true if the specified Venue has an available
	* PublicVenueSubscription that allows anonymous access.
	*/
	public boolean hasAnonymousAccessSubscription(Venue venue) {
		Customer c;
		
		try {
			c = getCustomer(venue.getCustomerPK());

			Collection subscriptions  = c.getActiveSubscriptions(
					SubscriptionType.PUBLIC_VENUE);

			for (Iterator it = subscriptions.iterator(); it.hasNext(); ) {
				PublicVenueSubscription pvs = (PublicVenueSubscription)
						it.next();

				if (pvs.hasAnonymousAccess()) {
					return true;
				}
			}

			return false;
		}
		catch (FinderException fe) {
			BusinessLogicError ble = new BusinessLogicError(fe);

			Logger.getLogger("com.iobeam.portal.task").throwing(
					SignonUserBean.class.getName(),
					"hasAnonymousSubscription", ble);

			throw ble;
		}
		catch (RemoteException re) {
			throw new EJBException(re);
		}
	}



	/**
	* Sign on an anonymous user via the Subscription matching the
	* specified SecureID.  The SecureID is a "subscription key" from a
	* Subscription Card or other distribution medium.
	*
	* @exception NoSuchSubscriptionException the specified SecureID
	* does not match any known Subscription.
	*
	* @exception VenueMismatchSubscriptionException the specified SecureID
	* is for a Subscription that cannot be used at the current Venue.
	*
	* @exception RegisteredSubscriptionException the specified SecureID
	* refers to a Subscription that is registered to some member, and
	* cannot be used via anonymous signon.
	*/
	public void signOnAnonymousUser(UserSession userSession,
			String subscriptionKey)
			throws SubscriptionException {

		try {

			SecureID sid = null;

			try {
				sid = SecureIDFactory.createSecureID(subscriptionKey);
			}
			catch (IllegalArgumentException iae) {
				throw new NoSuchSubscriptionException(subscriptionKey, iae);
			}


			if (userSession.isSignedOn()) {
				throw new IllegalStateException("session already signed on");
			}

			Subscription s = getUnregisteredSubscription(
					userSession.getVenue(), sid);

			if (s.requiresRegistration()) {
				throw new UnregisteredSubscriptionException(s);
			}

			userSession.signOnAnonymously(s);

			getManageUserContact().bindAnonymous(userSession.getContactID());
			InitialContext ic = new InitialContext();

			// notify gateway
			NotifyGatewayHome ngHome = 
				(NotifyGatewayHome)ic.lookup(NotifyGatewayHome.JNDI_NAME);
		  
			NotifyGateway nc = ngHome.create();

			nc.enableAnonymousContact(userSession.getContactID(),
					s.getExpirationDate());
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


	/**
	* Returns true if the specified Subscription requires
	* registration prior to use.  Subscriptions that require
	* registration cannot be used for anonymous signon in which the
	* user presents only the SecureID.
	*
	* @exception NoSuchSubscriptionException the specified SecureID
	* does not match any known Subscription.
	*
	* @exception VenueMismatchSubscriptionException the specified SecureID
	* is for a Subscription that cannot be used at the current Venue.
	*/
	public boolean requiresRegistration(UserSession userSession,
			String subscriptionKey)
			throws SubscriptionException {

		try {
			SecureID sid = null;

			try {
				sid = SecureIDFactory.createSecureID(subscriptionKey);
			}
			catch (IllegalArgumentException iae) {
				throw new NoSuchSubscriptionException(subscriptionKey, iae);
			}

			Subscription s = getUnregisteredSubscription(
					userSession.getVenue(), sid);

			return s.requiresRegistration();
		}
		catch (VenueMismatchSubscriptionException vmse) {
			throw vmse;
		}
		catch (NoSuchSubscriptionException nsee) {
			throw nsee;
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}



	/**
	* Returns the named User if the named User is known to the system
	* and the specified password is valid.
	*
	* @exception NoSuchUserException the specified user name is not
	* known to the system.
	*
	* @exception InvalidPasswordException the user name is known but
	* the password is incorrect.
	*
	* This method makes no statement about the User's subscriptions,
	* or right to use the system.
	*/
	private User getValidUser(String userName, char[] password)
			throws InvalidPasswordException, NoSuchUserException {

		User u = null;

		try {
			u = getAccessUser().findByName(userName);

			if (!u.isValidPassword(password)) {
				throw new InvalidPasswordException("incorrect password for " +
						userName);
			}
		}
		catch (FinderException fe) {
			throw new NoSuchUserException(userName, fe);
		}
		catch (RemoteException re) {
			throw new EJBException(re);
		}

		return u;
	}


	private AccessUser getAccessUser() {
		try {
			InitialContext ic = new InitialContext();

			AccessUserHome h = (AccessUserHome)
					ic.lookup(AccessUserHome.JNDI_NAME);

			return h.create();
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


	private AccessSubscription getAccessSubscription() {
		try {
			InitialContext ic = new InitialContext();

			AccessSubscriptionHome h = (AccessSubscriptionHome)
					ic.lookup(AccessSubscriptionHome.JNDI_NAME);

			return h.create();
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


	/**
	* Returns the Subscription that allows the specified User to gain
	* access via the specified Venue.
	*
	* @exception SubscriptionUnavailableException there was no
	* Subscription to allow the User access at the Venue.
	*/
	private Subscription getUserAccessSubscription(User user, Venue venue)
			throws SubscriptionException {

		VenueType venueType = venue.getVenueType();

		try {
			Customer userCustomer = getCustomer(user.getCustomerPK());
			Collection subscriptions = null;
			Subscription userAccessSubscription = null;

			if (venueType.equals(VenueType.PUBLIC)) {
				subscriptions = userCustomer.getActiveSubscriptions(
						SubscriptionType.PUBLIC_MEMBER);

				if (!subscriptions.isEmpty()) {
					Iterator it = subscriptions.iterator();
					userAccessSubscription = (Subscription) it.next();
				} else {
					subscriptions = userCustomer.getActiveSubscriptions(
							SubscriptionType.VENUE_OPERATOR);

					for (Iterator it = subscriptions.iterator();
							it.hasNext(); ) {
						VenueOperatorSubscription s =
								(VenueOperatorSubscription) it.next();

						if (s.getEffectiveVenuePK().equals(
								venue.getPK())) {
							userAccessSubscription = s;
							break;
						}
					}
				}

				if (userAccessSubscription == null) {
					throw new SubscriptionUnavailableException(
							SubscriptionType.PUBLIC_MEMBER);
				}
			} else
			if (venueType.equals(VenueType.PRIVATE)) {
				subscriptions = userCustomer.getActiveSubscriptions(
						SubscriptionType.PRIVATE_MEMBER);

				if (!subscriptions.isEmpty()) {
					for (Iterator it = subscriptions.iterator();
							it.hasNext(); ) {
						PrivateMemberSubscription s =
								(PrivateMemberSubscription) it.next();

						if (s.getEffectiveVenuePK().equals(
								venue.getPK())) {
							userAccessSubscription = s;
							break;
						}
					}
				} else {
					subscriptions = userCustomer.getActiveSubscriptions(
							SubscriptionType.VENUE_OPERATOR);

					for (Iterator it = subscriptions.iterator();
							it.hasNext(); ) {
						VenueOperatorSubscription s =
								(VenueOperatorSubscription) it.next();

						if (s.getEffectiveVenuePK().equals(
								venue.getPK())) {
							userAccessSubscription = s;
							break;
						}
					}
				}

				if (userAccessSubscription == null) {
					throw new SubscriptionUnavailableException(
							SubscriptionType.PRIVATE_MEMBER);
				}
			} else
			if (venueType.equals(VenueType.SEMIPRIVATE)) {
				throw new UnsupportedOperationException("not impl yet");
			}


			return userAccessSubscription;
		}
		catch (SubscriptionException se) {
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
	* Returns the Subcription that allows an anonymous User to gain access
	* via the specified Venue.
	*
	* @exception SubscriptionUnavailableException there was no
	* available PublicVenueSubscription for the specified Venue
	* that also allows anonymous signon.
	*/
	private Subscription getAnonymousAccessSubscription(Venue venue)
			throws SubscriptionUnavailableException {
		Customer c;
		
		try {
			c = getCustomer(venue.getCustomerPK());

			Collection subscriptions  = c.getActiveSubscriptions(
					SubscriptionType.PUBLIC_VENUE);

			for (Iterator it = subscriptions.iterator(); it.hasNext(); ) {
				PublicVenueSubscription pvs = (PublicVenueSubscription)
						it.next();

				if (pvs.hasAnonymousAccess()) {
					return pvs;
				}
			}

			throw new SubscriptionUnavailableException("no anon access for " +
					venue.getVenueName(), SubscriptionType.PUBLIC_VENUE);
		}
		catch (FinderException fe) {
			BusinessLogicError ble = new BusinessLogicError(fe);

			Logger.getLogger("com.iobeam.portal.task").throwing(
					SignonUserBean.class.getName(),
					"getAnonymousSubscription", ble);

			throw ble;
		}
		catch (RemoteException re) {
			throw new EJBException(re);
		}
	}



	/**
	* Returns an unregistered Subscription bearing the specified
	* SecureID key, suitable for login at the specified Venue.
	*
	* @exception NoSuchSubscriptionException the specified SecureID
	* does not match any known Subscription.
	*
	* @exception VenueMismatchSubscriptionException the specified SecureID
	* is for a Subscription that cannot be used at the current Venue.
	*
	* @exception RegisteredSubscriptionException the specified SecureID
	* refers to a Subscription that is registered to some member, and
	* cannot be used via anonymous signon.
	*/
	private Subscription getUnregisteredSubscription(Venue venue,
			SecureID key)
			throws SubscriptionException {
		
		try {
			Subscription s = getAccessSubscription().findBySecureID(key);


			// Need to implement SubscriptionCategory and
			// check for Member category.
			//
			if (!(s.getSubscriptionType().equals(
					SubscriptionType.PRIVATE_MEMBER) ||
					s.getSubscriptionType().equals(
					SubscriptionType.PUBLIC_MEMBER) ||
					s.getSubscriptionType().equals(
					SubscriptionType.VENUE_OPERATOR))) {

				throw new SubscriptionException("Subscription for " +
						key + " is not a USER Subscription: " +
						s.getSubscriptionType(), s);
			}


			if (s instanceof PrivateMemberSubscription) {
				PrivateMemberSubscription pms = (PrivateMemberSubscription) s;
				if (pms.getEffectiveVenuePK() != null &&
						!pms.getEffectiveVenuePK().equals(venue.getPK())) {
					throw new VenueMismatchSubscriptionException(
							venue.getPK(), pms);
				}
			}

			if (s instanceof VenueOperatorSubscription) {
				VenueOperatorSubscription vos = (VenueOperatorSubscription) s;
				if (vos.getEffectiveVenuePK() != null &&
						!vos.getEffectiveVenuePK().equals(venue.getPK())) {
					throw new VenueMismatchSubscriptionException(
							venue.getPK(), vos);
				}
			}

			if (s.isRegistered()) {
				throw new RegisteredSubscriptionException(
						key + " is already registered.", s);
			}

			return s;
		}
		catch (ObjectNotFoundException onfe) {
			throw new NoSuchSubscriptionException("SecureID " + key);
		}
		catch (SubscriptionException se) {
			throw se;
		}
		catch (EJBException ejbe) {
			throw ejbe;
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


	private ManageUserContact getManageUserContact() {
		try {
			InitialContext ic = new InitialContext();

			ManageUserContactHome h = (ManageUserContactHome)
					ic.lookup(ManageUserContactHome.JNDI_NAME);

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
