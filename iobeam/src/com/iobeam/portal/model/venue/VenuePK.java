package com.iobeam.portal.model.venue;


public class VenuePK implements java.io.Serializable {

	private long mID;


	public VenuePK(long id) {
		mID = id;
	}


	public long getID() {
		return mID;
	}


	public int hashCode() {
		return (int) ((mID >> 32) ^ mID);
	}


	public boolean equals(Object o) {
		if (o instanceof VenuePK) {
			VenuePK pk = (VenuePK) o;
			return getID() == pk.getID();
		} else {
			return false;
		}
	}


	public String toString() {
		return "VenuePK(" + getID() + ")";
	}
}
