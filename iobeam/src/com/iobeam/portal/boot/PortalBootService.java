package com.iobeam.portal.boot;


import java.util.*;
import java.util.logging.*;
import com.iobeam.boot.*;
import com.iobeam.portal.security.PortalKeyStore;
import com.iobeam.portal.security.PasswordHash;
import com.iobeam.portal.security.SecureIDFactory;
import com.iobeam.portal.security.StringRandomizer;


/**
* Main entry point for launching the Portal.
*/
public class PortalBootService extends BootService {

	public static final String PORTAL_PROPS_FILE = "/etc/portal.properties";



	protected Iterator getBootables() {
		System.out.println("PortalBootService.getBootables:");

		Vector v = new Vector();

		v.addElement(getPropertyBootable());
		v.addElement(getLoggerBootable());
		v.addElement(PortalKeyStore.getBootable());
		v.addElement(PasswordHash.getBootable());
		v.addElement(SecureIDFactory.getBootable());
		v.addElement(StringRandomizer.getBootable());

		return v.iterator();
	}


	protected BootContext getBootContext() {
		BootContext bootContext = new BootContext() {
			public Class getServiceClass() {
				return PortalBootService.class;
			}

			public Properties getProperties() {
				throw new UnsupportedOperationException("no impl yet.");
			}
		};

		return bootContext;
	}


	private Bootable getPropertyBootable() {
		return new Bootable() {
			public void boot(BootContext context) throws BootException {
				try {
					System.out.println("PortalBootService.Bootable: " +
							"load props");
					Properties p = System.getProperties();
					p.load(BootService.class.getResourceAsStream(
							PORTAL_PROPS_FILE));
					System.setProperties(p);
				}
				catch (Exception e) {
					throw new BootException(e);
				}
			}
		};
	}


	private Bootable getLoggerBootable() {
		return new Bootable() {
			public void boot(BootContext context) throws BootException {
				try {
					System.out.println("PortalBootService.Bootable: " +
							"init logger");

					// Force LogManager to reload properties.
					LogManager.getLogManager().readConfiguration();

					Logger l = Logger.getLogger(
							"com.iobeam.portal.BootLogger");
					l.info("Logger initialized");
				}
				catch (Exception e) {
					throw new BootException(e);
				}
			}
		};
	}


	public static void main(String[] args) throws Exception {

		BootService service = new PortalBootService();

		service.boot();
	}
}
