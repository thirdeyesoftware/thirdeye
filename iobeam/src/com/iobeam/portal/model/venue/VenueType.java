package com.iobeam.portal.model.venue;


import java.util.*;


public class VenueType implements java.io.Serializable {


	private static Map cVenueTypesByID = new Hashtable();

	public static final VenueType PUBLIC =
			new VenueType("Public", 1);

	public static final VenueType PRIVATE =
			new VenueType("Private", 2);

	public static final VenueType SEMIPRIVATE =
			new VenueType("Semiprivate", 3);


	private String mName;
	private int mID;

	private VenueType(String name, int id) {
		mName = name;
		if (name == null) {
			throw new NullPointerException("name");
		}

		mID = id;

		cVenueTypesByID.put(new Integer(id), this);
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
		if (o instanceof VenueType) {
			VenueType t = (VenueType) o;

			return getName().equals(t.getName()) &&
					getID() == t.getID();
		} else {
			return false;
		}
	}


	public String toString() {
		StringBuffer sb = new StringBuffer("VenueType(");

		sb.append(getName()).append(",");
		sb.append(getID());
		sb.append(")");

		return sb.toString();
	}


	public static VenueType getVenueType(int id) {
		return (VenueType) cVenueTypesByID.get(new Integer(id));
	}


	public static List getVenueTypes() {
		List l = new ArrayList();

		l.addAll(cVenueTypesByID.values());

		return l;
	}
}
