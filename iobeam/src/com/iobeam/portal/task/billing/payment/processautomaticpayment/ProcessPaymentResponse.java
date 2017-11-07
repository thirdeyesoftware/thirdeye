package com.iobeam.portal.task.billing.payment.processautomaticpayment;


import java.util.*;


public class ProcessPaymentResponse implements java.io.Serializable {

	private String mTransactionID;
	private String mApprovalCode;


	public ProcessPaymentResponse(String transactionID, String approvalCode) {
		mTransactionID = transactionID;
		if (transactionID == null) {
			throw new NullPointerException("transactionID");
		}

		mApprovalCode = approvalCode;
	}


	public String getTransactionID() {
		return mTransactionID;
	}


	public String getApprovalCode() {
		return mApprovalCode;
	}


	public String toString() {
		StringBuffer sb = new StringBuffer("ProcessPaymentResponse(");

		sb.append("tx=").append(getTransactionID());

		if (getApprovalCode() != null) {
			sb.append(",");
			sb.append("auth=").append(getApprovalCode());
		}

		sb.append(")");

		return sb.toString();
	}
}
