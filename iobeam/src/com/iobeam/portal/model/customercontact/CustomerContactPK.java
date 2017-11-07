package com.iobeam.portal.model.customercontact;


public class CustomerContactPK implements java.io.Serializable {

	private long mID;


	public CustomerContactPK(long id) {
		mID = id;
	}


	public long getID() {
		return mID;
	}


	public int hashCode() {
		return (int) ((mID >> 32) ^ mID);
	}


	public boolean equals(Object o) {
		if (o instanceof CustomerContactPK) {
			CustomerContactPK pk = (CustomerContactPK) o;
			return getID() == pk.getID();
		} else {
			return false;
		}
	}


	public String toString() {
		return "CustomerContactPK(" + getID() + ")";
	}
}
