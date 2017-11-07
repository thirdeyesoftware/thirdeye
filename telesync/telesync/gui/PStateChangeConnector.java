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
public class PStateChangeConnector implements PStateChangeListener {
	PStateChangeListener mListener;

	public PStateChangeConnector(PStateChangeListener listener) {
		mListener = listener;
	}

	public void stateChanged(PStateChangeEvent event) {
		if (SwingUtilities.isEventDispatchThread()) {
			mListener.stateChanged(event);
		} else {
			SwingUtilities.invokeLater(new PEventRunnable(event) {
				public void run() {
					stateChanged((PStateChangeEvent) mPEvent);
				}
			});
		}
	}


	public boolean equals(Object o) {
		if (o instanceof PStateChangeConnector) {
			PStateChangeConnector c  = (PStateChangeConnector) o;
			return c.mListener.equals(mListener);
		} else {
			return false;
		}
	}


	public int hashCode() {
		return mListener.hashCode();
	}
}
