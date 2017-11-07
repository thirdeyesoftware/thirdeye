package com.iobeam.portal.task.billing.invoice.processinvoices;

import javax.ejb.*;
import javax.naming.*;
import java.util.*;
import java.util.logging.*;

import com.iobeam.portal.model.billing.*;
import com.iobeam.portal.model.invoice.*;
import com.iobeam.portal.model.account.*;

import com.iobeam.portal.task.billing.*;
import com.iobeam.portal.task.billing.notifybillableparty.*;
import com.iobeam.portal.task.billing.payment.processautomaticpayment.*;
import com.iobeam.portal.task.billing.payment.applypayment.*;
import com.iobeam.portal.task.billing.payment.*;

/**
 * Process Invoices already created but not presented for payment.
 * Those invoices with billable parties w/automatic payment instruments
 * will be presented for payment.
 */
public class ProcessInvoicesBean implements SessionBean {

	private SessionContext mContext;
	private static Logger cLogger = 
		Logger.getLogger("com.iobeam.portal.task.billing.invoice.processinvoices");

	public void ejbCreate() throws CreateException {

	}

	public void ejbActivate() {

	}

	public void ejbPassivate() {

	}

	public void ejbPostCreate() {

	}

	public void setSessionContext(SessionContext ctx) {
		mContext = ctx;
	}

	public void unsetSessionContext() {
		mContext = null;
	}

	public void ejbRemove() {

	}

	/**
	 * process invoices with automatic payments.  
	 */
	public void processInvoices(BillingPeriod period) 
			throws BillingException {
		
		Collection invoices;

		try {
			invoices = getInvoices(period);

			ProcessAutomaticPayment processor = getProcessAutomaticPaymentBean();
			NotifyBillableParty nbp = getNotifyBillablePartyBean();

			ApplyPayment ap = getApplyPayment();

			for (Iterator it = invoices.iterator(); it.hasNext();) {
				Invoice invoice = (Invoice)it.next();
				if (invoice.getTotalDue().getAmount() == 0) continue;

				BillableParty party = getBillableParty(invoice.getAccountPK());

				PaymentInstrument pi = party.getPaymentInstrument();
			
				if (pi instanceof AutomaticPaymentInstrument) {
					AutomaticPaymentInstrument api = (AutomaticPaymentInstrument)pi;
					try {
						ProcessPaymentResponse response = 
								processor.processAutomaticPayment(api, invoice.getTotalDue());
						String memo = "tx: " + response.getTransactionID() + 
							" code: " + response.getApprovalCode();

						ap.applyPayment(invoice.getAccountPK(), api, invoice.getTotalDue(),
								memo);
					}
					catch (PaymentProcessingException e) { 

						cLogger.info("failed payment attempt: " + e.toString());
						cLogger.info("invoice: " + invoice.toString());
						cLogger.info("payment instrument: " + api.toString());

					}
						
				}
			}

			period.updateCurrentStep(BillingController.PROCESS_INVOICES);
		}
		catch (Exception ee) {
			getSessionContext().setRollbackOnly();
			throw new BillingException(
				"unable to process invoices.",ee);
		}

	}
	
	/**
	 * convenience method to get reference to NotifyBillableParty 
	 * controller.
	 */
	private NotifyBillableParty getNotifyBillablePartyBean() 
			throws Exception {
		InitialContext ic = new InitialContext();
		NotifyBillablePartyHome home = 
			(NotifyBillablePartyHome)ic.lookup(NotifyBillablePartyHome.JNDI_NAME);
		return home.create();
	}

	/**
	 * convenience method to get reference to ProcessAutomaticPayment
	 * controller.
	 */
	private ProcessAutomaticPayment getProcessAutomaticPaymentBean() 
			throws Exception {
		InitialContext ic = new InitialContext();
		ProcessAutomaticPaymentHome home = (ProcessAutomaticPaymentHome)
				ic.lookup(ProcessAutomaticPaymentHome.JNDI_NAME);
		return home.create();
	}

	/**
	 * convenience method to get billable party for specified
	 * account pk.
	 */
	private BillableParty getBillableParty(AccountPK pk) throws Exception {
		InitialContext ic = new InitialContext();
		AccountHome home = (AccountHome)ic.lookup(AccountHome.JNDI_NAME);
		Account account = home.findByPrimaryKey(pk);
		return account.getBillableParty();
	}

	/**
	 * convenience method to retrieve invoices for the specified 
	 * billing period.
	 */
	private Collection getInvoices(BillingPeriod period) throws Exception {
		InitialContext ic = new InitialContext();
		AccessInvoiceHome home =
			(AccessInvoiceHome)ic.lookup(AccessInvoiceHome.JNDI_NAME);
		AccessInvoice ai = home.create();
		return ai.findAllByBillingPeriod(period);
	}

	private SessionContext getSessionContext() {
		return mContext;
	}

	/** 
	 * returns a new ApplyPayment remote interface.
	 */
	private ApplyPayment getApplyPayment() throws Exception {
		ApplyPaymentHome home = (ApplyPaymentHome)
			(new InitialContext()).lookup(ApplyPaymentHome.JNDI_NAME);
		return home.create();
	}

}

