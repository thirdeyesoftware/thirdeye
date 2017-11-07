package com.iobeam.portal.ui.web.user;

import javax.ejb.*;
import javax.naming.*;
import javax.servlet.http.*;
import java.util.logging.Logger;

import com.iobeam.portal.task.actor.user.usersession.*;


/**
 * contains convenience methods to assist web front end with 
 * management of ejb user session retrieval and storage.
 */
public class UserSessionHelper {
	private static final String USER_SESSION_TOKEN_PROP = 
			"iobeam.portal.usersession.token";

	/**
	* Retrieves user session from http session.
	*/
	public static UserSession getUserSession(HttpSession session) {
		try {
			Logger l = Logger.getLogger("com.iobeam.portal.ui.web.user");

			if (session.getAttribute(getToken()) == null) {
				l.info("UserSessionHelper.getUserSession: " +
						"session missing token.");
				return null;
			} else {
				Handle h = (Handle)session.getAttribute(getToken());
				return (UserSession)h.getEJBObject();
			}
		}
		catch (Exception ee) { 
			System.out.println(ee.toString());
			return null;
		}
	}


	/**
	* Stores userSession handle in http session.
	*/
	public static void setUserSession(HttpSession httpSession,
			UserSession userSession) throws Exception {
		Logger l = Logger.getLogger("com.iobeam.portal.ui.web.user");

		try {
			l.info("UserSessionHelper.setUserSession():sess id = " +
				httpSession.getId());

			httpSession.setAttribute(getToken(), userSession.getHandle());
		}
		catch (Exception ee) {
			Logger.getLogger("com.iobeam.portal.ui.web.user").throwing(
				UserSessionHelper.class.getName(), "setUserSession", ee);
			throw ee;
		}
	}


	public static void removeUserSession(HttpSession session) 
			throws Exception {

		session.removeAttribute(getToken());
	}

	/**
	* Returns the system property token used to store
	* the UserSession handle in the HttpSession.
	*/
	private static String getToken() {
		//return System.getProperty(USER_SESSION_TOKEN_PROP);
		return "userSessionHandle";
	}


}

