////
// //
// PippinSoft
//
//

package pippin.util;



/**
* Exception subclasses may implement this interface in order to
* provide user consumable messages for a gui that displays
* Exceptions in an alert dialog.  Using this interface, certain
* Exception instances can be loaded with a "polite" message to appear
* in the dialog, rather than a dump of the Exception.toString().
*
* @see PersistenceException
* @see pippin.access.AccessKeyException
*/
public interface ExceptionMessage {

	/**
	* Sets the user consumable message to politely describe why
	* this Exception occurred.  <code>null</code> means no
	* message is set.
	*/
	public void setExceptionMessage(String message);

	/**
	* @return
	* The user consumable message to politely describe why
	* this Exception occurred.  <code>null</code> means no
	* message has been set.
	*/
	public String getExceptionMessage();
}
