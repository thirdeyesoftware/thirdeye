package com.iobeam.portal.model.gateway.usercontact;


public class ContactID implements java.io.Serializable {
	private long mID;


	public ContactID(long id) {
		mID = id;
	}


	public long getID() {
		return mID;
	}


	public int hashCode() {
		return (int) ((mID >> 32) ^ mID);
	}


	public boolean equals(Object o) {
		if (o instanceof ContactID) {
			ContactID c = (ContactID) o;
			return getID() == c.getID();
		} else {
			return false;
		}
	}


	public String toString() {
		return "ContactID(" + getID() + ")";
	}
}
