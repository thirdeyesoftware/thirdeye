package telesync.pui.tsi5320;
import java.util.*;
import java.io.*;

public class Pattern implements Serializable {
	
	private static Vector cInstances = new Vector();
	private static Hashtable cInstanceHash = new Hashtable();
	public  static final int INVERT_OFFSET = 1000;

	static public final Pattern ZEROES = new Pattern("Zeroes", 0);
	static public final Pattern ALTERNATING = new Pattern("101010", 1);
	static public final Pattern PRBS_15 = new Pattern("PRBS 15", 2);
	static public final Pattern PRBS_20 = new Pattern("PRBS 20", 3);
	static public final Pattern PRBS_23 = new Pattern("PRBS 23", 4);

	//static public final Pattern ZEROES_INVERTED = new Pattern("Zeroes - Inverted", 0 + + INVERT_OFFSET, true);
	//static public final Pattern ALTERNATING_INVERTED = new Pattern("101010 - Inverted", 1 + + INVERT_OFFSET, true);
	static public final Pattern PRBS_15_INVERTED = new Pattern("PRBS 15 - Inverted", 2 + INVERT_OFFSET, true);
	static public final Pattern PRBS_20_INVERTED = new Pattern("PRBS 20 - Inverted", 3 + INVERT_OFFSET, true);
	static public final Pattern PRBS_23_INVERTED = new Pattern("PRBS 23 - Inverted", 4 + INVERT_OFFSET, true);


	String mName;
	Integer mValue;
	Boolean mIsInverted;

	static public Pattern getInstanceFor(Integer id) {
		return (Pattern) cInstanceHash.get(id);
	}

	static public Vector getInstances() {
		return cInstances;
	}

	private Pattern(String name, int value) {
		this(name, value,Boolean.FALSE.booleanValue());
	}

	private Pattern(String name, int value, boolean inverted) {
		mName = name;
		mValue = new Integer(value);
		mIsInverted = new Boolean(inverted);
		cInstances.addElement(this);
		cInstanceHash.put(mValue, this);
	}

	public static Pattern getBasePatternFor(Pattern p) {

		if (p.isInverted()) {
			return getInstanceFor(new Integer(p.getValue().intValue()- INVERT_OFFSET));
		}
		else return p;
	}
	public static Pattern getInvertedFor(Pattern p) {
		if (p != null) {
			int id = p.getValue().intValue() + INVERT_OFFSET;
			Pattern pat = getInstanceFor(new Integer(id));
			if (pat == null) {
				System.out.println("Pattern null for id = " + id);
				return p;
			}
			else return pat;
		}
		else return p;

	}

	public String toString() {
		return mName;
	}

	public Integer getValue() {
		return mValue;
	}
	public boolean isInverted() {
		return mIsInverted.booleanValue();
	}




}