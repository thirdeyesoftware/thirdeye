package com.iobeam.portal.ui.web.billing.invoice;

import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import javax.naming.*;
import javax.ejb.*;

import org.apache.struts.action.*;
import com.iobeam.portal.model.account.*;
import com.iobeam.portal.model.invoice.*;
import com.iobeam.portal.ui.web.user.UserSessionHelper;
import com.iobeam.portal.task.actor.user.usersession.UserSession;

/**
 * ui controller that retrieves an invoice for a user.
 * @iid invoice id 
 * @aid account id
 */
public class GetInvoiceDetailAction extends Action {

	public ActionForward execute(
			ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {

		ActionErrors errors = new ActionErrors();
		if (request.getParameter("iid") == null) {
			errors.add("application", new ActionError("app.exception"));
			saveErrors(request, errors);
			return mapping.findForward("error");
		}


		UserSession usersession = 
				UserSessionHelper.getUserSession(request.getSession());

		if (usersession == null) {
			return mapping.findForward("portalsignon");
		}

		Invoice invoice;
		Vector invoiceItems = new Vector();
		double balanceForward = 0.0d;

		try {
			long iid = Long.parseLong(request.getParameter("iid"));

			invoice = getAccessInvoice().findByPrimaryKey(
				new InvoicePK(iid));

			if (!isInvoiceForUser(usersession, invoice)) {
				errors.add("application", new ActionError("app.exception"));
				saveErrors(request, errors);
				return mapping.findForward("error");
			}

			for (Iterator it = invoice.getInvoiceLineItems().iterator();
					it.hasNext(); ) { 
				InvoiceLineItem item = (InvoiceLineItem)it.next();
				if (item.getAccountEntry().getAccountEntryType().equals(
						AccountEntryType.BALANCE_FORWARD)) {
					balanceForward = item.getAccountEntry().getAmount().getAmount();
				}
				else {
					invoiceItems.addElement(item);
				}
			}

			request.setAttribute("balanceforward", Double.toString(balanceForward));
			request.setAttribute("invoiceitems", invoiceItems);

			AccountData data = getAccountData(usersession, invoice.getAccountPK());
			request.setAttribute("accountdata", data);
				
		}
		catch (Exception ee) {
			errors.add("application", new ActionError("app.exception"));
			saveErrors(request, errors);
			return mapping.findForward("error");
		}

		request.setAttribute("invoice", invoice);
		
		return mapping.findForward("success");

	}

	/**
	 * returns true if this invoice is for the 
	 * user associated with the specified usersession.
	 */
	private boolean isInvoiceForUser(UserSession us, Invoice invoice) 
			throws Exception {

		AccountPK pk = invoice.getAccountPK();
		Collection accounts = us.getCustomer().getAccounts();
		for (Iterator it = accounts.iterator(); it.hasNext(); ) {
			AccountData data = (AccountData)it.next();
			if (data.getPK().equals(pk)) return true;
		}

		return false;

	}
	
	/** 
	 * returns the account data instance, specified by accountpk,
	 * from a collection of accounts associated with the specified 
	 * user in the usersession.
	 * Otherwise, returns null.
	 */
	private AccountData getAccountData(UserSession usersession, AccountPK pk) 
			throws Exception {
		
		Collection accounts = usersession.getCustomer().getAccounts();
		for (Iterator it = accounts.iterator(); it.hasNext(); ) {
			AccountData data = (AccountData)it.next();
			if (data.getPK().equals(pk)) return data;
		}

		return null;

	}

	private AccessInvoice getAccessInvoice() 
			throws Exception {

		InitialContext ic = new InitialContext();
		AccessInvoiceHome home = 
			(AccessInvoiceHome)ic.lookup(AccessInvoiceHome.JNDI_NAME);

		return home.create();
	}

		

}

