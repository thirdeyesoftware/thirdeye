package com.iobeam.gateway.router;


public class TestRouterFactoryProvider implements RouterFactoryProvider {

	private Router mRouter;
  
	TestRouterFactoryProvider() throws RouterException {
		System.out.println("TestRouterFactoryProvider.<init>:");

		mRouter = new TestRouter();
	}
  

	/**
	* @see com.iobeam.gateway.router.RouterFactoryProvider#getRouter()
	*/
	public Router getRouter() {
		System.out.println("TestRouterFactoryProvider.getRouter:");

		return mRouter;
	}
}
