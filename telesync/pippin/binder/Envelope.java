////
// //
// PippinSoft
//
//

package pippin.binder;


import java.io.*;
import java.util.*;



/**
* A transportable container for collections of named
* Serializable Objects.
*/
public class Envelope implements Cloneable, Serializable {

	private static final long serialVersionUID = 1999041602L;

	/**
	* The set of active elements in this Envelope.  The names
	* of these elements form a subset of the names in mTypeSet.
	* The types of these elements must match the corresponding
	* types as defined in mTypeSet.
	*/
	protected Hashtable mElements = new Hashtable();


	/**
	* A set of named types that are acceptable but not necessary
	* for this Envelope.
	*
	* This set is transient to conserve bandwidth, and must be
	* recreated through initTypeSet() as needed.
	*/
	transient TypeSet mTypeSet;


	/**
	* The subclass of TypeSet to be used by this Envelope.
	*/
	Class mTypeSetClass;


	/**
	* The set of element names that are required for this
	* Envelope instance.  Elements are required if they are in this
	* set -or- if they are considered required by mTypeSet.
	*
	* This set is transient to conserve bandwidth, and must be
	* recreated through initRequiredNames() as needed.
	*/
	transient Vector mRequiredNames;



	/**
	* @param typeSetClass
	* The subclass of TypeSet to be used by this Envelope.
	*/
	public Envelope(Class typeSetClass) {
		if (!TypeSet.class.isAssignableFrom(typeSetClass)) {
			throw new ClassCastException(typeSetClass.getName());
		}

		mTypeSetClass = typeSetClass;
	}


	public Envelope(TypeSet typeSet) {
		mTypeSet = typeSet;
		mTypeSetClass = typeSet.getClass();
	}


	public Class getTypeSetClass() {
		return mTypeSetClass;
	}


	public TypeSet getTypeSet() {
		initTypeSet();

		return mTypeSet;
	}


	private void initTypeSet() {
		if (mTypeSet == null) {
			try {
				mTypeSet = (TypeSet) mTypeSetClass.newInstance();
			}
			catch (Exception e) {
				throw new EnvelopeException(e.toString() + " " +
						mTypeSetClass.getName());
			}
		}
	}


	private void initRequiredNames() {
		if (mRequiredNames == null) {
			mRequiredNames = new Vector();
		}
	}


	private void checkTypeName(String name) {
		initTypeSet();

		mTypeSet.checkName(name);
	}


	private void checkType(String name, Object object) {
		checkTypeName(name);

		if (object != null && !mTypeSet.isValidType(name, object.getClass())) {
			throw new ClassCastException(name + ": " +
					object.getClass().getName() + " is not " +
					mTypeSet.getType(name).getName());
		}
	}


	private Object getDefaultValue(String name) {
		initTypeSet();

		return mTypeSet.getDefaultValue(name);
	}


	public void putElement(String name, Serializable object) {
		checkType(name, object);

		if (object != null) {
			mElements.put(name, object);
		} else {
			removeElement(name);
		}
	}


	public void removeElement(String name) {
		checkTypeName(name);
		mElements.remove(name);
	}


	/**
	* Gets a required element.
	*/
	public Object getElement(String name) {
		return getElement(name, true);
	}


	public Hashtable getElementsTable() {
		return mElements;
	}


	/**
	* @param required
	* Indicates that the named element must be present among the
	* Envelope's active elements, or in the TypeSet's defaults.
	*/
	public Object getElement(String name, boolean required) {
		checkTypeName(name);

		Object value = mElements.get(name);
		if (value == null) {
			value = getDefaultValue(name);
		}

		if (value == null && required) {
			throw new NoSuchElementException(name);
		}

		return value;
	}


	/**
	* Returns the names of populated elements in this Envelope.
	*/
	public Enumeration getElementNames() {
		return getElementNames(true);
	}


	public Enumeration getElementNames(boolean useDefaults) {
		Vector elementNames = new Vector();

		for (Enumeration en = getTypeSet().getNames(); en.hasMoreElements(); ) {
			String name = (String) en.nextElement();
			if (hasElement(name, useDefaults)) {
				elementNames.addElement(name);
			}
		}

		return elementNames.elements();
	}


	public boolean hasElement(String name) {
		return hasElement(name, true);
	}


	public boolean hasElement(String name, boolean checkDefault) {
		boolean result = false;

		checkTypeName(name);

		if (checkDefault) {
			result = mElements.containsKey(name) || mTypeSet.hasDefault(name);
		} else {
			result = mElements.containsKey(name);
		}

		return result;
	}


