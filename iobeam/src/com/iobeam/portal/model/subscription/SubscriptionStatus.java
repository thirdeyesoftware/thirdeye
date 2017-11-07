package com.iobeam.portal.model.subscription;


import java.util.Hashtable;
import java.util.Vector;
import java.util.Collection;
import java.io.Serializable;

/**
 * enumeration that represents the possible status values for an Subscription.
 */
 
public class SubscriptionStatus  implements Serializable {
	public static final long serialVersionUID = 2003042103L;
	
	private static Vector cInstances = new Vector();
	private static Hashtable cInstanceHash = new Hashtable();

	private String mName;
	private int mID;


	/**
	* Indicates a newly created Subscription that is not yet active.
	* Subscriptions in the CREATED state are typically 'inventory'
	* waiting for an owner.
	*/
	public static final SubscriptionStatus CREATED = 
			new SubscriptionStatus(1, "created");

	/**
	* Indicates a Subscription that is currently usable and in use.
	*/
	public static final SubscriptionStatus ACTIVE = 
			new SubscriptionStatus(2, "active");
			
	/**
	* Indicates a Subscription that has been turned off and can
	* not be used.
	*/
	public static final SubscriptionStatus CLOSED = 
			new SubscriptionStatus(3, "closed");

	/**
	*/
	public static final SubscriptionStatus SUSPENDED = 
			new SubscriptionStatus(4, "suspended");



	private SubscriptionStatus(int id, String name) {
		mID = id;
		mName = name;
		cInstanceHash.put(new Integer(id), this);
		cInstances.addElement(this);
		
	}

	public static SubscriptionStatus getInstanceFor(int l) {
		return (SubscriptionStatus)cInstanceHash.get(new Integer(l));
	}

	public int getID() {
		return mID;
	}

	public String getName() {
		return mName;
	}
	
	public static Collection getInstances() {
		return cInstances;
	}

	public String toString() {
		return "SubscriptionStatus(" + mID + "," + mName + ")";
	}

	public boolean equals(Object o) {
		if (o instanceof SubscriptionStatus) {
			return (((SubscriptionStatus)o).getID() == getID());
		}
		return false;
	}

	public int hashCode() {
		return (int)((mID >> 32) ^ mID);
	}
	
}
