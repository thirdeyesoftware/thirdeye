package com.iobeam.portal.ui.web.account;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.naming.*;
import java.util.logging.Logger;
import java.math.BigInteger;
import javax.ejb.*;
import javax.naming.*;
import java.text.SimpleDateFormat;

import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import com.iobeam.portal.task.actor.user.signon.*;
import com.iobeam.portal.task.actor.user.usersession.*;

import com.iobeam.portal.security.*;
import com.iobeam.portal.model.subscription.*;
import com.iobeam.portal.model.prototype.subscription.*;
import com.iobeam.portal.model.customercontact.*;
import com.iobeam.portal.model.billing.*;
import com.iobeam.portal.model.account.*;
import com.iobeam.portal.ui.web.user.*;

import com.iobeam.portal.util.*;


import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.util.*;

public class GetAccountsAction extends Action {
		
		Logger cLogger = Logger.getLogger("com.iobeam.portal.ui.web.account");

	public ActionForward perform(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		ActionErrors errors = new ActionErrors();
		cLogger.info("GetAccountsAction.perform()");
	
		UserSession usersession = UserSessionHelper.getUserSession(
				request.getSession());

		if (usersession == null) {
			return mapping.findForward("portalsignon");
		}

		Vector accounts = new Vector();
		try {
			accounts = new Vector(usersession.getCustomer().getAccounts());
		}
		catch (Exception ee) { 
			errors.add("app.exception", new ActionError("app.exception"));
			cLogger.info("throwing " + ee.toString());
			return mapping.findForward("error");
		}

		request.setAttribute("accounts", accounts);
		AccountData firstaccount = (AccountData)accounts.firstElement();

		try {
			response.sendRedirect(
				response.encodeRedirectURL(
					mapping.findForward("success").getPath() + 
					"?aid=" + firstaccount.getPK().getID()));
		}
		catch (Exception ee) {
			errors.add("application", new ActionError("app.exception"));
			saveErrors(request, errors);
			return mapping.findForward("error");
		}

		return null;

	}

}

