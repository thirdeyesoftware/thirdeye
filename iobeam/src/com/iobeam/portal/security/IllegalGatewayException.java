package com.iobeam.portal.security;


import com.iobeam.util.MACAddress;
import com.iobeam.portal.model.venue.VenuePK;


public class IllegalGatewayException extends GatewayException {

	public IllegalGatewayException(VenuePK venuePK, MACAddress mac) {
		super(mac.toString() + " illegal for " + venuePK);
	}


	public IllegalGatewayException(VenuePK venuePK, MACAddress mac,
			Throwable cause) {

		super(mac.toString() + " illegal for " + venuePK, cause);
	}
}
