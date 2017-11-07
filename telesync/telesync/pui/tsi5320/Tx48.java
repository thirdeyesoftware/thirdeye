package telesync.pui.tsi5320;

import pippin.pui.*;
import telesync.gui.OpticalPowerBinder;

import java.util.*;
import java.io.*;
import pippin.binder.*;
import pippin.util.*;

public class Tx48
	extends Card
{
	private PComponent mTemperature,mDatapath,mTOH;
	
	private boolean mLaserWasChanged = false;
	private boolean mLaserChangePending = false;
	private Boolean mCurrentLaserValue;
		
	public Tx48(PClient client,PComponent parent,String name,int id) {
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
				}),
				new Tx48AlarmStatusTypeSet()
			);
	}

	protected PComponent makeSubComponent(String name,int id) { 
		System.out.println("Tx48.makeSubcomponent() " + name + "," + id);
		
		if (name.equals("temperature")) {
			if (mTemperature == null)
				mTemperature = new Tx48temperature(mClient,this,name,id);
			return mTemperature;
		}
		if (name.equals("datapath")) {
			if (mDatapath == null)
				mDatapath = new Tx48datapath(mClient,this,name,id);
			return mDatapath;
		}
		if (name.equals("TOH")) {
			if (mTOH == null)
				mTOH = new TOH(mClient,this,name,id);
			return mTOH;
		}
		return null;
	}


	public boolean pllLOS() throws Exception {
		return ((Boolean) get("pll_los")).booleanValue();
	}
	public Mode getMode() throws Exception {
		Integer iMode = (Integer)get("mode");
		return Mode.getInstanceFor(iMode);
	}
	

	public CardType getCardType() {
		return CardType.OC48_TRANSMIT;
	}

	public Class getAlarmTypeSetClass() {
		return Tx48AlarmStatusTypeSet.class;
	}

	protected void doStateChanged(PStateChangeEvent event) {
		boolean pllLOS = false;
		
		PAttributeSet incomingAttributes = event.getAttributes();
		Boolean laserAttributeValue;
		
		//maintain existing state to allow pre-processing of new stateChange
		if (mCurrentLaserValue==null) {
			try {
				mCurrentLaserValue = (Boolean) get("lasersOn");
			}
			catch (Exception e) {
				mCurrentLaserValue = new Boolean(false);
			}
		}
			
		// if state actually changes, as opposed to being notified of the same value,
		// do some work.  setLaserWasChanged(true) sets unsolicited flag.
		if (incomingAttributes.contains("lasersOn")) {
			
			laserAttributeValue = (Boolean) incomingAttributes.getAttribute("lasersOn",true).getValue();
			
			if ( !mLaserChangePending && !laserAttributeValue.equals(mCurrentLaserValue) ) {
				setLaserWasChanged( true );
			}
			else {
				//reset pending flag
				mLaserChangePending = false; 
			}
			mCurrentLaserValue = laserAttributeValue;
		}
			
		try {
			pllLOS = pllLOS();
		}
		catch (Exception e) {
			// It shouldn't be possible to get PStateChangeEvents
			// if we have dropped the connection.
			e.printStackTrace();
		}

		//setHasAlarm(pllLOS);
	}


	// unsolicited state change methods
	public void setLaserWasChanged(boolean laserChanged) {
		mLaserWasChanged = laserChanged;
	}
	
	public boolean hasLaserChanged() {
		return mLaserWasChanged;
	}
	
	public void setLaserChangePending( boolean laserChangePending ) {
		mLaserChangePending = laserChangePending;
	}
	
	public boolean laserChangePending() {
		return mLaserChangePending;
	}
	
	//hooks before super.sync()
	public void setOutgoingAttributeChanges(PAttributeSet changes) {
		
		/* added (!mLaserChangePending) due to change in Tx48 attributeset. 9/25/01 -jsb*/
		if (changes != null && !mLaserChangePending) {
			mLaserChangePending = changes.contains("lasersOn");
			
		}
		
	}
	
	public AlarmType getAlarmType(Integer id) {
		return Tx48AlarmType.getInstanceFor(id);
	}
	
	static class Tx48AlarmType implements AlarmType,Serializable {
		private static Vector cInstances = new Vector();
		private static Hashtable cInstanceHash = new Hashtable();
		
		static public final Tx48AlarmType ALARM_UNKOWN = 
				new Tx48AlarmType("UNKNOWN", new Integer(-1));

		static public final Tx48AlarmType ALARM_BX_ERR =
				new Tx48AlarmType("BX-ERR", new Integer(0));

		static public final Tx48AlarmType ALARM_TX_ERR =
				new Tx48AlarmType("TX-ERR", new Integer(1));
		static public final Tx48AlarmType ALARM_MX_ERR =
				new Tx48AlarmType("MX-ERR", new Integer(2));
		static public final Tx48AlarmType ALARM_PLL =
				new Tx48AlarmType("PLL", new Integer(3));
		static public final Tx48AlarmType ALARM_FWARN0 =
				new Tx48AlarmType("FWARN0", new Integer(4));
		static public final Tx48AlarmType ALARM_LOS =
				new Tx48AlarmType("FWARN1", new Integer(5));
		static public final Tx48AlarmType ALARM_LOF =
				new Tx48AlarmType("FWARN2", new Integer(6));
		static public final Tx48AlarmType ALARM_AIS =
				new Tx48AlarmType("FWARN3", new Integer(7));
		static public final Tx48AlarmType ALARM_RDI =
				new Tx48AlarmType("LSROFF", new Integer(8));
	
		String mName;
		Integer mValue;
	
		static public Tx48AlarmType getInstanceFor(Integer id) {
			Tx48AlarmType type = (Tx48AlarmType)cInstanceHash.get(id);
			if (type == null) {
				type = ALARM_UNKOWN;
			}
			return type;
		}
	
		static public Vector getInstances() {
			return cInstances;
		}
	
		private Tx48AlarmType(String name, Integer value) {
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
			if (o instanceof Tx48AlarmType) {
				Tx48AlarmType type = (Tx48AlarmType)o;
				return type.getValue().equals(getValue());
			}
			return false;
		}
		
	}

	static public class Tx48AlarmStatusTypeSet extends TypeSet {
	
		public Tx48AlarmStatusTypeSet() {
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
