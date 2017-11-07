package com.iobeam.portal.ui.web.customer;

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

import javax.ejb.*;
import javax.naming.*;
import java.rmi.*;

import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

public class UpdateCustomerAction extends Action {

	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		ActionErrors errors = new ActionErrors();

		try {
			UserSession usersession = UserSessionHelper.getUserSession(
				request.getSession());

			if (usersession == null) {
				return mapping.findForward("portalsignon");
			}
			
			CustomerForm customerForm = (CustomerForm)form;
			Customer customer = usersession.getCustomer();
			updateCustomer(customer, customerForm);
		
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
				.throwing(UpdateCustomerAction.class.getName(), "perform", 
					(Throwable)ee);
			errors.add("application", new ActionError("app.exception"));
			saveErrors(request, errors);
			return mapping.findForward("error");

		}

		return mapping.findForward("success");	

	}

	private void updateCustomer(Customer customer, CustomerForm form) 
			throws Exception {

		CustomerData data = customer.getData();
		CustomerContact contact = customer.getCustomerContact();
		Address address = contact.getAddress();
		
		contact.setEmailAddress(form.getEmailAddress());
		contact.setPhoneNumber(form.getPhoneNumber());
		contact.setFaxNumber(form.getFaxNumber());

		address.setCountry(getAccessCountry().findByPrimaryKey(
			new CountryPK(Long.parseLong(form.getCountryId()))));

		MailingAddress ma = new MailingAddress(
			form.getAddress1(),
			form.getAddress2(),
			form.getCity(),
			form.getState().toUpperCase(),
			form.getZipcode());

		address.setMailingAddress(ma);

		contact.setAddress(address);

		ContactName name = contact.getContactName();

		contact.setContactName( new ContactName(form.getFirstName(), 
				name.getMiddleInitial(), 
				form.getLastName()));

		data.setCustomerContact(contact);
		customer.setData(data);

	}

	private AccessCountry getAccessCountry() throws Exception {
		AccessCountryHome home = 
				(AccessCountryHome)(new InitialContext()).lookup(
					AccessCountryHome.JNDI_NAME);
		return home.create();
	}

		

}
