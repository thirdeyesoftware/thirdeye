package com.iobeam.portal.model.account;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Collection;
import java.io.Serializable;

/**
 * enumeration that represents the possible status values for an account.
 */
 
public class AccountStatus  implements Serializable {

	private static Vector cInstances = new Vector();
	private static Hashtable cInstanceHash = new Hashtable();

	private String mStatus;
	private long mID;

	public static final AccountStatus OPEN = 
			new AccountStatus(1, "open");
			
	public static final AccountStatus CLOSED = 
			new AccountStatus(2, "closed");

	public static final AccountStatus SUSPENDED = 
			new AccountStatus(3, "suspended");
			
	private AccountStatus(long id, String status) {
		mID = id;
		mStatus = status;
		cInstanceHash.put(new Long(id), this);
		cInstances.addElement(this);
		
	}

	public static AccountStatus getInstanceFor(long l) {
		return (AccountStatus)cInstanceHash.get(new Long(l));
	}

	public long getID() {
		return mID;
	}

	public String getStatus() {
		return mStatus;
	}
	
	public static Collection getInstances() {
		return cInstances;
	}

	public String toString() {
		return "AccountStatus(" + mID + "," + mStatus + ")";
	}

	public boolean equals(Object o) {
		if (o instanceof AccountStatus) {
			return (((AccountStatus)o).getID() == getID());
		}
		return false;
	}

	public int hashCode() {
		return (int)((mID >> 32) ^ mID);
	}
	
}
