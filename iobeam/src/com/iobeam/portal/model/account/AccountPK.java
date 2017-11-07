package com.iobeam.portal.model.account;


public class AccountPK implements java.io.Serializable {

	private long mID;

	public AccountPK(long id) {
		mID = id;
	}

	public long getID() {
		return mID;
	}

	public int hashCode() {
		return (int)((mID >> 32) ^ mID);
		
	}

	public boolean equals(Object o) {
		if (o instanceof AccountPK) {
			AccountPK pk = (AccountPK) o;
			return pk.getID() == getID();
		} else {
			return false;
		}
	}

	public String toString() {
		return "AccountPK(" + getID() + ")";
	}
	
}
