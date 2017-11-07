package com.iobeam.portal.model.subscription;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Collection;

public class SubscriptionCategory implements java.io.Serializable {

	private static Hashtable mInstanceHash = new Hashtable();
	private static Vector mInstances = new Vector();

	public static final SubscriptionCategory MEMBER =
			new SubscriptionCategory("Member", 1);

	public static final SubscriptionCategory VENUE =
			new SubscriptionCategory("Venue", 2);

	private String mName;
	private int mID;

	private SubscriptionCategory(String name, int id) {
		mName = name;
		if (name == null) {
			throw new NullPointerException("name");
		}
		try {
			mID = id;
			mInstanceHash.put(new Integer(mID), this);
			mInstances.addElement(this);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String getName() {
		return mName;
	}

	public static SubscriptionCategory getInstanceFor(int id) {
		return (SubscriptionCategory)mInstanceHash.get(new Integer(id));
	}

	public static Collection getInstances() {
		return mInstances;
	}

	public int getID() {
		return mID;
	}

	public int hashCode() {
		return getID();
	}

	public boolean equals(Object o) {
		if (o instanceof SubscriptionCategory) {
			SubscriptionCategory c = (SubscriptionCategory) o;

			return getName().equals(c.getName()) &&
					getID() == c.getID();
		} else {
			return false;
		}
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("SubscriptionCategory(");

		sb.append(getName()).append(",");
		sb.append(",");
		sb.append(getID());
		sb.append(")");

		return sb.toString();
	}
}
