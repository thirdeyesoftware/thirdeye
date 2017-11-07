////
//
// Telesync 5320 Project
//
//

package telesync.gui;



import pippin.pui.*;
import javax.swing.*;



/**
* This is a utility class for moving PUI events
* onto the AWT event dispatch thread.
*/
public abstract class PEventRunnable implements Runnable {
	PEvent mPEvent;

	public PEventRunnable(PEvent event) {
		mPEvent = event;
	}
}
