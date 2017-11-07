package telesync.pui.tsi5320;

import pippin.pui.*;
import pippin.util.*;
import java.util.*;
import java.io.*;

public class RxStsNc extends PComponent 
		implements PStateChangeListener {
	Hashtable mChildren;
	int mRate;
	int mSts3num;
	int mSts1num;
	
	private long mOffsetTimeMillis;
	private int mTimeIntoTest;
	
	private int mPattErrSecs, mB3ErrSecs;
	private boolean mRateEnabled, mRateEnabledUser;
	
	private RxStsNc(PClient client,PComponent parent,
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
	}

	public RxStsNc(PClient client,PComponent parent,
			String name,int id, int rate,int s3,int s1) {
		this(client,parent,name,id);
	
		// System.out.print("RxStsNc: " +name+ "\n");

		mRate = rate;
		mSts3num = s3;
		mSts1num = s1;

		mChildren = new Hashtable();

		if (rate == 1) {
		} else if (rate == 3) {
			for (int i=0 ; i<3 ; i++) {
				mChildren.put("sts1-" +s3+ "," +(i+1),
						new StsID(s3,i+1));
				// System.out.println("    sts1-" +s3+ "," +(i+1));
			}
		} else {
			for (int i=0 ; i<4 ; i++) {
				mChildren.put("sts" +(rate/4)+ "c-" + (s3+i),
						new StsID(s3+i,s1));
				// System.out.println("    sts" +(rate/4)+ "c-" + (s3+i));
			}
		}
		
		try {
			addStateChangeListener(this);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Error(e.toString());
		}

	}

	public void stateChanged(PStateChangeEvent e) {
		try {
			/*if (get("timeIntoTest",false) != null) {
				setTestTime(((Integer)get("timeIntoTest")).intValue());
			}
			*/
			
			PAttributeList attrs = e.getAttributes();
			//System.out.println(attrs);
			if (attrs.contains("b3ErrdSecs")) {
				//System.out.println("RxStsNC.stateChanged()-b3ErrdSecs = " + ((Integer)attrs.getValue("b3ErrdSecs")).intValue());
				mB3ErrSecs = ((Integer)attrs.getValue("b3ErrdSecs")).intValue();
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
	
	/*public int getTestTime() {
		return (mTimeIntoTest + (new Long(System.currentTimeMillis() - mOffsetTimeMillis)).intValue() / 1000);

	}
	public void setTestTime(int time) {
		mTimeIntoTest = time;
		mOffsetTimeMillis = System.currentTimeMillis();

	}
	*/
	
	public boolean isUserEnabled() {
		return mRateEnabledUser;
	}
	public boolean isRateEnabled() {
		return mRateEnabled;
	}
	
	public int getB3ErrSecs() {
		return mB3ErrSecs;
	}

	public int getPatternErrSecs() {
		return mPattErrSecs;
	}

	public void resetErrorSeconds() {
		mB3ErrSecs = 0;
		mPattErrSecs = 0;
	}
	protected PComponent makeSubComponent(String name,int id) {
		System.out.println("RxStsNc.makeSubComponent: type=" +name+
						"  id=" +id);

		if (mChildren.containsKey(name)) {
			Object tmp = (Object)mChildren.get(name);
			RxStsNc sts = null;

			if (tmp instanceof StsID) {
				StsID sid = (StsID)tmp;
				sts = new RxStsNc(mClient,this,name,id,
						(mRate == 12) ? 3 : 1,
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

	public static class RxStsNcAlarmType implements Serializable {
		private static Vector cInstances = new Vector();
		private static Hashtable cInstanceHash = new Hashtable();

		static public final RxStsNcAlarmType ALARM_UNKOWN = 
				new RxStsNcAlarmType("UNKNOWN", new Integer(-1));

		static public final RxStsNcAlarmType ALARM_AIS_P =
				new RxStsNcAlarmType("AIS-P", new Integer(0));

		static public final RxStsNcAlarmType ALARM_RDI_P =
				new RxStsNcAlarmType("RDI-P", new Integer(1));
		
		String mName;
		Integer mValue;

		static public RxStsNcAlarmType getInstanceFor(Integer id) {
			RxStsNcAlarmType type = (RxStsNcAlarmType)cInstanceHash.get(id);
			if (type == null) {
				type = ALARM_UNKOWN;
			}
			return type;
		}

		static public Vector getInstances() {
			return cInstances;
		}

		private RxStsNcAlarmType(String name, Integer value) {
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
			if (o instanceof RxStsNcAlarmType) {
				RxStsNcAlarmType type = (RxStsNcAlarmType)o;
				return type.getValue().equals(getValue());
			}
			return false;
		}
			
	}

}
