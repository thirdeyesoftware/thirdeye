package com.iobeam.portal.model.asset;


public class AssetPK implements java.io.Serializable {

	private long mID;


	public AssetPK(long id) {
		mID = id;
	}


	public long getID() {
		return mID;
	}


	public int hashCode() {
		return (int) ((mID >> 32) ^ mID);
	}


	public boolean equals(Object o) {
		if (o instanceof AssetPK) {
			AssetPK pk = (AssetPK) o;
			return getID() == pk.getID();
		} else {
			return false;
		}
	}


	public String toString() {
		return "AssetPK(" + getID() + ")";
	}
}
