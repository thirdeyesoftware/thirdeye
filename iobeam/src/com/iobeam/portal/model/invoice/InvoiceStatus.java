package com.iobeam.portal.model.invoice;

import java.util.Vector;
import java.util.Collection;
import java.util.Hashtable;
import java.io.Serializable;

public class InvoiceStatus  implements Serializable {
	public static final long serialVersionUID = 2003042204L;

	private static Hashtable cInstanceHash = new Hashtable();
	private static Vector mInstances = new Vector();
	private String mName;
	private int mID;
	
	public static final InvoiceStatus INVOICE_OPEN = 	
		new InvoiceStatus(1, "open");
		
	public static final InvoiceStatus INVOICE_PENDING_PROCESS = 
		new InvoiceStatus(2, "pending");

	public static final InvoiceStatus INVOICE_CLOSED = 
		new InvoiceStatus(3, "closed");
	
	public InvoiceStatus(int id, String name) {
		mID = id;
		mName = name;
		cInstanceHash.put(new Integer(mID), this);
		mInstances.addElement(this);
	}

	public String getName() {
		return mName;
	}

	public int getID() {
		return mID;
	}
	
	public static InvoiceStatus getInstanceFor(int id) {
		return (InvoiceStatus) cInstanceHash.get(new Integer(id));
	}

	public static Collection getInstances() {
		return mInstances;
	}

	public boolean equals(Object o) {
		if (o instanceof InvoiceStatus) {
			return (((InvoiceStatus)o).getID() == getID());
		}
		return false;
	}

	public int hashCode() {
		return (17 * mID);
	}
	public String toString() {
		return "InvoiceStatus(" + mID + "," + mName+ ")";
	}


}

