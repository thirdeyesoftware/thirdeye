package com.iobeam.gateway.router;


import com.iobeam.boot.*;
import com.iobeam.gateway.util.GatewayConfiguration;


public class RouterFactory implements BootClient {

	public static final String FACTORY_PROVIDER_PROP =
			"iobeam.gateway.RouterFactoryProvider.class";


	private static RouterFactory theRouterFactory = null;

	private RouterFactoryProvider mProvider = null;


	private RouterFactory(RouterFactoryProvider provider) {
		mProvider = provider;
	}


	public static RouterFactory getRouterFactory() {
		if (theRouterFactory == null) {
			throw new RuntimeException("not initialized");
		}

		return theRouterFactory;
	}


	/**
	* Leave option for switchable RouterFactoryProviders for
	* testing and other purposes.
	*
	* Possibly use a property to indicate which class to create.
	*/
	static private RouterFactoryProvider createProvider()
			throws RouterException,
			ClassNotFoundException,
			InstantiationException,
			IllegalAccessException {

		String providerClassName = 
				GatewayConfiguration.getInstance().getProperty(FACTORY_PROVIDER_PROP);
		if (providerClassName == null) {
			throw new RuntimeException("no provider defined at property " +
					FACTORY_PROVIDER_PROP);
		}

		Class pc = Class.forName(providerClassName);

		return (RouterFactoryProvider) pc.newInstance();
	}


	private RouterFactoryProvider getProvider() {
		return mProvider;
	}





	/**
	* Clients of the system's Router use this method to access the
	* Router.
	*
	* The underlying RouterFactoryProvider is responsible for
	* lifecycle of the system's Router instance(s).
	*/
	static public Router getRouter() {
		return theRouterFactory.getProvider().getRouter();
	}





	public static Bootable getBootable() {
		return new RouterBootable();
	}


	private static class RouterBootable implements Bootable {
		public void boot(BootContext bootContext)
				throws BootException {

			try {
				theRouterFactory = new RouterFactory(createProvider());
			}
			catch (Exception e) {
				e.printStackTrace();
				throw new BootException(e);
			}
		}
	}
}
