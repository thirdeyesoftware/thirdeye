package com.iobeam.boot;


import java.util.*;
import java.util.logging.*;


/**
*/
public abstract class BootService {

	private static boolean cIsBooted = false;


	public BootService() {
		System.out.println("BootService.<init>: ");
	}


	public void boot() throws BootException {
		Logger.getLogger("com.iobeam.boot").info("boot");

		BootContext bootContext = getBootContext();
		Class serviceClass = bootContext.getServiceClass();
		if (!BootService.class.isAssignableFrom(serviceClass)) {
			throw new BootException("BootContext has service class " +
					serviceClass.getName());
		}

		synchronized (BootService.class) {
			String bootedProp =
					"com.iobeam.boot.isBooted." + serviceClass.getName();

			String isBooted = System.getProperty(bootedProp);
			if ("true".equals(isBooted)) {
				Logger.getLogger("com.iobeam.boot").info("Already booted " +
						serviceClass.getName() + ", ignoring request.");

				return;
			} else {
				System.setProperty(bootedProp, "true");
			}
		}



		try {
			for (Iterator it = getBootables(); it.hasNext(); ) {
				((Bootable) it.next()).boot(bootContext);
			}
		}
		catch (BootException be) {
			Logger.getLogger("com.iobeam.boot").throwing(
					BootService.class.getName(), "boot", be);
			throw be;
		}
		catch (Throwable t) {
			BootException be = new BootException(t);

			Logger.getLogger("com.iobeam.boot").throwing(
					BootService.class.getName(), "boot", be);

			throw be;
		}

		Logger.getLogger("com.iobeam.boot").exiting(
				BootService.class.getName(), "boot");
	}


	/**
	* Determine which classes are registered
	* as BootClients and returns an Iterator over their corresponding
	* Bootables.  Bootables are processed in order.
	*/
	protected abstract Iterator getBootables();


	protected abstract BootContext getBootContext();
}
