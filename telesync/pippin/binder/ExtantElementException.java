////
// //
// PippinSoft
//
//

package pippin.binder;



/**
* Indicates that an attempt was made to add an element
* using Envelope.addEnvelope() to an Envelope
* that already held an element of that name.
*/
public class ExtantElementException extends EnvelopeException {
	public ExtantElementException(String name) {
		super(name + "  Use Envelope.mergeEnvelope()");
	}
}
