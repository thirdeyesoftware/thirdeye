package com.iobeam.portal.task.billing;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.ObjectNotFoundException;

import javax.naming.InitialContext;

import java.util.*;
import java.util.logging.Logger;

import com.iobeam.portal.util.Money;
import com.iobeam.portal.model.account.*;
import com.iobeam.portal.model.billing.*;
import com.iobeam.portal.task.billing.account.processaccounts.*;
import com.iobeam.portal.task.billing.invoice.createinvoice.*;
import com.iobeam.portal.task.billing.invoice.processinvoices.*;
import com.iobeam.portal.task.billing.payment.applypayment.*;
import com.iobeam.portal.task.billing.payment.processautomaticpayment.*;
import com.iobeam.portal.task.billing.statement.processstatements.*;
import com.iobeam.portal.task.billing.notifybillableparty.*;


public class BillingControllerBean implements SessionBean {
	private SessionContext mContext;

	private static final String PAYMENT_LOGGING_PROP = 
		"com.iobeam.portal.task.billing.payment.processautomaticpayment";
		
	private Logger cLogger = Logger.getLogger(PAYMENT_LOGGING_PROP);

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

	public void setSessionContext(SessionContext ctx) {
		mContext = ctx;
	}

	private SessionContext getSessionContext() {
		return mContext;
	}

	/**
	 * method is the main entry point into the BillingControllerBean.
	 * @param step the billing step to run @see 
	 * com.iobeam.portal.task.billing.BillingController
	 * @param period the period to run billing step against
	 */
	public void run(int step, BillingPeriod period) 
			throws BillingException {
		int currentStep = period.getCurrentStep();
		if ((currentStep + 1) != step) 
				throw new BillingException("step " + step + 
					" has already been processed.");
		switch (step) {
			case BillingController.PROCESS_ACCOUNTS:
				cLogger.info("running processAccounts()");
				processAccounts(period);	
				break;
			case BillingController.CREATE_INVOICES:
				cLogger.info("running createInvoices()");
				createInvoices(period);
				break;
			case BillingController.DELIVER_INVOICES:
				cLogger.info("running deliverInvoices()");
				deliverInvoices(period);
				break;
			case BillingController.PROCESS_INVOICES:
				cLogger.info("running processInvoices()");
				processInvoices(period);
				break;
			case BillingController.PROCESS_STATEMENTS:
				cLogger.info("running processStatements()");
				processStatements(period);
				break;
			default:
				throw new BillingException("undefined billing step.");
		}
	}

	/**
	 * runs ALL the billing steps for the specified billing period
	 * @param period The period to run all steps against.
	 */
	public void run(BillingPeriod period) 
			throws BillingException {

		run(BillingController.PROCESS_ACCOUNTS, period);

		run(BillingController.CREATE_INVOICES, period);
		
		run(BillingController.DELIVER_INVOICES, period);

		run(BillingController.PROCESS_INVOICES, period);

		run(BillingController.PROCESS_STATEMENTS, period);

	}

	/**
	 * runs a specified billing step for a given target date.  If a 
	 * billing period is not found with the target date, a BillingException
	 * is thrown.
	 * @param step the billing step to run
	 * @param target the target date to use to lookup billing period
	 * @throws BillingException
	 */
	public void run(int step, Date target) throws BillingException {
		try {
			BillingPeriod period = BillingPeriod.getInstanceFor(target);
			run(step, period);
		}
		catch (ObjectNotFoundException ofe) {
			throw new BillingException(
				"could not find billing period containing target date.",ofe);
		}
	}

	/**
	 * processes accounts for a given billing period.
	 * The 'process' involves creating account entries for viable
	 * subscriptions and other liabilities.  These rules are implemented in
	 * ProcessAccounts Bean.  
	 * @see 
	 *  com.iobeam.portal.task.billing.account.processaccounts.ProcessAccountsBean 
	 * @throws BillingException
	 */

