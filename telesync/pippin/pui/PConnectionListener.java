////
// //
// PippinSoft
//
//

package pippin.pui;


public interface PConnectionListener
{
	/**
	* Invoked when the socket to the device is closed. <p>
	*
	* Note this method may be called from context of the
	* PClient's internal Threads.
	*/
	public void connectionLost();
}
