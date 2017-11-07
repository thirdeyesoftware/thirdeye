package telesync.pui.tsi5320;

import pippin.pui.*;
import java.util.*;
import java.io.*;

public class TxStsNc
	extends PComponent
{
	Hashtable mChildren;
	int mRate;
	int mSts3num;
	int mSts1num;

	private TxStsNc(PClient client,PComponent parent,
			String name,int id) {
		super(client,parent,name,id,
				new PTypeSet(new String[] {
					"rateEnabled",

					"payloadType",
					"pattern",
					"patternInvert",
				}, new byte[] {
					PAttribute.TYPE_BOOLEAN,

					PAttribute.TYPE_SHORT,
					PAttribute.TYPE_BYTE,
					PAttribute.TYPE_BOOLEAN,
				}));
	}

	public TxStsNc(PClient client,PComponent parent,
			String name,int id, int rate,int s3,int s1) {
		this(client,parent,name,id);
	
		// System.out.print("TxStsNc: " +name+ "\n");

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
	}

	protected PComponent makeSubComponent(String name,int id) {
		System.out.println("TxStsNc.makeSubComponent: type=" +name+
						"  id=" +id);

		if (mChildren.containsKey(name)) {
			Object tmp = (Object)mChildren.get(name);
			TxStsNc sts = null;

			if (tmp instanceof StsID) {
				StsID sid = (StsID)tmp;
				sts = new TxStsNc(mClient,this,name,id,
						(mRate == 12) ? 3 : 1,
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
