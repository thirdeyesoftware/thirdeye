package com.iobeam.gateway.router;


public class RouterException extends Exception {
  
	public RouterException(String msg) {
		super(msg);
	}


	public RouterException(Exception cause) {
		super(cause);
	}


	public RouterException(String message, Exception cause) {
		super(message, cause);
	}
}
