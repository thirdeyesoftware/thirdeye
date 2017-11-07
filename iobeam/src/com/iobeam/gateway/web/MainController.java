package com.iobeam.gateway.web;


import java.text.*;
import java.util.logging.Logger;
import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.Enumeration;
import java.security.cert.Certificate;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.net.ssl.*;
import com.iobeam.gateway.dhcp.*;
import com.iobeam.gateway.util.Portal;
import com.iobeam.gateway.router.*;
import com.iobeam.gateway.util.*;
import com.iobeam.util.MACAddress;
import com.iobeam.gateway.boot.HistoryLogger;



public class MainController extends HttpServlet {

	private static DateFormat cShortDateFormat =
			new SimpleDateFormat("MM/dd HH:mm:ss");

	public static final String VENUE_NAME_PROP =
			"iobeam.gateway.venue.name";

	public static final String VENUE_ID_PROP =
			"iobeam.gateway.venue.id";

	public static final String GLOBAL_ALLOW_ACCESS_PROP = 
			"iobeam.gateway.access.enabled";

	public void init(ServletConfig config) throws ServletException {
		System.out.println("MainController.init: " + getDebugTime());
		super.init(config);
	}


	public void doGet(HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {

		if (isAdminRequest(request)) {
			response.sendRedirect("/gwadmin/index.jsp");
			return;
		}
			
		StringBuffer debugSB = new StringBuffer("MainController.doGet: ");
		debugSB.append(getDebugTime()).append(" ");

		debugSB.append("uri=").append(request.getRequestURI());
		debugSB.append(" ");
		debugSB.append("remotehost=").append(request.getRemoteHost());
		debugSB.append(" ");
		debugSB.append("remoteIP=").append(request.getRemoteAddr());
		debugSB.append(" ");
		debugSB.append("path=").append(request.getServletPath());
		debugSB.append(" ");
		debugSB.append("pathInfo=").append(request.getPathInfo());
		System.out.println(debugSB.toString());

		System.out.println(getHeaders(request));

		HttpSession session = request.getSession();
		ServletContext context = getServletConfig().getServletContext();

		String inetAddress = request.getRemoteAddr();

		MACAddress mac;
		long contactID = 0L;
		RouteLease routeLease = null;
		String redirect = "";
		String finalRedirect = "";

		mac = getMACAddress(inetAddress);

		try {
			routeLease = RouterFactory.getRouter().getActiveLease(mac);
			boolean allowService = allowService();


			if (routeLease == null) {
				long time = (new Date()).getTime();
				//time += 21600000L; // 6 hours
				time += getRouteLeaseDurationMillis();

				contactID = getContactID(inetAddress, mac.toString());

				routeLease = new RouteLease(ClientState.MEMBER_RESTRICTED,
						new Date(time),
						InetAddress.getByName(inetAddress), mac, contactID);
				RouterFactory.getRouter().initializeRouteLease(routeLease);

				HistoryLogger.writeLog(routeLease);

				if (!allowService) {
					finalRedirect = Portal.getNoServiceURL();
				}
				else {
					redirect = URLEncoder.encode(
						buildRedirectURL(request), "UTF-8");

					finalRedirect = getPortalRedirectURL(contactID, redirect);

					System.out.println("MainController.doGet: " +
							getDebugTime() + " new contact redirect to " +
							finalRedirect);
				}
			} else {
				if (!allowService) {
					finalRedirect = Portal.getNoServiceURL();
				}
				else {
					if (routeLease.getInetAddress().equals(
				 			InetAddress.getByName(inetAddress))) {
						
						if (routeLease.getClientState().equals(
								ClientState.MEMBER_RESTRICTED)) {
							contactID = routeLease.getContactID();
	
							redirect = URLEncoder.encode(
									buildRedirectURL(request), "UTF-8");
	
							finalRedirect = getPortalRedirectURL(contactID, redirect);

							System.out.println("MainController.doGet: " +
									getDebugTime() + " existing contact " +
									contactID + " redirect to " +
									finalRedirect);
						} else {

							redirect = URLEncoder.encode(
								buildRedirectURL(request), "UTF-8");

							System.out.println("MainController.doGet: " +
									getDebugTime() + " existing unrestricted lease " +
									routeLease + " redirect to " + finalRedirect);

							return;
						}

					} else {

						RouterFactory.getRouter().removeRouteLease(mac);
					
						long time = (new Date()).getTime();
						time += 21600000L; // 6 hours
						contactID = getContactID(inetAddress, mac.toString());
					
						RouteLease newRouteLease = 
								new RouteLease(
									ClientState.MEMBER_RESTRICTED, new Date(time),
									InetAddress.getByName(inetAddress), mac, contactID);
						
						RouterFactory.getRouter().initializeRouteLease(newRouteLease);

						redirect = URLEncoder.encode(
								buildRedirectURL(request), "UTF-8");

						finalRedirect = getPortalRedirectURL(contactID, redirect);

						System.out.println("MainController.doGet: " +
								getDebugTime() + " new contact redirect to " +
								finalRedirect);
					}
				}
			}

			boolean isAnonymous = isAnonymousGateway();
			System.out.println("<><><> Gateway Anonymous? " + isAnonymous + 
					" <><><>");
			if (isAnonymous) {
				long expiration = 0L;

				if ((expiration = 
						validateAnonymousSignon(contactID, inetAddress, mac.toString())) > 0) {
					RouteLease anonRouteLease = 
							new RouteLease(ClientState.MEMBER_UNRESTRICTED, 
								new Date(expiration),
								InetAddress.getByName(inetAddress),
								mac,
								contactID);
					RouterFactory.getRouter().setRouteLease(anonRouteLease);
					HistoryLogger.writeLog(anonRouteLease);
					finalRedirect = getAnonymousRedirect(redirect);
				}
			}

			response.addHeader("Cache-Control", URLEncoder.encode(
					"no-cache, must-revalidate", "US-ASCII"));
			response.addHeader("Connection","close");

			response.sendRedirect(finalRedirect);
		}
		catch (RouterException re) {
			System.out.println(re.toString());
		}
		catch (UnsupportedEncodingException uee) {
			System.out.println("encoding unspported.");
		}
	}


	public void doPost(HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}


	private MACAddress getMACAddress(String inetAddress) {
		MACAddress mac = null;

		try {
			InetAddress ia = InetAddress.getByName(inetAddress);

			DHCPLease lease = DHCPLeaseManager.getLease(ia);
			if (lease != null && lease.isBound()) {
				mac = lease.getMACAddress();
			}
		}
		catch (Exception e) {
			e.printStackTrace();

			throw new RuntimeException(e.toString());
		}

		System.out.println("MainController.getMACAddress: " + inetAddress +
				" " + mac);

		if (mac == null) {
			throw new Error("contact " + inetAddress + " has no MAC");
		}

		return mac;
	}


	private String getVenueName() {
		String venueName = System.getProperty(VENUE_NAME_PROP);

		if (venueName == null) {
			throw new Error(VENUE_NAME_PROP + " not defined!");
		}

		return venueName;
	}


	private long getVenueID() {
		String venueID = System.getProperty(VENUE_ID_PROP);

		if (venueID == null) {
			throw new Error(VENUE_ID_PROP + " not defined!");
		}

		return Long.parseLong(venueID);
	}


	private String getPortalRedirectURL(long contactID, String redirect) {
		return Portal.getSignonURL() + "?cid=" + contactID + 
			"&redirect=" + redirect;
	}

	/**
	* @deprecated we send this information via POST to protect the
	* site key.
	*/
	private String getContactNotifyURL(long venueID,
			String inetAddress, String macAddress) {

		return Portal.getContactURL() + "?venueID=" + venueID +
				"&userInetAddress=" + inetAddress +
				"&userMACAddress=" + macAddress;
	}


	private void sendContactData(URLConnection conn,
			String inetAddress, String macAddress)
			throws IOException {

		BufferedWriter bw = new BufferedWriter(
				new OutputStreamWriter(conn.getOutputStream(), "US-ASCII"));

		StringBuffer sb = new StringBuffer();

		sb.append("venueID=").append(getVenueID());
		sb.append("&");
		sb.append("userInetAddress=").append(
				URLEncoder.encode(inetAddress, "UTF-8"));
		sb.append("&");
		sb.append("userMACAddress=").append(
				URLEncoder.encode(macAddress, "UTF-8"));
		sb.append("&");
		sb.append("siteKey=").append(Portal.getSiteKey());

		bw.write(sb.toString());
		bw.flush();
		bw.close();
	}

	/**
	* Notifies portal of the contact, and gets a contactID from the
	* portal.
	*
	* @param inetAddress the InetAddress of the contact.
	* @param macAddress the MAC Address of the contact's nic.
	*/
	private long getContactID(String inetAddress, String macAddress)
			throws IOException {

		long contactID = 0;

		System.out.println("MainController.getContactID: " +
				inetAddress + " " + macAddress);

		final boolean isTestHarness = "test".equals(
				System.getProperty("iobeam.gateway.mode", "live").trim());


		// String contactURLSpec = getContactNotifyURL(getVenueID(),
		// 		inetAddress, macAddress);

		String contactURLSpec = Portal.getContactURL();

		URL contactNotifyURL = new URL(contactURLSpec);


		URLConnection conn = contactNotifyURL.openConnection();

		// System.out.println("MainController.contactNotifyPortal: conn is " +
		// 		conn.getClass().getName());

		HttpsURLConnection sconn = (HttpsURLConnection) conn;

		// System.out.println("MainController.contactNotifyPortal: sconn is " +
		// 		sconn.getClass().getName());

		sconn.setHostnameVerifier(new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				System.out.println(
						"MainController.HostnameVerifier.verify: " +
						hostname + " against peer=" + session.getPeerHost());

				// Verifier is only called on a mismatch.
				if (isTestHarness) { 
					return true;
				}

				return false;
			}
		});

