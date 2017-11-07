package com.iobeam.portal.model.product;


public class ProductPK implements java.io.Serializable {

	public static final long serialVersionUID = 200304170105L;

	private long mProductID;

	public ProductPK(long id) { 
		mProductID = id;
	}

	public long getID() {
		return mProductID;
	}

	public long getProductID() {
		return mProductID;
	}

	public int hashCode() {
		return (int) ((mProductID >> 32) ^ mProductID);
	}

	public boolean equals(Object o) {
		if (o instanceof ProductPK) {
			ProductPK pk = (ProductPK) o;
			return getProductID() == pk.getProductID();
		} else {
			return false;
		}
	}

	public String toString() {
		return "ProductPK(" + mProductID + ")";
	}
}
