package com.iobeam.portal.util;

public class DuplicateRecordException extends Exception {
	
	public DuplicateRecordException() {
		super();
	}
	public DuplicateRecordException(String cause) {
		super(cause);
	}
	
	public String toString() {
		return "DuplicateRecordException:" + super.getMessage();
	}
}
