package com.iobeam.portal.task.actor.user.signon;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.naming.*;
import java.rmi.*;
import java.util.logging.Logger;
import java.net.InetAddress;
import java.io.IOException;
import javax.ejb.EJBException;

import com.iobeam.portal.model.gateway.*;
import com.iobeam.portal.model.gateway.usercontact.*;
import com.iobeam.portal.model.venue.*;
import com.iobeam.portal.task.actor.user.usersession.*;
import com.iobeam.portal.ui.web.user.UserSessionHelper;


/**
 * this servlet listens for first contact from user while at venue.
 */
public class SignonUserListener extends HttpServlet {

	public static final String SIGNON_URL_PROP = "iobeam.portal.signon.url";

	public static final String ANONYMOUS_URL_PROP =
			"iobeam.portal.signon.anonymous.url";

	public static final String SIGNON_SUCCESS_URL_PROP = 
			"iobeam.portal.signon.success.url";

	public static final String ERROR_URL_PROP =
			"iobeam.portal.signon.error.url";
	
	public static final String PORTAL_PORT_PROP =
			"iobeam.portal.port";

	public static final String PORTAL_HOST_PROP =
			"iobeam.portal.host";

	public static final String PORTAL_PORT_SECURE_PROP =
			"iobeam.portal.secure.port";

	public static final Logger cLogger = 
			Logger.getLogger("com.iobeam.portal.task.actor.user.signon");

	/**
	* doPost executes a proper user signon.  user is anonymous until this
	* method successfully completes.
	*/
	public void doPost(HttpServletRequest request, 
			HttpServletResponse response) {

		cLogger.info("SignonUserListener.doPost: -- ILLEGAL");

		doGet(request, response);
		return;
		
	}


	/**
	* doGet is first signon contact.  This method creates usersession
	* for user w/user contact info.  This method will redirect
	* to a jsp for signon if venue does not support anonymous
	* signon.  If venue supports anonymous signon, this method will
	* signon user anonymously.
	*/
	/*
	public void doGet(HttpServletRequest request, 
			HttpServletResponse response) {
		
		if (UserSessionHelper.getUserSession(request.getSession()) != null) {

			// if user ends up here, either
			// 1) gw and usersession are out of sync or 
			// 2) user stumbled on this URL - which is bad... bad... bad....

			cLogger.warning(
					"SignonUserListener.doGet: User already signed on.");

			try {
				UserSessionHelper.removeUserSession(request.getSession());
				request.getSession().invalidate();

				doGatewayRedirect(request, response);

				return;
			}
			catch (Exception ee) {
				cLogger.info("attemped to remove user session.");
				doErrorRedirect(response, "error during redirect.");
			}
		}

		UserContact userContact = null;
		String cid = request.getParameter("cid");
		ContactID contactID = null;
		request.getSession().setAttribute("cid", cid);
		cLogger.info("SignonUserListener.doGet():cid='" + cid + "'");

		if (cid == null || cid.trim().equals("")) {
			doErrorRedirect(response, "contact is null");
			return;
		}

		try {
			contactID = new ContactID(Long.parseLong(cid));
		}
		catch (NumberFormatException nfe) {
			doErrorRedirect(response, "error parsing contact id");
			return;
		}

		cLogger.info("SignonUserListener.doGet()");
		
		String redirect = request.getParameter("redirect");
		
		try {
			InitialContext ic = new InitialContext();

			userContact = getUserContact(contactID);

			UserSessionHome usHome = (UserSessionHome)
					ic.lookup(UserSessionHome.JNDI_NAME);
			UserSession us = usHome.create(contactID,
					userContact.getVenuePK());
			
			UserSessionHelper.setUserSession(request.getSession(true), us);

			if (redirect != null && !redirect.trim().equals("")) {
				request.getSession().setAttribute("redirecturl", redirect);
			}

			if (getSignonUser().hasAnonymousAccessSubscription(us.getVenue())) {

				signonAnonymousUser(us);

				response.sendRedirect(
						response.encodeRedirectURL(
						System.getProperty(ANONYMOUS_URL_PROP)));

			} else {
				response.sendRedirect(
						response.encodeRedirectURL(
						System.getProperty(SIGNON_URL_PROP)));
			}

		}
		catch (Exception ejbe) {
			doErrorRedirect(response,
					"error encountered while attempting to signon " + "user.");
			return;
		}

	}
	*/


