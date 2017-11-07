package telesync.pui.tsi5320;

import pippin.pui.*;
import java.util.*;
import java.io.*;
import pippin.binder.*;
import pippin.util.*;
public class Rx192 extends Card {

	private PComponent mTemperature,mDatapath;
		
	public Rx192(PClient client,PComponent parent,String name,int id) {
		super(client,parent,name,id,
				new PTypeSet(new String[] {
					"serialNumber",
					"init_done",
					"status",
					"configCount",
					"configMax",
					"mode",

					"opticalPower",
					"loss_light",
					"loss_framing",
					"loss_pattern",

					"mpll_los",

					"b1Errors",
					"b2Errors",
					"frameErrors",

					"lol_hist",
					"lof_hist",
					"lop_hist",

					"clear_errors",
					"clear_history",
					
					"loss_signal",
					"los_hist",
					
					"name",
					"optpwrDB",
					

				}, new byte[] {
					PAttribute.TYPE_STRING,
					PAttribute.TYPE_BOOLEAN,
					PAttribute.TYPE_STRING,
					PAttribute.TYPE_SHORT,
					PAttribute.TYPE_SHORT,
					PAttribute.TYPE_SHORT,

					PAttribute.TYPE_SHORT,
					PAttribute.TYPE_BOOLEAN,
					PAttribute.TYPE_BOOLEAN,
					PAttribute.TYPE_BOOLEAN,

					PAttribute.TYPE_BOOLEAN,

					PAttribute.TYPE_LONGLONG,
					PAttribute.TYPE_LONGLONG,
					PAttribute.TYPE_LONGLONG,

					PAttribute.TYPE_BOOLEAN,
					PAttribute.TYPE_BOOLEAN,
					PAttribute.TYPE_BOOLEAN,

					PAttribute.TYPE_BOOLEAN,
					PAttribute.TYPE_BOOLEAN,
					
					PAttribute.TYPE_BOOLEAN,
					PAttribute.TYPE_BOOLEAN,
					
					PAttribute.TYPE_STRING,
					PAttribute.TYPE_SHORT,
				}),
				new Rx192AlarmStatusTypeSet()
				);
	}


	protected PComponent makeSubComponent(String name, int id) {
		if (name.equals("temperature")) {
			if (mTemperature == null)
				mTemperature = new Rx192temperature(mClient,this,name,id);
			return mTemperature;
		} else if (name.equals("datapath")) {
			if (mDatapath == null)
				mDatapath = new Rx192datapath(mClient,this,name,id);
			return mDatapath;
		}

		return null;
	}

	public Class getAlarmTypeSetClass() {
		return Rx192AlarmStatusTypeSet.class;
	}
	public boolean lossLight() throws Exception {
		return ((Boolean) get("loss_light")).booleanValue();
	}
	public boolean lossFraming() throws Exception {
		return ((Boolean) get("loss_framing")).booleanValue();
	}
	public boolean lossPattern() throws Exception {
		return ((Boolean) get("loss_pattern")).booleanValue();
	}
	public boolean mpllLOS() throws Exception {
		return ((Boolean) get("mpll_los")).booleanValue();
	}
	public Mode getMode() throws Exception {
		Integer mode = (Integer) get("mode");
		
		
		return Mode.getInstanceFor(mode);
	}
	

	
	/* ********** */
	/* task: jb-2 */
	/* ********** */
	
	/** 
	 * Sets clearErrors device property.
	 *
	 * Note: If interested in calling this method asyncronously, 
	 * set <code>setSynchronousPuts( true )</code> on PComponent superclass.  
	 *							
	 * @param boolFlag set this flag to <code>true</code> to clear errors.
	 * @see   pippin.pui.PComponent
	 */
	public void clearErrors( boolean boolFlag ) throws Exception {
		put( "clear_errors", new Boolean(boolFlag) );
	}


	public CardType getCardType() {
		return CardType.OC192_RECEIVE;
	}

	public AlarmType getAlarmType(Integer id) {
		return Rx192AlarmType.getInstanceFor(id);
	}
	
