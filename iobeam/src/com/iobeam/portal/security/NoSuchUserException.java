package com.iobeam.portal.security;


public class NoSuchUserException extends Exception {
	private String mUserName;

	public NoSuchUserException(String userName) {
		super("User '" + userName + "' is unknown");

		mUserName = userName;
	}

	public NoSuchUserException(String userName, Throwable cause) {
		super("User '" + userName + "' is unknown", cause);

		mUserName = userName;
	}


	public String getUserName() {
		return mUserName;
	}
}
