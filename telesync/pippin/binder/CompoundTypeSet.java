   //
   //
   // Joseph A. Dudar, Inc   (C) 1999
// //
////

package pippin.binder;



import java.util.*;
import java.io.*;



/**
* Utility for combining TypeSets for use in BinderPanels that
* display information from multiple TypeSets.
*/
public class CompoundTypeSet extends TypeSet {

	private static final long serialVersionUID = 1999082401L;

	public CompoundTypeSet(TypeSet set1, TypeSet set2) {
		Vector v = new Vector();
		Hashtable nameHash = new Hashtable();
		TypeSetElement el;

		for (Enumeration en = set1.getElements(); en.hasMoreElements(); ) {
			el = (TypeSetElement) en.nextElement();
			v.addElement(el);
			nameHash.put(el.getName(), el);
		}

		for (Enumeration en = set2.getElements(); en.hasMoreElements(); ) {
			el = (TypeSetElement) en.nextElement();

			if (!nameHash.containsKey(el.getName())) {
				v.addElement(el);
			} else {
				TypeSetElement firstEl = (TypeSetElement)
						nameHash.get(el.getName());
				if (!firstEl.getType().equals(el.getType())) {
					throw new TypeSetException("Duplicate and mismatched name: " +
							el.getName());
				}
			}
		}

		TypeSetElement[] elements = new TypeSetElement[v.size()];
		v.copyInto(elements);

		init(elements);
	}
}