	/**
	* @return
	* A Vector of the names of elements that are listed in the
	* TypeSet or in mRequiredNames as required, but as yet have no
	* values assigned to them in this Envelope.
	*
	* @deprecated
	* Use BinderSet.getRequiredNames().
	*/
	public Vector getRequiredNames() {
		initTypeSet();

		Vector requiredNames = new Vector();

		for (Enumeration en = mTypeSet.getRequiredNames();
				en.hasMoreElements(); ) {
			String name = (String) en.nextElement();
			if (!hasElement(name)) {
				requiredNames.addElement(name);
			}
		}

		if (mRequiredNames != null) {
			for (Enumeration en = mRequiredNames.elements();
					en.hasMoreElements(); ) {
				String name = (String) en.nextElement();
				if (!hasElement(name) && !requiredNames.contains(name)) {
					requiredNames.addElement(name);
				}
			}
		}

		return requiredNames;
	}


	/**
	* Sets the 'required' status of the named element.  This
	* is an Envelope-instance status that augments the
	* required status as described by the TypeSet.  An element is
	* required if the Envelope -or- the TypeSet indicates that it
	* is required.
	*
	* @deprecated
	* Use BinderSet.setRequiredName().
	*/
	public void setRequiredName(String name, boolean required) {
		checkTypeName(name);

		if (required) {
			initRequiredNames();
			if (!mRequiredNames.contains(name)) {
				mRequiredNames.addElement(name);
			}
		} else {
			if (mRequiredNames != null) {
				mRequiredNames.removeElement(name);
			}
		}
	}


	public int size() {
		return mElements.size();
	}


	/**
	* Adds the elements of Envelope e to this Envelope.  Forbids replacement
	* of existing elements.
	*/
	public void addEnvelope(Envelope e) {
		for (Enumeration en = e.getElementNames(); en.hasMoreElements(); ) {
			String elementName = (String) en.nextElement();

			if (this.hasElement(elementName)) {
				throw new ExtantElementException(elementName);
			} else {
				putElement(elementName, (Serializable) e.getElement(elementName));
			}
		}
	}


	/**
	* Adds and replaces elements in this Envelope with elements from
	* Envelope e.  Unpopulated elements in e are ignored.
	* Defaults in e are ignored.
	*/
	public void mergeEnvelope(Envelope e) {
		for (Enumeration en = e.getElementNames(false); en.hasMoreElements(); ) {
			String elementName = (String) en.nextElement();

			putElement(elementName, (Serializable) e.getElement(elementName));
		}
	}


	/**
	* Adds and replaces elements in this Envelope with elements from
	* Envelope e.  Ingores elements from e that are not
	* in this Envelope's TypeSet.  Ignores default values in e.
	*/
	public void selectiveMergeEnvelope(Envelope e) {
		TypeSet ts = getTypeSet();

		for (Enumeration en = e.getElementNames(false); en.hasMoreElements(); ) {
			String elementName = (String) en.nextElement();

			if (ts.hasName(elementName)) {
				putElement(elementName, (Serializable) e.getElement(elementName));
			}
		}
	}


	public boolean equals(Object o) {
		if (o instanceof Envelope) {
			Envelope e = (Envelope) o;
			if (mTypeSetClass.equals(e.getTypeSetClass()) &&
					size() == e.size()) {
				TypeSet ts = getTypeSet();
				for (Enumeration en = getElementNames(false);
						en.hasMoreElements(); ) {
					String tag = (String) en.nextElement();
					if (e.hasElement(tag, false)) {
						Class type = ts.getType(tag);
						if (type.equals(int[].class)) {
							int[] a,b;
							a = (int[]) getElement(tag);
							b = (int[]) e.getElement(tag);
							if (a.length == b.length) {
								for (int i = 0; i < a.length; ++i) {
									if (a[i] != b[i]) {
										return false;
									}
								}
							} else {
								return false;
							}
						} else {
							if (!getElement(tag).equals(e.getElement(tag))) {
								return false;
							}
						}
					} else {
						return false;
					}
				}
			} else {
				return false;
			}
		} else {
			return false;
		}

		return true;
	}


	public Envelope getDelta(Envelope newEnvelope) {
		if (getTypeSetClass() != newEnvelope.getTypeSetClass()) {
			throw new ClassCastException(
					newEnvelope.getTypeSetClass().getName() + " is not " +
					getTypeSetClass().getName());
		}

		Envelope delta = new Envelope(getTypeSetClass());

		for (Enumeration en = newEnvelope.getElementNames(false);
				en.hasMoreElements(); ) {
			String elementName = (String) en.nextElement();

			checkTypeName(elementName);


			if (!newEnvelope.getElement(elementName).equals(
					this.getElement(elementName, false))) {
				
				delta.putElement(elementName,
						(Serializable) newEnvelope.getElement(elementName));
			}
		}

		if (delta.size() > 0) {
			return delta;
		} else {
			return null;
		}
	}


	public String toString() {
		return mElements.toString();
	}


	public Object clone() {
		Envelope c = new Envelope(this.getTypeSet());

		c.mergeEnvelope(this);

		return (Object) c;
	}


	/**
	* Resets this Envelope so that it contains no elements.
	*/
	public void clear() {
		mElements.clear();
	}
}
