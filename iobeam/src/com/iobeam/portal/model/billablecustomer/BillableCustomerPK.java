package com.iobeam.portal.model.billablecustomer;


import com.iobeam.portal.model.customer.CustomerPK;


public class BillableCustomerPK implements java.io.Serializable {

	private CustomerPK mCustomerPK;

	public BillableCustomerPK(long id) {
		mCustomerPK = new CustomerPK(id);
	}


	public BillableCustomerPK(CustomerPK customerPK) {

		mCustomerPK = customerPK;
		if (customerPK == null) {
			throw new NullPointerException("customerPK");
		}
	}


	public long getID() {
		return mCustomerPK.getID();
	}


	public CustomerPK getCustomerPK() {
		return mCustomerPK;
	}


	public int hashCode() {
		return mCustomerPK.hashCode();
	}


	public boolean equals(Object o) {
		if (o instanceof BillableCustomerPK) {
			BillableCustomerPK pk = (BillableCustomerPK) o;
			return getID() == pk.getID();
		} else {
			return false;
		}
	}


	public String toString() {
		return "BillableCustomerPK(" + getID() + ")";
	}
}
