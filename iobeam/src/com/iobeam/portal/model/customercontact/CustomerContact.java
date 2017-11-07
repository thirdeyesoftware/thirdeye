package com.iobeam.portal.model.customercontact;


import java.util.Collection;
import java.util.Vector;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.ParseException;
import com.iobeam.portal.util.*;
import com.iobeam.portal.model.address.*;
import com.iobeam.portal.model.billing.BillingDeliveryType;


public class CustomerContact implements java.io.Serializable {

	private CustomerContactPK mPK;
	private ContactName mContactName;
	private Address mAddress;
	private String mPhoneNumber;
	private String mFaxNumber;
	private String mEmailAddress;
	private BillingDeliveryType mBillingDeliveryType;

	private Collection mDeletedAddressPKs = new Vector();


	public CustomerContact(ContactName contactName,
			Address address, String phoneNumber, String faxNumber,
			String emailAddress)
			throws ParseException {

		this(null, contactName, address, phoneNumber, faxNumber, emailAddress,
			null);
	}


	public CustomerContact(ContactName contactName,
			Address address, String phoneNumber, String faxNumber,
			InternetAddress emailAddress) {

		this(null, contactName, address, phoneNumber, faxNumber, emailAddress,
			null);
	}


	public CustomerContact(CustomerContactPK pk,
			ContactName contactName, Address address,
			String phoneNumber, String faxNumber, String emailAddress,
			BillingDeliveryType deliveryType)
			throws ParseException {

		this(pk, contactName, address, phoneNumber, faxNumber,
				new InternetAddress(emailAddress), deliveryType);
	}


	public CustomerContact(CustomerContactPK pk,
			ContactName contactName, Address address,
			String phoneNumber, String faxNumber,
			InternetAddress emailAddress, BillingDeliveryType deliveryType) {

		mPK = pk;

		mContactName = contactName;
		if (contactName == null) {
			throw new NullPointerException("contactName");
		}

		mAddress = address;
		if (address == null) {
			throw new NullPointerException("address");
		}

		mPhoneNumber = phoneNumber;
		if (phoneNumber == null) {
			throw new NullPointerException("phoneNumber");
		}

		mFaxNumber = faxNumber;

		mEmailAddress = emailAddress.getAddress();


		if (deliveryType == null) {
			if (emailAddress == null) {
				deliveryType = BillingDeliveryType.REGULAR_MAIL;
			} else {
				deliveryType = BillingDeliveryType.EMAIL;
			}
		}

		mBillingDeliveryType = deliveryType;
	}



	void setPK(CustomerContactPK pk) {
		if (pk == null) {
			throw new NullPointerException("pk");
		}
		mPK = pk;
	}

	public CustomerContactPK getPK() {
		return mPK;
	}


	public ContactName getContactName() {
		return mContactName;
	}

	public void setContactName(ContactName name) {
		mContactName = name;
	}


	public Address getAddress() {
		return mAddress;
	}


	public void setAddress(Address address) {
		if (mAddress != null) {
			AddressPK addressPK = mAddress.getPK();
			if (addressPK != null &&
					!addressPK.equals(address.getPK())) {
				mDeletedAddressPKs.add(addressPK);
			}
		}

		mAddress = address;
		if (address == null) {
			throw new NullPointerException("address");
		}
	}

	public void setBillingDeliveryType(BillingDeliveryType type) {
		mBillingDeliveryType = type;
	}
	public BillingDeliveryType getBillingDeliveryType() {
		return mBillingDeliveryType;
	}

	Collection getDeletedAddressPKs() {
		return mDeletedAddressPKs;
	}


	void resetDeletedAddressPKs() {
		mDeletedAddressPKs.clear();
	}


	public String getPhoneNumber() {
		return mPhoneNumber;
	}

	public void setPhoneNumber(String num) {
		mPhoneNumber = num;
	}


	public String getFaxNumber() {
		return mFaxNumber;
	}

	public void setFaxNumber(String s) {
		mFaxNumber = s;
	}


	public String getEmailAddress() {
		return mEmailAddress;
	}

	public void setEmailAddress(String s) {
		mEmailAddress = s;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer(0);
		sb.append("CustomerContact(");
		sb.append(getPK()).append(",");
		sb.append(getContactName()).append(",");
		sb.append(getAddress()).append(",");
		sb.append("email=").append(getEmailAddress()).append(",");
		sb.append("phone=").append(getPhoneNumber()).append(",");
		sb.append("fax=").append(getFaxNumber()).append(",");
		sb.append(getBillingDeliveryType());
		sb.append(")");

		return sb.toString();
	}
}
