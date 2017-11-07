package com.iobeam.portal.security.rdbms.authenticator;

import javax.management.*;
import weblogic.management.security.authentication.AuthenticatorMBean;
import weblogic.management.commo.StandardInterface;

public interface PortalAuthenticatorMBean 
		extends StandardInterface, AuthenticatorMBean {

	public String getProviderClassName();

	public String getDescription();

	public String getVersion();

}


