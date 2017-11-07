package com.iobeam.portal.model.billablecustomer;


import java.util.*;
import com.iobeam.portal.util.*;
import com.iobeam.portal.model.customer.*;
import com.iobeam.portal.model.billing.*;
import com.iobeam.portal.model.customercontact.*;


public class BillableCustomerData implements java.io.Serializable {

	private BillableCustomerPK mPK;
	private CustomerPK mParentCustomerPK;
	private PaymentInstrument mPaymentInstrument;
	private CustomerContact mCustomerContact;
	private boolean mIsActive;

	private Collection mDeletedCustomerContacts = new Vector();
	private Collection mDeletedPaymentInstruments = new Vector();



	BillableCustomerData(
			CustomerPK parentCustomerPK,
			CustomerContact customerContact,
			PaymentInstrument paymentInstrument,
			boolean isActive) {

		mPK = null;

		mParentCustomerPK = parentCustomerPK;

		mCustomerContact = customerContact;
		if (customerContact == null) {
			throw new NullPointerException("customerContact");
		}

		mPaymentInstrument = paymentInstrument;
		if (paymentInstrument == null) {
			throw new NullPointerException("paymentInstrument");
		}

		mIsActive = isActive;
	}


	BillableCustomerData(BillableCustomerPK pk,
			CustomerPK parentCustomerPK,
			CustomerContact customerContact,
			PaymentInstrument paymentInstrument,
			boolean isActive) {

		mPK = pk;
		if (pk == null) {
			throw new NullPointerException("pk");
		}

		mParentCustomerPK = parentCustomerPK;

		mCustomerContact = customerContact;
		if (customerContact == null) {
			throw new NullPointerException("customerContact");
		}

		mPaymentInstrument = paymentInstrument;
		if (paymentInstrument == null) {
			throw new NullPointerException("paymentInstrument");
		}

		mIsActive = isActive;
	}


	public BillableCustomerPK getPK() {
		return mPK;
	}

	void setPK(BillableCustomerPK pk) {
		mPK = pk;
		if (pk == null) {
			throw new NullPointerException("pk");
		}
	}


	public CustomerPK getParentCustomerPK() {
		return mParentCustomerPK;
	}


	public CustomerContact getCustomerContact() {
		return mCustomerContact;
	}

	void setCustomerContact(CustomerContact customerContact) {
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


	Collection getDeletedCustomerContacts() {
		return mDeletedCustomerContacts;
	}


	void resetDeletedCustomerContacts() {
		mDeletedCustomerContacts.clear();
	}


	public PaymentInstrument getPaymentInstrument() {
		return mPaymentInstrument;
	}

	void setPaymentInstrument(PaymentInstrument paymentInstrument) {

		if (mPaymentInstrument != null) {

			PaymentInstrumentPK paymentInstrumentPK =
					mPaymentInstrument.getPK();

			if (paymentInstrumentPK != null &&
					!paymentInstrumentPK.equals(paymentInstrument.getPK())) {
				mDeletedPaymentInstruments.add(mPaymentInstrument);
			}
		}

		mPaymentInstrument = paymentInstrument;
		if (paymentInstrument == null) {
			throw new NullPointerException("paymentInstrument");
		}
	}


	Collection getDeletedPaymentInstruments() {
		return mDeletedPaymentInstruments;
	}


	void resetDeletedPaymentInstruments() {
		mDeletedPaymentInstruments.clear();
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


	public boolean isActive() {
		return mIsActive;
	}


	public String toString() {
		StringBuffer sb = new StringBuffer("BillableCustomerData(");

		sb.append(getPK()).append(",");
		sb.append(getParentCustomerPK()).append(",");
		sb.append(getCustomerContact()).append(",");
		if (isActive()) {
			sb.append("active");
		} else {
			sb.append("inactive");
		}

		sb.append(")");

		return sb.toString();
	}
}
