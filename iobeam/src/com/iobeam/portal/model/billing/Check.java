package com.iobeam.portal.model.billing;


import java.io.Serializable;

public class Check implements PaymentInstrument, Serializable {
	private String mCheckNumber;
	private String mRoutingNumber;
	private String mAccountNumber;
	private String mBankName;
	private String mAccountHolderName;

	private PaymentInstrumentPK mPK;
	private PaymentInstrumentType mPaymentInstrumentType;

	public Check(String checkNumber) {
		mPaymentInstrumentType = PaymentInstrumentType.CHECK;

		mCheckNumber = checkNumber;
	}


	public Check(PaymentInstrumentPK pk, String checkNumber) {

		mPaymentInstrumentType = PaymentInstrumentType.CHECK;

		setPK(pk);

		mCheckNumber = checkNumber;
	}


	public void setAccountNumber(String s) {
		mAccountNumber = s;
	}
	public String getAccountNumber() {
		return mAccountNumber;
	}
	public void setRoutingNumber(String s) {
		mRoutingNumber = s;
	}
	public String getRoutingNumber() {
		return mRoutingNumber;
	}
	public String getBankName() {
		return mBankName;
	}
	public void setBankName(String s) {
		mBankName = s;
	}
	public String getAccountHolderName() {
		return mAccountHolderName;
	}
	public void setAccountHolderName(String s) {
		mAccountHolderName = s;
	}


	public PaymentInstrumentPK getPK() {
		return mPK;
	}


	void setPK(PaymentInstrumentPK pk) {
		mPK = pk;
		if (pk == null) {
			throw new NullPointerException("pk");
		}
	}


	public String getCheckNumber() {
		return mCheckNumber;
	}

	public int hashCode() {
		return mCheckNumber.hashCode();
	}

	public boolean equals(Object o) {
		if (o instanceof Check) {
			if (((Check)o).getCheckNumber().equals(getCheckNumber()))
				return true;
		}
		return false;
	}

	public PaymentInstrumentType getType() {
		return mPaymentInstrumentType;
	}

	public String toString() {
		return "Check(" + getPK() + "," + mCheckNumber + ")";
	}

}

	
