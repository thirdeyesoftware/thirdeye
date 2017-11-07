package com.iobeam.portal.task.billing.statement.processstatements;

import javax.ejb.*;
import java.util.*;
import javax.naming.*;

import com.iobeam.portal.model.billing.*;
import com.iobeam.portal.model.account.*;
import com.iobeam.portal.model.invoice.*;

import com.iobeam.portal.task.billing.notifybillableparty.*;
import com.iobeam.portal.task.billing.BillingController;

public class ProcessStatementsBean implements SessionBean {

	private SessionContext mContext;

	public void setSessionContext(SessionContext ctx) {
		mContext = ctx;
	}

	public void ejbCreate() throws CreateException {

	}

	public void ejbPostCreate() {

	}

	public void ejbActivate() {

	}

	public void ejbPassivate() {

	}

	public void ejbRemove() {

	}

	/**
	 * create statements and deliver them to billable
	 * party.
	 */
	public void process(BillingPeriod period) 
			throws BillingException {

		try {
			InitialContext ic = new InitialContext();
			AccessInvoiceHome aiHome = 
				(AccessInvoiceHome)ic.lookup(AccessInvoiceHome.JNDI_NAME);
			AccessInvoice ai = aiHome.create();
			
			NotifyBillablePartyHome nbpHome= 
				(NotifyBillablePartyHome)ic.lookup(NotifyBillablePartyHome.JNDI_NAME);
			NotifyBillableParty nbp = nbpHome.create();
			
			Statement statement = null;

			AccountHome home =  (AccountHome)ic.lookup(
					AccountHome.JNDI_NAME);

			Collection accountsWithActivity = 
					home.findByActivity(period.getStartDate(), period.getEndDate());

			for (Iterator it = accountsWithActivity.iterator(); it.hasNext(); ) { 
				Account account = (Account)it.next();
				Invoice invoice = ai.findByBillingPeriod(
					account.getData().getPK(), period);

				Collection payments = account.getAccountEntries(
					AccountEntryType.PAYMENT, period.getStartDate(),
					period.getEndDate());

				statement = new Statement(account.getData().getPK(),
					period, invoice, payments);
				
				nbp.notify(statement);
			}

			period.updateCurrentStep(BillingController.PROCESS_STATEMENTS);

		}
		catch (Exception ee) {
			getSessionContext().setRollbackOnly();
			throw new BillingException("could not process statements.", ee);
		}

	}

	private SessionContext getSessionContext() {
		return mContext;
	}


}


