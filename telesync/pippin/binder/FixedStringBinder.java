   //
   //
   // Joseph A. Dudar, Inc   (C) 2000
// //
////

package pippin.binder;



import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.border.*;
import javax.swing.event.*;



public class FixedStringBinder extends FormField {
	JTextField mJTextField;
	int mLength;

	public FixedStringBinder(String title, int length) {
		super(title);

		mLength = length;

		mJTextField = new JTextField();

		StringBuffer template = new StringBuffer();
		for (int i = 0; i < Math.min(mLength, 20); ++i) {
			template.append('W');
		}
		mJTextField.setDocument(new LengthDocument(mLength));
		setPreferredFieldWidth(mJTextField, template.toString());
		setFormComponent(mJTextField);
	}


	protected JComponent createFormComponent() {
		return null;
	}


	public Object getBoundValue() {
		return mJTextField.getText();
	}


	public void setBoundValue(Object value) {
		String s = (String) value;
		if (s != null && s.length() > mLength) {
			s = s.substring(0, mLength);
		}
		mJTextField.setText(s);
	}


	public Class getBoundType() {
		return String.class;
	}


	public void clear() {
		setBoundValue(null);
	}


	protected class LengthDocument extends PlainDocument {
		private int mLength;

		protected LengthDocument(int length) {
			mLength = length;
		}

		public void insertString(int offset, String insertString,
				AttributeSet attributeSet) 
				throws BadLocationException {
			
			if (insertString == null) {
				return;
			}
			if (offset + insertString.length() > mLength) {
				throw new BadLocationException("too long", mLength);
			}

			super.insertString(offset, insertString, attributeSet);
		}
	}
}
