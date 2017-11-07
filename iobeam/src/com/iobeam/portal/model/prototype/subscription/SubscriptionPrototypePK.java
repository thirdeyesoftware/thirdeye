package com.iobeam.portal.model.prototype.subscription;


public class SubscriptionPrototypePK implements java.io.Serializable {

	private long mID;


	public SubscriptionPrototypePK(long id) {
		mID = id;
	}


	public long getID() {
		return mID;
	}


	public int hashCode() {
		return (int) ((mID >> 32) ^ mID);
	}


	public boolean equals(Object o) {
		if (o instanceof SubscriptionPrototypePK) {
			SubscriptionPrototypePK pk = (SubscriptionPrototypePK) o;
			return getID() == pk.getID();
		} else {
			return false;
		}
	}


	public String toString() {
		return "SubscriptionPrototypePK(" + getID() + ")";
	}
}
