package com.iobeam.gateway.router;


public class DefaultRouterFactoryProvider implements RouterFactoryProvider {

	private Router mRouter;
  
	DefaultRouterFactoryProvider() throws RouterException {
		System.out.println("DefaultRouterFactoryProvider.<init>:");

		mRouter = new DefaultRouter();
	}
  

	/**
	* @see com.iobeam.gateway.router.RouterFactoryProvider#getRouter()
	*/
	public Router getRouter() {
		System.out.println("DefaultRouterFactoryProvider.getRouter:");

		return mRouter;
	}
}
