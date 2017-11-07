package com.iobeam.portal.model.country;


public class CountryPK implements java.io.Serializable {

	private long mID;


	public CountryPK(long id) {
		mID = id;
	}


	public long getID() {
		return mID;
	}


	public int hashCode() {
		return (int) ((mID >> 32) ^ mID);
	}


	public boolean equals(Object o) {
		if (o instanceof CountryPK) {
			CountryPK pk = (CountryPK) o;
			return getID() == pk.getID();
		} else {
			return false;
		}
	}


	public String toString() {
		return "CountryPK(" + getID() + ")";
	}
}
