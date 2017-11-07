package com.iobeam.gateway.router;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URLDecoder;
import javax.servlet.*;
import javax.servlet.http.*;
import com.iobeam.util.MACAddress;
import com.iobeam.util.Signer;
import com.iobeam.gateway.util.GatewayConfiguration;
import com.iobeam.gateway.util.Portal;
import com.iobeam.gateway.boot.HistoryLogger;



/**
* Listens for RouteLease notifications from the Portal.
*/
public class RouteLeaseListener extends HttpServlet {

	public static final String ROUTELEASE_DURATION_PROP =
			"iobeam.gateway.routelease.duration";

	private static DateFormat cDateFormat = new SimpleDateFormat(
			"MM/dd/yy HH:mm:ss");

	private static DateFormat cShortDateFormat = new SimpleDateFormat(
			"MM/dd HH:mm:ss");

	private static DateFormat cTimeFormat = new SimpleDateFormat(
			"HH:mm:ss");

	public void init(ServletConfig config) throws ServletException {
		System.out.println("RouteLeaseListener.init: " + getDebugTime());
		super.init(config);
	}


	public void doGet(HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {

		String tok;
		StringBuffer signedText = new StringBuffer();

		tok = URLDecoder.decode(request.getParameter("enabled"), "UTF-8");
		boolean enabled = Boolean.valueOf(tok).booleanValue();
		signedText.append(tok);


		tok = URLDecoder.decode(request.getParameter("userInetAddress"),
				"UTF-8");
		InetAddress userInetAddress = InetAddress.getByName(tok);
		signedText.append(tok);


		tok = URLDecoder.decode(request.getParameter("userMACAddress"),
				"UTF-8");
		MACAddress userMACAddress = new MACAddress(tok);
		signedText.append(tok);

		tok = URLDecoder.decode(request.getParameter("cid"),
				"UTF-8");
		long contactID = Long.parseLong(tok);
		signedText.append(tok);


		tok = URLDecoder.decode(request.getParameter("expiration"),
				"UTF-8");
		Date expiration = new Date(Long.parseLong(tok));
		signedText.append(tok);

		Date accessEndTime = null;

		if (request.getParameter("accessEndTime") != null) {
			tok = URLDecoder.decode(request.getParameter("accessEndTime"),
					"UTF-8");
			if (tok != null) {
				accessEndTime = new Date(Long.parseLong(tok));
				signedText.append(tok);
			}
		}

		String signatureText = request.getParameter("signature");


		/*System.out.println("RouteLeaseListener.doGet: enabled = " +
				enabled);

		System.out.println("RouteLeaseListener.doGet: user ip = " +
				userInetAddress);

		System.out.println("RouteLeaseListener.doGet: user mac = " +
				userMACAddress);

		System.out.println("RouteLeaseListener.doGet: expiration = " +
				cDateFormat.format(expiration));

		if (accessEndTime != null) {
			System.out.println("RouteLeaseListener.doGet: accessEndTime = " +
					cDateFormat.format(accessEndTime));
		}
		System.out.println("RouteLeaseListener.doGet: signature = " +
				signatureText);
		*/	

		StringBuffer parmDump = new StringBuffer("RouteLeaseListener.doGet: ");
		parmDump.append(getDebugTime()).append(" ");
		for (Enumeration en = request.getParameterNames();
				en.hasMoreElements(); ) {
			String n = (String) en.nextElement();

			parmDump.append(n).append("=").append(request.getParameter(n));
			if (en.hasMoreElements()) {
				parmDump.append(", ");
			}
		}
		//System.out.println(parmDump.toString());

		boolean isValidMessage = false;

		System.out.println("signedText=" + signedText.toString());
		System.out.println("signaturetext=" + signatureText);
		try {
			// Verify the lease message from the portal.

			if (Signer.isValidSignature(signedText.toString(), signatureText,
					Portal.getCertificate())) {

				System.out.println("RouteLeaseListener.doGet: valid signature");

				if (expiration.getTime() < System.currentTimeMillis()) {
					System.out.println(
							"RouteLeaseListener.doGet: message is expired");

					response.getOutputStream().print("dnack-");
				} else {
					System.out.println(
							"RouteLeaseListener.doGet: message is current");
					response.getOutputStream().print("ack-");

					isValidMessage = true;
				}

			} else {
				System.out.println(
						"RouteLeaseListener.doGet: invalid signature");
				response.getOutputStream().print("snack-");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(e);
		}

		response.getOutputStream().println(userInetAddress.getHostAddress());


		if (isValidMessage) {

			// set the RouteLease on the Router
			try {
				Date leaseExpiration = getLeaseExpiration();
				setRouteLease(enabled, leaseExpiration, contactID, userInetAddress,
					userMACAddress);
			}
			catch (Exception re) {
				re.printStackTrace();
				throw new ServletException(re);
			}
		}
	}

	public static void setRouteLease(boolean enabled,
				Date expiration, 
				long contactId,
				InetAddress inetAddress, 
				MACAddress macAddress) throws RouterException {
			
		RouteLease routeLease = new RouteLease(
					(enabled) ? ClientState.MEMBER_UNRESTRICTED :
					ClientState.MEMBER_RESTRICTED, expiration,
					inetAddress, macAddress, contactId);

		RouterFactory.getRouter().setRouteLease(routeLease);
		HistoryLogger.writeLog(routeLease);

	}


	public void doPost(HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}


	private static String getDebugTime() {
		return cShortDateFormat.format(new Date());
	}

	public static Date getAccessEndTime() throws Exception {
		String endTime = GatewayConfiguration.getInstance().getProperty(
				"iobeam.gateway.schedule.stop");
		Date time = cTimeFormat.parse(endTime);
		GregorianCalendar timeCalendar = new GregorianCalendar();
		timeCalendar.setTime(time);
		GregorianCalendar dateCalendar = new GregorianCalendar();
		timeCalendar.set(dateCalendar.get(Calendar.YEAR),
				dateCalendar.get(Calendar.MONTH),
				dateCalendar.get(Calendar.DAY_OF_MONTH));
		System.out.println("accessendtime=" + timeCalendar.getTime().toString());
		return timeCalendar.getTime();
	}

	public static Date getLeaseExpiration() throws Exception {
			Date accessEndTime = getAccessEndTime();

			int duration = Integer.getInteger(
					ROUTELEASE_DURATION_PROP, 6).intValue();

			// convert hours to millis.
			duration *= 60 * 60 * 1000;
			Date leaseExpiration =
					new Date(System.currentTimeMillis() + duration);

			if (accessEndTime != null &&
					leaseExpiration.after(accessEndTime)) {
				leaseExpiration = accessEndTime;
			}
			
			return leaseExpiration;
	}

}
