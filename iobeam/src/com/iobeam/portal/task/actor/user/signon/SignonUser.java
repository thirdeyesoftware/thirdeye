package com.iobeam.portal.task.actor.user.signon;


import java.rmi.RemoteException;
import javax.ejb.*;
import com.iobeam.portal.task.UseCaseController;
import com.iobeam.portal.task.actor.user.usersession.*;
import com.iobeam.portal.security.*;
import com.iobeam.portal.model.venue.Venue;
import com.iobeam.portal.model.subscription.*;


public interface SignonUser extends UseCaseController, EJBObject {

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
			throws RemoteException, NoSuchUserException,
			InvalidPasswordException;


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
	* Subscription to allow the User access at the Venue associated with the
	* specified UserSession.
	*/
	public void signOnUser(UserSession userSession,
			String userName, char[] password)
			throws RemoteException, NoSuchUserException,
			InvalidPasswordException, SubscriptionException;


	public void signOffUser(UserSession usersession) 
			throws RemoteException;

	/**
	* Sign on an anonymous user via the venue described in the
	* specified UserSession.  The venue must have a Subscription
	* that allows anonymous (no member subscription) signons.
	*
	* This method is specifically for the case where the User is
	* signing on via a Venue's subscription that allows anonymous access
	* within the Venue.  NOTE: this method is not for use in the case
	* where the User has knowledge of a valid Member Subscription and
	* uses the Subscription Key to sign on anonymously.
	*
	* @exception IllegalStateException the UserSession is already
	* signed on.
	*
	* @exception SubscriptionUnavailableException there is no
	* anonymous access for the Venue associated with the
	* specified UserSession.
	*/
	public void signOnAnonymousUser(UserSession userSession)
			throws SubscriptionException, RemoteException;


	/**
	* Returns true if the specified Venue has an available
	* PublicVenueSubscription that allows anonymous access.
	*/
	public boolean hasAnonymousAccessSubscription(Venue venue)
			throws RemoteException;



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
			throws SubscriptionException, RemoteException;


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
			throws SubscriptionException, RemoteException;
}
