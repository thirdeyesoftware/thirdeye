package com.iobeam.portal.model.subscription;


/**
* Indicates invalid use of a Subscription.
*/
public class SubscriptionException extends Exception {

	private Subscription mSubscription = null;


	SubscriptionException(String message) {
		super(message);
	}


	SubscriptionException(String message, Throwable cause) {
		super(message, cause);
	}


	SubscriptionException(Throwable cause) {
		super(cause);
	}


	public SubscriptionException(String message, Subscription subscription) {
		super(message);
		mSubscription = subscription;
	}


	public SubscriptionException(Throwable cause, Subscription subscription) {
		super(cause);
		mSubscription = subscription;
	}


	public SubscriptionException(String message, Throwable cause,
			Subscription subscription) {
		super(message, cause);
		mSubscription = subscription;
	}


	public Subscription getSubscription() {
		return mSubscription;
	}
}
