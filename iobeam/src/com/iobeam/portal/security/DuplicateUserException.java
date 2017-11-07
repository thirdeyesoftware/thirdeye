package com.iobeam.portal.security;


/**
* Indicates an attempt to create a new user with a userName
* that is already in use by another user.
*/
public class DuplicateUserException extends Exception {
	private String mUserName;

	public DuplicateUserException(String userName) {
		super("User '" + userName + "' already exists");

		mUserName = userName;
	}

	public DuplicateUserException(String userName, Throwable cause) {
		super("User '" + userName + "' already exists", cause);

		mUserName = userName;
	}


	public String getUserName() {
		return mUserName;
	}
}
