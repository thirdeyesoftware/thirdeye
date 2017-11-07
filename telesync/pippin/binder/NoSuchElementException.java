////
// //
// PippinSoft
//
//

package pippin.binder;



/**
* Indicates that a specified element is not an active element in
* an Envelope.
*/
public class NoSuchElementException extends EnvelopeException {
	public NoSuchElementException(String name) {
		super(name);
	}
}
