package com.iobeam.portal.model.subscription;


/**
* Indicates that a Subscription described by some index
* (such as a SecureID) does not exist in the system.
*/
public class NoSuchSubscriptionException extends SubscriptionException {

	public NoSuchSubscriptionException(String message) {
		super(message);
	}


	public NoSuchSubscriptionException(String message, Throwable cause) {
		super(message, cause);
	}
}
