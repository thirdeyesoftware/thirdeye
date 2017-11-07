package com.iobeam.portal.task.actor.user.usersession;


import javax.ejb.*;
import javax.naming.*;
import java.util.*;
import com.iobeam.portal.security.NoSuchVenueException;
import com.iobeam.portal.model.gateway.usercontact.*;
import com.iobeam.portal.security.User;
import com.iobeam.portal.model.venue.*;
import com.iobeam.portal.model.subscription.Subscription;
import com.iobeam.portal.model.customer.Customer;


public class UserSessionBean implements SessionBean  {

	private SessionContext mContext;

	private User mUser = null;
	private Venue mVenue = null;
	private Subscription mActiveSubscription = null;
	private ContactID mContactID = null;
	private Customer mCustomer = null;
	private boolean mIsSignedOn = false;
	private boolean mIsPortalSession = false;


	public void setSessionContext(SessionContext context) {
		mContext = context;
	}


	private SessionContext getSessionContext() {
		return mContext;
	}


	public void ejbCreate(ContactID contactID, VenuePK venuePK)
			throws NoSuchVenueException, CreateException {

		mContactID = contactID;
		if (contactID == null) {
			throw new NullPointerException("contactID");
		}

		try {
			mVenue = getAccessVenue().findByPrimaryKey(venuePK);
		}
		catch (FinderException fe) {
			throw new NoSuchVenueException(venuePK);
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		mIsSignedOn = false;
		mIsPortalSession = false;
	}


	public void ejbCreate() throws CreateException {
		mContactID = null;
		mVenue = null;

		mIsSignedOn = false;
		mIsPortalSession = true;
	}


	public void ejbCreate(VenuePK venuePK)
			throws NoSuchVenueException, CreateException {

		mContactID = null;

		try {
			mVenue = getAccessVenue().findByPrimaryKey(venuePK);
		}
		catch (FinderException fe) {
			throw new NoSuchVenueException(venuePK);
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		mIsSignedOn = false;
		mIsPortalSession = true;
	}


	public void ejbActivate() {
	}


	public void ejbPassivate() {
	}

	
	public void ejbRemove() {
	}



	/**
	* Returns the User that has logged in under this UserSession,
	* or if this is an anonymous signon session.
	*
	* @exception IllegalStateException this UserSession is not signed on.
	*/
	public User getUser() {
		if (!isSignedOn()) {
			throw new IllegalStateException("not signed on");
		}

		return mUser;
	}


	/**
	 * Returns the Customer that is bound to the User who was
	 * logged in under this UserSession.
	 */
	public Customer getCustomer() {
		if (!isSignedOn()) {
			throw new IllegalStateException("not signed on");
		}
		return mCustomer;
	}

	/**
	* Returns the Venue at which the associated contact was
	* established, or null if there is none.
	*
	* @see #isPortalSession
	*/
	public Venue getVenue() {
		return mVenue;
	}


	/**
	* Returns the Subscription that allows the associated User to gain
	* internet access via the associated Venue, or null if there
	* is no such Subscription.
	*
	* A UserSession may have no active Subscription if the User
	* is accessing the portal via wireless service for account maintenance
	* purposes.
	*
	* @exception IllegalStateException this UserSession is a portal-only
	* session, where no venue is involved, or, this UserSession is not
	* yet signed on.
	*
	* @see #isPortalSession
	*/
	public Subscription getActiveSubscription() {
		if (!isSignedOn()) {
			throw new IllegalStateException("not signed on");
		}

		if (isPortalSession()) {
			throw new IllegalStateException(
					"portal-only session has no active subscription");
		}

		return mActiveSubscription;
	}


	/**
	* Sets the active Subscription, i.e. the Subscription in use
	* by the corresponding User to gain access to the Internet.
	*
	* The UserSession typically is assigned an active Subscription
	* at signon time.
	*
	* This method may be used when a previously restricted User
	* buys or renews a Subscription.  This method may also be used
	* to remove the active Subscription by setting it to null.
	*
	* @exception IllegalStateException this UserSession is a portal-only
	* session, where no venue is involved.
	*
	* @see #isPortalSession
	*/
	public void setActiveSubscription(Subscription subscription) {
		if (isPortalSession()) {
			throw new IllegalStateException(
					"portal-only session cannot have active subscription");
		}

		mActiveSubscription = subscription;
	}


	/**
	* Returns the unique ContactID describing the UserContact
	* at the Venue where the User is connecting to the iobeam network.
	*
	* @exception IllegalStateException if this UserSession is a portal-only
	* session, where no venue is involved.
	*
	* @see #isPortalSession
	*/
	public ContactID getContactID() {
		if (isPortalSession()) {
			throw new IllegalStateException(
					"portal-only session has no contactID");
		}

		return mContactID;
	}


	/**
	* Sets the User, Customer, and Subscription properties on this
	* UserSession.  Marks this as a "signed on" session.
	*
	* No validation is performed by this method.  It is simply
	* used to change the state of this UserSession by a business
	* process that has already confirmed that the User may sign on.
	*
	* @exception IllegalStateException if this UserSession is a portal-only
	* session, where no venue is involved, or,
	* this UserSession is not signed on.
	*
	* @see #isPortalSession
	*/
	public void signOn(User user, Customer customer,
			Subscription subscription) {

		if (isSignedOn()) {
			throw new IllegalStateException("already signed on");
		}
		if (isPortalSession()) {
			throw new IllegalStateException(
					"portal-only session needs no subscription for signon");
		}

		mUser = user;
		if (user == null) {
			throw new NullPointerException("user");
		}

		mCustomer = customer;
		if (customer == null) {
			throw new NullPointerException("customer");
		}

		mActiveSubscription = subscription;

		mIsSignedOn = true;
	}


	/**
	* Sets the User and Customer on this portal-only UserSession.
	* Marks this as a "signed on" session.
	*
	* No validation is performed by this method.  It is simply
	* used to change the state of this UserSession by a business
	* process that has already confirmed that the User may sign on.
	*
	* @exception IllegalStateException if this UserSession is not a
	* portal-only session.
	*
	* @see #isPortalSession
	*/
	public void signOn(User user, Customer customer) {
		if (isSignedOn()) {
			throw new IllegalStateException("already signed on");
		}
		if (!isPortalSession()) {
			throw new IllegalStateException(
					"venue session needs subscription for signon");
		}

		mUser = user;
		if (user == null) {
			throw new NullPointerException("user");
		}

		mCustomer = customer;
		if (customer == null) {
			throw new NullPointerException("customer");
		}

		mActiveSubscription = null;

		mIsSignedOn = true;
	}


	/**
	* Sets this UserSession up as a Subscribed, anonymous session.
	* An anonymous UserSession returns null for the User and Customer, 
	* but returns true for isSignedOn.
	*
	* No validation is performed by this method.  It is used
	* to change the state of this UserSession by a business process
	* that has already confirmed that the user may sign on anonymously
	* at the Venue.
	*
	* @exception IllegalStateException if this UserSession is a
	* portal-only session, or, the UserSession is already signed on.
	*/
	public void signOnAnonymously(Subscription subscription) {
		if (isSignedOn()) {
			throw new IllegalStateException("already signed on");
		}
		if (isPortalSession()) {
			throw new IllegalStateException(
					"portal-only session needs no subscription for signon");
		}

		mActiveSubscription = subscription;
		if (subscription == null) {
			throw new NullPointerException("subscription");
		}

		mIsSignedOn = true;
	}


	/**
	* Returns true if this UserSession is signed on.  This signifies
	* that the session has a valid state for either credential-based
	* or anonymous signon.
	*/
	public boolean isSignedOn() {
		return mIsSignedOn;
	}


	/**
	* Returns true if this is a session established directly with the
	* Portal over the public net, that is, not at an iobeam venue.
	*/
	public boolean isPortalSession()  {
		return mIsPortalSession;
	}


	private AccessVenue getAccessVenue() {
		AccessVenueHome h = null;
		AccessVenue b = null;
		InitialContext ic = null;

		try {
			ic = new InitialContext();

			h = (AccessVenueHome) ic.lookup(AccessVenueHome.JNDI_NAME);

			b = h.create();
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		return b;
	}
}
