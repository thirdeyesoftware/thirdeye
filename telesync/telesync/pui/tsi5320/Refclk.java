package telesync.pui.tsi5320;

import pippin.pui.*;
import java.util.*;
import pippin.util.*;
import pippin.binder.*;
import java.io.*;


public class Refclk
	extends Card
{
	
	public Refclk(PClient client,PComponent parent,String name,int id) {
		super(client,parent,name,id,
				new PTypeSet(new String[] {
					"source",
					"pll_los",
					"tbitlos",
					"ebitlos",
				}, new byte[] {
					PAttribute.TYPE_SHORT,
					PAttribute.TYPE_BOOLEAN,
					PAttribute.TYPE_BOOLEAN,
					PAttribute.TYPE_BOOLEAN,
				}),
				new RefClkAlarmStatusTypeSet()
				);
	}

	protected PComponent makeSubComponent(String name,int id) {
		return null;
	}


	public CardType getCardType() {
		return CardType.REF_CLOCK;
	}

	public Class getAlarmTypeSetClass() {
		return RefClkAlarmStatusTypeSet.class;
	}
	
	public ClockSource getSource() throws Exception {
		Integer id = (Integer) get("source");
		return ClockSource.getClockSourceFor(id);
	}


	public void setSource(ClockSource source) throws Exception {
		put("source", source.getValue());
	}


	public boolean pllLOS() throws Exception {
		return ((Boolean) get("pll_los")).booleanValue();
	}


	public boolean tbitLOS() throws Exception {
		return ((Boolean) get("tbitlos")).booleanValue();
	}


	public boolean ebitLOS() throws Exception {
		return ((Boolean) get("ebitlos")).booleanValue();
	}


	protected void doStateChanged(PStateChangeEvent event) {
		boolean pllLOS = false;
		boolean tbitLOS = false;
		boolean ebitLOS = false;

		boolean hasAlarm = false;

		try {
			pllLOS = pllLOS();
			tbitLOS = tbitLOS();
			ebitLOS = ebitLOS();

			hasAlarm = pllLOS;

			if (getSource().equals(ClockSource.T1_BITS) && tbitLOS) {
				hasAlarm = true;
			}
			if (getSource().equals(ClockSource.E1_BITS) && ebitLOS) {
				hasAlarm = true;
			}
		}
		catch (Exception e) {
			// It shouldn't be possible to get PStateChangeEvents
			// if we have dropped the connection.
			e.printStackTrace();
		}

		setHasAlarm(hasAlarm);
	}


	public static class ClockSource implements Serializable {
		private static Vector cSources = new Vector();
		private static Hashtable cSourceHash = new Hashtable();

		static public final ClockSource INTERNAL = new ClockSource("Internal", 2);
		static public final ClockSource E1_BITS = new ClockSource("E1 BITS", 1);
		static public final ClockSource T1_BITS = new ClockSource("T1 BITS", 0);
		static public final ClockSource SLOT_2 = new ClockSource("Slot 2", 18);
		static public final ClockSource SLOT_3 = new ClockSource("Slot 3", 19);
		static public final ClockSource SLOT_4 = new ClockSource("Slot 4", 20);
		static public final ClockSource SLOT_5 = new ClockSource("Slot 5", 21);
		static public final ClockSource SLOT_6 = new ClockSource("Slot 6", 22);
		static public final ClockSource SLOT_7 = new ClockSource("Slot 7", 23);
		static public final ClockSource SLOT_8 = new ClockSource("Slot 8", 24);
		static public final ClockSource SLOT_9 = new ClockSource("Slot 9", 25);
		static public final ClockSource SLOT_10 = new ClockSource("Slot 10", 26);
		static public final ClockSource SLOT_11 = new ClockSource("Slot 11", 27);
		static public final ClockSource SLOT_12 = new ClockSource("Slot 12", 28);
		static public final ClockSource SLOT_13 = new ClockSource("Slot 13", 29);
		static public final ClockSource SLOT_14 = new ClockSource("Slot 14", 30);
		static public final ClockSource SLOT_15 = new ClockSource("Slot 15", 31);

		String mName;
		Integer mValue;

		static public ClockSource getClockSourceFor(Integer id) {
			return (ClockSource) cSourceHash.get(id);
		}

		static public Vector getClockSources() {
			return cSources;
		}

		private ClockSource(String name, int value) {
			mName = name;
			mValue = new Integer(value);

			cSources.addElement(this);
			cSourceHash.put(mValue, this);
		}

		public String toString() {
			return mName;
		}

		public Integer getValue() {
			return mValue;
		}
	}
public AlarmType getAlarmType(Integer id) {
		return RefClkAlarmType.getInstanceFor(id);
	}
	
	static public class RefClkAlarmType implements AlarmType, Serializable {
		private static Vector cInstances = new Vector();
		private static Hashtable cInstanceHash = new Hashtable();
		
		static public final RefClkAlarmType ALARM_UNKOWN = 
				new RefClkAlarmType("UNKNOWN", new Integer(-1));
		
			
		String mName;
		Integer mValue;
	
		static public RefClkAlarmType getInstanceFor(Integer id) {
			RefClkAlarmType type = (RefClkAlarmType)cInstanceHash.get(id);
			if (type == null) {
				type = ALARM_UNKOWN;
			}
			return type;
		}
	
		static public Vector getInstances() {
			return cInstances;
		}
	
		private RefClkAlarmType(String name, Integer value) {
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
			if (o instanceof RefClkAlarmType) {
				RefClkAlarmType type = (RefClkAlarmType)o;
				return type.getValue().equals(getValue());
			}
			return false;
		}
		
	}
	
	
	/*
	AIS-P - set H1 & H2 in TOH to FF
	RDI-P - set G1 in POH to 08
	*/
	
	static public class RefClkAlarmStatusTypeSet extends TypeSet {
	
		public RefClkAlarmStatusTypeSet() {
			super(new TypeSetElement[] {

					new TypeSetElement("UNKNOWN", IndicatorStatus.class, true),
				});
		}
	}
	
}
