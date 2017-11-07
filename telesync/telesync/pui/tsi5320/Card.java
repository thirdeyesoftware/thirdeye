package telesync.pui.tsi5320;



import pippin.pui.*;
import java.util.*;
import pippin.binder.*;
import pippin.util.*;

public abstract class Card extends PComponent
		implements PStateChangeListener, PAlarmEventListener  {

	private Vector mCardEventListeners = new Vector();
	private boolean mHasAlarm = false;
	private boolean mHasNewEvents = false;
	protected Envelope mAlarmStatusEnvelope;
		
	public Card() {
		init();
	}
	
	private TypeSet mAlarmStatusTypeSet;
	

	public Card(PClient client,PComponent parent,String name,
			PTypeSet typeset, TypeSet alarmStatusTypeSet) {
		super(client, parent, name, typeset);
		mAlarmStatusTypeSet = alarmStatusTypeSet;
		init();
	}


	public Card(PClient client,PComponent parent,
			String name,int id,PTypeSet typeset, 
			TypeSet alarmStatusTypeSet) {
		super(client, parent, name, id, typeset);
		mAlarmStatusTypeSet = alarmStatusTypeSet;
		init();
	}


	private void init() {
		try {
			addStateChangeListener(this);
			mAlarmStatusEnvelope = new Envelope(mAlarmStatusTypeSet);
			addAlarmEventListener(this);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Error(e.toString());
		}
		doStateChanged(
				new PStateChangeEvent(this, new PAttributeSet(getTypeSet())));
	}


	abstract public CardType getCardType();
	public Mode getMode() throws Exception {
		return null;
	}
	

	protected void setHasAlarm(boolean hasAlarm) {
		if (mHasAlarm != hasAlarm) {
			mHasAlarm = hasAlarm;
			fireCardEvent();
		}
	}

	public boolean hasAlarm() {
		Enumeration en = mAlarmStatusEnvelope.getElementNames(false);
		boolean alarm = false;
		while (en.hasMoreElements()) {
			IndicatorStatus status = (IndicatorStatus)
				mAlarmStatusEnvelope.getElement((String)en.nextElement());
			if (status != null) {
				if (status.getError()) {
					alarm = true;
					break;
				}
			}
		}
		return (alarm || mHasAlarm);
	}
	
	public synchronized void setHasNewEvents(boolean hasNewEvents) {
		if (mHasNewEvents != hasNewEvents) {
			mHasNewEvents = hasNewEvents;
			fireCardEvent();
		}
	}

	public boolean hasNewEvents() {
		return mHasNewEvents;
	}
	public void resetHasNewEvents() {
		mHasNewEvents = false;
	}
	


	public void addCardEventListener(CardEventListener listener) {
		synchronized (mCardEventListeners) {
			if (!mCardEventListeners.contains(listener)) {
				mCardEventListeners.addElement(listener);
			}
		}

		listener.cardEventNotify(new CardEvent(this));
	}


	public void removeCardEventListener(CardEventListener listener) {
		mCardEventListeners.removeElement(listener);
	}


	protected void fireCardEvent() {
		CardEvent ce = new CardEvent(this);

		for (Enumeration en = mCardEventListeners.elements();
				en.hasMoreElements(); ) {
			((CardEventListener) en.nextElement()).cardEventNotify(ce);
		}
	}

	public final void stateChanged(PStateChangeEvent e) {
		setHasNewEvents(true); 
		
		doStateChanged(e);
	}

	public void alarmNotify(PAlarmEvent event) {
		
		//System.out.println("Card.alarmNotify()." + event.getAttributes().toString());		
		//System.out.println("for " + getName());
		
		Integer id = ((Integer)event.getAttributes().getValue("id"));
		AlarmType type = getAlarmType(id);
		IndicatorStatus status = new IndicatorStatus();
		status.setError(((Boolean)event.getAttributes().getValue("state")).booleanValue());
		status.setHistory(((Boolean)event.getAttributes().getValue("history")).booleanValue());
		
		mAlarmStatusEnvelope.putElement(type.getName(), status);
		fireCardEvent();
		
	}

	public Envelope getAlarmStatusEnvelope() {
		if (mAlarmStatusEnvelope == null) {
			try {
				mAlarmStatusEnvelope = new Envelope(mAlarmStatusTypeSet);
			}
			catch (Exception e) {
				System.out.println(e.toString());
			}
		}
		return mAlarmStatusEnvelope;
	}
	
	abstract protected void doStateChanged(PStateChangeEvent e);
	
	abstract protected AlarmType getAlarmType(Integer id);
	
}