	/**
	* doGet is first signon contact.  This method creates usersession
	* for user w/user contact info.  This method will redirect
	* to a jsp for signon if venue does not support anonymous
	* signon.  If venue supports anonymous signon, this method will
	* signon user anonymously.
	*/
	public void doGet(HttpServletRequest request, 
			HttpServletResponse response) {

		cLogger.info(" ");

		
		UserContact userContact = null;
		UserSession userSession = UserSessionHelper.getUserSession(
				request.getSession());

		String cidParm = request.getParameter("cid");
		long cid = 0;

		if (cidParm != null) {
			request.getSession().setAttribute("cid", cidParm);
			try {
				cid = Long.parseLong(cidParm);
			}
			catch (NumberFormatException nfe) {
				doErrorRedirect(request, response, "error parsing contact id");
				return;
			}
		}
		cLogger.info("cid='" + cidParm + "'");

		try {
			if (cidParm == null || cidParm.trim().equals("") ||
					userSession != null &&
					(userSession.isPortalSession() ||
					userSession.getContactID().getID() != cid)) {
				try {
					if (userSession != null) {
						UserSessionHelper.removeUserSession(
								request.getSession());
						request.getSession().invalidate();
					}

					doGatewayRedirect(request, response);
				}
				catch (Exception ee) {
					cLogger.info("attemped to remove user session.");
					doErrorRedirect(request, response, "error during redirect.");
				}

				return;
			}
		}
		catch (RemoteException re) {
			cLogger.warning(re.toString());
			doErrorRedirect(request, response,
					"error encountered while qualifying session.");
			return;
		}


		ContactID contactID = null;
		contactID = new ContactID(cid);

		String redirect = request.getParameter("redirect");
	
		Gateway gateway = null;

		try {
			userContact = getUserContact(contactID);

			// update gw ip if necessary	
			InetAddress address = InetAddress.getByName(request.getRemoteAddr());
			AccessGateway ag = getAccessGateway();
			gateway  = ag.findByVenuePK(userContact.getVenuePK());

			if (!address.equals(gateway.getPublicIPAddress())) {
				gateway.setPublicIPAddress(address);
				ag.update(gateway);
			}

			if (userSession == null) {
				userSession = createUserSession(contactID,
						userContact.getVenuePK());
			
				UserSessionHelper.setUserSession(request.getSession(),
						userSession);
			}

			if (redirect != null && !redirect.trim().equals("")) {
				request.getSession().setAttribute("redirecturl", redirect);
			}

			Venue venue = userSession.getVenue();

			if (getSignonUser().hasAnonymousAccessSubscription(
					venue)) {

				signonAnonymousUser(userSession);

				cLogger.info("RedirectURL = " + venue.getRedirectUrl());

				if (venue.getRedirectUrl() != null) {
					response.sendRedirect(venue.getRedirectUrl());
				}
				else {
					response.sendRedirect(
						response.encodeRedirectURL(
						System.getProperty(ANONYMOUS_URL_PROP)));
				}
			} else {
				if (userSession.isSignedOn()) {
					response.sendRedirect(
							response.encodeRedirectURL(
							System.getProperty(SIGNON_SUCCESS_URL_PROP)));
				} else {
					response.sendRedirect(
							response.encodeRedirectURL(
							System.getProperty(SIGNON_URL_PROP)));
				}
			}

		}
		catch (Exception ejbe) {
			ejbe.printStackTrace();
			doErrorRedirect(request, response,
					"error encountered while attempting to signon " + "user.");
			return;
		}

	}


	private UserSession createUserSession(ContactID contactID, VenuePK venuePK)
			throws Exception {

		InitialContext ic = new InitialContext();

		UserSessionHome h = (UserSessionHome)
				ic.lookup(UserSessionHome.JNDI_NAME);

		return h.create(contactID, venuePK);
	}


	private UserContact getUserContact(ContactID contactID)
			throws Exception {

		InitialContext ic = null;

		AccessUserContactHome acHome = null;
		AccessUserContact auc = null;
		UserContact contact = null;

		ic = new InitialContext();

		acHome = (AccessUserContactHome)
				ic.lookup(AccessUserContactHome.JNDI_NAME);

		auc = acHome.create();

		return auc.findByContactID(contactID);
	}


	private void doErrorRedirect(HttpServletRequest request, 
			HttpServletResponse response, String msg) {
		try {
			cLogger.info("SignonUserListener.error encountered = " + msg);
			request.getSession().setAttribute("errormessage", msg);
			response.sendRedirect(
					response.encodeRedirectURL(
					System.getProperty(ERROR_URL_PROP)));
		}
		catch (IOException ioe) {

			cLogger.throwing(SignonUserListener.class.getName(),
					"doErrorRedirect",
					(Throwable)ioe);
		}
	}


	/**
	* convenience method to signon anonymous user.
	*/
	private void signonAnonymousUser(UserSession us) throws Exception {
		getSignonUser().signOnAnonymousUser(us);
	}


	private SignonUser getSignonUser() throws Exception {
		InitialContext ic = new InitialContext();

		SignonUserHome home = (SignonUserHome)
				ic.lookup(SignonUserHome.JNDI_NAME);

		SignonUser signonUser = home.create();

		return signonUser;
	}

	private void doGatewayRedirect(HttpServletRequest request, 
			HttpServletResponse response) throws Exception {

		Gateway gw = getAccessGateway().findByIPAddress(
				InetAddress.getByName(request.getRemoteAddr()));
		
		response.sendRedirect("http://" + gw.getPrivateIPAddress().getHostAddress() + 
				":" + gw.getNotifyPort());

	}

	private AccessGateway getAccessGateway() throws Exception {
		AccessGatewayHome home = (AccessGatewayHome)(new InitialContext()).lookup(
				AccessGatewayHome.JNDI_NAME);
		return home.create();
	}



}

