package telesync.pui.tsi5320;

import java.io.*;
import java.util.Hashtable;
import java.util.Vector;

public class TxMode implements Serializable {
	
	private static Vector cInstances = new Vector();
	private static Hashtable cInstanceHash = new Hashtable();

	static public final TxMode US_SONET = new TxMode("US SONET", 0);
	static public final TxMode EURO_SDH = new TxMode("EURO SDH", 1);
	
	String mName;
	Integer mValue;
	
	static public TxMode getInstanceFor(Integer id) {
		return (TxMode) cInstanceHash.get(id);
	}

	static public Vector getInstances() {
		return cInstances;
	}

	protected TxMode(String name, int value) {
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