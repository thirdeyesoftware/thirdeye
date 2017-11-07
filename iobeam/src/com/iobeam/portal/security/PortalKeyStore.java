package com.iobeam.portal.security;


import java.io.*;
import java.security.cert.Certificate;
import java.security.*;
import java.util.Enumeration;
import java.util.logging.Logger;
import com.iobeam.boot.*;


public class PortalKeyStore implements BootClient {

	public static final String KEYSTORE_PATH_PROP =
			"iobeam.portal.keystore.path";

	public static final String KEYSTORE_PASSWORD_PROP =
			"iobeam.portal.keystore.password";

	public static final String KEYSTORE_KEY_PASSWORD_PROP =
			"iobeam.portal.keystore.key.password";

	public static final String KEYSTORE_CERT_ALIAS_PROP =
			"iobeam.portal.keystore.certAlias";


	static private PortalKeyStore thePortalKeyStore;


	private KeyStore mKeyStore;
	private String mKeyAlias;
	private char[] mKeyPassword;



	private PortalKeyStore() throws Exception {
		Logger logger = Logger.getLogger(
				"com.iobeam.portal.util.PortalKeyStore");

		logger.info("PortalKeyStore.<init>: ");

		String path = System.getProperty(KEYSTORE_PATH_PROP);
		String password = System.getProperty(KEYSTORE_PASSWORD_PROP);

		mKeyStore = KeyStore.getInstance("jks");

		InputStream is = new FileInputStream(path);

		mKeyStore.load(is, password.toCharArray());

		mKeyAlias = System.getProperty(KEYSTORE_CERT_ALIAS_PROP);
		if (mKeyAlias == null) {
			throw new Error(KEYSTORE_CERT_ALIAS_PROP + " not defined");
		}

		String keyPassword = System.getProperty(KEYSTORE_KEY_PASSWORD_PROP);
		if (keyPassword == null) {
			throw new Error(KEYSTORE_KEY_PASSWORD_PROP + " not defined");
		}
		mKeyPassword = keyPassword.toCharArray();
	}


	static public PrivateKey getPrivateKey() throws GeneralSecurityException {
		Logger logger = Logger.getLogger(
				"com.iobeam.portal.util.PortalKeyStore");

		logger.info("PortalKeyStore.getPrivateKey: ");

		return thePortalKeyStore._getPrivateKey();

	}


	private PrivateKey _getPrivateKey() throws GeneralSecurityException {
		Logger l = Logger.getLogger("com.iobeam.portal.security");
		l.info(mKeyAlias);
		l.info(new String(mKeyPassword));

		return (PrivateKey) getKeyStore().getKey(mKeyAlias, mKeyPassword);
	}


	static public Certificate getCertificate()
			throws GeneralSecurityException {
		return thePortalKeyStore._getCertificate();
	}


	private Certificate _getCertificate() throws GeneralSecurityException {
		return getKeyStore().getCertificate(mKeyAlias);
	}


	public static Bootable getBootable() {
		return new Bootable() {
			public void boot(BootContext context) throws BootException {
				try {
					Logger logger = Logger.getLogger(
							"com.iobeam.portal.util.PortalKeyStore");

					logger.info("PortalKeyStore.Bootable: ");

					PortalKeyStore.thePortalKeyStore = new PortalKeyStore();
				}
				catch (Throwable t) {
					throw new BootException(t);
				}
			}
		};
	}


	public KeyStore getKeyStore() {
		return mKeyStore;
	}
}
