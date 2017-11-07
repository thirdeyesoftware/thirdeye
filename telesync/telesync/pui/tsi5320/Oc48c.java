package telesync.pui.tsi5320;

import pippin.pui.*;
import java.util.*;
import java.io.*;

public class Oc48c
	extends PComponent
{
	PComponent mTOH,mPOH;
	Hashtable mChildren;

	public Oc48c(PClient client,PComponent parent,
			String name,int id) {
		super(client,parent,name,id,
				new PTypeSet(new String[] {
					"rateEnabled",

					"payloadType",
					"pattern",
					"prbsInvert",

					"errorEnable",
					"errorRate",
					"errorType",
					"errorSingle",
					"errorButton",
					
				}, new byte[] {
					PAttribute.TYPE_BOOLEAN,

					PAttribute.TYPE_SHORT,
					PAttribute.TYPE_BYTE,
					PAttribute.TYPE_BOOLEAN,

					PAttribute.TYPE_BOOLEAN,
					PAttribute.TYPE_BYTE,
					PAttribute.TYPE_BYTE,
					PAttribute.TYPE_BYTE,
					PAttribute.TYPE_BYTE
				}));

		mChildren = new Hashtable();

		for (int i=0 ; i<4 ; i++) {
			int s3 = 4*i + 1;
			mChildren.put("sts12c-" + s3, new StsID(s3,0));
			//System.out.println("sts12c-" +s3);
		}
	}


	protected PComponent makeSubComponent(String name,int id) {
		System.out.println("Oc48c.makeSubComponent: type=" +name+
						"  id=" +id);

		if (name.equals("TOH")) {
			if (mTOH == null)
				mTOH = new TOH(mClient,this,name,id);
			return mTOH;
		}
		if (name.equals("POH")) {
			if (mPOH == null)
				mPOH = new POH(mClient,this,name,id);
			return mPOH;
		}
		if (mChildren.containsKey(name)) {
			Object tmp = (Object)mChildren.get(name);
			TxStsNc sts = null;

			if (tmp instanceof StsID) {
				StsID sid = (StsID)tmp;
				sts = new TxStsNc(mClient,this,name,id, 12,
						sid.mSts3number,sid.mSts1number);
				mChildren.put(name,sts);
			} else {
				sts = (TxStsNc)tmp;
			}
			if (sts != null)
				return sts;
		}

		return null;
	}


	public PayloadType getPayloadType() throws Exception {
		Integer id = (Integer) get("payloadType");
		return PayloadType.getInstanceFor(id);
	}

	public void setPayloadType(PayloadType payloadType) throws Exception {
		put("payloadType", payloadType.getValue());
	}


	public Pattern getPattern() throws Exception {
		Integer id = (Integer) get("pattern");
		return Pattern.getInstanceFor(id);
	}

	public void setPattern(Pattern pattern) throws Exception {
		put("pattern", pattern.getValue());
	}


	public ErrorRate getErrorRate() throws Exception {
		Integer id = (Integer) get("errorRate");
		return ErrorRate.getInstanceFor(id);
	}

	public void setErrorRate(ErrorRate errorRate) throws Exception {
		put("errorRate", errorRate.getValue());
	}


	public ErrorType getErrorType() throws Exception {
		Integer id = (Integer) get("errorType");
		return ErrorType.getInstanceFor(id);
	}

	public ErrorType getErrorType(Integer id) throws Exception {
		return ErrorType.getInstanceFor(id);
	}
	
	public void setErrorType(ErrorType errorType) throws Exception {
		put("errorType", errorType.getValue());
	}

	// jb-01080501 send b1,b2,b3 errors
	public void insertError(ErrorType errorType) throws Exception {
		put("errorSingle", errorType.getValue());
	}
	


	public static class PayloadType implements Serializable {
		private static Vector cInstances = new Vector();
		private static Hashtable cInstanceHash = new Hashtable();

		static public final PayloadType FIXED_PATTERN =
				new PayloadType("Fixed Pattern", 0);
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
		static public final Pattern ONES = new Pattern("Ones", 1);
		static public final Pattern ALTERNATING = new Pattern("101010", 2);
		static public final Pattern PRBS_15 = new Pattern("PRBS 15", 3);
		static public final Pattern PRBS_20 = new Pattern("PRBS 20", 4);
		static public final Pattern PRBS_23 = new Pattern("PRBS 23", 5);
		
		//static public final Pattern ZEROES_INVERTED = new Pattern("Zeroes - Inverted", 0 + + INVERT_OFFSET, true);
		//static public final Pattern ONES_INVERTED = new Pattern("Ones - Inverted", 1 + INVERT_OFFSET, true);
		//static public final Pattern ALTERNATING_INVERTED = new Pattern("101010 - Inverted", 2 + + INVERT_OFFSET, true);
		static public final Pattern PRBS_15_INVERTED = new Pattern("PRBS 15 - Inverted", 3 + INVERT_OFFSET, true);
		static public final Pattern PRBS_20_INVERTED = new Pattern("PRBS 20 - Inverted", 4 + INVERT_OFFSET, true);
		static public final Pattern PRBS_23_INVERTED = new Pattern("PRBS 23 - Inverted", 5 + INVERT_OFFSET, true);
		

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


	public static class ErrorRate implements Serializable {
		private static Vector cInstances = new Vector();
		private static Hashtable cInstanceHash = new Hashtable();

		static public final ErrorRate ONE_E_9 = new ErrorRate("1E-9", 0);
		static public final ErrorRate ONE_E_8 = new ErrorRate("1E-8", 1);
		static public final ErrorRate ONE_E_7 = new ErrorRate("1E-7", 2);
		static public final ErrorRate ONE_E_6 = new ErrorRate("1E-6", 3);
		static public final ErrorRate ONE_E_5 = new ErrorRate("1E-5", 4);
		//static public final ErrorRate ONE_E_4 = new ErrorRate("1E-4", 5);
		//static public final ErrorRate ONE_E_3 = new ErrorRate("1E-3", 6);

		String mName;
		Integer mValue;

		static public ErrorRate getInstanceFor(Integer id) {
			return (ErrorRate) cInstanceHash.get(id);
		}

		static public Vector getInstances() {
			return (Vector)(cInstances.clone());
		}

		private ErrorRate(String name, int value) {
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


	public static class ErrorType implements Serializable {
		private static Vector cInstances = new Vector();
		private static Hashtable cInstanceHash = new Hashtable();

		static public final ErrorType B1_ERROR = new ErrorType("B1", 0);
		static public final ErrorType B2_ERROR = new ErrorType("B2", 1);
		static public final ErrorType B3_ERROR = new ErrorType("B3", 2);
		static public final ErrorType PAYLOAD_ERROR = new ErrorType("Payload", 3);
		static public final ErrorType RANDOM_ERROR = new ErrorType("Random", 4);

		String mName;
		Integer mValue;

		static public ErrorType getInstanceFor(Integer id) {
			return (ErrorType) cInstanceHash.get(id);
		}

		static public Vector getInstances() {
			return cInstances;
		}

		private ErrorType(String name, int value) {
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
	
	static class AlarmType implements Serializable {
		private static Vector cInstances = new Vector();
		private static Hashtable cInstanceHash = new Hashtable();

		static public final AlarmType ALARM_UNKOWN = 
				new AlarmType("UNKNOWN", new Integer(-1));

		static public final AlarmType ALARM_AIS_P =
				new AlarmType("AIS-P", new Integer(0));

		static public final AlarmType ALARM_RDI_P =
				new AlarmType("RDI-P", new Integer(1));
		
		String mName;
		Integer mValue;

		static public AlarmType getInstanceFor(Integer id) {
			AlarmType type = (AlarmType)cInstanceHash.get(id);
			if (type == null) {
				type = ALARM_UNKOWN;
			}
			return type;
		}

		static public Vector getInstances() {
			return cInstances;
		}

		private AlarmType(String name, Integer value) {
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
			if (o instanceof AlarmType) {
				AlarmType type = (AlarmType)o;
				return type.getValue().equals(getValue());
			}
			return false;
		}
			
	}
	
}
