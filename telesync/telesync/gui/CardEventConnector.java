////
//
// Telesync 5320 Project
//
//

package telesync.gui;



import pippin.pui.*;
import telesync.pui.tsi5320.*;
import javax.swing.*;



/**
* This is a utility class for moving PUI events
* onto the AWT event dispatch thread.
*/
public class CardEventConnector implements CardEventListener {
	CardEventListener mListener;

	public CardEventConnector(CardEventListener listener) {
		mListener = listener;
	}

	public void cardEventNotify(CardEvent event) {
		if (SwingUtilities.isEventDispatchThread()) {
			mListener.cardEventNotify(event);
		} else {
			SwingUtilities.invokeLater(new PEventRunnable(event) {
				public void run() {
					cardEventNotify((CardEvent) mPEvent);
				}
			});
		}
	}


	public boolean equals(Object o) {
		if (o instanceof CardEventConnector) {
			CardEventConnector c  = (CardEventConnector) o;
			return c.mListener.equals(mListener);
		} else {
			return false;
		}
	}


	public int hashCode() {
		return mListener.hashCode();
	}
}
