package com.iobeam.portal.task.billing.payment.processautomaticpayment;


import java.util.*;


public class VerisignResponse implements java.io.Serializable {

	public static final int RC_APPROVED = 0;
	public static final int RC_INVALID_TRXTYPE = 3;
	public static final int RC_INVALID_ACCOUNT_NUMBER = 23;

	public String mResponse;
	public Map mResponseFields = new Hashtable();

	public VerisignResponse(String codedResponse) {
		mResponse = codedResponse;

		parseResponse(codedResponse);
	}


	private void parseResponse(String codedResponse) {
		StringTokenizer st = new StringTokenizer(codedResponse, "&");

		while (st.hasMoreTokens()) {
			String expr = st.nextToken();

			StringTokenizer et = new StringTokenizer(expr, "=");

			String fieldName = et.nextToken();
			String fieldValue = et.nextToken();

			mResponseFields.put(fieldName, fieldValue);
		}
	}


	public String getCodedResponse() {
		return mResponse;
	}


	public String getField(String fieldName) {
		return (String) mResponseFields.get(fieldName);
	}


	public int getResultCode() {
		return Integer.parseInt(getField("RESULT"));
	}


	public String getTransactionID() {
		return getField("PNREF");
	}


	public String getApprovalCode() {
		return getField("AUTHCODE");
	}


	public String getMessage() {
		return getField("RESPMSG");
	}


	public String toString() {
		StringBuffer sb = new StringBuffer("VerisignResponse(");

		sb.append(getCodedResponse()).append(",");
		sb.append(getMessage()).append(",");
		sb.append("rc=").append(getResultCode()).append(",");
		sb.append("tx=").append(getTransactionID());

		if (getApprovalCode() != null) {
			sb.append(",");
			sb.append("auth=").append(getApprovalCode());
		}

		sb.append(")");

		return sb.toString();
	}
}
