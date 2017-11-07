package com.iobeam.portal.ui.web.user.signoff;

import javax.servlet.*;
import javax.servlet.http.*;
import java.util.logging.Logger;
import java.net.*;
import javax.ejb.*;
import java.rmi.*;
import javax.naming.*;
import com.iobeam.portal.service.contact.ContactService;
import com.iobeam.portal.task.actor.user.signon.*;
import com.iobeam.portal.task.actor.user.usersession.*;
import com.iobeam.portal.ui.web.user.*;

import org.apache.struts.action.*;

public class SignoffUserAction extends Action {
	

	public ActionForward execute(ActionMapping mapping, 
			ActionForm form, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {

		Logger l = Logger.getLogger("com.iobeam.portal.ui.web.user.signoff");
		ActionErrors errors = new ActionErrors();

		try {
		
			UserSession usersession = 
				UserSessionHelper.getUserSession(request.getSession(false));

			if (usersession == null) {
				//nothing to do...
				return mapping.findForward("success");
			}

			l.info("usersession is null ? " + (usersession == null));
			if (usersession.getVenue() != null) {
				signoffUser(usersession);
			}

			request.setAttribute("servicetype",
				usersession.isPortalSession() ? "subscriber portal" : "wireless service");
				
			l.info("invalidate session");
			request.getSession(false).invalidate();
			return mapping.findForward("success");


		}
		catch (Exception ee) {
			l.throwing(SignoffUserAction.class.getName(),"doPost()",(Throwable)ee);
			errors.add("application", new ActionError("app.exception"));
			saveErrors(request, errors);
			return mapping.findForward("error");

		}


	}

	/**
	 * delegates signoff to SignonUserBean
	 */
	private void signoffUser(UserSession usersession) throws Exception {
		InitialContext ic = new InitialContext();
		SignonUserHome home =
			(SignonUserHome)ic.lookup(SignonUserHome.JNDI_NAME);
		SignonUser su = home.create();
		su.signOffUser(usersession);

	}

}

