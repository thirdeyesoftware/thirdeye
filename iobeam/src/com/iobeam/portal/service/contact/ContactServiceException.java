package com.iobeam.portal.service.contact;


public class ContactServiceException extends Exception {

	public ContactServiceException(String msg) {
		super(msg);
	}


	public ContactServiceException(Throwable cause) {
		super(cause);
	}


	public ContactServiceException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
