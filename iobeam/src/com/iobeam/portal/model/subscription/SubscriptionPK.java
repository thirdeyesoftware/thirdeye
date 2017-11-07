package com.iobeam.portal.model.subscription;


public class SubscriptionPK implements java.io.Serializable {

	private long mID;


	public SubscriptionPK(long id) {
		mID = id;
	}


	public long getID() {
		return mID;
	}


	public int hashCode() {
		return (int) ((mID >> 32) ^ mID);
	}


	public boolean equals(Object o) {
		if (o instanceof SubscriptionPK) {
			SubscriptionPK pk = (SubscriptionPK) o;
			return getID() == pk.getID();
		} else {
			return false;
		}
	}


	public String toString() {
		return "SubscriptionPK(" + getID() + ")";
	}
}
