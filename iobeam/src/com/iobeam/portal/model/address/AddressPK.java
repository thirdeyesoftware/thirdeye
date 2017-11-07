package com.iobeam.portal.model.address;


public class AddressPK implements java.io.Serializable {

	private long mID;


	public AddressPK(long id) {
		mID = id;
	}


	public long getID() {
		return mID;
	}


	public int hashCode() {
		return (int) ((mID >> 32) ^ mID);
	}


	public boolean equals(Object o) {
		if (o instanceof AddressPK) {
			AddressPK pk = (AddressPK) o;
			return getID() == pk.getID();
		} else {
			return false;
		}
	}


	public String toString() {
		return "AddressPK(" + getID() + ")";
	}
}
