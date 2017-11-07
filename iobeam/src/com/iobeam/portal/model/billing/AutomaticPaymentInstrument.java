package com.iobeam.portal.model.billing;



abstract public class AutomaticPaymentInstrument
		implements PaymentInstrument, java.io.Serializable {

	private PaymentInstrumentType mPaymentInstrumentType;


	AutomaticPaymentInstrument(PaymentInstrumentType piType) {
		mPaymentInstrumentType = piType;
		if (piType == null) {
			throw new NullPointerException("piType");
		}
	}

	public PaymentInstrumentType getType() {
		return mPaymentInstrumentType;
	}
}

