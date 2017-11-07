package com.iobeam.portal.model.product;

import java.io.Serializable;

public class ProductCategoryPK implements Serializable {
	
	public static final long serialVersionUID = 200304180133L;

	private long mID;

	public ProductCategoryPK(long id) {
		mID = id;
	}

	public long getID() {
		return mID;
	}

	public boolean equals(Object o) {
		if (o instanceof ProductCategoryPK) {
			ProductCategoryPK pk = (ProductCategoryPK)o;
			return (getID() == pk.getID());
		}
		return false;
	}

	public int hashCode() {
		return (int)((mID >> 32) ^ mID);
	}

	public String toString() {
		return "ProductCategoryPK: (" + mID + ")";
	}

}

