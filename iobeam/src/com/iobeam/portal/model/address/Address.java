package com.iobeam.portal.model.address;


import com.iobeam.portal.util.*;
import com.iobeam.portal.model.country.*;


public class Address implements java.io.Serializable {

	private AddressPK mPK;
	private MailingAddress mMailingAddress;
	private Country mCountry;
	

	public Address(MailingAddress mailingAddress, Country country) {
		this(null, mailingAddress, country);
	}


	public Address(AddressPK pk, MailingAddress mailingAddress,
			Country country) {

		mPK = pk;

		mMailingAddress = mailingAddress;
		if (mailingAddress == null) {
			throw new NullPointerException("mailingAddress");
		}

		mCountry = country;
		if (country == null) {
			throw new NullPointerException("country");
		}
	}


	public AddressPK getPK() {
		return mPK;
	}


	void setPK(AddressPK pk) {
		if (pk == null) {
			throw new NullPointerException("pk");
		}
		mPK = pk;
	}


	public MailingAddress getMailingAddress() {
		return mMailingAddress;
	}

	public void setMailingAddress(MailingAddress ma) {
		mMailingAddress = ma;
	}

	public Country getCountry() {
		return mCountry;
	}

	public void setCountry(Country country) {
		mCountry = country;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("Address(");

		sb.append(getPK()).append(",");
		sb.append(getMailingAddress()).append(",");
		sb.append(getCountry());

		sb.append(")");

		return sb.toString();
	}
}
