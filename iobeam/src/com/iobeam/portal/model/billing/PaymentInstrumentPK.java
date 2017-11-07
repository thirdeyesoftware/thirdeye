package com.iobeam.portal.model.billing;


public class PaymentInstrumentPK implements java.io.Serializable {

	private long mID;


	public PaymentInstrumentPK(long id) {
		mID = id;
	}


	public long getID() {
		return mID;
	}


	public int hashCode() {
		return (int) ((mID >> 32) ^ mID);
	}


	public boolean equals(Object o) {
		if (o instanceof PaymentInstrumentPK) {
			PaymentInstrumentPK pk = (PaymentInstrumentPK) o;
			return getID() == pk.getID();
		} else {
			return false;
		}
	}


	public String toString() {
		return "PaymentInstrumentPK(" + getID() + ")";
	}
}
