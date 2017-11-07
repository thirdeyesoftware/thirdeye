package com.iobeam.portal.ui.web.subscription;

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

/**
 * retrieves account, subscription and billable party details for
 * specified account id in request.
 * @parameter sid -- subscription id
 * @parameter aid -- account id
 */
public class GetSubscriptionDetailAction extends Action {
		
		Logger cLogger = Logger.getLogger("com.iobeam.portal.ui.web.subscription");

	public ActionForward perform(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		ActionErrors errors = new ActionErrors();
		cLogger.info("GetSubscriptionDetailAction.perform()");
	
		UserSession usersession = UserSessionHelper.getUserSession(
				request.getSession());

		Subscription subscription = null;

		if (usersession == null) {
			return mapping.findForward("portalsignon");
		}

		if (request.getParameter("sid") == null ||
				request.getParameter("sid").trim().equals("")) {
		 	
			errors.add("parameter", new ActionError("app.exception"));
			saveErrors(request, errors);
			return mapping.findForward("error");
		
		}

		try {

			Account account = getAccount(request.getParameter("aid"));

			if (!account.getData().getCustomerPK().equals(
					usersession.getCustomer().getData().getPK())) {
				errors.add("application", new ActionError("user.customer.mismatch"));
				saveErrors(request, errors);
				return mapping.findForward("error");
			}

			Collection subscriptions =
					account.getAllSubscriptions();
			
			System.out.println("subscription count = " + subscriptions.size());

			for (Iterator it = subscriptions.iterator(); it.hasNext(); ) {
				subscription = (Subscription)it.next();
				if (subscription.getPK().getID() ==
					Long.parseLong(request.getParameter("sid"))) break;
			}

			if (subscription == null) {
				System.out.println("subscription is null");
				errors.add("application", new ActionError("app.exception"));
				saveErrors(request, errors);
				return mapping.findForward("error");
			}
		
			request.setAttribute("account", account);
			request.setAttribute("subscription", subscription);

		}
		catch (Exception ee) {
			ee.printStackTrace();
			errors.add("application", new ActionError("app.exception"));
			cLogger.throwing(GetSubscriptionDetailAction.class.getName(), "perform",
					(Throwable)ee);
		 	saveErrors(request, errors);
			return mapping.findForward("error");
		}

		return mapping.findForward("success");

	}

	/**
	 * returns the account instance specified by the account id.
	 */
	private Account getAccount(String aidparam) throws Exception {

		InitialContext ic = new InitialContext();
		AccountHome home = (AccountHome)ic.lookup(AccountHome.JNDI_NAME);
		
		long accountId = Long.parseLong(aidparam);

		return home.findByPrimaryKey(new AccountPK(accountId));

	}
		
}

