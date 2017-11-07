package com.iobeam.portal.ui.web.account;

import com.iobeam.portal.model.account.*;
import com.iobeam.portal.model.billing.*;
import com.iobeam.portal.model.customer.*;
import com.iobeam.portal.model.customercontact.*;
import com.iobeam.portal.model.address.*;
import com.iobeam.portal.model.country.*;
import com.iobeam.portal.util.ContactName;
import com.iobeam.portal.util.MailingAddress;

import com.iobeam.portal.ui.web.user.UserSessionHelper;
import com.iobeam.portal.task.actor.user.usersession.*;

import javax.servlet.http.*;
import javax.servlet.*;
import java.util.*;
import java.util.logging.Logger;
import java.text.SimpleDateFormat;
import javax.ejb.*;
import javax.naming.*;
import java.rmi.*;

import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

public class UpdateBillablePartyDetailAction extends Action {

	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		ActionErrors errors = new ActionErrors();

		try {
			UserSession usersession = UserSessionHelper.getUserSession(
				request.getSession());

			if (usersession == null) {
				return mapping.findForward("portalsignon");
			}
			
			Account account = getAccount(request.getParameter("aid"));
			
			BillablePartyForm bpForm = (BillablePartyForm)form;
			
			updateBillableParty(account, bpForm);
		
			// remove ui bean from request or session
			if (mapping.getAttribute() != null) {
				if ("request".equals(mapping.getScope())) {
					request.removeAttribute(mapping.getAttribute());
				}
				else {
					request.getSession().removeAttribute(mapping.getAttribute());
				}
			}

		}
		catch (Exception ee) {
			ee.printStackTrace();
			Logger.getLogger("com.iobeam.portal.ui.web.customer")
				.throwing(UpdateBillablePartyDetailAction.class.getName(), "perform", 
					(Throwable)ee);
			errors.add("application", new ActionError("app.exception"));
			saveErrors(request, errors);
			return mapping.findForward("error");

		}

		return mapping.findForward("success");	

	}

	private void updateBillableParty(Account account, 
			BillablePartyForm form) throws Exception {

		CustomerContact billableCustomerContact = 
			getCustomerContact(account, form);
		
		CustomerContactDAO.update(billableCustomerContact);
		
		CreditCard cc = getCreditCard(account, form);

		PaymentInstrumentDAO.update(cc);

	}

	/**
	 * takes credit card from account and applies updates
	 * from billablePartyForm.
	 */
	private CreditCard getCreditCard(Account account, BillablePartyForm form) 
			throws Exception {

		CreditCard card = (CreditCard)account.getBillableParty().
			getPaymentInstrument();
	
		card.setCreditCardNumber(form.getCreditCardNumber());

		String dateString = form.getExpirationMonth() + "/01/" +
			form.getExpirationYear();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		
		card.setExpirationDate(sdf.parse(dateString));
		card.setSecurityCode(form.getSecurityCode());
		card.setCardHolderName(form.getCardHolderName());

		Address address = card.getCardHolderAddress();
		MailingAddress ma = new MailingAddress(
			form.getAddress1(),
			form.getAddress2(),
			form.getCity(),
			form.getState(),
			form.getZipcode());

		address.setCountry(getAccessCountry().findByPrimaryKey(
			new CountryPK(Long.parseLong(form.getCountryId()))));

		address.setMailingAddress(ma);

		card.setCardHolderAddress(address);

		return card;

	}

	/**
	 * applies updates from billablePartyForm to specified
	 * account's billingCustomerContact.
	 */
	private CustomerContact getCustomerContact(Account account, 
			BillablePartyForm form) throws Exception {

		CustomerContact contact = account.getBillableParty().
				getBillingCustomerContact();

	 	ContactName name = contact.getContactName();

		contact.setContactName(new ContactName(
			form.getFirstName(),
			name.getMiddleInitial(),
			form.getLastName()));

		Address address = contact.getAddress();
		MailingAddress ma = new MailingAddress(
			form.getAddress1(),
			form.getAddress2(),
			form.getCity(),
			form.getState(),
			form.getZipcode());
		
		address.setCountry(getAccessCountry().findByPrimaryKey(
			new CountryPK(Long.parseLong(form.getCountryId()))));

		address.setMailingAddress(ma);

		contact.setAddress(address);

		contact.setPhoneNumber(form.getPhoneNumber());
		contact.setFaxNumber(form.getFaxNumber());
		contact.setEmailAddress(form.getEmailAddress());

		return contact;

	}

	private AccessCountry getAccessCountry() throws Exception {
		AccessCountryHome home = 
				(AccessCountryHome)(new InitialContext()).lookup(
					AccessCountryHome.JNDI_NAME);
		return home.create();
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
