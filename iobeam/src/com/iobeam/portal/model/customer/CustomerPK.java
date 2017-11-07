package com.iobeam.portal.model.customer;


public class CustomerPK implements java.io.Serializable {

	private long mID;


	public CustomerPK(long id) {
		mID = id;
	}


	public long getID() {
		return mID;
	}


	public int hashCode() {
		return (int) ((mID >> 32) ^ mID);
	}


	public boolean equals(Object o) {
		if (o instanceof CustomerPK) {
			CustomerPK pk = (CustomerPK) o;
			return getID() == pk.getID();
		} else {
			return false;
		}
	}


	public String toString() {
		return "CustomerPK(" + getID() + ")";
	}
}
