////
// //
// PippinSoft
//
//

package pippin.binder;

import java.util.*;
import java.io.*;



/**
* Base implementation of BinderSet methods to be used as a delegate
* for classes implementing BinderSet.
*/
public class BinderSetSupport implements BinderSet {

	/**
	* The BinderSet that is using this delegate.
	*/
	private BinderSet mBinderSet;


	/**
	* The TypeSet to be used by this BinderSet.
	*/
	private TypeSet mTypeSet;

	
	private Hashtable mBinders = new Hashtable();
	private Hashtable mNames = new Hashtable();
	private Vector mSortedNames = new Vector();
	private Vector mSortedBinders = new Vector();
	private Vector mFlagListeners = new Vector();
	
	/**
	* The set of element names that are required for this
	* BinderSetSupport instance.  Elements are required if they are in this
	* set -or- if they are considered required by mTypeSet.
	*/
	private Vector mRequiredNames = new Vector();



	/**
	* @param typeSetClass
	* The subclass of TypeSet to be used by this BinderSet.
	*/
	public BinderSetSupport(BinderSet binderSet, TypeSet typeSet) {
		mBinderSet = binderSet;
		mTypeSet = typeSet;
	}


	private void checkType(String name, Object object) {
		checkType(name, object.getClass());
	}


	private void checkType(String name, Class type) {
		if (!mTypeSet.isValidType(name, type)) {
			throw new ClassCastException(name + ": " +
					type.getName() + " is not " +
					mTypeSet.getType(name).getName());
		}
	}


	public Class getTypeSetClass() {
		return mTypeSet.getClass();
	}


	public TypeSet getTypeSet() {
		return mTypeSet;
	}


	/**
	* Caution:  this method is not sensitive to incorrect TypeSets.
	* If the specified Envelope doesn't contain any names
	* matching those in this BinderSetSupport's TypeSet,
	* none of its elements will be set on this BinderSetSupport.
	*/
	public void setEnvelope(Envelope e) {
		setEnvelope(e,false);
	}

	public void setEnvelope(Envelope e, boolean init) {
		
		Envelope delta = getEnvelope().getDelta(e);
		
		boolean raiseFlag = false;
		
		if (delta==null) return;
				
		for (Enumeration en = getBinderNames(); en.hasMoreElements(); ) {
			String name = (String) en.nextElement();
			
			if (delta.getTypeSet().hasName(name) && delta.hasElement(name)) {
				Binder b = getBinder(name);
				Object o = delta.getElement(name);

				checkType(name, o);
				if (!init) {
					if (b instanceof StateChangeBinder) {
						long time = System.currentTimeMillis();
						StateChangeBinder scb = (StateChangeBinder)b;
						if (scb.indicatorEnabled()) {
							
							scb.setBoundValue(o,time);
							raiseFlag = scb.isIndicatorRaised();
							continue;
						} 
					}
				} 
				
				b.setBoundValue(o);
				
			} //end for
			if (raiseFlag) raiseFlag();
		}
	}
	
	public Envelope getEnvelope() {
		Envelope e = new Envelope(getTypeSetClass());

		for (Enumeration en = getBinderNames(); en.hasMoreElements(); ) {
			String name = (String) en.nextElement();
			Binder b = getBinder(name);
			Object value;

			value = b.getBoundValue();
			if (value != null) {
				e.putElement(name, (Serializable) value);
			}
		}

		return e;
	}


	public void loadEnvelope(Envelope e) {
		if (!getTypeSetClass().equals(e.getTypeSetClass())) {
			throw new ClassCastException(
					e.getTypeSetClass().getName() + " is not " +
					getTypeSetClass().getName());
		}

		for (Enumeration en = getBinderNames(); en.hasMoreElements(); ) {
			String name = (String) en.nextElement();
			Binder b = getBinder(name);
			Object value;

			value = b.getBoundValue();
			if (value != null) {
				e.putElement(name, (Serializable) value);
			}
		}
	}


	/**
	* Sets the Binder to the default value if a default is present
	* in the TypeSet.
	*/
	public void addBinder(String name, Binder binder) {
		checkType(name, binder.getBoundType());
		
		if (mBinders.containsKey(name)) {
			throw new ExtantBinderException(name);
		}
		mBinders.put(name, binder);
		mNames.put(binder, name);
		mSortedNames.addElement(name);
		mSortedBinders.addElement(binder);

		binder.setBinderSet(mBinderSet);

		if (mTypeSet.hasDefault(name)) {
			binder.setBoundValue(mTypeSet.getDefaultValue(name));
		}
	}
	

	public boolean hasBinder(String name) {
		return mBinders.containsKey(name);
	}


	public Binder getBinder(String name) {
		return (Binder) mBinders.get(name);
	}


