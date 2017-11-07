package com.iobeam.portal.model.billing;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Collection;

import java.io.Serializable;
/**
 * describes the possible payment instrument types in the system.
 */
public class PaymentInstrumentType implements Serializable {

	public static final long serialVersionUID = 200304170101L;

	private static Hashtable mHash = new Hashtable();
	private static Vector mInstances = new Vector();
	
	private int mID;
	private String mDescription;
	
	public static final PaymentInstrumentType CREDIT_CARD = 
		new PaymentInstrumentType(1, "CREDIT CARD");
	
	public static final PaymentInstrumentType ELECTRONIC_CHECK = 
		new PaymentInstrumentType(2, "ELECTRONIC CHECK");
	
	public static final PaymentInstrumentType ACH_TRANSFER = 
		new PaymentInstrumentType(3, "ACH_TRANSFER");
	
	public static final PaymentInstrumentType CHECK = 
		new PaymentInstrumentType(4, "CHECK");
	
	public static final PaymentInstrumentType UNKNOWN = 
		new PaymentInstrumentType(5, "UNKNOWN");

	private PaymentInstrumentType(int id, String desc) {
		mID = id;
		mDescription = desc;
		mHash.put(new Integer(id), this);
		mInstances.addElement(this);
	}

	public String getDescription() {
		return mDescription;
	}
	
	public int getID() {
		return mID;
	}

	public String toString() {
		return "PaymentInstrumentType(" + mID + "," + mDescription + ")";
	}

	public boolean equals(Object o) {
		if (o instanceof PaymentInstrumentType) {
			return (((PaymentInstrumentType)o).getID() == getID());
		}
		return false;
	}

	public static PaymentInstrumentType getInstanceFor(int id) {
		return (PaymentInstrumentType)mHash.get((new Integer(id)));
	}

	public static Collection getInstances() {
		return mInstances;
	}
}


		

