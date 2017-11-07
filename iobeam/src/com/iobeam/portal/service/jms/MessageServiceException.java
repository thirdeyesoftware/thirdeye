package com.iobeam.portal.service.jms;

public class MessageServiceException extends Exception {

	public MessageServiceException(String msg ) {
		super(msg);
	}

	public MessageServiceException(String msg, Throwable t) {
		super(msg, t);
	}

	public String toString() {
		return "MessageServiceException: " + super.toString();
	}

}

