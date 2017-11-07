package com.iobeam.portal.task.actor.user.signon;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.naming.*;
import java.rmi.*;
import java.util.logging.Logger;
import java.net.InetAddress;
import java.io.IOException;
import javax.ejb.EJBException;

import com.iobeam.util.*;
import com.iobeam.portal.util.*;

import com.iobeam.portal.model.gateway.*;
import com.iobeam.portal.model.gateway.usercontact.*;
import com.iobeam.portal.model.venue.*;
import com.iobeam.portal.task.actor.user.usersession.*;
import com.iobeam.portal.ui.web.user.UserSessionHelper;


/**
 * this servlet listens for request for anonymous user signon.
 */
public class ValidateAnonymousUserListener extends HttpServlet {


	public static final Logger cLogger = 
			Logger.getLogger("com.iobeam.portal.task.actor.user.signon");

	/**
	* doPost executes a proper user signon.  user is anonymous until this
	* method successfully completes.
	*/
	public void doPost(HttpServletRequest request, 
			HttpServletResponse response) {

		cLogger.info("ValidateAnonymousUserListener.doPost: -- ILLEGAL");

		doGet(request, response);
		return;
		
	}

	public void doGet(HttpServletRequest request, 
			HttpServletResponse response) {

		cLogger.info(" ");

		long expiration = 0L;

		UserContact userContact = null;

		String cidParm = request.getParameter("contactID");
		String venueIdParm = request.getParameter("venueID");
		String expirationParm = request.getParameter("expiration");

		long venueId = 0;
		long cid = 0;

		if (cidParm != null) {
			try {
				cid = Long.parseLong(cidParm);
			}
			catch (NumberFormatException nfe) {
				expiration = 0L;	
			}
		}
		cLogger.info("cid='" + cidParm + "'");

		String inetAddress = request.getParameter("userInetAddress");
		String mac = request.getParameter("userMACAddress");
		if (venueIdParm != null) {
			venueId = Long.parseLong(venueIdParm);
		}
		else {
			expiration = 0L;
		}

		try {
			ContactID contactID = new ContactID(cid);
			userContact = UserContactDAO.select(contactID);
			VenuePK venuePK = new VenuePK(venueId);
			MACAddress macAddress = new MACAddress(mac);
			InetAddress userInetAddress = InetAddress.getByName(inetAddress);

			if (userContact != null) {

				if (venuePK.equals(userContact.getVenuePK()) &&
						macAddress.equals(userContact.getUserMACAddress()) &&
						userInetAddress.equals(userContact.getUserIPAddress())) {

					expiration = Long.parseLong(expirationParm);

				}
			}
		}
		catch (Exception re) {
			cLogger.warning(re.toString());
			expiration = 0;
		}
		finally {
			try{
				response.getOutputStream().print(""+ expiration);
			}
			catch (Exception ee) { }
		}
		
	}

}

