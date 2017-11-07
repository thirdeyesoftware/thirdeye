package com.iobeam.portal.task.billing.payment.processautomaticpayment;


import com.iobeam.portal.util.Money;
import com.iobeam.portal.model.billing.CreditCard;


public class VerisignParms implements java.io.Serializable {
	private CreditCard mCreditCard;
	private Money mAmount;

	VerisignParms(CreditCard creditCard, Money amount) {
		mCreditCard = creditCard;
		if (creditCard == null) {
			throw new NullPointerException("creditCard");
		}

		mAmount = amount;
		if (amount == null) {
			throw new NullPointerException("amount");
		}
	}

	public String getACCT() {
		return "ACCT=" + mCreditCard.getCreditCardNumber();
	}

	public String getAMT() {
		return "AMT=" + mAmount.getStringAmount();
	}

	public String getEXPDATE() {
		return "EXPDATE=" + mCreditCard.getStringExpirationDate();
	}

	public String getNAME() {
		return "NAME[" + mCreditCard.getCardHolderName().length() + "]=" +
				mCreditCard.getCardHolderName();
	}

	public String getPARTNER() {
		return "PARTNER=wfb";
	}

	public String getPWD() {
		return "PWD=108eammerch";
	}

	public String getTENDER() {
		// CreditCard only
		return "TENDER=C";
	}

	public String getTRXTYPE() {
		// sale only for now
		return "TRXTYPE=S";
	}

	public String getUSER() {
		return "USER=" + getUserName();
	}

	private String getUserName() {
		return "vrn481301474";
	}

	public String getVENDOR() {
		return "VENDOR=" + getUserName();
	}


	public String getParmList() {
		StringBuffer sb = new StringBuffer();

		sb.append(getTRXTYPE()).append("&");
		sb.append(getTENDER()).append("&");
		sb.append(getPARTNER()).append("&");
		sb.append(getVENDOR()).append("&");
		sb.append(getUSER()).append("&");
		sb.append(getPWD()).append("&");
		sb.append(getACCT()).append("&");
		sb.append(getNAME()).append("&");
		sb.append(getEXPDATE()).append("&");
		sb.append(getAMT());

		return sb.toString();
	}
}
