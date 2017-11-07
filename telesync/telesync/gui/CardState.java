////
//
// Telesync 5320 Project
//
//

package telesync.gui;



import java.util.*;



public class CardState {
	public static final CardState BASE = new CardState(0, "Base");
	public static final CardState EVENT = new CardState(1, "Event");
	public static final CardState ALERT = new CardState(2, "Alert");
	public static final CardState ALARM = new CardState(3, "Alarm");

	private Integer mStateID;
	private String mName;

	private CardState(int stateID, String name) {
		mStateID = new Integer(stateID);
		mName = name;
	}


	public boolean equals(Object o) {
		if (o instanceof CardState) {
			CardState cs = (CardState) o;
			return cs.mStateID.equals(mStateID);
		} else {
			return false;
		}
	}


	public int hashCode() {
		return mStateID.hashCode();
	}


	public String toString() {
		StringBuffer sb = new StringBuffer("CardState(");

		sb.append(mStateID.toString());
		sb.append(",");
		sb.append(mName);
		sb.append(")");

		return sb.toString();
	}
}