	public Object getBoundValue(String name) {
		Binder b = (Binder) mBinders.get(name);
		if (b == null) {
			throw new NoSuchBinderException(name);
		} else {
			return b.getBoundValue();
		}
	}
	public void setBinderVisible(String name, boolean visability) {
		Object b = mBinders.get(name);
		if (b != null) {
			if (b instanceof FormField) {
				((FormField)b).setVisible(visability);
			}
		}
	
	}
	
	public void setBoundValue(String name, Object value) {
		Binder b = (Binder) mBinders.get(name);
		if (b == null) {
			throw new NoSuchBinderException(name);
		} else {
			b.setBoundValue(value);
		}
	}


	public void setDefaultBoundValue(String name) {
		Binder b = (Binder) mBinders.get(name);
		if (b == null) {
			throw new NoSuchBinderException(name);
		} else {
			if (mTypeSet.hasDefault(name)) {
				b.setBoundValue(mTypeSet.getDefaultValue(name));
			}
		}
	}


	public String getName(Binder binder) {
		return (String) mNames.get(binder);
	}


	public Binder getBinderAt(int index) {
		String name = (String) mSortedNames.elementAt(index);

		return getBinder(name);
	}


	public void replaceBinder(String name, Binder binder) {
		checkType(name, binder.getBoundType());

		Binder oldBinder = getBinder(name);
		int index = mSortedBinders.indexOf(oldBinder);

		mBinders.put(name, binder);
		mNames.remove(oldBinder);
		mNames.put(binder, name);
		mSortedBinders.setElementAt(binder, index);
	}


	public void removeBinder(String name) {
		Binder binder = getBinder(name);

		mBinders.remove(name);
		mSortedNames.removeElement(name);
		mSortedBinders.removeElement(binder);
		mNames.remove(binder);
	}
	

	public void addBinderSet(BinderSet set) {
		for (Enumeration en = set.getBinderNames(); en.hasMoreElements(); ) {
			String name = (String) en.nextElement();
			Binder b = set.getBinder(name);

			addBinder(name, b);
		}
	}


	public Enumeration getBinderNames() {
		return mSortedNames.elements();
	}


	public Enumeration getBinders() {
		return mSortedBinders.elements();
	}


	public void setEnabled(boolean enabled) {
		for (Enumeration en = mBinders.elements(); en.hasMoreElements(); ) {
			Binder b = (Binder) en.nextElement();

			b.setEnabled(enabled);
		}
	}


	public int getBinderSetSize() {
		return mBinders.size();
	}


	public void cancel() {
		for (Enumeration en = mBinders.elements(); en.hasMoreElements(); ) {
			Binder b = (Binder) en.nextElement();

			b.cancel();
		}
	}


	public void clear() {
		for (Enumeration en = mBinders.elements(); en.hasMoreElements(); ) {
			Binder b = (Binder) en.nextElement();

			b.clear();
		}
	}


	public Vector getRequiredNames(Envelope env) {
		if (!getTypeSetClass().equals(env.getTypeSetClass())) {
			throw new ClassCastException(
					env.getTypeSetClass().getName() + " is not " +
					getTypeSetClass().getName());
		}

		Vector requiredNames = new Vector();

		for (Enumeration en = getTypeSet().getRequiredNames();
				en.hasMoreElements(); ) {
			String name = (String) en.nextElement();
			if (!env.hasElement(name)) {
				requiredNames.addElement(name);
			}
		}

		for (Enumeration en = mRequiredNames.elements();
				en.hasMoreElements(); ) {
			String name = (String) en.nextElement();
			if (!env.hasElement(name) && !requiredNames.contains(name)) {
				requiredNames.addElement(name);
			}
		}

		return requiredNames;
	}


	public void setRequiredName(String name, boolean required) {
		getTypeSet().checkName(name);

		if (required) {
			if (!mRequiredNames.contains(name)) {
				mRequiredNames.addElement(name);
			}
		} else {
			mRequiredNames.removeElement(name);
		}
	}
	
	
	public void clearBinderFlags() {
		for (Enumeration en = getBinderNames(); en.hasMoreElements(); ) {
					String name = (String) en.nextElement();
			Binder b = getBinder(name);
			if (b instanceof StateChangeBinder) {
				((StateChangeBinder)b).resetChangeIndicator();
			}
			
		}
		for (Iterator it = mFlagListeners.iterator();it.hasNext();) {
						((FlagListener)it.next()).clearFlag();
		}
	}
	public void raiseFlag() {

		for (Iterator it = mFlagListeners.iterator();it.hasNext();) {
			((FlagListener)it.next()).raiseFlag();
		}
	}
	
	
	public void addFlagListener( FlagListener l ) {
		mFlagListeners.add(l);
	}
	
}
