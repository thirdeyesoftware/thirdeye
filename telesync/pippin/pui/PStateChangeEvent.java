////
// //
// PippinSoft
//
//

package pippin.pui;


import java.util.*;


/**
* A PStateChangeEvent describes a change in one or more
* attributes of a PComponent.
*/
public class PStateChangeEvent extends PEvent {

	PAttributeSet mAttrs;


	public PStateChangeEvent(PComponent source,PAttributeSet attrs) {
		super(source);
		mAttrs = attrs;
	}


	/**
	* @return
	* The PAttributeSet containing the attributes that changed
	* on the source PComponent.
	*/
	public PAttributeSet getAttributes() {
		return mAttrs;
	}


	public PAttribute getAttribute(String name) {
		return getAttributes().getAttribute(name,false);
	}


	public String toString() {
		return super.toString() + " " + mAttrs.toString();
	}
}
