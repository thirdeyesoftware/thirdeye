package com.iobeam.portal.util;


/**
* Describes an exception in the system business logic that higher level
* elements are expected to handle.
*/
public class BusinessLogicException extends Exception {

	public BusinessLogicException(String message) {
		super(message);
	}

	public BusinessLogicException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessLogicException(Throwable cause) {
		super(cause);
	}
}
