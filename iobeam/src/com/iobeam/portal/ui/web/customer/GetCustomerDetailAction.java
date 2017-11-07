package com.iobeam.portal.ui.web.customer;

import com.iobeam.portal.model.customer.*;
import com.iobeam.portal.model.customercontact.*;
import com.iobeam.portal.model.address.*;
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

/**
 * GetCustomerDetailAction populates a view (CustomerForm) with customer data
 * retrieved from the user session if request attribute 'targetcustomerid' is null
 * or missing.
 */
public class GetCustomerDetailAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		ActionErrors errors = new ActionErrors();

		try {
			UserSession usersession = UserSessionHelper.getUserSession(
				request.getSession());
			if (usersession == null) {
				return mapping.findForward("portalsignon");
			}

			Customer customer = null;
	
			if (request.getParameter("targetcustomerid") == null ||
					request.getParameter("targetcustomerid").equals("")) {
				customer = usersession.getCustomer();
			}
			else {
				customer = getCustomerHome().findByPrimaryKey(
					new CustomerPK(Long.parseLong(request.getParameter("targetcustomerid"))));
			}
			
			CustomerForm customerForm = createCustomerForm(usersession.getCustomer());
			request.setAttribute("CustomerForm", customerForm);

			if (usersession == null) {
				return mapping.findForward("portalsignon");
			}

		}
		catch (Exception ee) {
			Logger.getLogger("com.iobeam.portal.ui.web.customer")
				.throwing(GetCustomerDetailAction.class.getName(), "doPost", 
					(Throwable)ee);
			errors.add("application", new ActionError("app.exception"));
			saveErrors(request, errors);
			return mapping.findForward("error");

		}

		return mapping.findForward("success");	

	}

	/**
	 * transforms customer into CustomerForm
	 */
	private CustomerForm createCustomerForm(Customer customer) 
			throws Exception {
		CustomerContact contact = customer.getCustomerContact();
		MailingAddress address = contact.getAddress().getMailingAddress();

		CustomerForm form = new CustomerForm();
		form.setFirstName(contact.getContactName().getFirstName());
		form.setLastName(contact.getContactName().getLastName());
		form.setEmailAddress(contact.getEmailAddress());
		form.setPhoneNumber(contact.getPhoneNumber());
		form.setFaxNumber(contact.getFaxNumber());
		form.setAddress1(address.getLine1());
		form.setAddress2(address.getLine2());
		form.setCity(address.getCity());
		form.setState(address.getState().toUpperCase());
		form.setZipcode(address.getZipcode());
		form.setCountryId(
			String.valueOf(contact.getAddress().getCountry().getPK().getID()));

		return form;

	}
	private CustomerHome getCustomerHome() throws Exception {
		InitialContext ic = new InitialContext();
		return ((CustomerHome)ic.lookup(CustomerHome.JNDI_NAME));
	}

}
