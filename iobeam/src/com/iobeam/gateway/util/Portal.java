package com.iobeam.gateway.util;


import java.util.logging.Logger;
import java.util.*;
import java.security.cert.Certificate;
import com.iobeam.gateway.util.GatewayConfiguration;


/**
* Represents the Portal from the Gateway's perspective.
*/
public class Portal {

	private static final String LOGGER = "com.iobeam.gateway";


	public static final String PORTAL_DOMAIN_PROP = 
		"iobeam.portal.domain";

	public static final String PORTAL_PORT_PROP = 
		"iobeam.portal.port";

	public static final String PORTAL_SECURE_PORT_PROP = 
		"iobeam.portal.secure.port";

	public static final String PORTAL_TIME_SERVICE_URL_PROP = 
		"iobeam.portal.timeservice.url";

	public static final String PORTAL_ANON_SIGNON_URL_PROP =
			"iobeam.portal.validate.anonymous.url";

	public static final String PORTAL_ANON_SIGNON_TYPE_PROP =
			"iobeam.portal.validate.anonymous.type";

	public static final String PORTAL_SIGNON_URL_PROP =
			"iobeam.portal.signon.url";
	
	public static final String PORTAL_SIGNON_TYPE_PROP = 
			"iobeam.portal.signon.type";

	public static final String PORTAL_CONTACT_LISTENER_URL_PROP =
			"iobeam.portal.contactListener.url";

	public static final String PORTAL_CONTACT_LISTENER_TYPE_PROP = 
			"iobeam.portal.contactListener.type";

	public static final String PORTAL_REGISTER_GATEWAY_URL_PROP = 
			"iobeam.portal.registerGateway.url";

	public static final String PORTAL_REGISTER_GATEWAY_TYPE_PROP = 
			"iobeam.portal.registerGateway.type";
	
	private static Certificate theCertificate = null;
	private static long theSiteKey = 0;

	private static final String PORTAL_REDIRECT_HACK_URL_PROP = 
			"iobeam.portal.signon.success.url";

	private static final String PORTAL_NO_SERVICE_URL_PROP = 	
			"iobeam.portal.noservice.url";

	private static final GatewayConfiguration cConfig = 
			GatewayConfiguration.getInstance();

	private Portal() {
	}

	static public String getTimeServiceURL() {
		String url = cConfig.getProperty(PORTAL_TIME_SERVICE_URL_PROP);

		if (url == null) {
			throw new Error(PORTAL_TIME_SERVICE_URL_PROP + " not defined!");
		}
		else {
			return createUrl(url,null);
		}
	}
	
	static public String getPortalDomain() {
		String url = cConfig.getProperty(PORTAL_DOMAIN_PROP);
		if (url == null) {
			throw new Error(PORTAL_DOMAIN_PROP + " not defined!");
		}
		return url;
	}

	static public String getPortalPort(boolean secure) {
		String port = (secure) ? 
				cConfig.getProperty(PORTAL_SECURE_PORT_PROP) : 
				cConfig.getProperty(PORTAL_PORT_PROP);
		if (port == null) {
			throw new Error(PORTAL_PORT_PROP + " not defined!");
		}
		return port;
	}

	static public String getPingURL() {
		String url = createUrl("", null);
		if (url == null) {
			throw new Error(PORTAL_DOMAIN_PROP + " not defined!");
		}
		return url;

	}

	static public String getNoServiceURL() {
		String url = cConfig.getProperty(PORTAL_NO_SERVICE_URL_PROP);
		return createUrl(url, null);
	}

	static public String getAnonymousValidateURL() {
		String portalSignonURL = cConfig.getProperty(PORTAL_ANON_SIGNON_URL_PROP);

		if (portalSignonURL == null) {
			throw new Error(PORTAL_ANON_SIGNON_URL_PROP + " not defined!");
		}

		String type = cConfig.getProperty(PORTAL_ANON_SIGNON_TYPE_PROP);
		
		return createUrl(portalSignonURL, type);

	}

	static public String getSignonURL() {
		String portalSignonURL = cConfig.getProperty(PORTAL_SIGNON_URL_PROP);

		if (portalSignonURL == null) {
			throw new Error(PORTAL_SIGNON_URL_PROP + " not defined!");
		}

		String type = cConfig.getProperty(PORTAL_SIGNON_TYPE_PROP);
		
		return createUrl(portalSignonURL, type);

	}

	static public String getSignonSuccessfulURL() {
		String url = cConfig.getProperty(
			PORTAL_REDIRECT_HACK_URL_PROP);

		if (url == null) {
			throw new Error(PORTAL_REDIRECT_HACK_URL_PROP + " not defined!");
		}
		
		return createUrl(url, null);
	}

	static public String getContactURL() {
		String portalContactListenerURL =
				cConfig.getProperty(PORTAL_CONTACT_LISTENER_URL_PROP);

		if (portalContactListenerURL == null) {
			throw new Error(PORTAL_CONTACT_LISTENER_URL_PROP +
					" not defined!");
		}
		
		String type = cConfig.getProperty(PORTAL_CONTACT_LISTENER_TYPE_PROP);

		return createUrl(portalContactListenerURL, type);
	}

	static public String getRegisterGatewayURL() {
		Properties props = GatewayConfiguration.getInstance().
				getProperties();
		/*
		for (Enumeration en = props.keys();en.hasMoreElements();) {
			String key = (String)en.nextElement();
			System.out.println(key + "=" + props.getProperty(key));
		}
		*/

		String url = 
			cConfig.getProperty(PORTAL_REGISTER_GATEWAY_URL_PROP);
		if (url == null) {
			throw new Error(PORTAL_REGISTER_GATEWAY_URL_PROP + 
				" not defined.");
		}
		
		String type = cConfig.getProperty(PORTAL_REGISTER_GATEWAY_TYPE_PROP);

		return createUrl(url, type);
	}

	/**
	 * generates http/https url for specified url and connection type.
	 */
	static private String createUrl(String url, String connectionType) {
		String type = null;
		String portalUrl = null;

		if ("secure".equals(connectionType)) {
			portalUrl = "https://" + getPortalDomain() + ":" + 
				getPortalPort(true);
		}
		else {
			portalUrl = "http://" + getPortalDomain() + ":" + 
				getPortalPort(false);
		}
		System.out.println("<<<<< url = " + portalUrl + url + " >>>>>");

		return portalUrl + url;
	}


	static public void setCertificate(Certificate certificate) {
		theCertificate = certificate;

		Logger.getLogger(LOGGER).info("");
	}


	static public Certificate getCertificate() {
		return theCertificate;
	}


	/**
	* Initializes the shared-secret between Venue and Portal.
	* Called during registration process.
	*/
	static public void setSiteKey(long siteKey) {
		theSiteKey = siteKey;

		Logger.getLogger(LOGGER).info("siteKey = " + siteKey);
	}


	/**
	* Returns shared-secret used for secure communication between the
	* Venue and the Portal.
	*/
	static public long getSiteKey() {
		return theSiteKey;
	}
}
