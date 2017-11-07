package com.iobeam.portal.model.user;


public class UserPK implements java.io.Serializable {

	private long mID;


	public UserPK(long id) {
		mID = id;
	}


	public long getID() {
		return mID;
	}


	public int hashCode() {
		return (int) ((mID >> 32) ^ mID);
	}


	public boolean equals(Object o) {
		if (o instanceof UserPK) {
			UserPK pk = (UserPK) o;
			return getID() == pk.getID();
		} else {
			return false;
		}
	}


	public String toString() {
		return "UserPK(" + getID() + ")";
	}
}
