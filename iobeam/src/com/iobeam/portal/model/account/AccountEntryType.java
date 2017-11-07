package com.iobeam.portal.model.account;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Collection;

public class AccountEntryType implements java.io.Serializable {
	public static final long serialVersionUID = 2003042101L;
	
	private static Hashtable cInstanceHash = new Hashtable();
	private static Vector cInstances = new Vector();
	
	public static final AccountEntryType PAYMENT =
			new AccountEntryType("Payment", 1);

	public static final AccountEntryType PURCHASE = 
			new AccountEntryType("Purchase", 2);

	public static final AccountEntryType BALANCE_FORWARD = 
			new AccountEntryType("Balance Forward", 0);
	
	public static final AccountEntryType SALES_TAX = 
			new AccountEntryType("Tax", 3);

	private String mName;
	private int mID;
	
	private AccountEntryType(String name, int id) {
		mName = name;
		if (name == null) {
			throw new NullPointerException("name");
		}

		mID = id;
		cInstanceHash.put(new Integer(id), this);
		cInstances.addElement(this);
	}

	public static AccountEntryType getInstanceFor(int i) {
		return (AccountEntryType)cInstanceHash.get(new Integer(i));
	}

	public static Collection getInstances() {
		return cInstances;
	}

	public String getName() {
		return mName;
	}


	public int getID() {
		return mID;
	}


	public int hashCode() {
		int h = 17;

		h = 37 * h + getName().hashCode();
		h = 37 * h + getID();

		return h;
	}


	public boolean equals(Object o) {
		if (o instanceof AccountEntryType) {
			AccountEntryType t = (AccountEntryType) o;

			return getName().equals(t.getName()) &&
					getID() == t.getID();
		} else {
			return false;
		}
	}


	public String toString() {
		StringBuffer sb = new StringBuffer("AccountEntryType(");

		sb.append(getName()).append(",");
		sb.append(getID());
		sb.append(")");

		return sb.toString();
	}
}
