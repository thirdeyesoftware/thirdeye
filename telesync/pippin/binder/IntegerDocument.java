   //
   //
   // Joseph A. Dudar, Inc   (C) 2000
// //
////

package pippin.binder;



import javax.swing.text.*;



public class IntegerDocument extends PlainDocument {
	public void insertString(int offset, String insertString,
			AttributeSet attributeSet) 
			throws BadLocationException {
		
		if (insertString == null) {
			return;
		}

		for (int i = 0; i < insertString.length(); ++i) {
			char c = insertString.charAt(i);

			if (!Character.isDigit(c) && c != '-') {
				throw new BadLocationException("illegal character '" +
						c + "'", offset + i);
			}
		}

		super.insertString(offset, insertString, attributeSet);
	}
}
