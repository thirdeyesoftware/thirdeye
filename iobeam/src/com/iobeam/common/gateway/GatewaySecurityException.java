package com.iobeam.common.gateway;



/**
* Indicates a security problem within a Gateway, or between a Gateway
* and the Portal.
*
* @see InvalidGatewayLicenseException
*/
public class GatewaySecurityException extends Exception {

	public GatewaySecurityException(String msg) {
		super(msg);
	}


	public GatewaySecurityException(String msg, Throwable cause) {
		super(msg, cause);
	}


	public GatewaySecurityException(Throwable cause) {
		super(cause);
	}
}
