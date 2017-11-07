package com.iobeam.portal.model.billing;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Collection;

/**
 * an enumeration of billing delivery types for
 * invoices and statements.
 */

public class BillingDeliveryType implements Serializable {

	private static Hashtable cInstanceHash = new Hashtable();
	private static Vector cInstances = new Vector();
	private String mName;
	private int mID;

	public static final BillingDeliveryType EMAIL = 
			new BillingDeliveryType("EMAIL", 1);
	
	public static final BillingDeliveryType REGULAR_MAIL = 
			new BillingDeliveryType("REGULAR MAIL", 2);
	
	private BillingDeliveryType(String name, int id) {
		mName = name;
		mID = id;
		cInstanceHash.put(new Integer(id), this);
		cInstances.addElement(this);
	}

	public String getName() {
		return mName;
	}
	public int getID() {
		return mID;
	}

	public static Collection getInstances() {
		return cInstances;
	}

	public static BillingDeliveryType getInstanceFor(int id) {
		return (BillingDeliveryType)cInstanceHash.get(new Integer(id));
	}

	public String toString() {
		return "BillingDeliveryType(" + mName + "," + mID + ")";
	}

	public int hashCode() {
		return mName.hashCode();
	}

	public boolean equals(Object o) {
		if (o instanceof BillingDeliveryType) {
			return (((BillingDeliveryType)o).getID() == getID());
		}
		return false;
	}
}
