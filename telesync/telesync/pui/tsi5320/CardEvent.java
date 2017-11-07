package telesync.pui.tsi5320;



import pippin.pui.*;
import java.util.*;



public class CardEvent extends PEvent {

	boolean mHasAlarm = false;
	boolean mHasNewEvents = false;

	public CardEvent(Card card) {
		super(card);

		mHasAlarm = card.hasAlarm();
		mHasNewEvents = card.hasNewEvents();
	}


	public boolean hasAlarm() {
		return mHasAlarm;
	}


	public boolean hasNewEvents() {
		return mHasNewEvents;
	}


	public String toString() {
		return "CardEvent(" + hasAlarm() + "," + hasNewEvents() + ")";
	}
}
