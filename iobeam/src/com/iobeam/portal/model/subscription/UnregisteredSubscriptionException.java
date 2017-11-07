package com.iobeam.portal.model.subscription;


/**
* Indicates an attempt to use an unregistered Subscription in a way that
* is reserved only for registered Subscriptions.
*/
public class UnregisteredSubscriptionException extends SubscriptionException {

	public UnregisteredSubscriptionException(Subscription subscription) {
		this("invalid use of unregistered Subscription", subscription);
	}


	public UnregisteredSubscriptionException(String message,
			Subscription subscription) {
		super(message, subscription);
	}
}
