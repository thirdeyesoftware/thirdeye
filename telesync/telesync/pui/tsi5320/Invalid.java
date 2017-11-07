package telesync.pui.tsi5320;

import pippin.pui.*;
import pippin.util.*;
import pippin.binder.*;

import java.util.*;
import java.io.*;

public class Invalid
	extends Card
{
	public Invalid(PClient client,PComponent parent,String name,int id) {
		super(client,parent,name,id,
				new PTypeSet(new String[] {
					"cardType",
				}, new byte[] {
					PAttribute.TYPE_SHORT,
				}), new InvalidAlarmStatusTypeSet());
	}

	protected PComponent makeSubComponent(String name,int id) {
		return null;
	}


	public CardType getCardType() {
		return CardType.INVALID;
	}


	protected void doStateChanged(PStateChangeEvent e) {
	}


	public AlarmType getAlarmType(Integer id) {
		return InvalidAlarmType.getInstanceFor(id);
	}	
		
	static public class InvalidAlarmType implements AlarmType, Serializable {
		private static Vector cInstances = new Vector();
		private static Hashtable cInstanceHash = new Hashtable();

		static public final InvalidAlarmType ALARM_UNKOWN = 
				new InvalidAlarmType("UNKNOWN", new Integer(-1));
		
		String mName;
		Integer mValue;

		static public InvalidAlarmType getInstanceFor(Integer id) {
			InvalidAlarmType type = (InvalidAlarmType)cInstanceHash.get(id);
			if (type == null) {
				type = ALARM_UNKOWN;
			}
			return type;
		}

		static public Vector getInstances() {
			return cInstances;
		}

		private InvalidAlarmType(String name, Integer value) {
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
			if (o instanceof InvalidAlarmType) {
				InvalidAlarmType type = (InvalidAlarmType)o;
				return type.getValue().equals(getValue());
			}
			return false;
		}
			
	}
	static public class InvalidAlarmStatusTypeSet extends TypeSet {
	
		public InvalidAlarmStatusTypeSet() {
			super(new TypeSetElement[] {
					new TypeSetElement("UNKNOWN", IndicatorStatus.class, true),
				});
		}
	}	
	
	public Class getAlarmTypeSetClass() {
		return InvalidAlarmStatusTypeSet.class;
	}
	
	
}
