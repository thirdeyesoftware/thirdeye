package com.iobeam.portal.model.invoice;

import java.util.*;
import com.iobeam.portal.util.Money;
import java.io.Serializable;
import com.iobeam.portal.model.account.AccountEntry;

public class InvoiceLineItem implements Serializable {
	public static final long serialVersionUID = 2003042201L;

	private AccountEntry mAccountEntry;
	private long mID;

	public InvoiceLineItem(long id, AccountEntry accountEntry) {
		mID = id;
		mAccountEntry = accountEntry;
	}

	public long getID() {
		return mID;
	}

	public AccountEntry getAccountEntry() {
		return mAccountEntry;
	}

	public boolean equals(Object o) {
		if (o instanceof InvoiceLineItem) {
			InvoiceLineItem item = (InvoiceLineItem)o;
			if (item.getID() == getID() &&
					item.getAccountEntry().equals(getAccountEntry()))
				return true;
		}
		return false;
	}

	public String toString() {
		return "InvoiceLineItem(" + mID + "," +
			mAccountEntry.toString();
	}

	public int hashCode() {
		return (int)((mID >> 32) ^ mID);
	}

}

