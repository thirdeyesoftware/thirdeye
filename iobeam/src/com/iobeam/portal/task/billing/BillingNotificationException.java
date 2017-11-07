package com.iobeam.portal.task.billing;

/**
 * describes an exception encountered wwhile attempting
 * a billing notification.
 */
public class BillingNotificationException extends Exception {

	public BillingNotificationException(String msg, Throwable t) {
		super(msg, t);
	}
	
	public BillingNotificationException(String msg) {
		super(msg);
	}

	public String toString() {
		return "BillingNotificationException: " + super.toString();
	}
}

