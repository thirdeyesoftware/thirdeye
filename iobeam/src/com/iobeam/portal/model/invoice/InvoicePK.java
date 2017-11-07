package com.iobeam.portal.model.invoice;


public class InvoicePK implements java.io.Serializable {

	private String mInvoiceNumber;
	private long mID;
	
	public InvoicePK(long id) {

		mID = id;
	}

	public long getID() {
		return mID;
	}
	


	public int hashCode() {
		return (int)((mID >> 32) ^ mID);
	}


	public boolean equals(Object o) {
		if (o instanceof InvoicePK) {
			InvoicePK pk = (InvoicePK) o;
			return pk.getID() == getID();
		} else {
			return false;
		}
	}


	public String toString() {
		return "InvoicePK(" + getID() + ")";
	}
}
