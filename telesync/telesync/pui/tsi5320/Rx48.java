package telesync.pui.tsi5320;

import pippin.pui.*;
import java.util.*;
import java.io.*;
import pippin.binder.*;
import pippin.util.*;

public class Rx48 extends Card 
		{

	private long mOffsetTimeMillis;						// offset milliseconds 
	private int mTimeIntoTest;  							//in seconds
	private boolean mIsTestRunning = false;
	private int mFrmErrSecs, mB1ErrSecs, mB2ErrSecs;
	
	private PComponent mTemperature,mDatapath;
	
	
	public Rx48(PClient client,PComponent parent,String name,int id) {
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
					
					"testRunning",
					"timeIntoTest",
					
					"b1ErrdSecs",
					"b2ErrdSecs",
					"FrmErrdSecs",
										

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
					
					PAttribute.TYPE_BOOLEAN,
					PAttribute.TYPE_SHORT,
					
					PAttribute.TYPE_SHORT,
					PAttribute.TYPE_SHORT,
					PAttribute.TYPE_SHORT,
				}),
				new Rx48AlarmStatusTypeSet()
				);
		try {
			mIsTestRunning = ((Boolean)get("testRunning")).booleanValue();
			
					
		}
		catch (Exception e) {}
		
	}

	public Class getAlarmTypeSetClass() {
		return Rx48AlarmStatusTypeSet.class;
	}
	
	protected PComponent makeSubComponent(String name, int id) {
		System.out.println("Rx48.makeSubComponent: type=" +name+
						"  id=" +id);

		if (name.equals("temperature")) {
			if (mTemperature == null)
				mTemperature = new Rx48temperature(mClient,this,name,id);
			return mTemperature;
		} else if (name.equals("datapath")) {
			if (mDatapath == null)
				mDatapath = new Rx48datapath(mClient,this,name,id);
			return mDatapath;
		}

		return null;
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
		return CardType.OC48_RECEIVE;
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
			PAttributeSet attributes = event.getAttributes();
			
			if (attributes.contains("testRunning")) {
				mIsTestRunning = ((Boolean)attributes.getValue("testRunning")).booleanValue();
				System.out.println("Rx48.testRunning? " + mIsTestRunning);
			}
			
			if (attributes.contains("timeIntoTest")) {
				setTestTime(((Integer)attributes.getValue("timeIntoTest")).intValue());
			}
			if (attributes.contains("b1ErrdSecs")) {
				setB1ErrorSecs(((Integer)attributes.getValue("b1ErrdSecs")).intValue());
			}
			if (attributes.contains("b2ErrdSecs")) {
				setB2ErrorSecs(((Integer)attributes.getValue("b2ErrdSecs")).intValue());
			}
			if (attributes.contains("FrmErrdSecs")) {
				setFrmErrorSecs(((Integer)attributes.getValue("FrmErrdSecs")).intValue());
			}

			
		}
		catch (Exception e) {
			// It shouldn't be possible to get PStateChangeEvents
			// if we have dropped the connection.
			e.printStackTrace();
		}
		
		
		
		//setHasAlarm(lossLight || lossFraming || lossPattern || mpllLOS);
		
	}

	public boolean isTestRunning() {
		return mIsTestRunning;
	}
	
	public int getTestTime() {
		return (mTimeIntoTest + (new Long(System.currentTimeMillis() - mOffsetTimeMillis)).intValue() / 1000);
		
	}
	public void setTestTime(int time) {
		mTimeIntoTest = time;
		mOffsetTimeMillis = System.currentTimeMillis();
		
	}
	
	public int getB1ErrorSecs() {
		return mB1ErrSecs;
	}
	public void setB1ErrorSecs(int secs) {
		mB1ErrSecs = secs;
	}
	
	public int getB2ErrorSecs() {
		return mB2ErrSecs;
	}
	public void setB2ErrorSecs(int secs) {
		mB2ErrSecs = secs;
	}
	
	public int getFrmErrorSecs() {
		return mFrmErrSecs;
	}
	public void setFrmErrorSecs(int secs) {
		mFrmErrSecs = secs;
	}
	
	public void resetErrorSeconds() {
		mFrmErrSecs = 0;
		mB1ErrSecs = 0;
		mB2ErrSecs = 0;
	}
	
	public void clearAlarms() {
		try {
			clearAlarmHistory();
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	
	public AlarmType getAlarmType(Integer id) {
		return Rx48AlarmType.getInstanceFor(id);
	}
	
	static public class Rx48AlarmType implements AlarmType, Serializable {
		private static Vector cInstances = new Vector();
		private static Hashtable cInstanceHash = new Hashtable();
		
		static public final Rx48AlarmType ALARM_UNKOWN = 
				new Rx48AlarmType("UNKNOWN", new Integer(-1));
		static public final Rx48AlarmType ALARM_BX_ERR =
				new Rx48AlarmType("BX-ERR", new Integer(0));

		static public final Rx48AlarmType ALARM_FX_ERR =
				new Rx48AlarmType("FX-ERR", new Integer(1));
		static public final Rx48AlarmType ALARM_PX_ERR =
				new Rx48AlarmType("PX-ERR", new Integer(2));
		static public final Rx48AlarmType ALARM_PLL =
				new Rx48AlarmType("PLL", new Integer(3));

		static public final Rx48AlarmType ALARM_LOL =
				new Rx48AlarmType("LOL", new Integer(4));
		static public final Rx48AlarmType ALARM_LOF =
				new Rx48AlarmType("LOF", new Integer(5));
		static public final Rx48AlarmType ALARM_LOP =
				new Rx48AlarmType("LOP", new Integer(6));

		static public final Rx48AlarmType ALARM_LOS =
				new Rx48AlarmType("LOS", new Integer(7));

		static public final Rx48AlarmType ALARM_AIS =
				new Rx48AlarmType("AIS-L", new Integer(8));
		static public final Rx48AlarmType ALARM_RDI =
				new Rx48AlarmType("RDI-L", new Integer(9));

		static public final Rx48AlarmType ALARM_AISP =
			new Rx48AlarmType("AIS-P", new Integer(10));
		static public final Rx48AlarmType ALARM_RDIP =
			new Rx48AlarmType("RDI-P", new Integer(11));
		static public final Rx48AlarmType ALARM_PL =
			new Rx48AlarmType("PL", new Integer(12));			
			
		String mName;
		Integer mValue;
	
		static public Rx48AlarmType getInstanceFor(Integer id) {
			Rx48AlarmType type = (Rx48AlarmType)cInstanceHash.get(id);
			if (type == null) {
				type = ALARM_UNKOWN;
			}
			return type;
		}
	
		static public Vector getInstances() {
			return cInstances;
		}
	
		private Rx48AlarmType(String name, Integer value) {
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
			if (o instanceof Rx48AlarmType) {
				Rx48AlarmType type = (Rx48AlarmType)o;
				return type.getValue().equals(getValue());
			}
			return false;
		}
		
	}
	
	
	/*
	AIS-P - set H1 & H2 in TOH to FF
	RDI-P - set G1 in POH to 08
	*/
	
	static public class Rx48AlarmStatusTypeSet extends TypeSet {
	
		public Rx48AlarmStatusTypeSet() {
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
					new TypeSetElement("PL", IndicatorStatus.class, true),
				});
		}
	}

}
