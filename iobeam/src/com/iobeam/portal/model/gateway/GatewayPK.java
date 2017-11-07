package com.iobeam.portal.model.gateway;


public class GatewayPK implements java.io.Serializable {

	private long mID;


	public GatewayPK(long id) {
		mID = id;
	}


	public long getID() {
		return mID;
	}


	public int hashCode() {
		return (int) ((mID >> 32) ^ mID);
	}


	public boolean equals(Object o) {
		if (o instanceof GatewayPK) {
			GatewayPK pk = (GatewayPK) o;
			return getID() == pk.getID();
		} else {
			return false;
		}
	}


	public String toString() {
		return "GatewayPK(" + getID() + ")";
	}
}
