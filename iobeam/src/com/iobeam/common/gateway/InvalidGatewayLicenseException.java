package com.iobeam.common.gateway;



/**
* Indicates an operation could not be performed because a
* supplied GatewayLicense was invalid.
*/
public class InvalidGatewayLicenseException extends GatewaySecurityException {

	public InvalidGatewayLicenseException(String msg) {
		super(msg);
	}


	public InvalidGatewayLicenseException(String msg, Throwable cause) {
		super(msg, cause);
	}


	public InvalidGatewayLicenseException(Throwable cause) {
		super(cause);
	}
}
