////
// //
// PippinSoft
//
//

package pippin.pui;


import java.util.*;


/**
*
* A PAlarmEvent describes a an alarm received for a PComponent.
*
*/
public class PAlarmEvent extends PEvent {

	PAttributeSet mAttrs;


	public PAlarmEvent(PComponent source,PAttributeSet attrs) {
		super(source);
		mAttrs = attrs;
	}


	/**
	* @return
	* The PAttributeSet containing the attributes of the alarm received
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
