package com.iobeam.portal.model.account;


import com.iobeam.portal.util.*;
import com.iobeam.portal.model.customer.CustomerPK;
import com.iobeam.portal.model.billablecustomer.BillableCustomerPK;


public class AccountData implements java.io.Serializable {

	private AccountPK mPK;
	private CustomerPK mCustomerPK;
	private BillableCustomerPK mBillableCustomerPK;
	private AccountNumber mAccountNumber;
	private AccountStatus mAccountStatus;
	
	public AccountData(AccountPK pk) {
		this(pk,null,null,null,null);
	}

	public AccountData(AccountPK pk, CustomerPK custPK, 
			BillableCustomerPK billablePK, AccountNumber number,
			AccountStatus status) {
		mPK = pk;
		if (pk == null) {
			throw new NullPointerException("pk");
		}
		mCustomerPK = custPK;
		mBillableCustomerPK = billablePK;
		mAccountNumber = number;
		mAccountStatus = status;
	}

	public AccountPK getPK() {
		return mPK;
	}

	public AccountNumber getAccountNumber() {
		return mAccountNumber;
	}

	public void setAccountNumber(AccountNumber number) {
		mAccountNumber = number;
	}

	public CustomerPK getCustomerPK() {
		return mCustomerPK;
	}

	public void setCustomerPK(CustomerPK pk) {
		mCustomerPK = pk;
	}

	public BillableCustomerPK getBillableCustomerPK() {
		return mBillableCustomerPK;
	}

	public void setBillableCustomerPK(BillableCustomerPK pk) {
		if (mBillableCustomerPK != null && pk == null) {
			throw new IllegalStateException("cannot demote to " +
					"non-billing account");
		}
		mBillableCustomerPK = pk;
	}

	public AccountStatus getAccountStatus() {
		return mAccountStatus;
	}

	public void setAccountStatus(AccountStatus status) {
		mAccountStatus = status;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("AccountData(");

		sb.append(getPK()).append(",");
		sb.append(getCustomerPK()).append(",");
		sb.append(getBillableCustomerPK()).append(",");
		sb.append(getAccountStatus());

		sb.append(")");

		return sb.toString();
	}

	
}
