package com.iobeam.portal.model.subscription;


public class DefaultSubscriptionBuilderFactory
		extends SubscriptionBuilderFactory {


	public SubscriptionBuilder getSubscriptionBuilder() {
		return new DefaultSubscriptionBuilder();
	}
		
}

