package telesync.pui.tsi5320;

import pippin.pui.*;
import java.util.*;
import java.io.*;

public class Oc48c_rx extends PComponent 
		implements PStateChangeListener {

	PComponent mTOH,mPOH;
	Hashtable mChildren;
	private long mOffsetTimeMillis;
	private int mTimeIntoTest;
	
	private int mPattErrSecs, mB3ErrSecs;
	private boolean mRateEnabled, mRateEnabledUser;
	private static int mInstanceId;
	
	public Oc48c_rx(PClient client,PComponent parent,
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
					
					"timeIntoTest", 
					
					"b3ErrdSecs",
					"pattnErrdSecs",
					
					

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
					
					PAttribute.TYPE_SHORT,
					
					PAttribute.TYPE_SHORT,
					PAttribute.TYPE_SHORT,
					
				}));
		mInstanceId ++;
		
		mChildren = new Hashtable();
		for (int i=0 ; i<4 ; i++) {
			int s3 = 4*i + 1;
			mChildren.put("sts12c-" + s3, new StsID(s3,0));
			
		}
		
		try {
			addStateChangeListener(this);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Error(e.toString());
		}
		
		
	}
	public int getInstanceId() {
		return mInstanceId;
	}
	
	public void stateChanged(PStateChangeEvent event) {
		try {
			
			//System.out.println(event.getAttributes().toString());
			/*int time = 0;
			if (get("timeIntoTest",false) != null) {
				time = ((Integer)get("timeIntoTest")).intValue();
			}
			setTestTime(time);
			*/
			
			PAttributeList attrs = event.getAttributes();
			//System.out.println(attrs.toString());
			if (attrs.contains("b3ErrdSecs")) {
				mB3ErrSecs = ((Integer)attrs.getValue("b3ErrdSecs")).intValue();
				//System.out.println("Oc48c_rx.setB3ErrSecs = " + mB3ErrSecs);

			}
			if (attrs.contains("pattnErrdSecs")) {
				mPattErrSecs = ((Integer)attrs.getValue("pattnErrdSecs")).intValue();
			}
			if (attrs.contains("rateEnabled")) {
				mRateEnabled = ((Boolean)attrs.getValue("rateEnabled")).booleanValue();
			}
			if (attrs.contains("rateEnabledUser")) {
				mRateEnabledUser = ((Boolean)attrs.getValue("rateEnabledUser")).booleanValue();
			}
			
		}
		catch (Exception ex) {
		}
	}

	protected PComponent makeSubComponent(String name,int id) {
		System.out.println("Oc48c_rx.makeSubComponent: type=" +name+
				"  id=" +id);

		if (name.equals("TOH")) {
		 	if (mTOH == null) {
		 		mTOH = new TOH(mClient,this,name,id);
		 	}
		 	return mTOH;
		}


		if (mChildren.containsKey(name)) {
			Object tmp = (Object)mChildren.get(name);
			RxStsNc sts = null;

			if (tmp instanceof StsID) {
				StsID sid = (StsID)tmp;

				sts = new RxStsNc(mClient,this,name,id,12,
						sid.mSts3number,sid.mSts1number);
				
				mChildren.put(name,sts);
			} else {
				sts = (RxStsNc)tmp;
			}
			if (sts != null)
				return sts;
		}
		return null;
	}
	
	public boolean isUserEnabled() {
		return mRateEnabledUser;
	}
	public boolean isRateEnabled() {
		return mRateEnabled;
	}
	
	/*
	public int getTestTime() {
		
		return (mTimeIntoTest + (new Long(System.currentTimeMillis() - mOffsetTimeMillis)).intValue() / 1000);

	}
	
	public void setTestTime(int time) {
		mTimeIntoTest = time;
		mOffsetTimeMillis = System.currentTimeMillis();

	}
	*/
	
	public int getB3ErrSecs() {
		return mB3ErrSecs;
	}
	
	public int getPatternErrSecs() {
		return mPattErrSecs;
	}
	
	public void resetErrorSeconds() {
		//System.out.println("resetErrorSeconds()");
		mB3ErrSecs = 0;
		mPattErrSecs = 0;
	}
	

	public static class PayloadType implements Serializable {
		private static Vector cInstances = new Vector();
		private static Hashtable cInstanceHash = new Hashtable();

		static public final PayloadType FIXED_PATTERN =
				new PayloadType("Fixed Pattern", 0);
		static public final PayloadType UNKNOWN_PATTERN =
				new PayloadType("LIVE Pattern", 1);
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

		static public final Pattern ZEROES_INVERTED = new Pattern("Zeroes - Inverted", 0 + + INVERT_OFFSET, true);
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
	
	public static class Oc48c_RxAlarmType implements Serializable {
		private static Vector cInstances = new Vector();
		private static Hashtable cInstanceHash = new Hashtable();

		static public final Oc48c_RxAlarmType ALARM_UNKOWN = 
				new Oc48c_RxAlarmType("UNKNOWN", new Integer(-1));

		static public final Oc48c_RxAlarmType ALARM_AIS_P =
				new Oc48c_RxAlarmType("AIS-P", new Integer(0));

		static public final Oc48c_RxAlarmType ALARM_RDI_P =
				new Oc48c_RxAlarmType("RDI-P", new Integer(1));

		String mName;
		Integer mValue;

		static public Oc48c_RxAlarmType getInstanceFor(Integer id) {
			Oc48c_RxAlarmType type = (Oc48c_RxAlarmType)cInstanceHash.get(id);
			if (type == null) {
				type = ALARM_UNKOWN;
			}
			return type;
		}

		static public Vector getInstances() {
			return cInstances;
		}

		private Oc48c_RxAlarmType(String name, Integer value) {
			mName = name;
			mValue = value;

			cInstances.addElement(this);
			cInstanceHash.put(mValue, this);
		}

		public String getName() {
			return mName;
		}

		public Integer getValue() {
			return mValue;
		}

		public boolean equals(Object o) {
			if (o instanceof Oc48c_RxAlarmType) {
				Oc48c_RxAlarmType type = (Oc48c_RxAlarmType)o;
				return type.getValue().equals(getValue());
			}
			return false;
		}

	}
	
}
