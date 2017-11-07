package com.iobeam.portal.task.billing.invoice.createinvoice;

import javax.ejb.*;
import javax.naming.*;
import java.rmi.*;

import java.util.Collection;
import java.util.Vector;
import java.util.Iterator;
import java.util.logging.Logger;

import com.iobeam.portal.model.invoice.*;
import com.iobeam.portal.model.account.*;
import com.iobeam.portal.model.billing.BillingException;
import com.iobeam.portal.model.billing.BillingPeriod;

import com.iobeam.portal.task.billing.BillingController;

public class CreateInvoiceBean implements SessionBean {
	
	private SessionContext mContext;

	public void ejbCreate() throws CreateException {

	}

	public void ejbActivate() {

	}

	public void ejbPassivate() {

	}

	public void ejbRemove() {

	}

	public void setSessionContext(SessionContext ctx) {
		mContext = ctx;
	}

	public void unsetSessionContext() {
		mContext = null;
	}

	public Collection createInvoices(BillingPeriod period)
			throws BillingException {
		Vector invoices = new Vector();
		Logger logger = Logger.getLogger(
			"com.iobeam.portal.task.billing.invoice.createinvoice");

		Collection accounts;
		try {
			InitialContext ic = new InitialContext();	
			AccountHome home = (AccountHome)
				ic.lookup(AccountHome.JNDI_NAME);

			accounts = home.findByActivity(
				period.getStartDate(), period.getEndDate());
			logger.info(
				"createInvoice(): found " + accounts.size() + " accounts.");

			for (Iterator it = accounts.iterator();it.hasNext();) {
				Account account = (Account)it.next();
				Collection activity = 
					account.getAccountEntries(period.getStartDate(),
							period.getEndDate());
					logger.info(
						"createInvoice():found " + activity.size() + " records of " + 
					"activity.");

				invoices.addElement(createInvoice(account, activity));
			}

			period.updateCurrentStep(BillingController.CREATE_INVOICES);
			Logger.getLogger("com.iobeam.portal.task.billing.createinvoice").
				info(period.toString());

		}
		catch (Exception ee) {
			ee.printStackTrace();
			Logger.getLogger("com.iobeam.portal.task.billing.createinvoice").
				throwing(CreateInvoice.class.getName(), "createInvoices",(Throwable)ee);
			getSessionContext().setRollbackOnly();
			throw new BillingException(ee.toString());
		}
		return invoices;

	}

	public Invoice createInvoice(Account account, Collection accountEntries) 
			throws BillingException {
		AccessInvoiceHome home = null;	
		AccessInvoice ai = null;
		Invoice invoice = null;
		try {
			InitialContext ic = new InitialContext();
			home = 
					(AccessInvoiceHome)ic.lookup(AccessInvoiceHome.JNDI_NAME);
			ai = home.create();
			invoice = ai.create(account.getData().getPK(), accountEntries);
		}
		catch (Exception ee) {
			getSessionContext().setRollbackOnly();
			throw new BillingException(
				"could not create invoice.", ee);
		}
		return invoice;
	}

	private SessionContext getSessionContext() {
		return mContext;
	}



}

