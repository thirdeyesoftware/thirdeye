package com.iobeam.portal.model.billing;

import java.io.Serializable;
import java.util.Collection;
import java.text.SimpleDateFormat;

import com.iobeam.portal.model.account.*;
import com.iobeam.portal.model.invoice.Invoice;

/**
 * Summary of billable activity on an account and payments on the same account
 * over a billing period.
 */

public class Statement implements Serializable {

	public static final long serialVersionUID = 200304170104L;

	private Invoice mInvoice;
	private Collection mPayments;
	private AccountPK mAccountPK;
	private BillingPeriod mPeriod;

	public Statement(AccountPK accountPK, BillingPeriod period, 
			Invoice invoice, Collection otherActivity) {
		mAccountPK = accountPK;
		mInvoice = invoice;
		mPayments = otherActivity;
		mPeriod = period;
	}

	public Statement(AccountPK pk, BillingPeriod period, Invoice invoice) {
		this(pk, period, invoice, null);
	}

	public AccountPK getAccountPK() {
		return mAccountPK;
	}

	public Invoice getInvoice() {
		return mInvoice;
	}

	public Collection getPayments() {
		return mPayments;
	}
	public BillingPeriod getBillingPeriod() {
		return mPeriod;
	}

	public String toString() {
		return "Statement: " + mAccountPK + 
			", " + mInvoice.toString() + ", " + 
			mPeriod.toString();
	}

	public String toEmailMessage() {
		StringBuffer sb = new StringBuffer(0);
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mi:ss");

		sb.append("Here is your iobeam statement for:\t\t ");
		sb.append(format.format(getBillingPeriod().getStartDate()));
		sb.append(" to ");
		sb.append(format.format(getBillingPeriod().getEndDate()));
		return sb.toString();
	}

}



