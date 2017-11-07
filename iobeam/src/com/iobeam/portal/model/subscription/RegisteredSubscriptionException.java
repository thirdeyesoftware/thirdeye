package com.iobeam.portal.model.subscription;


/**
* Indicates an attempt to use a registered Subscription in a way that
* is reserved only for unregistered Subscriptions.
*
* This Exception is used to described an attempt to use a
* registered Subscription by securedID only.  Registered Subscriptions
* must be accessed through the full authentication credentials of the
* registered account holder.
*
* This Exception is also used to describe attempts to register
* Subscriptions that are already registered.
*/
public class RegisteredSubscriptionException extends SubscriptionException {

	public RegisteredSubscriptionException(Subscription subscription) {
		this("invalid use of registered Subscription", subscription);
	}


	public RegisteredSubscriptionException(String message,
			Subscription subscription) {
		super(message, subscription);
	}
}
