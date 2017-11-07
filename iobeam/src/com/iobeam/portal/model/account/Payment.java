package com.iobeam.portal.model.account;


import java.util.Date;
import com.iobeam.portal.util.Money;
import com.iobeam.portal.model.billing.PaymentInstrumentType;

public class Payment extends AccountEntry implements Comparable {

	private String mMemo;
	private PaymentInstrumentType mPaymentInstrumentType;

	public Payment(long id, Money amount, Date postDate,
			PaymentInstrumentType paymentInstrumentType, String memo) {
		super(id, AccountEntryType.PAYMENT, amount, postDate);
		setPaymentInstrumentType(paymentInstrumentType);
		setMemo(memo);
	}

	private void setPaymentInstrumentType(PaymentInstrumentType type) {
		mPaymentInstrumentType = type;
	}

	public PaymentInstrumentType getPaymentInstrumentType() {
		return mPaymentInstrumentType;
	}

	public String toString() {
		return "Payment:" + super.toString();
	}

	public int compareTo(Object o) {
		return super.compareTo(o);
	}

}
