////
// //
// PippinSoft
//
//

package pippin.pui;


import java.util.*;


/**
* An instance of a PTypeSet maps attribute-IDs to names
* and versa-vice.  It also defines the primitive types for
* the attributes for building messages to send to the
* appliance.
*/

public class PTypeSet
{
	protected String[] mNames;
	protected byte[] mTypes;
	private Hashtable mHash;

	public PTypeSet(String[] names,byte[] types) {
		if (names.length != types.length)
			throw new RuntimeException(getClass().getName() + " names and types "+
					"arrays are of different length!");
		init(names,types);
	}

	protected void init(String[] names,byte[] types) {
		mNames = names;
		mTypes = types;
		mHash = new Hashtable();
		for (int i=0 ; i<mNames.length ; i++) {
			Object o = mHash.put(mNames[i],new Integer(i));
			if (o != null) {
				throw new Error(getClass().getName() +
						" duplicate name " + mNames[i]);
			}
		}
	}


	public int getID(String name) {
		Integer val = (Integer)mHash.get(name);
		if (val == null) {
			throw new RuntimeException(getClass().getName() +
					" name '" +name+ "' not found!");
		}
		return val.intValue();
	}


	public String getName(int id) {
		checkID(id);
		
		return mNames[id];
	}


	protected boolean hasName(String name) {
		return mHash.containsKey(name);
	}


	protected byte getType(int id) {
		checkID(id);

		return mTypes[id];
	}


	protected byte getType(String name) {
		return mTypes[getID(name)];
	}


	protected boolean hasID(int id) {
		if (id >= mNames.length || id < 0) {
			return false;
		} else {
			return true;
		}
	}


	protected void checkID(int id) {
		if (!hasID(id)) {
			throw new RuntimeException(getClass().getName() +
					" id " +id+ " invalid!");
		}
	}
}
