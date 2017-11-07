////
// //
// PippinSoft
//
//

package pippin.pui;

import java.util.*;
import java.net.*;
import java.io.*;

public class PMessage {
	public static final int TYPE_RESPONSE_BIT = 0x8000;

	public static final int TYPE_POLL = 0;
	public static final int TYPE_LIST_SUBS = 1;
	public static final int TYPE_SUBSCRIBE = 2;
	public static final int TYPE_GET_ATTRS = 3;
	public static final int TYPE_SUBCANCEL = 4;
	public static final int TYPE_PUT_ATTRS = 5;
	public static final int TYPE_STATE_CHG = 6;
	public static final int TYPE_ALARM = 7;
	
	
	protected static final int ATTR_END_OF_MESSAGE = 0xFFFF;
	protected static final int ATTR_ERROR = 0xFFFE;
	protected static final int ATTR_EXCEPTION = 0xFFFD;
	protected static final int ATTR_EXCEPTION_TYPE = 0xFFFC;

	
	static final String[] TYPE_NAMES = {
		"poll",
		"list_subs", "subscribe", "get_attrs", "subcancel",
		"put_attrs", "state_chg","alarm"
	};

	private int mType;
	private int mSource;
	private int mComponentID;
	private PComponent mComponent;
	boolean mResponse;

	private String mError, mException;
	private int mExceptionType;

	PAttributeList mAttrs;

	/**
	* Creates a PMessage with no attribute set.
	*
	* @param comp
	* The PComponent that is the target of this PMessage.
	*
	* @param type
	* The type of this PMessage.
	*/
	public PMessage(PComponent comp, int type) {
		this(comp, type, null);
	}

	/**
	* Creates a PMessage with the specfied attribute set.
	*
	* @param comp
	* The PComponent that is the target of this PMessage.
	*
	* @param type
	* The type of this PMessage.
	*
	* @param attrs
	* The PAttributeList for this PMessage.
	*/
	public PMessage(PComponent comp, int type, PAttributeList attrs) {
		mType = type;
		mComponent = comp;
		mComponentID = comp.getID();

		if (comp == null) {
			// The appliance requires a PComponent in association with
			// any PMessage.
			throw new NullPointerException();
		}

		if (attrs == null) {
			switch (type) {
				case TYPE_LIST_SUBS:
					mAttrs = new PAttributeList();
					break;
				case TYPE_ALARM:
					mAttrs = new PAttributeSet(new PAlarmTypeSet());
					break;
				default:
					mAttrs = new PAttributeSet(comp.getTypeSet());
			}
						
		} else {
			if (type != TYPE_LIST_SUBS &&
					!(attrs instanceof PAttributeSet)) {
				throw new Error("PAttributeSet required");
			}
			mAttrs = attrs;
		}
	}


	public boolean isCommand() {
		return !mResponse;
	}

	public boolean isResponse() {
		return mResponse;
	}

	public void setCommand() {
		mResponse = false;
	}

	public void setResponse() {
		mResponse = true;
	}

	public int getType() {
		return mType;
	}

	public void setType(int type) {
		mType = type;
	}

	/**
	* Used by PClient to track responses.
	*/
	public int getSource() {
		return mSource;
	}

	/**
	* Used by PClient to track responses.
	*/
	public void setSource(int source) {
		mSource = source;
	}

	public PComponent getComponent() {
		return mComponent;
	}

	public int getComponentID() {
		return mComponentID;
	}

	public void setComponentID(int comp) {
		mComponentID = comp;
	}


	/**
	* @return
	* The attribute associated with the specified name.
	*
	* @exception java.lang.RuntimeException
	* Indicates there is no attribute for the specified name.
	*/
	public PAttribute getAttribute(String name) {
		return mAttrs.getAttribute(name);
	}


	/**
	* @return
	* The attribute associated with the specified name.
	*
	* @param required
	* Indicates the named attribute must exist.
	*
	* @exception java.lang.RuntimeException
	* Indicates there is no attribute for the specified name,
	* but only when <code>require</code> is <code>true</code>.
	*/
	public PAttribute getAttribute(String name,boolean required) {
		return mAttrs.getAttribute(name, required);
	}


	public void putAttribute(PAttribute attr) {
		
		mAttrs.putAttribute(attr);
	}


	public PAttributeList getAttributeList() {
		return mAttrs;
	}

	public PAttributeSet getAttributeSet() {
		return (PAttributeSet) mAttrs;
	}

	public Enumeration getAttributes() {
		return mAttrs.getAttributes();
	}



	public String toString() {
		String rtn = "PMessage(" + (isCommand() ? "C-" : "R-")
			+TYPE_NAMES[mType]+  "=" +mType+
			"  src=" +mSource+ "  comp=" +mComponentID+
			" {\n";

		for (Enumeration e = mAttrs.getAttributes() ; e.hasMoreElements() ; ) {
			PAttribute attr = (PAttribute)e.nextElement();

			rtn += "   " + attr.toString() + "\n";
		}
		rtn += "} )";

		return rtn;
	}

	protected void putError(String error) {
		mError = error;
	}

	protected void putException(String exception) {
		mException = exception;
	}

	protected void putExceptionType(int exceptionType) {
		mExceptionType = exceptionType;
	}

	public String getError() {
		return mError;
	}

	public String getException() {
		return mException;
	}

	/**
	* This value has no meaning unless there is an Exception or Error.
	*/
	public int getExceptionType() {
		if (mError == null && mException == null) {
			throw new IllegalStateException(
					"There is no Error or Exception on this PMessage");
		}
		return mExceptionType;
	}
	
			
}
