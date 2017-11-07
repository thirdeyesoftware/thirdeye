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

/**
 * retrieves account, subscription and billable party details for
 * specified account id in request.
 * @parameter aid - account id 
 */
public class GetBillablePartyDetailAction extends Action {
		
		Logger cLogger = Logger.getLogger("com.iobeam.portal.ui.web.account");

	public ActionForward perform(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		ActionErrors errors = new ActionErrors();
		cLogger.info("GetBillablePartyDetailAction.perform():<start>");
	
		UserSession usersession = UserSessionHelper.getUserSession(
				request.getSession());

		if (usersession == null) {
			return mapping.findForward("portalsignon");
		}

		if (request.getParameter("aid") == null ||
				request.getParameter("aid").trim().equals("")) {
		 	
			errors.add("parameter", new ActionError("app.exception"));
			saveErrors(request, errors);
			return mapping.findForward("error");
		
		}
		try {

			Account account = getAccount(request.getParameter("aid"));
			if (!account.getData().getCustomerPK().equals(
					usersession.getCustomer().getData().getPK())) {
				errors.add("application", new ActionError("app.exception"));
				saveErrors(request, errors);
				return mapping.findForward("error");
			}

			BillableParty bp = account.getBillableParty();
		
			request.setAttribute("account", account);
			request.setAttribute("BillablePartyForm", createBillablePartyForm(bp));
			cLogger.info("GetBillablePartyDetailAction.perform():<finished>");


		}
		catch (Exception ee) {
			ee.printStackTrace();
			errors.add("application", new ActionError("app.exception"));
			cLogger.throwing(GetBillablePartyDetailAction.class.getName(), "perform",
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


	private BillablePartyForm createBillablePartyForm(BillableParty party) 
			throws Exception {
		BillablePartyForm form = new BillablePartyForm();
		CustomerContact contact = party.getBillingCustomerContact();
		CreditCard creditCard = (CreditCard)party.getPaymentInstrument();
		SimpleDateFormat month = new SimpleDateFormat("MM");
		SimpleDateFormat year = new SimpleDateFormat("yyyy");

		form.setFirstName(contact.getContactName().getFirstName());
		form.setLastName(contact.getContactName().getLastName());
		form.setAddress1(contact.getAddress().getMailingAddress().getLine1());
		form.setAddress2(contact.getAddress().getMailingAddress().getLine2());
		form.setCity(contact.getAddress().getMailingAddress().getCity());
		form.setState(contact.getAddress().getMailingAddress().getState().toUpperCase());
		form.setZipcode(contact.getAddress().getMailingAddress().getZipcode());
		form.setCountryId(String.valueOf(
			contact.getAddress().getCountry().getPK().getID()));
		form.setPhoneNumber(contact.getPhoneNumber());
		form.setFaxNumber(contact.getFaxNumber());
		form.setEmailAddress(contact.getEmailAddress());
		form.setCardHolderName(creditCard.getCardHolderName());
		form.setCreditCardNumber(creditCard.getCreditCardNumber());
		form.setSecurityCode(creditCard.getSecurityCode());
		form.setExpirationMonth(month.format(creditCard.getExpirationDate()));
		form.setExpirationYear(year.format(creditCard.getExpirationDate()));
	
		return form;
	}

}

