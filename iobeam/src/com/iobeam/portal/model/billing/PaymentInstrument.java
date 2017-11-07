package com.iobeam.portal.model.billing;


/**
* Describes a financial instrument representing monies paid to iobeam.
*/
public interface PaymentInstrument {

	public PaymentInstrumentPK getPK();


	public PaymentInstrumentType getType();

}
