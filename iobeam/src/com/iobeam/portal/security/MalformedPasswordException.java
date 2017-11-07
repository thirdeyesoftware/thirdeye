package com.iobeam.portal.security;


public class MalformedPasswordException extends PasswordException {
	public MalformedPasswordException(String message) {
		super(message);
	}

	public MalformedPasswordException(String message, Throwable cause) {
		super(message, cause);
	}

	public MalformedPasswordException(Throwable cause) {
		super(cause);
	}
}
