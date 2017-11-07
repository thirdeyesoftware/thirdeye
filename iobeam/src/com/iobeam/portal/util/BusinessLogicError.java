package com.iobeam.portal.util;


/**
* Describes an error in the system business logic that higher
* level elements are not expected to handle.  This signifies an
* unexpected condition.  The system shall log the occurrence of
* BusinessLogicErrors.
*/
public class BusinessLogicError extends Error {

	public BusinessLogicError(String message) {
		super(message);
	}

	public BusinessLogicError(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessLogicError(Throwable cause) {
		super(cause);
	}
}
