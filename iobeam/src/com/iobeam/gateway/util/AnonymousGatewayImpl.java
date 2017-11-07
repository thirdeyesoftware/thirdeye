package com.iobeam.gateway.util;

public class AnonymousGatewayImpl implements AnonymousGateway {

	public AnonymousGatewayImpl() {

	}

	public boolean isAnonymous() {
  	String anon = GatewayConfiguration.getInstance().
			getProperty("iobeam.gateway.subscription.anonymous");
		if (anon == null) { 
			return false;
		}
		return Boolean.valueOf(anon).booleanValue();
	}
}

