package com.iobeam.boot;


/**
* Describes an exception that occurs during the boot process.
*/
public class BootException extends java.lang.Exception {
	public BootException(String msg) {
		super(msg);
	}


	public BootException(Throwable cause) {
		super(cause);
	}


	public BootException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
