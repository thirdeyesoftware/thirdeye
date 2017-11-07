package com.iobeam.portal.security;


public class PasswordException extends Exception {
	public PasswordException(String message) {
		super(message);
	}

	public PasswordException(String message, Throwable cause) {
		super(message, cause);
	}

	public PasswordException(Throwable cause) {
		super(cause);
	}
}
