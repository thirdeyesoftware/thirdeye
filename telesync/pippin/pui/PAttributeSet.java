////
// //
// PippinSoft
//
//

package pippin.pui;

import java.util.*;

public class PAttributeSet extends PAttributeList {
	PTypeSet mTypeSet;

	public PAttributeSet(PTypeSet typeset) {
		mTypeSet = typeset;
	}

	public PTypeSet getTypeSet() {
		return mTypeSet;
	}


	/**
	* Creates a new PAttribute within this PAttributeSet
	* using the specified name, id, and value.
	*/
	public void putValue(String name, int id, Object value) {
		checkInsert(name, id, value);

		putAttribute(new PAttribute(name, id, mTypeSet.getType(id), value));
	}


	private void checkInsert(String name, int id, Object value) {
				
		int type = mTypeSet.getType(id);
		String shouldbe = null;

		if (!mTypeSet.getName(id).equals(name)) {
			throw new Error(name + " != " + mTypeSet.getName(id));
		}

		switch (type) {
		case PAttribute.TYPE_BYTE:
		case PAttribute.TYPE_SHORT:
		case PAttribute.TYPE_LONG:
		// case PAttribute.TYPE_INTEGER:
			if ( !(value instanceof Integer))
				shouldbe = "an Integer";
			break;

		case PAttribute.TYPE_STRING:
			if ( !(value instanceof String))
				shouldbe = "a String";
			break;

		case PAttribute.TYPE_MEMORY:
			if ( !(value instanceof byte[]) )
				shouldbe = "a byte-array";
			break;

		case PAttribute.TYPE_BOOLEAN:
			if ( !(value instanceof Boolean) )
				shouldbe = "a Boolean";
			break;
		}

		if (shouldbe != null) {
			System.out.println("value is an " + value.getClass().getName());
			throw new Error("Attribute " +name+
					" expected to be " + shouldbe);
		}
		
	}


	private void checkInsert(PAttribute attr) {
		checkInsert(attr.getName(), attr.getID(), attr.getValue());
	}


	/**
	* Creates a new PAttribute within this PAttributeSet
	* using the specified name and value.
	*/
	public void putValue(String name, Object value) {
		putValue(name, mTypeSet.getID(name), value);
	}


	/**
	* Creates a new PAttribute within this PAttributeSet
	* using the specified id and value.
	*/
	public void putValue(int id, Object value) {
		putValue(mTypeSet.getName(id), id, value);
	}


	/**
	* Stores the specified PAttribute in this PAttributeSet.
	* Any pre-existing attribute with the same name is replaced.
	*/
	public void putAttribute(PAttribute attr) {
		checkInsert(attr);
		super.putAttribute(attr);
	}
}
