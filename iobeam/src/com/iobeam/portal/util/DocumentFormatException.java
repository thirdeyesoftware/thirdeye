package com.iobeam.portal.util;

public class DocumentFormatException extends Exception {
	
	public DocumentFormatException(String msg) {
		super(msg);
	}

	public DocumentFormatException(String msg, Throwable t) {
		super(msg, t);
	}

	public String toString() {
		return "DocumentFormatException:" + super.toString();
	}
}

