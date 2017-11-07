package com.iobeam.portal.security.rdbms.authenticator;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import weblogic.management.security.ProviderMBean;
import weblogic.security.spi.AuthenticationProvider;
import weblogic.security.spi.IdentityAsserter;
import weblogic.security.spi.IdentityAssertionException;
import weblogic.security.spi.PrincipalValidator;
import weblogic.security.spi.SecurityServices;

public final class PortalIdentityAsserterProvider
		implements AuthenticationProvider, IdentityAsserter {
  
	final static private String TOKEN_TYPE   = "PortalPerimeterAtnToken";
  final static private String TOKEN_PREFIX = "username=";

  private String mDescription;

  public void initialize(ProviderMBean mbean, SecurityServices services) {
		System.out.println("PortalIdentityAsserterProvider.initialize");

    PortalIdentityAsserterMBean myMBean = 
			(PortalIdentityAsserterMBean)mbean;
    mDescription = 
			myMBean.getDescription() + "\n" + myMBean.getVersion();
  }

  public IdentityAsserter getIdentityAsserter() {
    return this;
	}

  public CallbackHandler assertIdentity(String type, Object token) 
			throws IdentityAssertionException {
		System.out.println("PortalIdentityAsserterProvider.assertIdentity");
		System.out.println("\tType\t\t= "  + type);
		System.out.println("\tToken\t\t= " + token);

    if (!(TOKEN_TYPE.equals(type))) {

			String error =
        "PortalIdentityAsserter received unknown token type \"" + type + "\"." +
        " Expected " + TOKEN_TYPE;

			System.out.println("\tError: " + error);

			throw new IdentityAssertionException(error);
    }

    if (!(token instanceof byte[])) {

 	  	String error =
			"PortalIdentityAsserter received unknown token class \"" + token.getClass() + "\"." +
  			" Expected a byte[].";
			System.out.println("\tError: " + error);

      throw new IdentityAssertionException(error);
    }

    byte[] tokenBytes = (byte[])token;
    if (tokenBytes == null || tokenBytes.length < 1) {
      String error =
        "PortalIdentityAsserter received empty token byte array";

	    System.out.println("\tError: " + error);

      throw new IdentityAssertionException(error);
    }

    String tokenStr = new String(tokenBytes);

    if (!(tokenStr.startsWith(TOKEN_PREFIX))) {
      String error =
        "PortalIdentityAsserter received unknown token string \"" + type + "\"." +
        " Expected " + TOKEN_PREFIX + "username";

      System.out.println("\tError: " + error);

      throw new IdentityAssertionException(error);
    }

    String userName = tokenStr.substring(TOKEN_PREFIX.length());

    System.out.println("\tuserName\t= " + userName);

    return new PortalIdentityAsserterCallbackHandler(userName);
  }

  public void shutdown() {
	  System.out.println("PortalIdentityAsserterProvider.shutdown");
  }

  public String getDescription() {
    return mDescription;
  }

  public AppConfigurationEntry getLoginModuleConfiguration() {
    return null;
  }

  public AppConfigurationEntry getAssertionModuleConfiguration() {
    return null;
  }

  public PrincipalValidator getPrincipalValidator() {
    return null;
  }
}

/**
 * A simple CallbackHandler used for returning user name and token
 */
class PortalIdentityAsserterCallbackHandler 
		implements CallbackHandler {

  private String mUserName;

  /**
   * Obtain user name
   */
  public PortalIdentityAsserterCallbackHandler(String user) {
    mUserName = user;
  }

  /**
   * Handle the callback from the PrincipalAuthenticator
   */
  public void handle(Callback[] callbacks) 
			throws UnsupportedCallbackException {

    for (int i = 0; i < callbacks.length; i++) {
      if (callbacks[i] instanceof NameCallback) {
        // Supply the user name
        NameCallback nc = (NameCallback) callbacks[i];
        nc.setName(mUserName);
      } else {
        throw new UnsupportedCallbackException(
					callbacks[i], "Unrecognized Callback");
      }
    }
  }
}
