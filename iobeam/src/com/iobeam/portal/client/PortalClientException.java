package com.iobeam.portal.client;

public class PortalClientException extends Exception {

	public PortalClientException(String msg) {
		super(msg);
	}

	public PortalClientException(String msg, Throwable t) {
		super(msg, t);
	}

	public String toString() {
		return "PortalClientException: " + super.toString();
	}
}

