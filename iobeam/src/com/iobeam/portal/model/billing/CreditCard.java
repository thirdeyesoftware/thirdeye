package com.iobeam.portal.model.billing;

import java.io.Serializable;
import java.util.*;
import java.text.*;
import com.iobeam.portal.model.address.*;


public class CreditCard extends AutomaticPaymentInstrument 
		implements Serializable {


	private static final DateFormat cDateFormat =
			new SimpleDateFormat("MM/dd/yy");

	private static final DateFormat cShortDateFormat =
			new SimpleDateFormat("MMyy");

	private PaymentInstrumentPK mPK;
	private String mCreditCardNumber;
	private String mSecurityCode;
	private Date mExpirationDate;
	private Address mCardHolderAddress;
	private String mCardHolderName;

	private Collection mDeletedAddressPKs = new Vector();
	

	public CreditCard(String cardNumber, String securityCode,
			String cardHolderName,
			Address cardHolderAddress, Date expirationDate) {

		super(PaymentInstrumentType.CREDIT_CARD);

		mCardHolderName = cardHolderName;
		mCardHolderAddress = cardHolderAddress;
		mSecurityCode = securityCode;
		mCreditCardNumber = cardNumber;
		mExpirationDate = expirationDate;
	}


	CreditCard(PaymentInstrumentPK pk,
			String cardNumber, String securityCode, String
			cardHolderName, Address cardHolderAddress, Date expirationDate) {

		super(PaymentInstrumentType.CREDIT_CARD);

		setPK(pk);
		mCardHolderName = cardHolderName;
		mCardHolderAddress = cardHolderAddress;
		mSecurityCode = securityCode;
		mCreditCardNumber = cardNumber;
		mExpirationDate = expirationDate;
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


	public String toString() {
		StringBuffer sb = new StringBuffer("CreditCard(");

		sb.append(getPK()).append(",");
		sb.append(getType()).append(",");
		sb.append(getCardHolderName()).append(",");
		sb.append(getCardHolderAddress()).append(",");
		sb.append(getCreditCardNumber()).append(",");
		sb.append(getSecurityCode()).append(",");
		sb.append(cDateFormat.format(getExpirationDate()));

		sb.append(")");

		return sb.toString();
	}

	public String getCardHolderName() {
		return mCardHolderName;
	}

	public void setCardHolderName(String s) {
		mCardHolderName = s;
	}

	public String getCreditCardNumber() {
		return mCreditCardNumber;
	}

	public void setCreditCardNumber(String s) {
		mCreditCardNumber = s;
	}

	public String getSecurityCode() {
		return mSecurityCode;
	}

	public void setSecurityCode(String s) {
		mSecurityCode = s;
	}

	public Date getExpirationDate() {
		return mExpirationDate;
	}

	public void setExpirationDate(Date d) {
		mExpirationDate = d;
	}


	public String getStringExpirationDate() {
		return cShortDateFormat.format(getExpirationDate());
	}


	public Address getCardHolderAddress() {
		return mCardHolderAddress;
	}

	public void setCardHolderAddress(Address address) {
		if (mCardHolderAddress != null) {
			AddressPK addressPK = mCardHolderAddress.getPK();
			if (addressPK != null &&
					!addressPK.equals(address.getPK())) {
				mDeletedAddressPKs.add(addressPK);
			}
		}

		mCardHolderAddress = address;
		if (address == null) {
			throw new NullPointerException("address");
		}
	}


	Collection getDeletedAddressPKs() {
		return mDeletedAddressPKs;
	}


	void resetDeletedAddressPKs() {
		mDeletedAddressPKs.clear();
	}


	public boolean equals(Object o) {
		if (o instanceof CreditCard) {
			CreditCard cc = (CreditCard)o;
			if (getCreditCardNumber().equals(cc.getCreditCardNumber()) &&
					getSecurityCode().equals(cc.getSecurityCode()) &&
					getCardHolderAddress().equals(cc.getCardHolderAddress()) &&
					getCardHolderName().equals(cc.getCardHolderName())) return true;
		}
		return false;
	}

}

