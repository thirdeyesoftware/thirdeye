package com.iobeam.portal.model.subscription;

import java.util.*;

public class BillingCycle implements java.io.Serializable {
	
	private static Hashtable mInstanceHash = new Hashtable();
	private static Vector mInstances = new Vector();



	public static final BillingCycle MONTHLY =
			new BillingCycle("Monthly", 1);

	public static final BillingCycle QUARTERLY =
			new BillingCycle("Quarterly", 2);

	public static final BillingCycle YEARLY =
			new BillingCycle("Yearly", 3);

	public static final BillingCycle IMMEDIATE =
			new BillingCycle("Immediate", 4);



	private String mName;
	private int mID;


	private BillingCycle(String name, int id) {
		mName = name;
		if (name == null) {
			throw new NullPointerException("name");
		}

		mID = id;
		mInstanceHash.put(new Integer(mID), this);
		mInstances.addElement(this);
	}


	public String getName() {
		return mName;
	}


	public int getID() {
		return mID;
	}


	public static Collection getInstances() {
		return mInstances;
	}
	
	public static BillingCycle getInstanceFor(int id) {
		return (BillingCycle)mInstanceHash.get(new Integer(id));
	}

	public int hashCode() {
		int h = 17;

		h = 37 * h + getName().hashCode();
		h = 37 * h + getID();

		return h;
	}


	public boolean equals(Object o) {
		if (o instanceof BillingCycle) {
			BillingCycle bc = (BillingCycle) o;

			return getName().equals(bc.getName()) &&
					getID() == bc.getID();
		} else {
			return false;
		}
	}


	public String toString() {
		StringBuffer sb = new StringBuffer("BillingCycle(");

		sb.append(getName()).append(",");
		sb.append(getID());
		sb.append(")");

		return sb.toString();
	}
}
