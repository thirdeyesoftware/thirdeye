package com.iobeam.portal.model.subscription;


import java.util.Date;
import com.iobeam.portal.model.prototype.subscription.*;


public abstract class SubscriptionBuilder {

	abstract public Subscription createSubscription(
			SubscriptionPrototype prototype, Date startDate)
			throws SubscriptionException;


	abstract public Subscription createDefaultSubscription(
			SubscriptionType type, Date startDate)
			throws SubscriptionException;


	/**
	* Creates a new Subscription according to the specified "generator"
	* Subscription and the specified prototype.  The generator must have
	* remaining generation capacity, that is, it hasn't exhausted the
	* number of Subscriptions it can generate.  Also, the generator must
	* be able to build Subscriptions of the SubscriptionType described
	* by the specified prototype.
	*
	* The resulting, generated Subscription has the same AccountPK as
	* the generator, and has a ParentSubscriptionPK set to the generator.
	*
	* @exception SubscriptionException there is a problem
	* creating the Subscription.
	*/
	abstract public Subscription createSubscription(
			Subscription generator, SubscriptionPrototype prototype)
			throws SubscriptionException;


	/**
	* Creates a new Subscription according to the default generating
	* behavior for the specified "generator" Subscription.  The generator
	* must have remaining generation capacity, that is, it hasn't
	* exhausted the number of Subscriptions it can generate.
	*
	* The resulting, generated Subscription has the same AccountPK as
	* the generator, and has a ParentSubscriptionPK set to the generator.
	*
	* @exception SubscriptionException there is a problem creating
	* the Subscription.
	*/
	abstract public Subscription createDefaultSubscription(
			Subscription generator)
			throws SubscriptionException;
}

