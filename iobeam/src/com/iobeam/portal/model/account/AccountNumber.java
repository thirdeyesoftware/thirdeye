package com.iobeam.portal.model.account;

import java.io.Serializable;

public class AccountNumber implements Serializable {

	public static final long serialVersionUID = 2003042001L;
	
	private String mNumber;
	
	public AccountNumber(String number) {
		mNumber = number;
	}

	public String getNumber() {
		return mNumber;
	}

	public String getAccountNumberString() {
		return mNumber;
	}

	public String toString() {
		return "AccountNumber(" + mNumber + ")";
	}

	public boolean equals(Object o) {
		if (o instanceof AccountNumber) {
			return (((AccountNumber)o).getNumber().equals(getNumber()));
		}
		return false;
	}

	public int hashCode() {
	
		int h = 17;
		return (37 * h + mNumber.hashCode());
		
	}
}
