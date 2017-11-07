package com.iobeam.portal.model.subscription;


import java.util.Hashtable;
import java.util.Vector;
import java.util.Collection;
import com.iobeam.portal.model.prototype.subscription.*;


public class SubscriptionType implements java.io.Serializable {

	private static Hashtable mInstanceHash = new Hashtable();
	private static Vector mInstances = new Vector();


	public static final SubscriptionType PUBLIC_MEMBER =
			new SubscriptionType("PublicMember", null,
			new SubscriptionPrototypePK(3), 1);

	public static final SubscriptionType PUBLIC_VENUE =
			new SubscriptionType("PublicVenue", PUBLIC_MEMBER,
			null, 2);



	public static final SubscriptionType PRIVATE_MEMBER =
			new SubscriptionType("PrivateMember", null,
			new SubscriptionPrototypePK(5), 5);

	public static final SubscriptionType PRIVATE_VENUE =
			new SubscriptionType("PrivateVenue", PRIVATE_MEMBER,
			null, 3);



	public static final SubscriptionType VENUE_OPERATOR =
			new SubscriptionType("VenueOperator", null,
			new SubscriptionPrototypePK(6), 6);

	public static final SubscriptionType VENUE_STAFF =
			new SubscriptionType("VenueStaff", VENUE_OPERATOR,
			new SubscriptionPrototypePK(2), 4);


	public static final SubscriptionType EQUIPMENT_LEASE =
			new SubscriptionType("EquipmentLease", null, null, 7);


	public static final SubscriptionType WEB_HOSTING =
			new SubscriptionType("WebHosting", null,
			new SubscriptionPrototypePK(8), 8);

	public static final SubscriptionType WISP_OPERATOR =
			new SubscriptionType("WebHosting", null,
			new SubscriptionPrototypePK(9), 9);
	
	public static final SubscriptionType CUSTOMER_SUPPORT_OPERATOR = 
			new SubscriptionType("CustomerSupportOperator", null,
			new SubscriptionPrototypePK(10), 10);

	private String mName;
	private SubscriptionType mGeneratedType;
	private int mID;
	private SubscriptionPrototypePK mDefaultSubscriptionPrototypePK = null;

	SubscriptionType(String name, SubscriptionType generatedType,
			SubscriptionPrototypePK defaultSubscriptionPrototypePK, int id) {
		mName = name;
		if (name == null) {
			throw new NullPointerException("name");
		}
		try {
		
			mGeneratedType = generatedType;

			mID = id;
			mInstanceHash.put(new Integer(mID), this);
			mInstances.addElement(this);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		mDefaultSubscriptionPrototypePK = defaultSubscriptionPrototypePK;
	}

	public String getName() {
		return mName;
	}

	public static SubscriptionType getInstanceFor(int id) {
		return (SubscriptionType)mInstanceHash.get(new Integer(id));
	}

	public static Collection getInstances() {
		return mInstances;
	}

	public boolean isGenerative() {
		return getGeneratedType() != null;
	}

	public SubscriptionType getGeneratedType() {
		return mGeneratedType;
	}


	/**
	* Returns the SubscriptionPrototyupePK of the default
	* SubscriptionPrototype of this type, or null if there is none.
	*/
	public SubscriptionPrototypePK getDefaultSubscriptionPrototypePK() {
		return mDefaultSubscriptionPrototypePK;
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
		if (o instanceof SubscriptionType) {
			SubscriptionType t = (SubscriptionType) o;

			return getName().equals(t.getName()) &&
					getID() == t.getID();
		} else {
			return false;
		}
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("SubscriptionType(");

		sb.append(getName()).append(",");
		sb.append(isGenerative() ? "generative" : "nongenerative");
		sb.append(",");

		if (getGeneratedType() != null) {
			sb.append("generates ").append(
					getGeneratedType().getName()).append(",");
		}

		sb.append(getDefaultSubscriptionPrototypePK()).append(",");
		sb.append(getID());
		sb.append(")");

		return sb.toString();
	}
}
