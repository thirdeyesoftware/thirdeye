package com.iobeam.portal.model.venue;


import com.iobeam.portal.model.customer.CustomerPK;
import com.iobeam.portal.security.PasswordHash;


public class VenueData implements Venue, java.io.Serializable {

	private VenuePK mPK;
	private String mVenueName;
	private CustomerPK mCustomerPK;
	private VenueType mVenueType;
	private long mSiteKey;
	private String mRedirectUrl;

	public VenueData(String venueName, CustomerPK customerPK,
			VenueType venueType) {

		mVenueName = venueName;
		if (venueName == null) {
			throw new NullPointerException("venueName");
		}

		mCustomerPK = customerPK;
		if (customerPK == null) {
			throw new NullPointerException("customerPK");
		}

		mVenueType = venueType;
		if (venueType == null) {
			throw new NullPointerException("venueType");
		}

		mSiteKey = PasswordHash.getSiteKey();

	}


	VenueData(VenuePK pk, String venueName, CustomerPK customerPK,
			VenueType venueType, long siteKey) {

		mPK = pk;
		if (pk == null) {
			throw new NullPointerException("pk");
		}

		mVenueName = venueName;
		if (venueName == null) {
			throw new NullPointerException("venueName");
		}

		mCustomerPK = customerPK;
		if (customerPK == null) {
			throw new NullPointerException("customerPK");
		}

		mVenueType = venueType;
		if (venueType == null) {
			throw new NullPointerException("venueType");
		}

		mSiteKey = siteKey;
	}


	public void setRedirectUrl(String s) {
		mRedirectUrl = s;
	}

	public String getRedirectUrl() {
		return mRedirectUrl;
	}

	void setPK(VenuePK pk) {
		mPK = pk;
		if (pk == null) {
			throw new NullPointerException("pk");
		}
	}


	public VenuePK getPK() {
		return mPK;
	}


	public String getVenueName() {
		return mVenueName;
	}


	/**
	* Returns the pk for the Customer instance associated with this
	* Venue.  All Venues are also Customers.
	*/
	public CustomerPK getCustomerPK() {
		return mCustomerPK;
	}


	/**
	*/
	public VenueType getVenueType() {
		return mVenueType;
	}


	/**
	* Returns a random key used for generating and validating
	* keys and other secure Strings associated with this Venue.
	*/
	public long getSiteKey() {
		return mSiteKey;
	}


	public String toString() {
		StringBuffer sb = new StringBuffer("VenueData(");

		sb.append(getPK()).append(",");
		sb.append(getVenueName()).append(",");
		sb.append(getSiteKey()).append(",");
		sb.append(getVenueType()).append(",");
		sb.append(getCustomerPK());

		sb.append(")");

		return sb.toString();
	}
}
