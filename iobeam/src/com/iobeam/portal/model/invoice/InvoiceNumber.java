package com.iobeam.portal.model.invoice;

import java.io.Serializable;

public class InvoiceNumber implements Serializable {
	public static final long serialVersionUID = 200304181601L;
	private String mNumber;
	
	public InvoiceNumber(String number) {
		mNumber = number;
	}
	public String toString() {
		return mNumber;
	}

	public String getInvoiceNumber() {
		return mNumber;
	}

	public boolean equals(Object o) {
		if (o instanceof InvoiceNumber) {
			return (((InvoiceNumber)o).toString().equals(toString()));
		}
		return false;
	}

	public int hashCode() {
		return mNumber.hashCode();
	}
	
}
