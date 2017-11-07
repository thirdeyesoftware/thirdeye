package com.iobeam.portal.task.vendible.managesubscription;

import java.util.Date;
import javax.ejb.EJBObject;
import javax.ejb.CreateException;
import java.rmi.RemoteException;
import com.iobeam.portal.model.billing.BillingException;
import com.iobeam.portal.model.account.*;
import com.iobeam.portal.model.subscription.*;
import com.iobeam.portal.model.prototype.subscription.*;


public interface ManageSubscription extends EJBObject {


	/**
	* Creates a new Subscription in the specified Account
	* using the specified SubscriptionPrototype and start Date.
	*
	* @exception BillingException the Subscription would result
	* in a violation of Account billing rules for the specified
	* Account.
	*/
	public Subscription createSubscription(AccountPK accountPK,
			SubscriptionPrototype prototype, Date startDate)
			throws SubscriptionException, BillingException, RemoteException;


	/**
	* Cancels the specified Subscription.  The Subscription
	* is no longer active after cancellation.
	*/
	public void cancelSubscription(Subscription subscription)
			throws RemoteException;


	/**
	* Generates count Subscriptions in the Account enclosing the
	* specified generator Subscription.
	*
	* @exception SubscriptionException the specified Subscription
	* is not generative.
	*
	* @exception ExhaustedGeneratorSubscriptionException the specified
	* Subscription has no remaining generation capacity to satisfy
	* the request.
	*/
	public void generateSubscriptions(SubscriptionPK generatorPK,
			int count) throws SubscriptionException, RemoteException;


	/**
	* Transfers the specified Subscription from its current Account
	* to the specified destination Account.
	*
	* @exception SubscriptionException the Subscription is not
	* compatible with the transfer.
	*
	* @exception BillingException the Subscription would result
	* in a violation of Account billing rules in either the source or
	* destination Account.
	*/
	public Subscription transferSubscription(SubscriptionPK subscriptionPK,
			AccountPK destinationAccountPK)
			throws SubscriptionException, BillingException, RemoteException;


	/**
	* Marks the specified Subscription as registered to the
	* Account that contains it.
	*/
	public Subscription registerSubscription(SubscriptionPK subscriptionPK)
			throws SubscriptionException, RemoteException;


	
	/**
	* Marks the specified Subscription as unregistered.
	*/
	public Subscription unregisterSubscription(SubscriptionPK subscriptionPK)
			throws SubscriptionException, RemoteException;


	/**
	* Sets the start date (and end date based on duration if the
	* Subscription is not continuous) for the specified Subscription.
	*
	* Marks the Subscription as status active.
	*/
	public Subscription activateSubscription(SubscriptionPK subscriptionPK,
			Date startDate)
			throws SubscriptionException, RemoteException;
}
