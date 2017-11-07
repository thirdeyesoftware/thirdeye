   //
   //
   // Joseph A. Dudar, Inc   (C) 1999
// //
////

package pippin.binder;



import java.util.*;
import java.io.*;



/**
* A set of named types employed by BinderSets and Envelopes.  TypeSets
* are used to keep a consistent set of names and types between producers
* and consumers of Envelopes.
*
* The intention here is for implementors to subclass TypeSet in order
* to create a fixed, documented set of named types.

* A subclass can populate
* the TypeSet superclass by providing a default constructor that
* stuffs the TypeSet via its TypeSet(TypeSetElement[] elements) constructor.
*/
public abstract class TypeSet implements Serializable {

	private static final long serialVersionUID = 1999041601L;

	public static final TypeSet EMPTY_SET = new EmptyTypeSet();
	Hashtable mElements = new Hashtable();
	Vector mSortedElements = new Vector();
	Vector mSortedNames = new Vector();


	/**
	* Constructor for subclasses that call init() directly
	* in their constructors.
	*/
	protected TypeSet() {
	}


	protected TypeSet(TypeSetElement[] elements) {
		init(elements);
	}


	protected void init(TypeSetElement[] elements) {
		for (int i = 0; i < elements.length; ++i) {
			if (mElements.containsKey(elements[i].getName())) {
				throw new TypeSetException("Duplicate name: " +
						elements[i].getName());
			}

			mElements.put(elements[i].getName(), elements[i]);
			mSortedElements.addElement(elements[i]);
			mSortedNames.addElement(elements[i].getName());
		}
	}


	public TypeSetElement getElement(String name) {
		TypeSetElement e = (TypeSetElement) mElements.get(name);

		if (e == null) {
			throw new TypeSetException(getClass() + " does not contain " + name);
		}

		return e;
	}


	public boolean hasElement(String name) {
		return mElements.containsKey(name);
	}


	protected void checkName(String name) {
		if (!mElements.containsKey(name)) {
			throw new TypeSetException(getClass() + " does not contain " + name);
		}
	}


	public Enumeration getNames() {
		return mSortedNames.elements();
	}


	public Enumeration getElements() {
		return mSortedElements.elements();
	}


	/**
	* @return
	* An Enumeration of the names of all required elements in
	* this TypeSet.
	*/
	public Enumeration getRequiredNames() {
		Vector requiredNames = new Vector();

		for (Enumeration en = mSortedElements.elements();
				en.hasMoreElements(); ) {
			TypeSetElement e = (TypeSetElement) en.nextElement();

			if (e.isRequired()) {
				requiredNames.addElement(e.getName());
			}
		}

		return requiredNames.elements();
	}


	public boolean hasName(String name) {
		return mElements.containsKey(name);
	}


	public boolean isRequired(String name) {
		return getElement(name).isRequired();
	}


	public boolean isValidType(String name, Class type) {
		return getElement(name).isValidType(type);
	}


	public Class getType(String name) {
		return getElement(name).getType();
	}


	public boolean hasDefault(String name) {
		return getElement(name).hasDefault();
	}


	public Object getDefaultValue(String name) {
		return getElement(name).getDefaultValue();
	}


	protected void setHistorical(String name, boolean isHistorical) {
		getElement(name).setHistorical(isHistorical);
	}


	public boolean isHistorical(String name) {
		return getElement(name).isHistorical();
	}


	protected void setContinuous(String name, boolean isContinuous) {
		getElement(name).setContinuous(isContinuous);
	}


	public boolean isContinuous(String name) {
		return getElement(name).isContinuous();
	}


	protected void setLabel(String name, String label) {
		getElement(name).setLabel(label);
	}


	public String getLabel(String name) {
		return getElement(name).getLabel();
	}


	/**
	* @return
	* The number of TypeSetElements in the set.
	*/
	public int size() {
		return mElements.size();
	}


	/**
	* @return
	* The TypeSetElement at the specified index.
	*/
	public TypeSetElement elementAt(int i) {
		return (TypeSetElement) mSortedElements.elementAt(i);
	}


	public String toString() {
		return mElements.toString();
	}


	static private class EmptyTypeSet extends TypeSet {
		EmptyTypeSet() {
			super(new TypeSetElement[0]);
		}
	}
}
