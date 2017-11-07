////
// //
// PippinSoft
//
//

package pippin.pui;

import java.util.*;
import java.io.*;

public class PComponent {
	protected int mID;
	protected Vector mListeners = new Vector();
	protected Vector mAlarmListeners = new Vector();
	
	protected boolean mSubscribed;

	protected PTypeSet mTypeSet;
	protected Hashtable mAttributes;

	protected PClient mClient;
	protected PComponent mParent;
	protected String mName;

	protected Hashtable mSubComps;
	protected boolean mSubsLoaded = false;

	protected PAttributeSet mAttrs;

	protected PAttributeSet mChanges;
	protected PAlarmTypeSet mAlarmTypeSet;
	
		
	/**
	* The default behavior of a PComponent is to
	* incrementally persist itself for every mutator call.
	*/
	protected boolean mSyncPuts = true;


	// Unsolicited stateChangeEvent members
	protected Vector mUnsolicitedListeners = new Vector();
	
	public PComponent() {
	}
	public PComponent(PClient client, PComponent parent, String name, 
			PTypeSet typeset, PAlarmTypeSet alarmSet) {
		
		this(client,parent,name, typeset);
		mAlarmTypeSet = alarmSet;
	}
	
	public PComponent(PClient client,PComponent parent,String name,
			PTypeSet typeset) {
		this(client,parent,name,-1,typeset);
	}


	public PComponent(PClient client,PComponent parent,
			String name,int id,PTypeSet typeset) {
		mTypeSet = typeset;
		mClient = client;
		mName = name;
		mParent = parent;
		setId(id);

		mClient.mDef.addComponent(this,id);
		mAlarmTypeSet = new PAlarmTypeSet();
	}
	
	public synchronized PClient getPClient() {
	
		return mClient;
	}
	
	public PComponent getParent() {
		return mParent;
	}
	
	public PTypeSet getTypeSet() {
		return mTypeSet;
	}

	protected synchronized void dispatchAlarms(PMessage msg) {
		
		PAttributeSet attrs = msg.getAttributeSet();
		PAlarmEvent event = new PAlarmEvent(this, attrs);
		Enumeration en = mAlarmListeners.elements();
		while (en.hasMoreElements()) {
			PAlarmEventListener listener = 
				(PAlarmEventListener)en.nextElement();
			listener.alarmNotify(event);
		}
		
	}
	
	protected synchronized void dispatch(PMessage msg) {
		switch (msg.getType()) {
			case PMessage.TYPE_STATE_CHG:
				PAttributeSet attrs = msg.getAttributeSet();

				mAttrs.merge(attrs);

				PStateChangeEvent event = new PStateChangeEvent(this,attrs);

				/**
				 * && LOOK && 
				 * We're sending out delta attributes.  what are we
				 * going to do about the race condition between
				 * the init and the subscription.
				 */

				Enumeration e = mListeners.elements();
				while (e.hasMoreElements()) {
					PStateChangeListener lsnr =
							(PStateChangeListener)e.nextElement();

					lsnr.stateChanged(event);
				}
				break;
			case PMessage.TYPE_ALARM:
				System.out.println("PComponent.dispatch():alarm");
				dispatchAlarms(msg);
				break;
			default:
				System.out.println("Message Type " + msg.getType() + 
					"unsupported.");
		}
		
	}


	public int getID() {
		return mID;
	}


	protected void setId(int id) {
		mID = id;
	}


	public String getName() {
		return mName;
	}


	protected synchronized void loadSubComponents()
			throws IOException,PApplianceException {
		loadSubComponents(false);
	}


