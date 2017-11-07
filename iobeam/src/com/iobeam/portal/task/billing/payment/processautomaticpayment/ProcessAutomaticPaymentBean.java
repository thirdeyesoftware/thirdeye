package com.iobeam.portal.task.billing.payment.processautomaticpayment;


import java.util.logging.*;
import javax.ejb.*;
import javax.naming.*;
import com.iobeam.portal.model.billing.*;
import com.iobeam.portal.task.billing.payment.*;
import com.iobeam.portal.util.Money;
import com.Verisign.payment.*;


public class ProcessAutomaticPaymentBean implements SessionBean {

	public static final String LOGGER =
			"com.iobeam.portal.task.billing.payment";

	private SessionContext mContext;
	
	public void setSessionContext(SessionContext ctx) {
		mContext = ctx;
	}

	public SessionContext getSessionContext() {
		return mContext;
	}


	public void ejbCreate() throws CreateException {
	}

	public void ejbRemove() {
	}

	public void ejbActivate() {
	}

	public void ejbPassivate() {
	}


	/**
	* Presents the specified PaymentInstrument and charge amount
	* to the appropriate payment gateway for authorization.
	*
	* Returns the ProcessPaymentResponse, containing transactionID and
	* approval code.
	*
	* @exception PaymentProcessingException the payement could not
	* be processed, with explanation.
	*/
	public ProcessPaymentResponse processAutomaticPayment(
			AutomaticPaymentInstrument api,
			Money amount)
			throws PaymentProcessingException {

		if (api instanceof CreditCard) {
			return processAutomaticPayment((CreditCard) api, amount);
		} else {
			throw new UnsupportedOperationException(
					"No processing support for " + api.getClass().getName());
		}
	}


	/**
	* Presents the specified CreditCard and charge amount
	* to the appropriate payment gateway for authorization.
	*
	* Returns the ProcessPaymentResponse, containing transactionID and
	* approval code.
	*
	* @exception PaymentProcessingException the payment
	* could not be processed, with explanation.
	*/
	private ProcessPaymentResponse processAutomaticPayment(
			CreditCard creditCard,
			Money amount) throws PaymentProcessingException {

		VerisignParms parms = new VerisignParms(creditCard, amount);

		Logger.getLogger(LOGGER).info(parms.getParmList());

		boolean testOnly = Boolean.getBoolean(
				"iobeam.portal.paymentprocess.testonly");

		try {
			VerisignResponse r = null;

			if (testOnly) {
				Logger.getLogger(LOGGER).info("TEST ONLY TX");

				r = VerisignGateway.getTestInstance().submitTransaction(parms);
			} else {
				Logger.getLogger(LOGGER).info("LIVE TX");

				r = VerisignGateway.getInstance().submitTransaction(parms);
			}

			Logger.getLogger(LOGGER).info(r.toString());

			return new ProcessPaymentResponse(r.getTransactionID(),
					r.getApprovalCode());
		}
		catch (VerisignException ve) {

			// We explicitly do not call
			//
			// getSessionContext().setRollbackOnly();
			//
			// because this is expected to be a logged condition
			// that may require db work in this tx.


			PaymentProcessingException.CauseCode causeCode;

			switch(ve.getResultCode()) {
			case VerisignResponse.RC_INVALID_ACCOUNT_NUMBER:
				causeCode = PaymentProcessingException.INVALID_IDENTITY;
				break;
			case VerisignResponse.RC_INVALID_TRXTYPE:
				causeCode = PaymentProcessingException.INVALID_CONFIGURATION;
				break;
			default:
				causeCode = PaymentProcessingException.DECLINED;
				break;
			}

			throw new PaymentProcessingException(ve.getResponseMessage(),
					ve, causeCode,
					ve.getTransactionID());
		}
	}


	/**
	* Returns the version String of the underlying
	* transaction processing client.
	*/
	public String getTransactionClientVersion() {
		return VerisignGateway.getTestInstance().getVersion();
	}


	private void captureCreditCard(CreditCard cc)
			throws PaymentProcessingException {
		throw new UnsupportedOperationException("no impl yet.");
	}


//	private void captureElectronicCheck(ElectronicCheck check) 
//		throws PaymentProcessingException {
//		throw new UnsupportedOperationException("no impl yet.");
//	}

}
