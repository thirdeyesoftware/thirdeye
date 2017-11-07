package com.iobeam.portal.security.rdbms.authenticator;



import javax.management.*;



public interface PortalIdentityAsserterMBean extends weblogic.management.commo.StandardInterface, weblogic.management.security.authentication.IdentityAsserterMBean {

        public java.lang.String getProviderClassName ();


        public java.lang.String getDescription ();


        public java.lang.String getVersion ();

        public java.lang.String[] getSupportedTypes ();



}
