package com.iobeam.portal.task.actor.user.usersession;


import java.rmi.RemoteException;
import javax.ejb.*;
import com.iobeam.portal.model.gateway.usercontact.*;
import com.iobeam.portal.security.User;
import com.iobeam.portal.model.venue.Venue;
import com.iobeam.portal.model.subscription.Subscription;
import com.iobeam.portal.model.customer.Customer;


public interface UserSession extends EJBObject {

	/**
	* Returns the User that has logged in under this UserSession,
	* or null if no signon has yet occurred, or null if this is
	* an anonymous signon session.
	*/
	public User getUser() throws RemoteException;


	/**
	* Returns the customer associated with the user who was 
	* logged in under this UserSession.
	*/
	public Customer getCustomer() throws RemoteException;


	/**
	* Returns the Venue at which the associated contact was
	* established.
	*
	* @exception IllegalStateException this UserSession is a portal-only
	* session, where no venue is involved.
	*
	* @see #isPortalSession
	*/
	public Venue getVenue() throws RemoteException;


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
	public Subscription getActiveSubscription() throws RemoteException;


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
	public void setActiveSubscription(Subscription subscription)
			throws RemoteException;


	/**
	* Returns the unique ContactID describing the UserContact
	* at the Venue where the User is connecting to the iobeam network.
	*
	* @exception IllegalStateException if this UserSession is a portal-only
	* session, where no venue is involved.
	*
	* @see #isPortalSession
	*/
	public ContactID getContactID() throws RemoteException;


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
	* this UserSession is already signed on.
	*
	* @see #isPortalSession
	*/
	public void signOn(User user, Customer customer,
			Subscription subscription) throws RemoteException;


	/**
	* Sets the User and Customer on this portal-only UserSession.
	* Marks this as a "signed on" session.
	*
	* No validation is performed by this method.  It is simply
	* used to change the state of this UserSession by a business
	* process that has already confirmed that the User may sign on.
	*
	* @exception IllegalStateException if this UserSession is not
	* a portal-only session, or, this UserSession is already signed on.
	*
	* @see #isPortalSession
	*/
	public void signOn(User user, Customer customer) throws RemoteException;


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
	*
	* @see #isPortalSession
	*/
	public void signOnAnonymously(Subscription subscription)
			throws RemoteException;


	/**
	* Returns true if this UserSession is signed on.  This signifies
	* that the session has a valid state for either credential-based
	* or anonymous signon.
	*/
	public boolean isSignedOn() throws RemoteException;


	/**
	* Returns true if this is a session established directly with the
	* Portal over the public net, that is, not at an iobeam venue.
	*/
	public boolean isPortalSession() throws RemoteException;
}