		sconn.setRequestMethod("POST");
		sconn.setDoOutput(true);
		sconn.setDoInput(true);


		try {
			conn.connect();
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
			throw ioe;
		}

		sendContactData(sconn, inetAddress, macAddress);


		InputStream is = conn.getInputStream();

		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String cid = br.readLine();
		contactID = Long.parseLong(cid);
		br.close();

		// What resources are left lingering?

		if (conn instanceof HttpURLConnection) {
			// System.out.println("MainController.contactNotifyPortal: " +
			// 		"disconnect HttpURLConnection");

			((HttpURLConnection) conn).disconnect();
		}

		System.out.println("MainController.getContactID: done " +
				getDebugTime() + " " +
				"contactID=" + contactID + " " + inetAddress + " " +
				macAddress);

		return contactID;
	}


	private static String getDebugTime() {
		return cShortDateFormat.format(new Date());
	}


	/**
	* Returns a String of http headers from the specified request.
	*/
	private String getHeaders(HttpServletRequest request) {
		Enumeration headerNames = request.getHeaderNames();
		StringBuffer sb = new StringBuffer(0);
		sb.append("--http headers--\n");
		while (headerNames.hasMoreElements()) {
			String name = (String)headerNames.nextElement();
			sb.append(name).append(":");
			sb.append(request.getHeader(name));
			sb.append("\n");
		}
		return sb.toString();
	}


	/**
	* Reconstructs the original request based on information specified in the
	* request object.
	*/
	private String buildRedirectURL(HttpServletRequest request) {
		StringBuffer sb = new StringBuffer(0);
		
		String host = request.getHeader("host");
		if (host == null || host.trim().equals("")) {
			return "";
		}

		// Protects against recursive loop
		// In IE and Mozilla, this is possible.
		if (host.equals(request.getRemoteHost())) {
			return "";
		}


		// Need to check if host matches the host in any existing
		// request parm.  THAT is a loop.


		String scheme = request.getScheme();
		if (scheme == null || scheme.trim().equals("")) {
			scheme = "http";
		}

		String uri = request.getRequestURI();
		if (uri == null || uri.trim().equals("")) {
			uri = "/";
		}

		// Protect against message.real.com req by RMA/1.0
		// (and similar case) in which client does redirect
		// but does not respect host field in redirect.
		// The result is the client continues to request
		// a host that iptables rewrites to here, causing the
		// redirect field to grow.
		if (uri.indexOf(Portal.getSignonURL()) >= 0) {
			return "";
		}


		String queryString = request.getQueryString();
		
		sb.append(scheme).append("://");
		sb.append(host);
		sb.append(uri);

		if (queryString != null && !queryString.trim().equals("")) {
			sb.append("?").append(queryString);
		}

		System.out.println("MainController.buildRedirectURL: " + sb);

		StringBuffer sb2 = request.getRequestURL();
		if (queryString != null && !queryString.trim().equals("")) {
			sb2.append("?").append(queryString);
		}

		System.out.println("MainController.buildRedirectURL: alt " + sb2);

		return sb.toString();
	}

	private static boolean allowService()  {
		try {
			boolean allowSchedule = GatewaySchedule.getInstance().
				allowService(new Date());
			System.out.println("allowSchedule = " + allowSchedule);
			boolean globalAllowAccess = false;

			if (System.getProperty(GLOBAL_ALLOW_ACCESS_PROP) == null) {
				globalAllowAccess = false;
			}
			else {
				globalAllowAccess = Boolean.valueOf(
						System.getProperty(GLOBAL_ALLOW_ACCESS_PROP)).booleanValue();
				System.out.println("global_access_allowed = " + globalAllowAccess);
			}
			return (allowSchedule && globalAllowAccess);

		}
		catch (Exception e) {
			return false;
		}
	}

	public static void main(String args[]) {
		System.out.println(allowService());
	}

	private boolean isAdminRequest(HttpServletRequest request) {
		String myip = GatewayConfiguration.getInstance().
				getProperty("iobeam.gateway.address.internal.1");
		System.out.println("<<request uri == " + request.getRequestURI() + ">>");
		//if (request.getRequestURI().indexOf(myip) >= 0) {
			if (request.getRequestURI().indexOf("gwadmin") >= 0) {
				return true;
			}
		//}
		return false;
	}

	private static long getRouteLeaseDurationMillis() {
		String duration = GatewayConfiguration.getInstance().
				getProperty("iobeam.gateway.routelease.duration");
		System.out.println("routelease.duration=" + duration);
		long dur = Long.parseLong(duration);
		return (dur * 60 * 60 * 1000);
	}

	private boolean isAnonymousGateway() {
		try {
			Class agClass =
				Class.forName("com.iobeam.gateway.util.AnonymousGatewayImpl");
			AnonymousGateway ag = (AnonymousGateway)agClass.newInstance();
			return ag.isAnonymous();
		}
		catch (Exception e) {
			return false;
		}
	}

	private long validateAnonymousSignon(long contactID, 
			String inetAddress, String macAddress) throws IOException {

		long expiration = 0L;

		final boolean isTestHarness = "test".equals(
				System.getProperty("iobeam.gateway.mode", "live").trim());

		// String contactURLSpec = getContactNotifyURL(getVenueID(),
		// 		inetAddress, macAddress);

		String contactURLSpec = Portal.getAnonymousValidateURL();

		URL url = new URL(contactURLSpec);


		URLConnection conn = url.openConnection();

		// System.out.println("MainController.contactNotifyPortal: conn is " +
		// 		conn.getClass().getName());

		HttpsURLConnection sconn = (HttpsURLConnection) conn;

		// System.out.println("MainController.contactNotifyPortal: sconn is " +
		// 		sconn.getClass().getName());

		sconn.setHostnameVerifier(new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				System.out.println(
						"MainController.HostnameVerifier.verify: " +
						hostname + " against peer=" + session.getPeerHost());

				// Verifier is only called on a mismatch.
				if (isTestHarness) { 
					return true;
				}

				return false;
			}
		});

		sconn.setRequestMethod("POST");
		sconn.setDoOutput(true);
		sconn.setDoInput(true);

		try {
			conn.connect();
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
			throw ioe;
		}

		sendAnonymousSignonData(sconn, contactID, inetAddress, macAddress);

		InputStream is = conn.getInputStream();

		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String cid = br.readLine();
		expiration = Long.parseLong(cid);
		br.close();

		// What resources are left lingering?

		if (conn instanceof HttpURLConnection) {
			// System.out.println("MainController.contactNotifyPortal: " +
			// 		"disconnect HttpURLConnection");

			((HttpURLConnection) conn).disconnect();
		}

		return expiration;
	}

	private void sendAnonymousSignonData(URLConnection conn, long contactid,
			String inetAddress, String macAddress)
			throws IOException {

		BufferedWriter bw = new BufferedWriter(
				new OutputStreamWriter(conn.getOutputStream(), "US-ASCII"));

		StringBuffer sb = new StringBuffer();

		Date expirationDate = null;
		long expiration = 0L;
		try {
			expirationDate = RouteLeaseListener.getLeaseExpiration();
			expiration = expirationDate.getTime();
		}
		catch (Exception ee) {
			System.out.println("can't compute expiration." + ee.toString());
		}

		String gatewayID = GatewayConfiguration.getInstance().
				getProperty("iobeam.gateway.id");

		sb.append("venueID=").append(getVenueID());
		sb.append("&");
		sb.append("userInetAddress=").append(
				URLEncoder.encode(inetAddress, "UTF-8"));
		sb.append("&");
		sb.append("userMACAddress=").append(
				URLEncoder.encode(macAddress, "UTF-8"));
		sb.append("&");
		sb.append("contactID=").append(contactid);
		sb.append("&");
		sb.append("expiration=").append(expiration);
		sb.append("&");
		sb.append("gatewayID=").append(gatewayID);

		bw.write(sb.toString());
		bw.flush();
		bw.close();
	}

	private String getAnonymousRedirect(String originalRequest) {
		String gwRedirect =
			System.getProperty("iobeam.gateway.anonymous.redirect.url");
		if (gwRedirect == null || gwRedirect.trim().equals("")) {
			return originalRequest;
		}
		else {
			return gwRedirect;
		}
	}

}
