////
// //
// PippinSoft
//
//

package pippin.pui;


import java.util.*;


public class PAttributeList {
	Hashtable mAttributes;

	public PAttributeList() {
		mAttributes = new Hashtable();
	}


	/**
	* @param name
	* The name of the attribute to be returned.
	*
	* @boolean required
	* Indicates that the named attribute must be present in this
	* PAttributeSet.
	*
	* @return
	* The named attribute, or <code>null</code>
	* if there is no attribute with the specified name.
	*
	* @exception java.lang.RuntimeException
	* Indicates the specified attribute does not exist.  This
	* Exception is only thrown if <code>required</code> is
	* <code>true</code>.
	*/
	public PAttribute getAttribute(String name, boolean required) {
		PAttribute pa = (PAttribute) mAttributes.get(name);

		if (pa == null && required)
			throw new Error("Missing required attribute " + name);

		return pa;
	}


	/**
	* @param name
	* The name of the attribute to be returned.
	*
	* @return
	* The named attribute.
	*
	* @exception java.lang.RuntimeException
	* Indicates the specified attribute does not exist.
	*/
	public PAttribute getAttribute(String name) {
		return getAttribute(name, true);
	}


	/**
	* @return
	* The value of the specified attribute.
	*/
	public Object getValue(String name) {
		return getValue(name, true);
	}


	/**
	* @param required
	* If <code>false</code> no Exception is thrown if the specified
	* attribute does no exist.
	*
	* @return
	* The value of the specified attribute.
	*/
	public Object getValue(String name, boolean required) {
		PAttribute pa = getAttribute(name, required);

		if (pa != null) {
			return pa.getValue();
		} else {
			return null;
		}
	}


	/**
	* Stores the specified PAttribute in this PAttributeSet.
	* Any pre-existing attribute with the same name is replaced.
	*/
	public void putAttribute(PAttribute attr) {
		mAttributes.put(attr.getName(), attr);
	}


	public Enumeration getAttributes() {
		return mAttributes.elements();
	}


	public Enumeration getAttributeNames() {
		return mAttributes.keys();
	}

	public boolean contains(String attributeName) {
		try {
			for (Enumeration e = getAttributeNames();e.hasMoreElements();) {
				String key = (String)e.nextElement();
				if (key.equals(attributeName)) return true;
			}
			return false;
		}
		catch (Exception npe) {
			npe.printStackTrace();
		}
		return false;
	}
	
			
			
	public void merge(PAttributeSet attrs) {
		for (Enumeration en = attrs.getAttributes(); en.hasMoreElements(); ) {
			putAttribute((PAttribute) en.nextElement());
		}
	}


	public String toString() {
		String className = getClass().getName();
		int i = className.lastIndexOf(".");
		if (i >= 0) {
			className = className.substring(i+1);
		}
		String rtn = className + "(";

		for (Enumeration e = getAttributes() ; e.hasMoreElements() ; ) {
			PAttribute attr = (PAttribute) e.nextElement();

			rtn += attr.toString();
			if (e.hasMoreElements()) {
				rtn += ",";
			}
		}
		rtn += ")";

		return rtn;
	}
	
}