	private void processAccounts(BillingPeriod period) 
			throws BillingException {
		
		ProcessAccountsHome home = null;
		try {
			InitialContext ic = new InitialContext();
			home = (ProcessAccountsHome)ic.lookup(ProcessAccountsHome.JNDI_NAME);
			ProcessAccounts pa = home.create();
			pa.processAccounts(period);
		}
		catch (Exception ee) {
			throw new BillingException(
				"could not process accounts", ee);
		}
	}

	/**
	 * creates invoices for a specified billing period.
	 * @param period the specified period to create invoices
	 * @throws BillingException
	 */
	private void createInvoices(BillingPeriod period) 
			throws BillingException {

		CreateInvoiceHome home = null;
		try {
			InitialContext ic = new InitialContext();
			home = (CreateInvoiceHome)ic.lookup(CreateInvoiceHome.JNDI_NAME);
			CreateInvoice ci = home.create();
			ci.createInvoices(period);
		}
		catch (Exception ee) {
			throw new BillingException(
				"could not create invoices", ee);
		}
	}

	/**
	 * delivers invoices for the specified period
	 * the delivery mechanisms are implemented in NotifyBillablePartyBean
	 * @see
	 com.iobeam.portal.task.billing.notifybillableparty.NotifyBillablePartyBean
	 *
	 * @throws BillingException
	 */
	public void deliverInvoices(BillingPeriod period) 
			throws BillingException {
		NotifyBillablePartyHome home = null;
		try {
			InitialContext ic = new InitialContext();
			home = (NotifyBillablePartyHome)ic.lookup(
				NotifyBillablePartyHome.JNDI_NAME);
			NotifyBillableParty nbp = home.create();
			nbp.notifyInvoices(period);
		}
		catch (Exception ee) {
			throw new BillingException("could not notify for invocies.",ee);
		}
	}

	/**
	 * process invoices within the specified billing period.
	 * attempts takes accounts with balances and exercises
	 * automatic payment instrument to satisfy those balances. 
	 * @param period the billing period
	 * @throws BillingException
	 */
	private void processInvoices(BillingPeriod period) 
			throws BillingException {

		
		ProcessAutomaticPayment ppb;
		ApplyPayment ap;

		try {
			ProcessInvoices pi = getProcessInvoices();
			pi.processInvoices(period);

		}
		catch (Exception ee) {
			cLogger.info("could not process automatic payment or apply" + 
			" payment");
			getSessionContext().setRollbackOnly();
			throw new BillingException(ee);
		}
			
	}

	
	private AccountHome getAccountHome() throws Exception {
		AccountHome home = 
			(AccountHome)(new InitialContext()).lookup(AccountHome.JNDI_NAME);
		return home;
	}

	private ProcessAutomaticPayment getProcessAutomaticPayment()
			throws Exception {
		ProcessAutomaticPaymentHome home = 
			(ProcessAutomaticPaymentHome)(new InitialContext()).lookup(
				ProcessAutomaticPaymentHome.JNDI_NAME);

		return home.create();

	}

	private ProcessInvoices getProcessInvoices() throws Exception {
		ProcessInvoicesHome home = 
				(ProcessInvoicesHome)(new InitialContext()).lookup(
					ProcessInvoicesHome.JNDI_NAME);
		return home.create();
	}


	private ApplyPayment getApplyPayment() 
			throws Exception {
		ApplyPaymentHome home = 
				(ApplyPaymentHome)(new InitialContext()).lookup(
					ApplyPaymentHome.JNDI_NAME);
		return home.create();
	}

	private void processStatements(BillingPeriod period) 
	  	throws BillingException {
		try {
			InitialContext ic = new InitialContext();
			ProcessStatementsHome home =	
				(ProcessStatementsHome)ic.lookup(ProcessStatementsHome.JNDI_NAME);
			ProcessStatements ps = home.create();
			ps.process(period);
		}
		catch (Exception ee) {
				cLogger.throwing(
					BillingControllerBean.class.getName(), "processStatements",
					(Throwable)ee);
			throw new BillingException("could not process statements.",ee);

		}

	}


}

