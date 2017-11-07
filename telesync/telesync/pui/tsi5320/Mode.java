package telesync.pui.tsi5320;

import java.io.*;
import java.util.Hashtable;
import java.util.Vector;

public class Mode implements Serializable {
	
	private static Vector cInstances = new Vector();
	private static Hashtable cInstanceHash = new Hashtable();

	static public final Mode US_SONET = new Mode("US SONET", 0);
	static public final Mode EURO_SDH = new Mode("EURO SDH", 1);
	
	String mName;
	Integer mValue;
	
	static public Mode getInstanceFor(Integer id) {
		return (Mode) cInstanceHash.get(id);
	}

	static public Vector getInstances() {
		return cInstances;
	}

	protected Mode(String name, int value) {
		mName = name;
		mValue = new Integer(value);

		cInstances.addElement(this);
		cInstanceHash.put(mValue, this);
	}

	public String toString() {
		return mName;
	}

	public Integer getValue() {
		return mValue;
	}
}