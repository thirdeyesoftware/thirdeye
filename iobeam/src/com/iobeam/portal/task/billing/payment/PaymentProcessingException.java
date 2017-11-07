package com.iobeam.portal.task.billing.payment;


import com.iobeam.portal.model.billing.BillingException;


/**
* Indicates a problem in payment processing, in either
* authorization or capture, or in the gateway communication
* associated with authorization or capture.
*/
public class PaymentProcessingException extends BillingException {


	/**
	* The transaction was declined.
	*
	* This is general statement about the outcome of the transaction.
	* The transaction may still be one of the other types, but it is
	* <i>at least</i> declined.
	*
	* Refinements of the glue connecting this abstraction to underlying
	* gateway-specific code should strive to yeild better descriptions
	* over time.
	*/
	public static final CauseCode DECLINED =
			new CauseCode("Declined", 1);


	/**
	* The BillableParty has an insufficient funds in the presented account.
	*/
	public static final CauseCode INSUFFICIENT_FUNDS =
			new CauseCode("InsufficientFunds", 2);


	/**
	* The BillableParty has an invalid identity presented to the
	* payment gateway.
	*/
	public static final CauseCode INVALID_IDENTITY =
			new CauseCode("InvalidIdentity", 3);


	/**
	* The iobeam portal cannot communicate with the payment gateway.
	*/
	public static final CauseCode NO_GATEWAY_CONTACT =
			new CauseCode("NoGatewayContact", 4);


	/**
	* The iobeam portal is misconfigured for use with
	* the payment gateway.
	*
	* NOTE:  consider moving this state to an Error abstraction.
	*/
	public static final CauseCode INVALID_CONFIGURATION =
			new CauseCode("InvalidConfiguration", 5);



	private CauseCode mCauseCode;
	private String mTransactionID = null;


	public PaymentProcessingException(String msg) {

		this(msg, DECLINED, null);
	}


	public PaymentProcessingException(String msg,
			String transactionID) {

		this(msg, DECLINED, transactionID);
	}


	public PaymentProcessingException(String msg, CauseCode causeCode,
			String transactionID) {
		super(msg);

		mCauseCode = causeCode;
		mTransactionID = transactionID;
	}


	public PaymentProcessingException(String msg, Throwable cause,
			CauseCode causeCode, String transactionID) {
		super(msg, cause);

		mCauseCode = causeCode;
		mTransactionID = transactionID;
	}


	public PaymentProcessingException(Throwable cause, CauseCode causeCode,
			String transactionID) {
		super(cause);

		mCauseCode = causeCode;
		mTransactionID = transactionID;
	}


	/**
	* Returns one of the causeCodes defined as manifest constants of this
	* class.
	*/
	public CauseCode getCauseCode() {
		return mCauseCode;
	}


	/**
	* Returns the transaction ID used by the payment gateway to
	* identify the underlying transaction, or null if there is none.
	*/
	public String getTransactionID() {
		return mTransactionID;
	}


	public String toString() {
		return super.toString() + " causeCode=" + getCauseCode() +
				", transactionID=" + getTransactionID();
	}


	public static class CauseCode implements java.io.Serializable {
		private String mName;
		private int mCode;

		private CauseCode(String name, int code) {
			mName = name;
			mCode = code;
		}


		public int getCode() {
			return mCode;
		}


		public String getName() {
			return mName;
		}


		public String toString() {
			return Integer.toString(getCode()) + ":" + getName();
		}
	}
}
