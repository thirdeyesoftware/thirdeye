package com.iobeam.portal.task.customer.setupuser;


import javax.ejb.*;
import javax.naming.*;
import java.util.*;
import java.util.logging.Logger;
import java.rmi.RemoteException;
import com.iobeam.portal.model.customercontact.CustomerContact;
import com.iobeam.portal.model.account.*;
import com.iobeam.portal.model.subscription.*;
import com.iobeam.portal.model.customer.*;
import com.iobeam.portal.model.billablecustomer.*;
import com.iobeam.portal.model.prototype.subscription.*;
import com.iobeam.portal.model.billing.*;
import com.iobeam.portal.model.venue.*;
import com.iobeam.portal.model.account.*;
import com.iobeam.portal.security.*;
import com.iobeam.portal.task.customer.managecustomer.*;
import com.iobeam.portal.task.customer.manageuser.*;
import com.iobeam.portal.task.vendible.managesubscription.*;


public class SetupUserBean implements SessionBean {
	private SessionContext mContext;

	public void ejbCreate() throws CreateException {
	}

	public void ejbActivate() {

	}
	public void ejbPassivate() {
	}
	
	public void ejbRemove() {
	}
	
	public void setSessionContext(SessionContext context) {
		mContext = context;
	}

	private SessionContext getSessionContext() {
		return mContext;
	}


