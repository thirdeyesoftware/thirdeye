package com.iobeam.portal.model.subscription;


/**
* Indicates no Subscription was available for a specific
* SubscriptionType.
*/
public class SubscriptionUnavailableException extends SubscriptionException {

	private SubscriptionType mSubscriptionType;


	public SubscriptionUnavailableException(
			SubscriptionType subscriptionType) {
		this(subscriptionType.toString(), subscriptionType);
	}


	public SubscriptionUnavailableException(String message,
			SubscriptionType subscriptionType) {
		super(message);
		mSubscriptionType = subscriptionType;
	}


	public SubscriptionUnavailableException(Throwable cause,
			SubscriptionType subscriptionType) {
		super(cause);
		mSubscriptionType = subscriptionType;
	}


	public SubscriptionType getSubscriptionType() {
		return mSubscriptionType;
	}

	public String toString() {
		return super.toString() + " " + getSubscriptionType();
	}
}
