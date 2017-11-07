package telesync.pui.tsi5320;

import pippin.pui.*;
import pippin.binder.*;
import pippin.util.*;
import java.util.*;
import java.io.*;

public class Booting
	extends Card
{
	public Booting(PClient client,PComponent parent,String name,int id) {
		super(client,parent,name,id,
				new PTypeSet(new String[] {
					"cardType",
					"status",
					"failed",
					"failMsg",
				}, new byte[] {
					PAttribute.TYPE_SHORT,
					PAttribute.TYPE_STRING,
					PAttribute.TYPE_BOOLEAN,
					PAttribute.TYPE_STRING,
				}),
				new BootingAlarmStatusTypeSet()
				);
	}

	protected PComponent makeSubComponent(String name,int id) {
		return null;
	}


	public CardType getCardType() {
		return CardType.BOOT_IN_PROCESS;
	}


	protected void doStateChanged(PStateChangeEvent e) {
	}

	public AlarmType getAlarmType(Integer id) {
		return BootingAlarmType.getInstanceFor(id);
	}	
		
	static public class BootingAlarmType implements AlarmType, Serializable {
		private static Vector cInstances = new Vector();
		private static Hashtable cInstanceHash = new Hashtable();

		static public final BootingAlarmType ALARM_UNKOWN = 
				new BootingAlarmType("UNKNOWN", new Integer(-1));
		
		String mName;
		Integer mValue;

		static public BootingAlarmType getInstanceFor(Integer id) {
			BootingAlarmType type = (BootingAlarmType)cInstanceHash.get(id);
			if (type == null) {
				type = ALARM_UNKOWN;
			}
			return type;
		}

		static public Vector getInstances() {
			return cInstances;
		}

		private BootingAlarmType(String name, Integer value) {
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
			if (o instanceof BootingAlarmType) {
				BootingAlarmType type = (BootingAlarmType)o;
				return type.getValue().equals(getValue());
			}
			return false;
		}
			
	}
	static public class BootingAlarmStatusTypeSet extends TypeSet {
	
		public BootingAlarmStatusTypeSet() {
			super(new TypeSetElement[] {
					new TypeSetElement("UNKNOWN", IndicatorStatus.class, true),
				});
		}
	}	
	
	public Class getAlarmTypeSetClass() {
		return BootingAlarmStatusTypeSet.class;
	}
	
	
	
}
