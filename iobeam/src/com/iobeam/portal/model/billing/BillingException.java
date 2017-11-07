package com.iobeam.portal.model.billing;


/**
* Indicates a problem in a Billing-related process, in which the
* process cannot be completed for reasons described in the exception.
*/
public class BillingException extends Exception {

	public BillingException(String msg) {
		super(msg);
	}


	public BillingException(String msg, Throwable t) {
		super(msg, t);
	}


	public BillingException(Throwable cause) {
		super(cause);
	}
}
