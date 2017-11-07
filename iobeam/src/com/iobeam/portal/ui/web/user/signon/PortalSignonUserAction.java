package com.iobeam.portal.ui.web.user.signon;

import java.net.InetAddress;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.naming.*;
import java.util.logging.Logger;
import java.math.BigInteger;
import javax.ejb.*;

import com.iobeam.portal.ui.web.user.*;
import com.iobeam.portal.ui.web.user.signup.SignupUserForm;

import com.iobeam.portal.task.actor.user.signon.*;
import com.iobeam.portal.task.actor.user.usersession.*;
import com.iobeam.portal.security.*;
import com.iobeam.portal.model.subscription.*;
import com.iobeam.portal.model.gateway.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.*;

/**
 * signon user at portal
 */

public final class PortalSignonUserAction extends Action {
	private static final Logger cLogger = 
		Logger.getLogger("com.iobeam.portal.ui.web.user.SignonUserAction");
		
	public ActionForward perform(ActionMapping mapping, ActionForm _form,
				HttpServletRequest request, HttpServletResponse response) {
	
		cLogger.info("PortalSignonUserAction.perform()");

		HttpSession session = request.getSession();
		SignonUserForm form = (SignonUserForm)_form;

		UserSession usersession = null;

		ActionErrors errors = new ActionErrors();
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if (username == null || username.trim().equals("")) {
			errors.add("username", new ActionError("error.username.required"));
		}
		else if (password == null || password.trim().equals("")) {
			errors.add("password", new ActionError("error.password.required"));
		}
		if (errors.size() > 0) { 
			saveErrors(request, errors);
			return mapping.findForward("error");
		}

		try {
			
			cLogger.info("doPortalSignon()");
			//this is portal signon. 	
			usersession = doPortalSignon(request, username,
				password.toCharArray());
			UserSessionHelper.setUserSession(request.getSession(), usersession);	
			return mapping.findForward("success");

		}
		catch (IllegalStateException ise) {
			cLogger.info("illegal state");
			cLogger.throwing(SignonUserAction.class.getName(),"perform", 
				(Throwable)ise);
			errors.add("app.exception", new ActionError("app.exception"));
		}
		catch (InvalidPasswordException ipe) {
			cLogger.info("bad password");
			errors.add("password", new ActionError("error.password.invalid"));
		}
		catch (NoSuchUserException nsue) {
			cLogger.info("unknown user");
			errors.add("username", new ActionError("error.username.unknown"));
		}
		catch (SecurityException se) {
			errors.add("application", new ActionError("security.exception"));
			return mapping.findForward("error");
		}
		catch (RegisteredSubscriptionException rse) {
			errors.add("secureid", new ActionError("error.secureid.inuse"));
		}
		catch (Exception ee) {
			Logger.getLogger("com.iobeam.portal.ui.web.user").throwing(
				SignonUserAction.class.getName(), "execute", (Throwable)ee);
			
			errors.add("application", new ActionError("app.exception"));
			return mapping.findForward("error");
		}
		
		// remove ui bean from request or session
		if (mapping.getAttribute() != null) {
			if ("request".equals(mapping.getScope())) {
				request.removeAttribute(mapping.getAttribute());
			}
			else {
				session.removeAttribute(mapping.getAttribute());
			}
		}

		if (errors.size() > 0) {
			cLogger.info("error size=" + errors.size());
			saveErrors(request, errors);
			return mapping.findForward("error");
		} else {
			return null;
		}


	}

	/**
	 * attempts to log into portal using credentials in LogonForm.
	 */
	private UserSession doPortalSignon(HttpServletRequest request, 
			String username, char[] password) throws Exception {

		InitialContext ic = new InitialContext();

		AccessGatewayHome agHome = (AccessGatewayHome)ic.lookup(
				AccessGatewayHome.JNDI_NAME);
		AccessGateway ag = agHome.create();

		InetAddress ia = InetAddress.getByName(request.getRemoteAddr());
		Gateway gw = null;
		try {
			gw = ag.findByIPAddress(ia);
		}
		catch (FinderException fe) {
		}


		UserSessionHome usHome = (UserSessionHome)ic.lookup(
				UserSessionHome.JNDI_NAME);

		UserSession us = null;
		if (gw != null) {
			us = usHome.create(gw.getVenuePK());
		} else {
			us = usHome.create();
		}

		SignonUser su = getSignonUserBean();
		cLogger.info("signonPortalUser");
		su.signOnPortalUser(us, username, password);
		cLogger.info("finished signonPortalUser");
		return us;
	}



	public void doGet(HttpServletRequest req, HttpServletResponse res) {
		throw new UnsupportedOperationException("GET not supported.");
	}

	/**
	 * populates UserRegistrationForm with secure id and transfers control to 
	 * registration url.
	 */
	private void redirectForRegistration(ActionMapping mapping, 
			HttpServletRequest request, HttpServletResponse response, 
				String secId) throws Exception {

		SignupUserForm form = new SignupUserForm();
		form.setSecureId(secId);
		request.setAttribute("SignupUserForm", form);

		RequestDispatcher dispatcher = 
			request.getRequestDispatcher(mapping.findForward("signup").getPath());
		dispatcher.forward(request, response);
	}
	
	private SignonUser getSignonUserBean() throws Exception {
		InitialContext ic = new InitialContext();
		SignonUserHome home = (SignonUserHome)ic.lookup(SignonUserHome.JNDI_NAME);
		return home.create();
	}

}
