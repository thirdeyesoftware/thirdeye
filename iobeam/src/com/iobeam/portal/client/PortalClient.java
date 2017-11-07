package com.iobeam.portal.client;

import javax.ejb.*;
import javax.naming.*;
import java.util.*;

abstract public class PortalClient {

	public static InitialContext getInitialContext(String server, String port,
			String username, String password) throws PortalClientException {

		try {
			Properties p = new Properties();
			p.setProperty(
				Context.INITIAL_CONTEXT_FACTORY,
				"weblogic.jndi.WLInitialContextFactory");
			p.setProperty(Context.PROVIDER_URL, "t3://" + 
				server + ":" + port);
			p.setProperty(
				"java.naming.security.principal", username);
			p.setProperty(
				"java.naming.security.credentials", password);
			
			return new InitialContext(p);
		}
		catch (Exception e) {
			throw new PortalClientException(
				"could not create initial context.", e);
		}
	
	}

	public static InitialContext getInitialContext() 
			throws PortalClientException {
		String host = System.getProperty("iobeam.portal.host");
		String port = System.getProperty("iobeam.portal.port");
		String username = System.getProperty("iobeam.portal.user");
		String password = System.getProperty("iobeam.portal.password");
		return getInitialContext(host, port, username, password);
	}

}
			
