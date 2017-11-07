package com.iobeam.portal.task.billing.payment.processautomaticpayment;


public class VerisignConstants {

	private static final String HOST_PROP = "iobeam.portal.verisign.host";
	private static final String TESTHOST_PROP =
			"iobeam.portal.verisign.testhost";
	private static final String PORT_PROP = "iobeam.portal.verisign.port";
	private static final String TIMEOUT_PROP =
			"iobeam.portal.verisign.timeout";
	private static final String CERTSPATH_PROP =
			"iobeam.portal.verisign.certspath";

	private VerisignConstants() {
	}


	public static String getHost() {
		String host = System.getProperty(HOST_PROP);
		if (host == null) {
			throw new Error("System property " + HOST_PROP + " not defined");
		}

		return host;
	}



	public static String getTestHost() {
		String host = System.getProperty(TESTHOST_PROP);
		if (host == null) {
			throw new Error("System property " + TESTHOST_PROP +
					" not defined");
		}

		return host;
	}


	public static int getPort() {
		String port = System.getProperty(PORT_PROP);
		if (port == null) {
			throw new Error("System property " + PORT_PROP +
					" not defined");
		}

		return Integer.parseInt(port);
	}


	public static int getTimeout() {
		String timeout = System.getProperty(TIMEOUT_PROP);
		if (timeout == null) {
			throw new Error("System property " + TIMEOUT_PROP +
					" not defined");
		}

		return Integer.parseInt(timeout);
	}


	public static String getCertsPath() {
		String host = System.getProperty(CERTSPATH_PROP);
		if (host == null) {
			throw new Error("System property " + CERTSPATH_PROP +
					" not defined");
		}

		return host;
	}
}
