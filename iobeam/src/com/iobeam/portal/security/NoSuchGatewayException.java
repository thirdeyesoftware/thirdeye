package com.iobeam.portal.security;


import com.iobeam.portal.model.venue.VenuePK;


public class NoSuchGatewayException extends GatewayException {

	public NoSuchGatewayException(VenuePK venuePK) {
		super(venuePK + " has no gateway");
	}

	public NoSuchGatewayException(VenuePK venuePK, Throwable cause) {
		super(venuePK + " has no gateway", cause);
	}
}
