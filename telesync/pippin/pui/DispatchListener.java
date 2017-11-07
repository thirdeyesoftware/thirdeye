////
// //
// PippinSoft
//
//

package pippin.pui;


import java.util.*;


/**
* Used by listeners interested in the
* beginning and ending of work within the PClient.dispatchMessages method.
* Dispatching messages can be timeconsuming, and listeners
* may wish to make some preparations before the dispaching
* occurs.
*/
public interface DispatchListener {
	public void dispatchStarted();

	public void dispatchComplete();
}