	/**
	* Returns a Collection of all available SubscriptionPrototypes for
	* public Members.
	*/
	public Collection getPublicMemberSubscriptionPrototypes() {
		
		try {
			return getAccessSubscriptionPrototype().findBySubscriptionType(
					SubscriptionType.PUBLIC_MEMBER);
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


	/**
	* Returns a Collection of PrivateVenueSubscriptions owned by
	* the specified Account.
	*/
	public Collection
			getPrivateVenueSubscriptions(AccountNumber accountNumber) {

		try {
			Account account = getAccountHome().findByAccountNumber(
					accountNumber);

			return account.getActiveSubscriptions(
					SubscriptionType.PRIVATE_VENUE);
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}



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
			throws DuplicateUserException,
			SubscriptionException, PasswordException {

		User user = null;

		try {
			user = getManageUser().createUser(userName, password,
					passwordReminderAnswer, customerContact);

			Customer userCustomer = getCustomer(user.getCustomerPK());

			Account account =
					getAccountHome().create(userCustomer);

			SubscriptionBuilder builder = SubscriptionBuilderFactory.
					getFactory().getSubscriptionBuilder();

			Subscription userSubscription = builder.createDefaultSubscription(
					privateVenueSubscription);

			userSubscription = getManageSubscription().transferSubscription(
					userSubscription.getPK(),
					(AccountPK) account.getPrimaryKey());

			userSubscription = getManageSubscription().registerSubscription(
					userSubscription.getPK());

			userSubscription = getManageSubscription().activateSubscription(
					userSubscription.getPK(), new Date());
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (PasswordException pe) {
			getSessionContext().setRollbackOnly();
			throw pe;
		}
		catch (DuplicateUserException due) {
			getSessionContext().setRollbackOnly();
			throw due;
		}
		catch (SubscriptionException se) {
			getSessionContext().setRollbackOnly();
			throw se;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		return user;
	}


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
			SubscriptionException {

		User user = null;

		try {
			user = getManageUser().createUser(userName, password,
					passwordReminderAnswer, customerContact);

			Customer userCustomer = getCustomer(user.getCustomerPK());

			Account account =
					getAccountHome().create(userCustomer);

			Subscription subscription = null;

			SecureID sid = null;
			try {
				sid = SecureIDFactory.createSecureID(secureID);
			}
			catch (IllegalArgumentException iae) {
				throw new NoSuchSubscriptionException(secureID, iae);
			}

			try {
				subscription =
						getAccessSubscription().findBySecureID(sid);
			}
			catch (FinderException fe) {
				throw new NoSuchSubscriptionException(sid.toString());
			}

			if (!subscription.getSubscriptionType().equals(
					SubscriptionType.PRIVATE_MEMBER)) {
				throw new SubscriptionException(sid.toString() +
						" is " + subscription.getSubscriptionType() + " not " +
						SubscriptionType.PRIVATE_MEMBER,
						subscription);
			}


			if (subscription.isRegistered()) {
				throw new RegisteredSubscriptionException(subscription);
			}


			subscription = getManageSubscription().transferSubscription(
					subscription.getPK(),
					(AccountPK) account.getPrimaryKey());

			subscription = getManageSubscription().registerSubscription(
					subscription.getPK());

			subscription = getManageSubscription().activateSubscription(
					subscription.getPK(), new Date());
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (PasswordException pe) {
			getSessionContext().setRollbackOnly();
			throw pe;
		}
		catch (DuplicateUserException due) {
			getSessionContext().setRollbackOnly();
			throw due;
		}
		catch (SubscriptionException se) {
			getSessionContext().setRollbackOnly();
			throw se;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		return user;
	}


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
			char[] password,
			String passwordReminderAnswer,
			CustomerContact customerContact,
			AutomaticPaymentInstrument paymentInstrument,
			SubscriptionPrototypePK subscriptionPrototypePK)
			throws DuplicateUserException, PasswordException,
			SubscriptionException {

		try {
			SubscriptionPrototype proto = null;

			try {
				proto = getAccessSubscriptionPrototype().findByPrimaryKey(
						subscriptionPrototypePK);
			}
			catch (FinderException fe) {
				throw new NoSuchSubscriptionException(
						subscriptionPrototypePK.toString(), fe);
			}

			PublicMemberSubscriptionPrototype pmProto = null;

			try {
				pmProto = (PublicMemberSubscriptionPrototype) proto;
			}
			catch (ClassCastException cce) {
				throw new NoSuchSubscriptionException(subscriptionPrototypePK +
						" is not for a PublicMemberSubscriptionPrototype",
						cce);
			}

			return setupPublicMember(userName, password, 
					passwordReminderAnswer, customerContact,
					paymentInstrument, pmProto);
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (SubscriptionException se) {
			throw se;
		}
		catch (DuplicateUserException due) {
			throw due;
		}
		catch (PasswordException pe) {
			throw pe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


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
			throws DuplicateUserException, PasswordException {

		User user = null;

		try {
			user = getManageUser().createUser(userName, password,
					passwordReminderAnswer, customerContact);

			Customer userCustomer = getCustomer(user.getCustomerPK());

			BillableCustomer billableParty =
					getManageCustomer().createBillableCustomer(
					userCustomer, paymentInstrument);

			Account account =
					getAccountHome().create(userCustomer, billableParty);

			SubscriptionBuilder builder = SubscriptionBuilderFactory.
					getFactory().getSubscriptionBuilder();

			Subscription userSubscription = builder.createSubscription(
					subscriptionPrototype, new Date());

			account.addSubscription(userSubscription);
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (PasswordException pe) {
			getSessionContext().setRollbackOnly();
			throw pe;
		}
		catch (DuplicateUserException due) {
			getSessionContext().setRollbackOnly();
			throw due;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		return user;
	}


	private AccessSubscriptionPrototype getAccessSubscriptionPrototype() {

		try {
			InitialContext ic = new InitialContext();

			AccessSubscriptionPrototypeHome h = 
					(AccessSubscriptionPrototypeHome) ic.lookup(
					AccessSubscriptionPrototypeHome.JNDI_NAME);

			return h.create();
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


	private AccessVenue getAccessVenue() {

		try {
			InitialContext ic = new InitialContext();

			AccessVenueHome h = 
					(AccessVenueHome) ic.lookup(
					AccessVenueHome.JNDI_NAME);

			return h.create();
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


	private ManageUser getManageUser() {

		try {
			InitialContext ic = new InitialContext();

			ManageUserHome h = 
					(ManageUserHome) ic.lookup(
					ManageUserHome.JNDI_NAME);

			return h.create();
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


	private ManageSubscription getManageSubscription() {

		try {
			InitialContext ic = new InitialContext();

			ManageSubscriptionHome h = 
					(ManageSubscriptionHome) ic.lookup(
					ManageSubscriptionHome.JNDI_NAME);

			return h.create();
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


	private AccessSubscription getAccessSubscription() {

		try {
			InitialContext ic = new InitialContext();

			AccessSubscriptionHome h = 
					(AccessSubscriptionHome) ic.lookup(
					AccessSubscriptionHome.JNDI_NAME);

			return h.create();
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


	private ManageCustomer getManageCustomer() {

		try {
			InitialContext ic = new InitialContext();

			ManageCustomerHome h = 
					(ManageCustomerHome) ic.lookup(
					ManageCustomerHome.JNDI_NAME);

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
			throw fe;
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


	private AccountHome getAccountHome() {

		try {
			InitialContext ic = new InitialContext();

			AccountHome h = 
					(AccountHome) ic.lookup(
					AccountHome.JNDI_NAME);

			return h;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}
}
