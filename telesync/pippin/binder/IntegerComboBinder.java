   //
   //
   // Joseph A. Dudar, Inc   (C) 2000
// //
////

package pippin.binder;



import java.awt.*;
import javax.swing.*;
import java.util.*;



public class IntegerComboBinder extends ComboBinder {

	public IntegerComboBinder(String title, int start, int count) {
		super(title, Integer.class);

		Vector v = new Vector();
		for (int i = 0; i < count; ++i) {
			v.addElement(new Integer(start + i));
		}

		setChoices(v);
	}
}