	protected synchronized void loadSubComponents(boolean debug)
			throws IOException,PApplianceException {

		
		if (debug) {
			//System.out.println("PComponent.loadSubComponents: " + getName());
		}

		if (mSubsLoaded) {
			if (debug) {
				System.out.println("PComponent.loadSubComponents: " + getName() +
						" already loaded");
			
			}
			return;
		}

		if (mSubComps == null) {
			mSubComps = new Hashtable();
		}

		PMessage msg = null;

		msg = new PMessage(this, PMessage.TYPE_LIST_SUBS);
		
		msg = mClient.sendCommand(msg);
			
		for ( Enumeration e = msg.getAttributes() ; e.hasMoreElements() ; ) {
			PAttribute attr = (PAttribute)e.nextElement();

			if (attr.getType() != PAttribute.TYPE_STRING) {
				// what todo ??
				continue;
			}

			String name = (String)attr.getValue();
			
			PComponent comp = makeSubComponent(name, attr.getID());

			if (comp == null) {
				throw new RuntimeException("No sub component name=" + name +
						" attr.getID=" + attr.getID());
			}

			mSubComps.put(name,comp);
			
		}

		mSubsLoaded = true;
		
		
	}

	protected PComponent makeSubComponent(String name,int id) {
		
		return new PComponent(mClient,this,name,id,null);
	}


	public Enumeration listSubComponents()
			throws IOException,PApplianceException {
		loadSubComponents();
		return mSubComps.keys();
	}


	public Enumeration getSubComponents()
			throws IOException,PApplianceException {
		return getSubComponents(false);
	}


	public Enumeration getSubComponents(boolean debug)
			throws IOException,PApplianceException {
		loadSubComponents(debug);
		return mSubComps.elements();
	}


	public PComponent getSubComponent(String name)
			throws IOException, PApplianceException {

		return getSubComponent(name, true);
	}


	public PComponent getSubComponent(String name, boolean debug)
			throws IOException, PApplianceException {
		//System.out.println("PComponent.getSubComponent(" + name +"," + debug + ")");
		if (!mSubsLoaded) loadSubComponents(debug);
		return (PComponent) mSubComps.get(name);
	}


	/**
	* @return the number of subcomponents of this PComponent.
	*/
	public int getSize() throws IOException, PApplianceException {
		loadSubComponents();
		return mSubComps.size();
	}


	private PAttributeSet _getAttributes()
			throws IOException,PApplianceException {
		PAttributeSet attrs;

		if (!mSubscribed) {
			PMessage msg = new PMessage(this, PMessage.TYPE_GET_ATTRS);

			msg = mClient.sendCommand(msg);

			attrs = msg.getAttributeSet();
		} else {
			attrs = mAttrs;
		}
		
		return attrs;
	}


	// synchronized wrapper for _getAttributes()
	public synchronized PAttributeSet getAttributes()
			throws IOException,PApplianceException {
		return _getAttributes();
	}


	/**
	* Get the value of a named attribute of this PComponent.
	*
	* @param required indicates the specified attribute must be
	* present in the underlying PAttributeSet, or an Error is thrown.
	*
	* @return the value of the named attribute for this PComponent.
	*/
	public synchronized Object get(String name, boolean required)
			throws IOException,PApplianceException {
		PAttributeSet attrs;

		attrs = _getAttributes();

		PAttribute attr = attrs.getAttribute(name, required);
		if (attr != null) {
			return attr.getValue();
		} else return null;
	}


	/**
	* Get the value of a named attribute of this PComponent.
	*
	* @exception Error the named attribute does not exist.
	*
	* @return the value of the named attribute for this PComponent.
	*/
	public synchronized Object get(String name)
			throws IOException, PApplianceException {
		return get(name, true);
	}


	private void doPut(PAttributeSet attrs)
			throws IOException,PApplianceException {
		PMessage msg = new PMessage(this,PMessage.TYPE_PUT_ATTRS,attrs);
		msg = mClient.sendCommand(msg);
	}


	public void setSynchronousPuts(boolean how) {
		mSyncPuts = how;
	}

