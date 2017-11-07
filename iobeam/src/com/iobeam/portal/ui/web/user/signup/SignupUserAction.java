package com.iobeam.portal.ui.web.user.signup;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.naming.*;
import java.util.logging.Logger;
import java.math.BigInteger;
import javax.ejb.*;
import javax.naming.*;
import java.text.SimpleDateFormat;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.util.MessageResources;

import com.iobeam.portal.task.actor.user.signon.*;
import com.iobeam.portal.task.actor.user.usersession.*;
import com.iobeam.portal.task.customer.setupuser.*;

import com.iobeam.portal.security.*;
import com.iobeam.portal.model.subscription.*;
import com.iobeam.portal.model.prototype.subscription.*;
import com.iobeam.portal.model.customercontact.*;
import com.iobeam.portal.model.billing.*;
import com.iobeam.portal.model.country.*;
import com.iobeam.portal.model.address.*;
import com.iobeam.portal.util.*;
import com.iobeam.portal.ui.web.user.*;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;

/**
 * servlet called by user while at venue.
 */
public final class SignupUserAction extends Action {

		public ActionForward execute(ActionMapping mapping,
				ActionForm _form,
				HttpServletRequest request,
				HttpServletResponse response) throws Exception {

		Logger l = Logger.getLogger("com.iobeam.portal.ui.web.user.SignonUserAction");
		ActionErrors errors = new ActionErrors();

		HttpSession session = request.getSession();
		l.info("session is new ? " + session.isNew());
		l.info("sess id = " + session.getId());
		l.info("SignupUserAction.perform():start");

		try {
			InitialContext ic = new InitialContext();
			SetupUserHome suh = (SetupUserHome)ic.lookup(SetupUserHome.JNDI_NAME);
			SetupUser su = suh.create();
			
			SignupUserForm form = (SignupUserForm)_form;
			
			CustomerContact contact = getCustomerContact(form);

			if (form.getSecureId() == null || form.getSecureId().trim().equals("")) {
				// signup public user
				AutomaticPaymentInstrument api = getPaymentInstrument(form);
				SubscriptionPrototypePK subProtoPK = new SubscriptionPrototypePK(
						Long.parseLong(request.getParameter("subscriptionPrototypeId")));
			
				l.info("constructed the data.");

				su.setupPublicMember(
					form.getUsername(),
					form.getPassword().toCharArray(),
					form.getPasswordReminderAnswer(),
					contact,
					api,
					subProtoPK);
				l.info("setup user correctly");

			}
			else {
				su.setupPrivateMember(form.getUsername(),
						form.getPassword().toCharArray(),
						form.getPasswordReminderAnswer(),
						contact,
						form.getSecureId());
				l.info("setup private user complete");
			}

		}
		catch (DuplicateUserException due) {
			errors.add(ActionErrors.GLOBAL_ERROR,
				new ActionError("error.username.unique"));
		}
		catch (PasswordException pe) {
			errors.add(ActionErrors.GLOBAL_ERROR,
				new ActionError("error.password.invalid"));
		}
		catch (SubscriptionException se) {
			errors.add(ActionErrors.GLOBAL_ERROR,
				new ActionError("error.subscription.invalid"));
		}
		catch (Exception ee) {
			l.throwing(SignupUserAction.class.getName(),"perform",(Throwable)ee);
			ee.printStackTrace();
			errors.add(ActionErrors.GLOBAL_ERROR,
				new ActionError("app.exception"));
			return mapping.findForward("failure");
		}
		
		if (errors.size() > 0) {
			saveErrors(request, errors);
			return new ActionForward(mapping.getInput());
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

		return mapping.findForward("success");

	}

	private CustomerContact getCustomerContact(SignupUserForm form) 
			throws Exception {

			ContactName name = 
				new ContactName(form.getFirstName(), null, form.getLastName());
			
			Address address = new Address( new MailingAddress(
				form.getAddress1(), form.getAddress2(),
				form.getCity(), form.getState(),
				form.getZipcode()), getCountry(form));

			return new CustomerContact(
				name, address, form.getPhoneNumber(), form.getFaxNumber(), 
					form.getEmailAddress());
	}

	private Address getBillingAddress(SignupUserForm form) 
			throws Exception {
		return new Address( new MailingAddress(
			form.getBillingAddress1(),
			form.getBillingAddress2(),
			form.getBillingCity(),
			form.getBillingState(),
			form.getBillingZipcode()), getCountry(form));
	}

	private Country getCountry(SignupUserForm form ) throws Exception {

		AccessCountryHome achome = (AccessCountryHome)
	  	(new InitialContext()).lookup(AccessCountryHome.JNDI_NAME);

		AccessCountry ac = achome.create();
		return ac.findByPrimaryKey(
			new CountryPK(Long.parseLong(form.getCountryId())));

	}

	private AutomaticPaymentInstrument getPaymentInstrument(
			SignupUserForm form) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

		CreditCard card = new CreditCard(
			form.getCreditCardNumber(),
			form.getSecurityCode(),
			form.getCardHolderName(),
			getBillingAddress(form),
			sdf.parse(form.getExpirationMonth() + "/01/" +
				form.getExpirationYear()));
		return card;
	}



}
