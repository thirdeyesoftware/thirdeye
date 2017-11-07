package com.iobeam.portal.model.subscription;


public abstract class SubscriptionBuilderFactory {

	private static String DEFAULT_SUBSCRIPTION_FACTORY_PROP = 
			"iobeam.portal.subscription.defaultsubscriptionbuilderfactory";



	public static SubscriptionBuilderFactory getFactory() 
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {

		String factoryClassName = System.getProperty(
				DEFAULT_SUBSCRIPTION_FACTORY_PROP);
		
		Class factoryClass = Class.forName(factoryClassName);

		return (SubscriptionBuilderFactory) factoryClass.newInstance();

	}


	abstract public SubscriptionBuilder getSubscriptionBuilder();
}


