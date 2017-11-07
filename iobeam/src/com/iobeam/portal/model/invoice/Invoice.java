package com.iobeam.portal.model.invoice;

import java.util.*;
import java.io.Serializable;
import com.iobeam.portal.util.Money;
import com.iobeam.portal.model.account.AccountPK;

public class Invoice implements Serializable {
	public static final long serialVersionUID = 2003042203L;

	private InvoiceNumber mInvoiceNumber;
	private InvoicePK mPK;
	private Date mInvoiceDate;
	private AccountPK mAccountPK;
	private Money mTotalDue;
	private Vector mInvoiceLineItems;
	private InvoiceStatus mInvoiceStatus;

	public Invoice(InvoicePK pk) {
		this(pk, null, null, null, null, null);
	}

	public Invoice(InvoicePK pk, InvoiceNumber number,
			Date date, AccountPK acctPK, Money totalDue, InvoiceStatus status) {
		mPK = pk;
		mInvoiceNumber = number;
		mInvoiceDate = date;
		mTotalDue = totalDue;
		mInvoiceStatus = status;
		mAccountPK = acctPK;

	}

	protected void setAccountPK(AccountPK pk) {
		mAccountPK = pk;
	}

	public AccountPK getAccountPK() {
		return mAccountPK;
	}

	public void setInvoiceLineItems(Collection c) {
		mInvoiceLineItems = new Vector(c);
	}

	public Collection getInvoiceLineItems() {
		return mInvoiceLineItems;
	}

	public InvoiceNumber getInvoiceNumber() {
		return mInvoiceNumber;
	}

	public void setInvoiceNumber(InvoiceNumber number) {
		mInvoiceNumber = number;
	}

	public InvoicePK getPK() {
		return mPK;
	}
	public Date getInvoiceDate() {
		return mInvoiceDate;
	}
	public void setInvoiceDate(Date d) {
		mInvoiceDate = d;
	}
	public Money getTotalDue() {
		return mTotalDue;
	}
	public void setTotalDue(Money m) {
		mTotalDue = m;
	}
	public InvoiceStatus getInvoiceStatus() {
		return mInvoiceStatus;
	}
	public void setInvoiceStatus(InvoiceStatus stat) {
		mInvoiceStatus = stat;
	}

	public boolean isProcessed() {
		return mInvoiceStatus.equals(InvoiceStatus.INVOICE_OPEN);
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("Invoice:");
		sb.append(mInvoiceNumber.toString()).append("\n");
		sb.append(mInvoiceDate.toString()).append("\n");
		sb.append(mInvoiceStatus.toString()).append("\n");
		sb.append(mTotalDue.toString()).append("\n");
		sb.append(mAccountPK.toString());
		return sb.toString();
	}
	
	public boolean equals(Object o) {
		if (o instanceof Invoice) {
			Invoice invoice = (Invoice)o;
			if (invoice.getPK().equals(getPK()) &&
					invoice.getInvoiceNumber().equals(getInvoiceNumber()) &&
					invoice.getInvoiceDate().equals(getInvoiceDate()) &&
					invoice.getInvoiceStatus().equals(getInvoiceStatus()) &&
					invoice.getTotalDue().equals(getTotalDue()))
				return true;
		}
		return false;
	}
	
}
