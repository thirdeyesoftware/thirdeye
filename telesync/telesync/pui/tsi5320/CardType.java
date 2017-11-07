package telesync.pui.tsi5320;


import java.util.*;


public class CardType {
	private static Vector cTypes = new Vector();
	private static Hashtable cTypeHash = new Hashtable();

	// This constant is defined for consistency in the GUI.
	public static final CardType SYSTEM =
			new CardType(0xc00, "System");

	// This constant is defined for consistency in the GUI.
	public static final CardType REF_CLOCK =
			new CardType(0xc01, "Reference Clock", "Reference<br>Clock");

	public static final CardType OC48_TRANSMIT =
			new CardType(0x000, "OC48 Transmit", "OC-48<br>Transmit");
	public static final CardType OC48_RECEIVE =
			new CardType(0x001, "OC48 Receive", "OC-48<br>Receive");
	public static final CardType OC192_TRANSMIT = 
			new CardType(0x002, "OC192 Transmit", "OC-192<br>Tansmit");
	public static final CardType OC192_RECEIVE = 
			new CardType(0x003, "OC192 Receive", "OC-192<br>Receive");
	public static final CardType NOT_PRESENT =
			new CardType(0x100, "<HTML><font size=1 face=\"arial\" color=\"#6C6C6C\">Empty</font></html>");
	public static final CardType BOOT_IN_PROCESS =
			new CardType(0x101, "Booting");
	public static final CardType INVALID =
			new CardType(0x102, "Invalid");

	private Integer mID;
	private String mName;
	private String mHTML = null;



	protected CardType(int id, String name) {
		this(id, name, null);
	}

	protected CardType(int id, String name, String html) {
		mID = new Integer(id);
		mName = name;
		mHTML = html;

		if (!cTypes.contains(this)) {
			cTypes.addElement(this);
			cTypeHash.put(mID, this);
		}
	}

	public int getCardID() {
		return mID.intValue();
	}

	public String getCardName() {
		return mName;
	}

	public String getText() {
		if (mHTML != null) {
			return mHTML;
		} else {
			return mName;
		}
	}

	public static Enumeration getTypes() {
		return cTypes.elements();
	}

	public static CardType getTypeFor(Integer id) {
		return (CardType) cTypeHash.get(id);
	}

	public boolean equals(Object o) {
		if (o instanceof CardType) {
			return ((CardType) o).getCardID() == getCardID();
		} else {
			return false;
		}
	}

	public int hashCode() {
		return mID.hashCode();
	}

	public String toString() {
		return "CardType(" + getCardName() + "," +
				Integer.toHexString(mID.intValue()) + ")";
	}
}
