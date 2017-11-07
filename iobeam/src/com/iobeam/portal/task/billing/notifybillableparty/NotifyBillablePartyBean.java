package com.iobeam.portal.task.billing.notifybillableparty;

import javax.ejb.*;
import javax.jms.*;
import javax.naming.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;

import com.iobeam.portal.model.account.*;
import com.iobeam.portal.model.customercontact.CustomerContact;
import com.iobeam.portal.model.invoice.*;
import com.iobeam.portal.model.billing.*;
import com.iobeam.portal.task.billing.*;

import com.iobeam.portal.service.jms.*;
import com.iobeam.portal.service.jms.mailer.*;


public class NotifyBillablePartyBean implements SessionBean {
	private SessionContext mContext;
	
	public void setSessionContext(SessionContext ctx) {
		mContext = ctx;
	}

	public void ejbCreate() throws CreateException {
	}

	public void ejbActivate() {
	}
	public void ejbPassivate() {
	}
	public void ejbRemove() {
	}
	public void ejbPostCreate() {
	}

	/**
	 * send system message to notify billable party
	 * specified by invoice.
	 */
	public void notify(Invoice invoice) 
			throws BillingNotificationException {
		CustomerContact contact;
		try {
			contact = getBillingCustomerContact(invoice.getAccountPK());
			if (contact == null) {
				Logger.getLogger("com.iobeam.portal.task.billing.notifybillableparty").
					info("could not notify for invoice: " + invoice.toString());
			}

			BillingDeliveryType type = contact.getBillingDeliveryType();
			
			if (type.equals(BillingDeliveryType.EMAIL)) {
				sendBillingEmail(contact, invoice);
			}
			else {
				sendBillingPrintjob(contact, invoice);
			}
		}
		catch (Exception ee) {
			//getSessionContext().setRollbackOnly();
			throw new BillingNotificationException(
				"could not notify for invoice.", ee);
		}

	}

	/**
	 * send system message to notify billable party specified
	 * by statement.
	 */
	public void notify(Statement statement) 
			throws BillingNotificationException {

		CustomerContact contact;
		try {
			contact = getBillingCustomerContact(statement.getAccountPK());
			
			if (contact == null) {
				Logger.getLogger("com.iobeam.portal.task.billing.notifybillableparty").
					info("could not notify for invoice: " + statement.toString());
			}
			BillingDeliveryType type = contact.getBillingDeliveryType();
			
			if (type.equals(BillingDeliveryType.EMAIL)) {
				sendBillingEmail(contact, statement);
			}
			else {
				sendBillingPrintjob(contact, statement);
			}
		}
		catch (Exception ee) {
			getSessionContext().setRollbackOnly();
			throw new BillingNotificationException(
				"could not notify for invoice.", ee);
		}
	}

	/**
	 * notify for all invoices in specified billig period.
	 */
	public void notifyInvoices(BillingPeriod period) 
			throws BillingNotificationException {
		AccessInvoiceHome acHome = null;
		try {
			InitialContext ic = new InitialContext();
			acHome = (AccessInvoiceHome)ic.lookup(AccessInvoiceHome.JNDI_NAME);
			AccessInvoice ai = acHome.create();
			Collection invoices = ai.findAllByBillingPeriod(period);
			for (Iterator it = invoices.iterator(); it.hasNext();) {
				Invoice invoice = (Invoice)it.next();
					Logger.getLogger("com.iobeam.portal.task.billing").info(
						"invoice pk = " + invoice.getAccountPK());

				try {
					notify(invoice);
				}
				catch (BillingNotificationException bne) {
					Logger.getLogger("com.iobeam.portal.task.billing").info(
						"notify failed for invoice id " + invoice.getPK().getID());
				}

					

			}
			period.updateCurrentStep(BillingController.DELIVER_INVOICES);
		}
		catch (Exception ee) {
			getSessionContext().setRollbackOnly();
			throw new BillingNotificationException(
				"could not notify invoices for period " + period.toString(),
				ee);
		}
	}

	/**
	 * convenience method to extract customer contact for billable
	 * party on record for specified account.
	 */
	private CustomerContact getBillingCustomerContact(AccountPK pk) throws Exception {
		InitialContext ic = new InitialContext();
		AccountHome acHome = (AccountHome)ic.lookup(AccountHome.JNDI_NAME);

		Account account = acHome.findByPrimaryKey(pk);

		if (account.getBillableParty() == null) {
			return null;
		}
		else {
			return account.getBillableParty().getBillingCustomerContact();
		}


	}

	/**
	 * send invoice to printer queue for processing for specified customer
	 * contact.
	 */
	private void sendBillingPrintjob(CustomerContact contact, Invoice invoice) 
			throws Exception {
	
		throw new UnsupportedOperationException("no impl yet.");
	}

	/**
	 * send statement to printer queue for processing for specified customer
	 * contact.
	 */
	private void sendBillingPrintjob(CustomerContact contact, 
			Statement statement) throws Exception {

		throw new UnsupportedOperationException("no impl yet.");

	}

	/**
	 * send billing document to printer queue.
	 * customer contact information is used for label printing.
	 */
	private void sendBillingPrintjob(CustomerContact contact, 
			BillingDocument document) throws Exception {

		throw new UnsupportedOperationException("no impl yet.");

	}

	/**
	 * sends invoice to mailer queue for dispatch to specified contact.
	 */
	private void sendBillingEmail(CustomerContact contact, Invoice invoice) 
			throws Exception {
	
		MailerQueue queue = QueueFactory.getMailerQueue();
		String subject = "Your iobeam invoice is ready.";	
		String body = "Your iobeam invoice has been created and is ready for " + 
			"your inspection.\n\n" +
			"To see an online version of this invoice, " + 
			"please visit http://www.iobeam.com, " + 
			"logon to the subscriber portal and click " +
			"'View Billing History'.  If you have any questions about your invoice, " + 
			"send an email to billing@iobeam.com.\n\n" +
			"If you have a credit card on file, your card will be charged " + 
			"automatically and no further action is required.\n\n" +
			"Regards,\n" + 
			"iobeam billing" +
			"\n\n\n" +
			(new InvoiceFormat(InvoiceFormat.EMAIL)).format(invoice);

		BillingMessage message = new BillingMessage(
			contact.getEmailAddress(),
			subject,
			body);
		queue.send(message);

	}

	/**
	 * sends statement to mailer queue for dispatch to specified contact.
	 */
	private void sendBillingEmail(CustomerContact contact, Statement statement)
			throws Exception {
	
	}

	/**
	 * sends billing message to mailer queue for dispatch.
	 */
	private void sendBillingEmail(BillingMessage message) throws Exception {

	}

	private SessionContext getSessionContext() {
		return mContext;
	}


}