	protected void doStateChanged(PStateChangeEvent event) {
		boolean lossLight = false;
		boolean lossFraming = false;
		boolean lossPattern = false;
		boolean mpllLOS = false;
		
		try {
			
			lossLight = lossLight();
			lossFraming = lossFraming();
			lossPattern = lossPattern();
			mpllLOS = mpllLOS();
			
			
		}
		catch (Exception e) {
			// It shouldn't be possible to get PStateChangeEvents
			// if we have dropped the connection.
			e.printStackTrace();
		}

		setHasAlarm(lossLight || lossFraming || lossPattern || mpllLOS);
	}

	
	static public class Rx192AlarmType implements AlarmType, Serializable {
		private static Vector cInstances = new Vector();
		private static Hashtable cInstanceHash = new Hashtable();

		static public final Rx192AlarmType ALARM_UNKOWN = 
				new Rx192AlarmType("UNKNOWN", new Integer(-1));
		static public final Rx192AlarmType ALARM_BX_ERR =
				new Rx192AlarmType("BX-ERR", new Integer(0));

		static public final Rx192AlarmType ALARM_FX_ERR =
				new Rx192AlarmType("FX-ERR", new Integer(1));
		static public final Rx192AlarmType ALARM_PX_ERR =
				new Rx192AlarmType("PX-ERR", new Integer(2));
		static public final Rx192AlarmType ALARM_PLL =
				new Rx192AlarmType("PLL", new Integer(3));

		static public final Rx192AlarmType ALARM_LOL =
				new Rx192AlarmType("LOL", new Integer(4));
		static public final Rx192AlarmType ALARM_LOF =
				new Rx192AlarmType("LOF", new Integer(5));
		static public final Rx192AlarmType ALARM_LOP =
				new Rx192AlarmType("LOP", new Integer(7));

		static public final Rx192AlarmType ALARM_LOS =
				new Rx192AlarmType("LOS", new Integer(7));

		static public final Rx192AlarmType ALARM_AIS =
				new Rx192AlarmType("AIS-L", new Integer(8));
		static public final Rx192AlarmType ALARM_RDI =
				new Rx192AlarmType("RDI-L", new Integer(9));

		static public final Rx192AlarmType ALARM_AISP =
			new Rx192AlarmType("AIS-P", new Integer(10));
		static public final Rx192AlarmType ALARM_RDIP =
			new Rx192AlarmType("RDI-P", new Integer(11));

		String mName;
		Integer mValue;

		static public Rx192AlarmType getInstanceFor(Integer id) {
			Rx192AlarmType type = (Rx192AlarmType)cInstanceHash.get(id);
			if (type == null) {
				type = ALARM_UNKOWN;
			}
			return type;
		}

		static public Vector getInstances() {
			return cInstances;
		}

		private Rx192AlarmType(String name, Integer value) {
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
			if (o instanceof Rx192AlarmType) {
				Rx192AlarmType type = (Rx192AlarmType)o;
				return type.getValue().equals(getValue());
			}
			return false;
		}

	}

	static public class Rx192AlarmStatusTypeSet extends TypeSet {
	
		public Rx192AlarmStatusTypeSet() {
			super(new TypeSetElement[] {
					new TypeSetElement("BX-ERR", IndicatorStatus.class, true),
					new TypeSetElement("FX-ERR", IndicatorStatus.class, true),
					new TypeSetElement("PX-ERR", IndicatorStatus.class, true),
					
					new TypeSetElement("LOL", IndicatorStatus.class, true),
					new TypeSetElement("LOF", IndicatorStatus.class, true),
					new TypeSetElement("LOS", IndicatorStatus.class, true),
					new TypeSetElement("LOP", IndicatorStatus.class, true),
					new TypeSetElement("AIS-L", IndicatorStatus.class, true),
					new TypeSetElement("RDI-L", IndicatorStatus.class, true),
					new TypeSetElement("AIS-P", IndicatorStatus.class, true),
					new TypeSetElement("RDI-P", IndicatorStatus.class, true),
					new TypeSetElement("PHASE-LOCK", IndicatorStatus.class, true),
					new TypeSetElement("UNKNOWN", IndicatorStatus.class, true),
				});
		}
	}	
}
