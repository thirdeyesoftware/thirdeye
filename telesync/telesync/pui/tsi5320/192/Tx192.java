package telesync.pui.tsi5320;

import pippin.pui.*;
import java.util.*;
import java.io.*;
import pippin.binder.*;
import pippin.util.*;
public class Tx192
	extends Card
{
	private PComponent mTemperature,mDatapath;
	
	public Tx192(PClient client,PComponent parent,String name,int id) {
		super(client,parent,name,id,
				new PTypeSet(new String[] {
					"serialNumber",
					"laserCount",
					"lasersOn",
					"pll_los",
					"init_done",

					"mode",
					"status",
					
					"configCount",
					"configMax",

					"dataSource",
					"name",

				}, new byte[] {
					PAttribute.TYPE_STRING,
					PAttribute.TYPE_SHORT,
					PAttribute.TYPE_BOOLEAN,
					PAttribute.TYPE_BOOLEAN,
					PAttribute.TYPE_BOOLEAN,

					PAttribute.TYPE_SHORT,
					PAttribute.TYPE_STRING,

					PAttribute.TYPE_SHORT,
					PAttribute.TYPE_SHORT,
					
					PAttribute.TYPE_SHORT,
					PAttribute.TYPE_STRING,
					
				}),new Tx192AlarmStatusTypeSet());
	}

	protected PComponent makeSubComponent(String name,int id) {
		if (name.equals("temperature")) {
			if (mTemperature == null)
				mTemperature = new Tx192temperature(mClient,this,name,id);
			return mTemperature;
		} else if (name.equals("datapath")) {
			if (mDatapath == null)
				mDatapath = new Tx192datapath(mClient,this,name,id);
			return mDatapath;
		}
		return null;
	}

	public Class getAlarmTypeSetClass() {
		return Tx192AlarmStatusTypeSet.class;
	}
	public boolean pllLOS() throws Exception {
		return ((Boolean) get("pll_los")).booleanValue();
	}


	public CardType getCardType() {
		return CardType.OC192_TRANSMIT;
	}


	protected void doStateChanged(PStateChangeEvent event) {
		boolean pllLOS = false;

		try {
			pllLOS = pllLOS();
		}
		catch (Exception e) {
			// It shouldn't be possible to get PStateChangeEvents
			// if we have dropped the connection.
			e.printStackTrace();
		}

		setHasAlarm(pllLOS);
	}
	public Mode getMode() throws Exception {
		return (Mode)get("mode");
	}
	
	public AlarmType getAlarmType(Integer id) {
		return Tx192AlarmType.getInstanceFor(id);
	}
	
	static public class Tx192AlarmType implements AlarmType, Serializable {
		private static Vector cInstances = new Vector();
		private static Hashtable cInstanceHash = new Hashtable();
		
		static public final Tx192AlarmType ALARM_UNKOWN = 
				new Tx192AlarmType("UNKNOWN", new Integer(-1));
		static public final Tx192AlarmType ALARM_BX_ERR =
				new Tx192AlarmType("BX-ERR", new Integer(0));

		static public final Tx192AlarmType ALARM_FX_ERR =
				new Tx192AlarmType("FX-ERR", new Integer(1));
		static public final Tx192AlarmType ALARM_PX_ERR =
				new Tx192AlarmType("PX-ERR", new Integer(2));
		static public final Tx192AlarmType ALARM_PLL =
				new Tx192AlarmType("PLL", new Integer(3));

		static public final Tx192AlarmType ALARM_LOL =
				new Tx192AlarmType("LOL", new Integer(4));
		static public final Tx192AlarmType ALARM_LOF =
				new Tx192AlarmType("LOF", new Integer(5));
		static public final Tx192AlarmType ALARM_LOP =
				new Tx192AlarmType("LOP", new Integer(7));

		static public final Tx192AlarmType ALARM_LOS =
				new Tx192AlarmType("LOS", new Integer(7));

		static public final Tx192AlarmType ALARM_AIS =
				new Tx192AlarmType("AIS-L", new Integer(8));
		static public final Tx192AlarmType ALARM_RDI =
				new Tx192AlarmType("RDI-L", new Integer(9));

		static public final Tx192AlarmType ALARM_AISP =
			new Tx192AlarmType("AIS-P", new Integer(10));
		static public final Tx192AlarmType ALARM_RDIP =
			new Tx192AlarmType("RDI-P", new Integer(11));
			
		String mName;
		Integer mValue;
	
		static public Tx192AlarmType getInstanceFor(Integer id) {
			Tx192AlarmType type = (Tx192AlarmType)cInstanceHash.get(id);
			if (type == null) {
				type = ALARM_UNKOWN;
			}
			return type;
		}
	
		static public Vector getInstances() {
			return cInstances;
		}
	
		private Tx192AlarmType(String name, Integer value) {
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
			if (o instanceof Tx192AlarmType) {
				Tx192AlarmType type = (Tx192AlarmType)o;
				return type.getValue().equals(getValue());
			}
			return false;
		}
		
	}	

	static public class Tx192AlarmStatusTypeSet extends TypeSet {
	
		public Tx192AlarmStatusTypeSet() {
			super(new TypeSetElement[] {
					new TypeSetElement("BX-ERR", IndicatorStatus.class, true),
					new TypeSetElement("TX-ERR", IndicatorStatus.class, true),
					new TypeSetElement("MX-ERR", IndicatorStatus.class, true),
					
					new TypeSetElement("PLL", IndicatorStatus.class, true),
					new TypeSetElement("LOF", IndicatorStatus.class, true),
					new TypeSetElement("LOS", IndicatorStatus.class, true),
					new TypeSetElement("LOP", IndicatorStatus.class, true),
					new TypeSetElement("LSROFF", IndicatorStatus.class, true),
					new TypeSetElement("FWARN0", IndicatorStatus.class, true),
					new TypeSetElement("FWARN1", IndicatorStatus.class, true),
					new TypeSetElement("FWARN2", IndicatorStatus.class, true),
					new TypeSetElement("FWARN3", IndicatorStatus.class, true),
					new TypeSetElement("UNKNOWN", IndicatorStatus.class, true),
				});
		}
	}			
	
}
