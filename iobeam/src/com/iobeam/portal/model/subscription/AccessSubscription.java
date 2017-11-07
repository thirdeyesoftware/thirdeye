package com.iobeam.portal.model.subscription;


import java.util.Collection;
import java.rmi.RemoteException;
import javax.ejb.EJBObject;
import javax.ejb.FinderException;
import javax.ejb.CreateException;

import com.iobeam.portal.model.account.AccountPK;
import com.iobeam.portal.model.EntityAccessor;
import com.iobeam.portal.model.subscription.SubscriptionType;
import com.iobeam.portal.security.*;


public interface AccessSubscription extends EntityAccessor, EJBObject {

	/**
	* Returns the Subscription for the specified pk.
	* Throws FinderException if there is none.
	* The returned value is cast to a type specific to
	* the SubscriptionType.
	*/
	public Subscription findByPrimaryKey(
			SubscriptionPK pk) throws FinderException, RemoteException;


	/**
	* Returns the Subscription for the specified SecureID.
	* Throws FinderException if there is none.
	* The returned value is cast to a type specific
	* to the SubscriptionType.
	*/
	public Subscription findBySecureID(SecureID subscriptionKey)
			throws FinderException, RemoteException;


	/**
	* Returns a Collection of Subscriptions for the specified
	* Account and SubscriptionType.
	*/
	public Collection findBySubscriptionType(
			AccountPK accountPK, SubscriptionType stype) 
					throws FinderException, RemoteException;
	

	/**
	* Returns a Collection of Subscriptions for the specified Account
	* and SubscriptionStatus.
	*/
	public Collection findBySubscriptionStatus(
			AccountPK accountPK, SubscriptionStatus status) 
					throws FinderException, RemoteException;


	/**
	* Returns a Collection of Subscriptions for the specified Account,
	* SubscriptionType, and SubscriptionStatus.
	*/
	public Collection findBySubscriptionStatus(
			AccountPK accountPK, SubscriptionType stype,
					SubscriptionStatus status) 
					throws FinderException, RemoteException;


	/**
	* Returns a Collection of all Subscriptions for a specified Account.
	*/
	public Collection findAllSubscriptions(AccountPK accountPK) 
			throws FinderException, RemoteException;


	/**
	* Stores the specified Subscription in the system under the
	* specified Account.
	*
	* Returns a Subscription populated with a SubscriptionPK and the
	* specified AccountPK, as well as a unique SecureID.
	*/
	public Subscription create(AccountPK pk, Subscription subscription) 
			throws CreateException, RemoteException;


	/**
	* Stores changes in the specified Subscription.
	*/
	public void update(Subscription subscription) 
			throws RemoteException;
	

	/**
	* Deletes the specified Subscription from the system.
	*/
	public void delete(SubscriptionPK subscriptionPK) 
			throws RemoteException;
}
