////
// //
// PippinSoft
//
//

package pippin.binder.pui;


import java.io.*;
import java.util.*;
import pippin.binder.*;
import pippin.pui.*;



/**
* A transportable container for collections of named
* Serializable Objects.
*/
public class PEnvelope extends Envelope {

	private static final long serialVersionUID = 2000012901L;

	/**
	* @param typeSetClass
	* The subclass of TypeSet to be used by this Envelope.
	*/
	public PEnvelope(Class typeSetClass) {
		super(typeSetClass);
	}


	public PEnvelope(TypeSet typeSet) {
		super(typeSet);
	}


	public PEnvelope(Envelope env) {
		super(env.getTypeSet());
		mElements = env.getElementsTable();
	}


	/**
	* Adds and replaces elements in this Envelope with elements from
	* PAttributeList p.
	*/
	public void mergePAttributeList(PAttributeList p) {
		for (Enumeration en = p.getAttributeNames(); en.hasMoreElements(); ) {
			String elementName = (String) en.nextElement();

			if (getTypeSet().hasElement(elementName)) {
				Object o = getTypeSet().getElement(elementName).mutateConvert(
						p.getAttribute(elementName).getValue());

				putElement(elementName, (Serializable) o);
			}
		}
	}


	public void mergePComponent(PComponent p) throws Exception {
		mergePAttributeList(p.getAttributes());
	}


	public void applyToPComponent(PComponent p) throws Exception {
		for (Enumeration en = getElementNames(false); en.hasMoreElements(); ) {
			String elementName = (String) en.nextElement();
			Object o = getElement(elementName);
			o = getTypeSet().getElement(elementName).accessConvert(o);

			p.put(elementName, o);
			
			p.sync();
		}
	}
}
