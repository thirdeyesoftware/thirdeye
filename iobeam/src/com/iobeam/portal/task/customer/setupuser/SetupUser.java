package com.iobeam.portal.task.customer.setupuser;


import java.util.Collection;
import java.rmi.RemoteException;
import javax.ejb.*;
import com.iobeam.portal.model.customercontact.CustomerContact;
import com.iobeam.portal.model.user.*;
import com.iobeam.portal.security.*;
import com.iobeam.portal.model.billing.AutomaticPaymentInstrument;
import com.iobeam.portal.model.prototype.subscription.*;
import com.iobeam.portal.model.subscription.*;
import com.iobeam.portal.model.venue.*;
import com.iobeam.portal.model.account.*;


public interface SetupUser extends EJBObject {


	/**
	* Returns a Collection of all available SubscriptionPrototypes for
	* public Members.
	*/
	public Collection getPublicMemberSubscriptionPrototypes()
			throws RemoteException;


	/**
	* Returns a Collection of PrivateVenueSubscriptions owned by
	* the specified Account.
	*/
	public Collection getPrivateVenueSubscriptions(AccountNumber accountNumber)
			throws RemoteException;


	/**
	* Sets up a new Private Member User by generating a
	* Subscription from the specified PrivateVenueSubscription.
	*
	* @exception DuplicateUserException if there is already a User
	* with the specified userName.
	*
	* @exception SubscriptionException if the specified Subscription
	* cannot be used to support an additional user, or this user.
	*
	* @exception MalformedPasswordException the specified password is
	* not valid in the system rules for well formed passwords.
	*/
	public User setupPrivateMember(String userName,
			char[] password, String passwordReminderAnswer,
			CustomerContact customerContact,
			PrivateVenueSubscription privateVenueSubscription)
			throws DuplicateUserException, PasswordException,
			SubscriptionException, RemoteException;


	/**
	* Sets up a new Private Member User according to the
	* Subscription described by the specified SecureID.
	*
	* @exception DuplicateUserException if there is already a User
	* with the specified userName.
	*
	* @exception NoSuchSubscriptionException if the specified SecureID
	* does not map to a known Subscription.
	*
	* @exception RegisteredSubscriptionException if the specified SecureID
	* describes a Subscription that is already registered to a user.
	*
	* @exception MalformedPasswordException the specified password is
	* not valid in the system rules for well formed passwords.
	*/
	public User setupPrivateMember(String userName,
			char[] password, String passwordReminderAnswer,
			CustomerContact customerContact,
			String secureID)
			throws DuplicateUserException, PasswordException,
			SubscriptionException, RemoteException;


	/**
	* Sets up a new Public Member User according to the specified information.
	*
	* @exception DuplicateUserException if there is already a User
	* with the specified userName.
	*
	* @exception MalformedPasswordException the specified password is
	* not valid in the system rules for well formed passwords.
	*
	* @exception SubscriptionException the specified
	* SubscriptionPrototypePK is not for a PublicMemberSubscriptionPrototype.
	*/
	public User setupPublicMember(String userName,
			char[] password, String passwordReminderAnswer,
			CustomerContact customerContact,
			AutomaticPaymentInstrument paymentInstrument,
			SubscriptionPrototypePK subscriptionPrototypePK)
			throws DuplicateUserException, PasswordException,
			SubscriptionException, RemoteException;


	/**
	* Sets up a new Public Member User according to the specified information.
	*
	* @exception DuplicateUserException if there is already a User
	* with the specified userName.
	*
	* @exception MalformedPasswordException the specified password is
	* not valid in the system rules for well formed passwords.
	*/
	public User setupPublicMember(String userName,
			char[] password, String passwordReminderAnswer,
			CustomerContact customerContact,
			AutomaticPaymentInstrument paymentInstrument,
			PublicMemberSubscriptionPrototype subscriptionPrototype)
			throws DuplicateUserException, PasswordException, RemoteException;
}
