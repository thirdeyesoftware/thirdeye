package com.iobeam.portal.security.rdbms.authenticator;

import java.util.HashMap;
import java.util.logging.Logger;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;
import weblogic.management.security.ProviderMBean;
import weblogic.security.provider.PrincipalValidatorImpl;
import weblogic.security.spi.AuthenticationProvider;
import weblogic.security.spi.IdentityAsserter;
import weblogic.security.spi.PrincipalValidator;
import weblogic.security.spi.SecurityServices;

public final class PortalAuthenticationProvider 
		implements AuthenticationProvider {

  private String mDescription;
  private LoginModuleControlFlag controlFlag;

	private static Logger cLogger = 
		Logger.getLogger("com.iobeam.portal.security.rdbms.authenticator");

  public void initialize(ProviderMBean mbean, SecurityServices services) {
		cLogger.info("PortalAuthenticationProvider.initialize");

    PortalAuthenticatorMBean myMBean = (PortalAuthenticatorMBean)mbean;
    mDescription = 
			myMBean.getDescription() + "\n" + myMBean.getVersion();

    String flag = myMBean.getControlFlag();
    if (flag.equalsIgnoreCase("REQUIRED")) {
      controlFlag = LoginModuleControlFlag.REQUIRED;
    } else if (flag.equalsIgnoreCase("OPTIONAL")) {
      controlFlag = LoginModuleControlFlag.OPTIONAL;
    } else if (flag.equalsIgnoreCase("REQUISITE")) {
      controlFlag = LoginModuleControlFlag.REQUISITE;
    } else if (flag.equalsIgnoreCase("SUFFICIENT")) {
      controlFlag = LoginModuleControlFlag.SUFFICIENT;
    } else {
      throw new IllegalArgumentException("invalid flag value" + flag);
    }
  }

  public String getDescription() {
    return mDescription;
  }

  public void shutdown() {
		System.out.println("PortalAuthenticationProvider.shutdown");
  }

  private AppConfigurationEntry getConfiguration(HashMap options) {
    cLogger.info("Requesting login module configurations");

    return new
      AppConfigurationEntry(
        "com.iobeam.portal.security.rdbms.authenticator.PortalLoginModule",
        controlFlag,
        options
      );
  }

  public AppConfigurationEntry getLoginModuleConfiguration() {
    HashMap options = new HashMap();
    return getConfiguration(options);
  }

  public AppConfigurationEntry getAssertionModuleConfiguration() {
    HashMap options = new HashMap();
    options.put("IdentityAssertion","true");
    return getConfiguration(options);
  }

  public PrincipalValidator getPrincipalValidator() {
    return new PrincipalValidatorImpl();
  }

  public IdentityAsserter getIdentityAsserter() {
		return null;
  }
}
