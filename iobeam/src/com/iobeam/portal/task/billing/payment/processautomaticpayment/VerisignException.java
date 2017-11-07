package com.iobeam.portal.task.billing.payment.processautomaticpayment;


import java.util.*;


public class VerisignException extends Exception {

	public VerisignResponse mResponse;
	public Map mResponseFields = new Hashtable();

	public VerisignException(String codedResponse) {
		this(new VerisignResponse(codedResponse));
	}

	public VerisignException(VerisignResponse response) {
		mResponse = response;
	}


	public String getCodedResponse() {
		return mResponse.getCodedResponse();
	}


	public String getResponseField(String fieldName) {
		return mResponse.getField(fieldName);
	}


	public int getResultCode() {
		return mResponse.getResultCode();
	}


	public String getTransactionID() {
		return mResponse.getTransactionID();
	}


	public String getResponseMessage() {
		return mResponse.getMessage();
	}


	public String getMessage() {
		return getResponseMessage() + ", rc=" + getResultCode() +
				", txID=" + getTransactionID();
	}


	public String toString() {
		return super.toString() + " resultCode = " + getResultCode();
	}
}
