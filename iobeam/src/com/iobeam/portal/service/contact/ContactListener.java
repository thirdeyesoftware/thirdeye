package com.iobeam.portal.service.contact;


import java.util.Enumeration;
import java.util.logging.Logger;
import java.io.IOException;
import java.net.InetAddress;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.naming.*;
import com.iobeam.util.MACAddress;
import com.iobeam.portal.model.venue.VenuePK;
import com.iobeam.portal.model.gateway.usercontact.ContactID;
import com.iobeam.portal.task.gateway.manageusercontact.*;



/**
* Listens for Gateway to report a Contact.  Assigns a contactID
* to the new Contact.
*/
public class ContactListener extends HttpServlet {


	public void init(ServletConfig config) throws ServletException {
		System.out.println("ContactListener.init:");
		super.init(config);
	}


	public void doGet(HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {

		Logger logger = Logger.getLogger("com.iobeam.portal.service.contact");

		InetAddress venueInetAddress = InetAddress.getByName(
				request.getRemoteAddr());

		String venueID = request.getParameter("venueID");

		VenuePK venuePK = new VenuePK(Long.parseLong(venueID));

		InetAddress userInetAddress = InetAddress.getByName(
				request.getParameter("userInetAddress"));

		MACAddress userMACAddress = new MACAddress(
				request.getParameter("userMACAddress"));

		long siteKey = Long.parseLong(request.getParameter("siteKey"));

		logger.info(venuePK.toString());

		logger.info("venue ip = " + venueInetAddress);

		logger.info("user ip = " + userInetAddress);

		logger.info("user mac = " + userMACAddress);

		logger.info("site key = " + siteKey);

		ContactID contactID = null;
		
		try {
			contactID = getManageUserContact().logInitialContact(
					venuePK, venueInetAddress, userInetAddress,
					userMACAddress, siteKey);
		}
		catch (Exception e) {
			throw new ServletException(e);
		}
		logger.info("ContactListener.doGet: contact id = " + contactID);

		response.getOutputStream().println(contactID.getID());
	}


	public void doPost(HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}


	private ManageUserContact getManageUserContact()
			throws ServletException {

		try {
			InitialContext ic = new InitialContext();

			ManageUserContactHome h = (ManageUserContactHome)
					ic.lookup(ManageUserContactHome.JNDI_NAME);

			return h.create();
		}
		catch (Exception e) {
			throw new ServletException(e);
		}
	}
}
