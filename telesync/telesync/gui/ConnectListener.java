////
//
// Telesync 5320 Project
//
//

package telesync.gui;



import pippin.pui.*;



/**
* ConnectListeners are connectNotified when they are added
* to a connected GClient.
*
* ConnectListeners should assume they are not connected until
* connectNotified.
*/
public interface ConnectListener {
	public void connectNotify();

	public void disconnectNotify();
}
