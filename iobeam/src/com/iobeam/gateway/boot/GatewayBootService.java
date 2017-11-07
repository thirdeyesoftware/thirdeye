package com.iobeam.gateway.boot;

import java.text.SimpleDateFormat;
import java.util.*;
import java.net.*;
import java.util.logging.*;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.HostnameVerifier;
import java.io.*;
import java.security.cert.Certificate;

import com.iobeam.boot.*;
import com.iobeam.gateway.scheduler.*;
import com.iobeam.gateway.router.*;
import com.iobeam.gateway.util.*;


/**
* Main entry point for launching the Gateway.
*/
public class GatewayBootService extends BootService {

	private BootContext mContext;

	public GatewayBootService(BootContext context) {
		mContext = context;
	}

	protected Iterator getBootables() {
		System.out.println("GatewayBootService.getBootables:");

		Vector v = new Vector();

		v.addElement(GatewayConfiguration.getBootable());
		v.addElement(getLoggerBootable());
		v.addElement(GatewayConfiguration.getBootable());
		v.addElement(getRegisterGatewayBootable());
		v.addElement(Scheduler.getBootable());
		v.addElement(RouterFactory.getBootable());
		v.addElement(HistoryLogger.getBootable());
		v.addElement(TimeService.getBootable());
		return v.iterator();
	}

	protected BootContext getBootContext() {
		return mContext;
	}


	private Bootable getRegisterGatewayBootable() {
		return new Bootable() {
			public void boot(BootContext context) throws BootException {
				final Logger l = Logger.getLogger("com.iobeam.gateway.boot");
				l.info("starting registration process.");
				GatewayConfiguration config = GatewayConfiguration.getInstance();
				l.info("config is null?" + (config == null));
				try {

					final boolean isTestHarness = "test".equals(
							config.getProperty("iobeam.gateway.mode"));

					String venueId = config.getProperty("iobeam.gateway.venue.id");
					
					config.setProperty("iobeam.gateway.registered", "false");

					// This should be replaced with code that gets the mac
					// from the hardware.
					String mac = config.getProperty("iobeam.gateway.mac.external");
					
					String prt = config.getProperty("iobeam.gateway.notify.port");

					int notifyPort = Integer.valueOf(config.getProperty(
							"iobeam.gateway.notify.port")).intValue();
					String privateIP = config.getProperty(
							"iobeam.gateway.address.internal.1");

					l.info("venue id = " + venueId);
					l.info("mac = " + mac);
					l.info("notifyPort = " + notifyPort);
					l.info("privateIP = " + privateIP);

					String url = Portal.getRegisterGatewayURL() + 	
						"?vid=" + venueId +
						"&mac=" + mac +
						"&notifyPort=" + notifyPort +
						"&privateip=" + privateIP;
					System.out.println("reg url = " + url);
					URL registerURL = new URL(url);

					l.info("registerURL = " + registerURL.toString());
					URLConnection conn = registerURL.openConnection();
					HttpsURLConnection sconn = (HttpsURLConnection) conn;
					sconn.setHostnameVerifier(new HostnameVerifier() {
						public boolean verify(String hostname,
								SSLSession session) {
							l.warning("verifying " + hostname + ", " +
									session.getPeerHost());

							if (isTestHarness) {
								return true;
							}
							// Verifier is only called on a mismatch.
							return false;
						}
					});

					System.out.println("connecting...");

					sconn.connect();

					System.out.println("done connecting...");
					// Learn the Portal's cert on first https contact.
					//
					Certificate[] certs = sconn.getServerCertificates();
					Portal.setCertificate(certs[0]);

					BufferedReader reader = new BufferedReader(
							new InputStreamReader(sconn.getInputStream()));
					String result = reader.readLine();

					l.info("result = " + result);
					boolean isAnonymous = false;
					if (result.indexOf(",") >= 0) {
						StringTokenizer st = new StringTokenizer(result, ",");

						if (st.hasMoreTokens()) {
							String ret = st.nextToken();
							if (!"success".equals(ret)) {
								throw new Exception("failed registration, ret=" + ret);
							}
						}

						if (st.hasMoreTokens()) {
							String gwId = st.nextToken();
							config.setProperty("iobeam.gateway.id",gwId);
						}

						if (st.hasMoreTokens()) {

							long siteKey = Long.parseLong(st.nextToken());
							Portal.setSiteKey(siteKey);
						}


						String venueSubscriptionType = null;
						String serverTimeInMillis = null;

						if (st.hasMoreTokens()) {
							venueSubscriptionType = st.nextToken();
						}

						if (st.hasMoreTokens()) {
							isAnonymous = "anonymous".equals(st.nextToken());
						}

					}

					config.setProperty("iobeam.gateway.subscription.anonymous", isAnonymous ? "true" : 
							"false");

					l.info("finished calling portal.");
					config.setProperty("iobeam.gateway.registered", "true");
					System.out.println("gw registered");

				}
				catch (UnknownHostException uhe) {
					l.throwing(GatewayBootService.class.getName(),
						"RegisterGatewayBootable.boot", (Throwable)uhe);
					throw new BootException("could not connect, " + 
						uhe.toString());
				}
				catch (IOException ioe) {
					l.throwing(GatewayBootService.class.getName(),
						"RegisterGatewayBootable.boot", (Throwable)ioe);

					ioe.printStackTrace();
					throw new BootException("could not connect, " +
							ioe.toString());
				}
				catch (Exception e) {
					e.printStackTrace();
					throw new BootException("could not connect, " +
							e.toString());
				}


			}
		};
	}

	private Bootable getLoggerBootable() {
		return new Bootable() {
			public void boot(BootContext context) throws BootException {
				try {
					System.out.println("GatewayBootService.Bootable: " +
							"init logger");

					// Force LogManager to reload properties.
					LogManager.getLogManager().readConfiguration();

					Logger l = Logger.getLogger(
							"com.iobeam.gateway.BootLogger");
					l.info("Logger initialized");
				}
				catch (Exception e) {
					throw new BootException(e);
				}
			}
		};
	}
}
