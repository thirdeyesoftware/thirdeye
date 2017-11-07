package com.iobeam.portal.ui.web.billing.invoice;

import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.util.logging.Logger;
import javax.naming.*;
import javax.ejb.*;

import org.apache.struts.action.*;
import com.iobeam.portal.model.account.*;
import com.iobeam.portal.model.invoice.*;
import com.iobeam.portal.ui.web.user.UserSessionHelper;
import com.iobeam.portal.task.actor.user.usersession.UserSession;

/**
 * ui controller that retrieves a collection of invoices for 
 * the user specified in this usersession and specified account id.
 * @aid account id
 */
public class GetInvoicesAction extends Action {

	public ActionForward execute(
			ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response) throws Exception  {
		
		Logger logger = Logger.getLogger("com.iobeam.portal.ui.web.billing");

		logger.info("GetInvoicesAction.execute():<start>");

		ActionErrors errors = new ActionErrors();

		UserSession usersession = 
				UserSessionHelper.getUserSession(request.getSession());

		if (usersession == null) {
			return mapping.findForward("portalsignon");
		}
		
		if (request.getParameter("aid") == null) {
			errors.add("application", new ActionError("app.exception"));
			saveErrors(request, errors);
			return mapping.findForward("error");
		}

		Collection invoices;

		try {
			AccountPK accountPK = new AccountPK(
				Long.parseLong(request.getParameter("aid")));
			
			if (!isAccountForUser(usersession, accountPK)) {
				errors.add("application", new ActionError("app.exception"));
				saveErrors(request, errors);
				return mapping.findForward("error");
			}

			invoices = getAccessInvoice().findByAccountPK(accountPK);
			request.setAttribute("invoices", invoices);
			
			request.setAttribute("accountdata", 
					getAccount(usersession, accountPK));

		}	
		catch (Exception ee) {
			errors.add("application", new ActionError("app.exception"));
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
	
		logger.info("GetInvoicesAction.execute():<finished>");

		return mapping.findForward("success");

	}

	/**
	 * returns true if this account pk is for the 
	 * user associated with the specified usersession.
	 */
	private boolean isAccountForUser(UserSession us, AccountPK accountPK) 
			throws Exception {

		Collection accounts = us.getCustomer().getAccounts();
		for (Iterator it = accounts.iterator(); it.hasNext(); ) {
			AccountData data = (AccountData)it.next();
			if (data.getPK().equals(accountPK)) return true;
		}

		return false;
	}

	private AccessInvoice getAccessInvoice() 
			throws Exception {

		InitialContext ic = new InitialContext();
		AccessInvoiceHome home = 
			(AccessInvoiceHome)ic.lookup(AccessInvoiceHome.JNDI_NAME);

		return home.create();
	}


	private AccountData getAccount(UserSession usersession, AccountPK pk) 
			throws Exception {
		Collection accounts = usersession.getCustomer().getAccounts();
		for (Iterator it = accounts.iterator(); it.hasNext(); ) {
			AccountData data = (AccountData)it.next();
			if (data.getPK().equals(pk)) return data;
		}
		return null;
	}

}

