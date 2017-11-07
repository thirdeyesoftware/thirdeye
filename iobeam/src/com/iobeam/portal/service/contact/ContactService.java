package com.iobeam.portal.service.contact;


import java.util.*;
import java.util.logging.Logger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.net.*;
import java.io.*;
import com.iobeam.util.MACAddress;
import com.iobeam.util.Signer;
import com.iobeam.portal.security.PortalKeyStore;
import com.iobeam.portal.model.gateway.*;
import com.iobeam.portal.model.gateway.usercontact.*;
import java.security.*;



public class ContactService {

	public static String GATEWAY_ROUTELEASE_LISTENER_PATH_PROP =
			"iobeam.gateway.routeLeaseListener.path";


	private static ContactService theContactService = new ContactService();



	private ContactService() {
		Logger.getLogger("com.iobeam.portal.service.contact").info(
				"ContactService.<init>:");
	}


	/**
	* Disables the specified UserContact on the specified Gateway.
	*
	* @exception ContactServiceException the ContactService was not able
	* to enable the specified Contact at the Contact's gateway.
	*/
	public static void disableContact(Gateway gateway,
			UserContact userContact) throws ContactServiceException {

		try {
			theContactService.sendContact(gateway, userContact,
					(Date) null, false);
		}
		catch (Throwable t) {
			t.printStackTrace();
			throw new ContactServiceException(t);
		}
	}

	/**
	* Enables the specified UserContact on the specified Gateway.
	*
	* @param accessEndTime the time at which the enabled access
	* shall end on the Gateway, or null if there is none.
	*
	* @exception ContactServiceException the ContactService was not able
	* to enable the specified Contact at the Contact's gateway.
	*/
	public static void enableContact(Gateway gateway,
			UserContact userContact,
			Date accessEndTime) throws ContactServiceException {

		try {
			theContactService.sendContact(gateway, userContact,
					accessEndTime, true);
		}
		catch (Throwable t) {
			t.printStackTrace();
			throw new ContactServiceException(t);
		}
	}


	private void sendContact(Gateway gateway,
			UserContact userContact, Date accessEndTime,
			boolean enable) throws Exception {

		URL u = getLeaseListenerURL(gateway);

		Logger.getLogger("com.iobeam.portal.service.contact").info(
				"ContactService.sendContact: " + u);

		HttpURLConnection conn = null;

		conn = (HttpURLConnection) u.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);

		conn.connect();

		OutputStreamWriter w = new OutputStreamWriter(
				conn.getOutputStream(), "US-ASCII");
		BufferedWriter bw = new BufferedWriter(w);

		StringBuffer sendText = new StringBuffer();
		StringBuffer signedText = new StringBuffer();

		if (enable) {
			sendText.append("enabled=true&");
			signedText.append("true");
		} else {
			sendText.append("enabled=false&");
			signedText.append("false");
		}

		sendText.append("userInetAddress=").append(
				URLEncoder.encode(
				userContact.getUserIPAddress().getHostAddress(),"UTF-8"));
		sendText.append("&");
		signedText.append(userContact.getUserIPAddress().getHostAddress());

		sendText.append("userMACAddress=").append(
				URLEncoder.encode(
				userContact.getUserMACAddress().toString(), "UTF-8"));
		sendText.append("&");
		signedText.append(userContact.getUserMACAddress().toString());

		sendText.append("cid=").append(userContact.getContactID().getID());
		sendText.append("&");
		signedText.append(userContact.getContactID().getID());


		String expiration = Long.toString(
				System.currentTimeMillis() + 10 * 60 * 1000);

		sendText.append("expiration=").append(
				URLEncoder.encode(expiration, "UTF-8"));
		sendText.append("&");
		signedText.append(expiration);

		if (accessEndTime != null) {
			String endTime = Long.toString(accessEndTime.getTime());
			sendText.append("accessEndTime=").append(
					URLEncoder.encode(endTime, "UTF-8"));
			sendText.append("&");
			signedText.append(endTime);
		}


		PrivateKey key = PortalKeyStore.getPrivateKey();
		sendText.append("signature=").append(
				URLEncoder.encode(
				Signer.getSignature(signedText.toString(), key),
				"UTF-8"));

		Logger.getLogger("com.iobeam.portal.service.contact").info(
			"sendText=" + sendText.toString());

		Logger.getLogger("com.iobeam.portal.service.contact").info(
			"signedText=" + signedText.toString());

		Logger.getLogger("com.iobeam.portal.service.contact").info(
			"key=" + key);

		Logger.getLogger("com.iobeam.portal.service.contact").info(
			"Certificate =" + PortalKeyStore.getCertificate());

		bw.write(sendText.toString());
		bw.flush();

		BufferedReader br = new BufferedReader(
				new InputStreamReader(conn.getInputStream()));
		Logger.getLogger("com.iobeam.portal.service.contact").info(
				"ContactService.sendContact: ack = " + br.readLine());

		br.close();
		bw.close();

		conn.disconnect();

		Logger.getLogger("com.iobeam.portal.service.contact").info(
				"ContactService.sendContact: done " + u);
	}

	private static URL getLeaseListenerURL(Gateway gateway)
			throws MalformedURLException {
		String path = System.getProperty(
				GATEWAY_ROUTELEASE_LISTENER_PATH_PROP);

		if (path == null) {
			throw new Error(GATEWAY_ROUTELEASE_LISTENER_PATH_PROP +
					" not defined");
		}

		StringBuffer sb = new StringBuffer("http://");
		sb.append(gateway.getPublicIPAddress().getHostAddress());
		sb.append(":").append(gateway.getNotifyPort());
		sb.append(path);

		return new URL(sb.toString());
	}

	public static String getGatewayURL(Gateway g) 
			throws MalformedURLException {
		return getLeaseListenerURL(g).toString();
	}

}
