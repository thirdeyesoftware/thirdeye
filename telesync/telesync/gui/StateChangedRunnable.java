////
//
// Telesync 5320 Project
//
//

package telesync.gui;



import pippin.pui.*;
import javax.swing.*;



/**
* This is a utility class for moving PStateChangeEvent notification
* onto the AWT event dispatch thread.
*/
public abstract class StateChangedRunnable implements Runnable {
	PStateChangeEvent mStateChangeEvent;

	public StateChangedRunnable(PStateChangeEvent event) {
		mStateChangeEvent = event;
	}
}
