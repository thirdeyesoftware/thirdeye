package telesync.pui.tsi5320;

import pippin.pui.*;
import java.util.*;
import java.io.*;

public class Oc192c_rx extends PComponent {

	PComponent mTOH,mPOH;
	Hashtable mChildren;

	public Oc192c_rx(PClient client,PComponent parent,
			String name,int id) {
		super(client,parent,name,id,
				new PTypeSet(new String[] {
					"rateEnabled",

					"payloadType",
					"pattern",
					"patternInvert",

					"pointerError",

					"b3Errors",
					"patternErrs",
					"patternLoss",

					"ptrace",
					"C2",
					"G1",
					"F2",
					"H4",
					"Z3",
					"Z4",
					"Z5",
					"rateEnabledUser",

				}, new byte[] {
					PAttribute.TYPE_BOOLEAN,

					PAttribute.TYPE_SHORT,
					PAttribute.TYPE_BYTE,
					PAttribute.TYPE_BOOLEAN,

					PAttribute.TYPE_BOOLEAN,

					PAttribute.TYPE_LONGLONG,
					PAttribute.TYPE_LONGLONG,
					PAttribute.TYPE_BOOLEAN,

					PAttribute.TYPE_STRING,
					PAttribute.TYPE_BYTE,
					PAttribute.TYPE_BYTE,
					PAttribute.TYPE_BYTE,
					PAttribute.TYPE_BYTE,
					PAttribute.TYPE_BYTE,
					PAttribute.TYPE_BYTE,
					PAttribute.TYPE_BYTE,
					PAttribute.TYPE_BOOLEAN,
				}));

		mChildren = new Hashtable();
		for (int i=0 ; i<4 ; i++) {
			int s3 = 16*i + 1;
			mChildren.put("sts48c-" + s3, new StsID(s3,0));
			//System.out.println("sts48c-" +s3);
					
		}
		
	}


	protected PComponent makeSubComponent(String name,int id) {
		//System.out.println("OC192c_rx: makeSubComonent - name = " + name + ", id = " + id);
		
		if (name.equals("TOH")) {
		 	if (mTOH == null) {
		 		mTOH = new TOH(mClient,this,name,id);
		 	}
		 	return mTOH;
		}


		if (mChildren.containsKey(name)) {
			Object tmp = (Object)mChildren.get(name);
			RxStsNc192 sts = null;

			if (tmp instanceof StsID) {
				StsID sid = (StsID)tmp;

				sts = new RxStsNc192(mClient,this,name,id,48,
						sid.mSts3number,sid.mSts1number);
				mChildren.put(name,sts);
			} else {
				sts = (RxStsNc192)tmp;
			}
			if (sts != null)
				return sts;
		}
		return null;
	}


	public static class PayloadType implements Serializable {
		private static Vector cInstances = new Vector();
		private static Hashtable cInstanceHash = new Hashtable();

		static public final PayloadType FIXED_PATTERN =
				new PayloadType("Fixed Pattern", 0);
		static public final PayloadType UNKNOWN_PATTERN =
				new PayloadType("LIVE Traffic", 1);

		String mName;
		Integer mValue;

		static public PayloadType getInstanceFor(Integer id) {
			return (PayloadType) cInstanceHash.get(id);
		}

		static public Vector getInstances() {
			return cInstances;
		}

		private PayloadType(String name, int value) {
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


	public static class Pattern implements Serializable {
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
				if (pat == null) return p;
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
}
