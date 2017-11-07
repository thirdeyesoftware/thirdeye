package com.iobeam.portal.ui.web.user.signon;

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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.*;

/**
 * signon user at venue 
 */

public final class SignonUserAction extends Action {
	private static final Logger cLogger = 
		Logger.getLogger("com.iobeam.portal.ui.web.user.SignonUserAction");
		
	public ActionForward execute(ActionMapping mapping, ActionForm _form,
				HttpServletRequest request, HttpServletResponse response) 
						throws Exception {
	
		cLogger.info("SignonUserAction.perform()");

		HttpSession session = request.getSession();
		SignonUserForm form = (SignonUserForm)_form;

		UserSession usersession = 
			UserSessionHelper.getUserSession(session);

		ActionErrors errors = new ActionErrors();

		try {
			
			if (!usersession.isSignedOn()) {
				doVenueSignon(mapping, request, response, form, usersession);
				UserSessionHelper.setUserSession(request.getSession(), usersession);	
				return mapping.findForward("venuewelcome");
			}
			else {
				errors.add("application", new ActionError("error.usersession.exists"));
				saveErrors(request, errors);
				return mapping.findForward("error");
			}

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
		catch (VenueMismatchSubscriptionException vmse) {
			errors.add("secureid", new ActionError("error.secureid.venuemismatch"));
		}
		catch (NoSuchSubscriptionException nsse) {
			errors.add("secureid", new ActionError("error.secureid.unknown"));
		}	
		catch (Exception ee) {
			ee.printStackTrace();
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
	 * signon user at specified venue using existing UserSession.
	 */
	private void doVenueSignon(ActionMapping mapping, HttpServletRequest request, 
			HttpServletResponse response, SignonUserForm form, 
				UserSession usersession) throws Exception {
		
		SignonUser su = getSignonUserBean();

		if (form.getSecureId() != null  && !form.getSecureId().trim().equals("")) {

			if (!su.requiresRegistration(usersession, form.getSecureId())) {
				su.signOnAnonymousUser(
					usersession, form.getSecureId());
			}
			else {
				redirectForRegistration(mapping, request, response, form.getSecureId());
			}
		} else {
			su.signOnUser(usersession, form.getUsername(),
				form.getPassword().toCharArray());

			UserSessionHelper.setUserSession(request.getSession(), usersession);
		}

	}

	public void doGet(HttpServletRequest req, HttpServletResponse res) {
		throw new UnsupportedOperationException("GET not supported.");
	}

	/**
	 * populates UserRegistrationForm with secure id and transfers control to 
	 * signup url.
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

	/**
	 * retrieves SignonUser remote interface.
	 */
	private SignonUser getSignonUserBean() throws Exception {
		InitialContext ic = new InitialContext();
		SignonUserHome home = (SignonUserHome)ic.lookup(SignonUserHome.JNDI_NAME);
		return home.create();
	}

}
