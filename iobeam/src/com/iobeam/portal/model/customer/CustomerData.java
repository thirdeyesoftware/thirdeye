package com.iobeam.portal.model.customer;


import java.util.Collection;
import java.util.Vector;
import java.util.Date;
import com.iobeam.portal.util.*;
import com.iobeam.portal.model.customercontact.*;


public class CustomerData implements java.io.Serializable {

	private CustomerPK mPK;
	private CustomerPK mParentCustomerPK;
	private CustomerContact mCustomerContact;
	private boolean mIsActive;
	private Date mCreateDate;

	private Collection mDeletedCustomerContacts = new Vector();


	CustomerData(CustomerContact customerContact,
			CustomerPK parentCustomerPK, boolean isActive) {

		mPK = null;

		mParentCustomerPK = parentCustomerPK;

		mCustomerContact = customerContact;
		if (customerContact == null) {
			throw new NullPointerException("customerContact");
		}

		mIsActive = isActive;

		mCreateDate = null;

	}


	CustomerData(CustomerPK pk, CustomerPK parentCustomerPK,
			CustomerContact customerContact, boolean isActive, Date createDate) {

		mPK = pk;
		if (pk == null) {
			throw new NullPointerException("pk");
		}

		mParentCustomerPK = parentCustomerPK;

		mCustomerContact = customerContact;
		if (customerContact == null) {
			throw new NullPointerException("customerContact");
		}

		mIsActive = isActive;

		mCreateDate = createDate;
	}


	public CustomerPK getPK() {
		return mPK;
	}


	void setPK(CustomerPK pk) {
		mPK = pk;
		if (pk == null) {
			throw new NullPointerException("pk");
		}
	}

	public void setParentCustomerPK(CustomerPK pk) {
		mParentCustomerPK = pk;
	}

	public CustomerPK getParentCustomerPK() {
		return mParentCustomerPK;
	}


	public void setCustomerContact(CustomerContact customerContact) {
		if (mCustomerContact != null) {
			CustomerContactPK customerContactPK = mCustomerContact.getPK();
			if (customerContactPK != null &&
					!customerContactPK.equals(customerContact.getPK())) {
				mDeletedCustomerContacts.add(mCustomerContact);
			}
		}

		mCustomerContact = customerContact;
		if (customerContact == null) {
			throw new NullPointerException("customerContact");
		}
	}


	public CustomerContact getCustomerContact() {
		return mCustomerContact;
	}


	Collection getDeletedCustomerContacts() {
		return mDeletedCustomerContacts;
	}


	void resetDeletedCustomerContacts() {
		mDeletedCustomerContacts.clear();
	}


	public boolean isActive() {
		return mIsActive;
	}


	public ContactName getContactName() {
		return getCustomerContact().getContactName();
	}


	public String getPhoneNumber() {
		return getCustomerContact().getPhoneNumber();
	}


	public MailingAddress getMailingAddress() {
		return getCustomerContact().getAddress().getMailingAddress();
	}


	public String getEmailAddress() {
		return getCustomerContact().getEmailAddress();
	}


	public void setCreateDate(Date date) {
		mCreateDate = date;
	}
	public Date getCreateDate() {
		return mCreateDate;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("CustomerData(");

		sb.append(getPK()).append(",");
		sb.append(getParentCustomerPK()).append(",");
		sb.append(getCustomerContact()).append(",");
		if (isActive()) {
			sb.append("active");
		} else {
			sb.append("inactive");
		}
		if (mCreateDate != null) {
			sb.append(", " + mCreateDate.toString());
		}
		
		sb.append(")");

		return sb.toString();
	}
}
