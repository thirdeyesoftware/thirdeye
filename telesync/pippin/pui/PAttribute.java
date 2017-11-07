////
// //
// PippinSoft
//
//

package pippin.pui;


/**
* A PAttribute describes a named attribute of a PComponent.
* PAttributes are grouped together in PAttributeSets.
*/
public class PAttribute
{
	public static final byte TYPE_BYTE = 'b';
	public static final byte TYPE_SHORT = 's';
	public static final byte TYPE_LONG = 'l';
	public static final byte TYPE_LONGLONG = 'L';
	// public static final byte TYPE_INTEGER = 'i';
	public static final byte TYPE_STRING = 'S';
	public static final byte TYPE_MEMORY = 'M';
	public static final byte TYPE_BOOLEAN = 'B';

	private String mName;
	private int mID;
	private byte mType;
	private Object mValue;


	/**
	* @param name
	* The name of this PAttribute.  This value is used
	* in fetching the attribute from a set of attributes.
	*
	* @param id
	*
	* @param type
	* The coded type of this PAttribute, such as PAttribute.TYPE_SHORT.
	*
	* @param value
	* The named value of this PAttribute.
	*/
	PAttribute(String name, int id, byte type, Object value) {
		mName = name;
		mID = id;
		mType = type;
		mValue = value;
	}

	public String getName() {
		return mName;
	}

	public Object getValue() {
		return mValue;
	}

	public int getID() {
		return mID;
	}

	public byte getType() {
		return mType;
	}

	public String toString() {
		return "PAttribute(" + mName + "," + mID + "," + (char) mType +
				"," + mValue + ")";
	}
}