	private void _put(String name,Object value,boolean synchronous)
			throws IOException,PApplianceException {

		if (synchronous) {
			PAttributeSet attrs = new PAttributeSet(mTypeSet);
			attrs.putValue(name,value);
			setOutgoingAttributeChanges(attrs);
			doPut(attrs);
		} else {
			if (mChanges == null)
				mChanges = new PAttributeSet(mTypeSet);
			mChanges.putValue(name,value);
		}
	}


	protected synchronized void put(String name,Object value,
			boolean synchronous)
			throws IOException,PApplianceException {
		_put(name,value,synchronous);
	}

	// put an attribute value for this Component
	public synchronized void put(String name,Object value)
			throws IOException,PApplianceException {
		_put(name,value,mSyncPuts);
	}

	public synchronized void sync()
			throws IOException,PApplianceException {
		if (mChanges != null) {
			setOutgoingAttributeChanges(mChanges);
			doPut(mChanges);
			mChanges = null;
		}
	}
	
	protected void clearAlarmHistory() 
			throws IOException, PApplianceException {
		PAttributeSet attrs = new PAttributeSet(new PAlarmTypeSet());
		attrs.putValue("opcode",new Integer(0));
		attrs.putValue("history",Boolean.FALSE);
		attrs.putValue("id",new Integer(8));
		PMessage message = new PMessage(this, PMessage.TYPE_ALARM, attrs);
		mClient.send(message);
		//dispatchAlarms(message);
		
	}
	
		
	
	protected void setOutgoingAttributeChanges( PAttributeSet changes ) { }
	
	
	public synchronized void subscribe(PStateChangeListener lsnr) 
		throws IOException,PApplianceException {
		
		PMessage msg = new PMessage(this, PMessage.TYPE_SUBSCRIBE);
		msg = mClient.sendCommand(msg);
		mAttrs = msg.getAttributeSet();
		mSubscribed = true;
		PStateChangeEvent event = new PStateChangeEvent(this,mAttrs);
		lsnr.stateChanged(event);
		mListeners.addElement(lsnr);
	
	}
	
	public synchronized void addStateChangeListener(PStateChangeListener lsnr)
			throws IOException,PApplianceException {
		
		if (!mSubscribed) {
			mClient.addSubscriptionRequest(this,lsnr);
		}
		else {
			mListeners.addElement(lsnr);
		}
		
		
		
	}
	
	public synchronized void addAlarmEventListener(PAlarmEventListener lsnr) {
		mAlarmListeners.addElement(lsnr);
	}
	
	public synchronized void setAttributes(PAttributeSet attrs) {
		mAttrs = attrs;
	}
	
	public synchronized void setSubscribed(boolean b) {
		mSubscribed = b;
	}
	
	public synchronized void removeStateChangeListener(
			PStateChangeListener lsnr)
			throws IOException,PApplianceException {
		
		boolean allowUnsubscribe = false;
		
		if (mListeners.size() > 0) {
			mListeners.removeElement(lsnr);
			allowUnsubscribe = true;
		}
		
		if (mListeners.size() == 0 && allowUnsubscribe) {
			try {
				dispose();
			}
			catch (Exception ee) {
				throw new PApplianceException(ee.toString());
			}
		}
	}

	public synchronized void removeAlarmEventListener( 
			PAlarmEventListener lsnr) {
		mAlarmListeners.removeElement(lsnr);
	}
	

	private void unsubscribe() throws IOException, PApplianceException {
		System.out.println("PComponent.unsubscribe()-" + getName());
		if (mSubscribed) {
			PMessage msg = new PMessage(this,
					PMessage.TYPE_SUBCANCEL);
			msg = mClient.sendCommand(msg);

			
			mSubscribed = false;
		}
	}


	protected void dispose() throws Exception {
		System.out.println("PComponent.dispose()");
		unsubscribe();
	}


	protected void finalize() throws Throwable {
		System.out.println("PComponent.finalize()");		
		dispose();
	}


	public String toString() {
		return "PComponent(" + mName + "," + mID + ")";
	}
}
