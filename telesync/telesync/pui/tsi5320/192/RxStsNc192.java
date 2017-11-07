package telesync.pui.tsi5320;

import pippin.pui.*;
import java.util.*;
import java.io.*;

public class RxStsNc192
	extends PComponent
{
	Hashtable mChildren;
	int mRate;
	int mSts3num;
	int mSts1num;

	private RxStsNc192(PClient client,PComponent parent,
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
	}

	public RxStsNc192(PClient client,PComponent parent,
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
				//System.out.println("RxStsNc192:    sts1-" +s3+ "," +(i+1));
			}
		} else {
			for (int i=0 ; i<4 ; i++) {
				int x = s3 + i*(rate/12);
				mChildren.put("sts" +(rate/4)+ "c-" + x,
						new StsID(x,s1));
				//System.out.println("RxStsNc192:    sts" +(rate/4)+ "c-" + x);
			}
		}
		
	}

	protected PComponent makeSubComponent(String name,int id) {
		//System.out.println("RxStsNc192: makeSubComonent - name = " + name + ", id = " + id);
		if (mChildren.containsKey(name)) {
			Object tmp = (Object)mChildren.get(name);
			RxStsNc192 sts = null;

			if (tmp instanceof StsID) {
				StsID sid = (StsID)tmp;
				sts = new RxStsNc192(mClient,this,name,id,
						(mRate == 3) ? 1 : mRate/4,
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

		static public final Pattern ZEROES = new Pattern("Zeroes", 0);
		static public final Pattern ALTERNATING = new Pattern("101010", 1);
		static public final Pattern PRBS_15 = new Pattern("PRBS 15", 2);
		static public final Pattern PRBS_20 = new Pattern("PRBS 20", 3);
		static public final Pattern PRBS_23 = new Pattern("PRBS 23", 4);

		String mName;
		Integer mValue;

		static public Pattern getInstanceFor(Integer id) {
			return (Pattern) cInstanceHash.get(id);
		}

		static public Vector getInstances() {
			return cInstances;
		}

		private Pattern(String name, int value) {
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
	
}
