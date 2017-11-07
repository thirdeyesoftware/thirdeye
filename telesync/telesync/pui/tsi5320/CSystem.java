package telesync.pui.tsi5320;

import pippin.pui.*;
import java.util.*;
import pippin.util.*;
import pippin.binder.*;
import java.io.*;

public class CSystem extends Card {
	private PComponent mClock;
	private PComponent mFans;
		
	public CSystem(PClient client,PComponent parent,String name,int id) {
		super(client,parent,name,id,
				new PTypeSet(new String[] {
					"softVersion",
					"name",
					"serialNumber",
				}, new byte[] {
					PAttribute.TYPE_STRING,
					PAttribute.TYPE_STRING,
					PAttribute.TYPE_STRING,
				}),
				new SystemAlarmStatusTypeSet()
				);
	}

	protected PComponent makeSubComponent(String name,int id) {
		if (name.equals("clock")) {
			if (mClock == null)
				mClock = new Clock(mClient,this,name,id);
			return mClock;
		} else
		if (name.equals("fans")) {
			if (mFans == null)
				mFans = new Fans(mClient,this,name,id);
			return mFans;
		}
		return null;
	}


	public CardType getCardType() {
		return CardType.SYSTEM;
	}


	protected void doStateChanged(PStateChangeEvent event) {
	}
	
	public Class getAlarmTypeSetClass() {
		return SystemAlarmStatusTypeSet.class;
	}
	
	public AlarmType getAlarmType(Integer id) {
		return SystemAlarmType.getInstanceFor(id);
	}
	
	static public class SystemAlarmType implements AlarmType, Serializable {
		private static Vector cInstances = new Vector();
		private static Hashtable cInstanceHash = new Hashtable();
		
		static public final SystemAlarmType ALARM_UNKOWN = 
				new SystemAlarmType("UNKNOWN", new Integer(-1));
		
			
		String mName;
		Integer mValue;
	
		static public SystemAlarmType getInstanceFor(Integer id) {
			SystemAlarmType type = (SystemAlarmType)cInstanceHash.get(id);
			if (type == null) {
				type = ALARM_UNKOWN;
			}
			return type;
		}
	
		static public Vector getInstances() {
			return cInstances;
		}
	
		private SystemAlarmType(String name, Integer value) {
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
			if (o instanceof SystemAlarmType) {
				SystemAlarmType type = (SystemAlarmType)o;
				return type.getValue().equals(getValue());
			}
			return false;
		}
		
	}
	
	
	static public class SystemAlarmStatusTypeSet extends TypeSet {
	
		public SystemAlarmStatusTypeSet() {
			super(new TypeSetElement[] {

					new TypeSetElement("UNKNOWN", IndicatorStatus.class, true),
				});
		}
	}
}
