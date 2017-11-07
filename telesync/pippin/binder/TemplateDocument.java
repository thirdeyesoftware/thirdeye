   //
   //
   // Joseph A. Dudar, Inc   (C) 2000
// //
////

package pippin.binder;



import java.util.*;
import javax.swing.*;
import javax.swing.text.*;



public class TemplateDocument extends PlainDocument {
	JTextComponent mTextComponent;
	String mTemplate;


	public TemplateDocument(JTextComponent textComponent, String template) {
		mTextComponent = textComponent;
		mTemplate = template;

		for (int i = 0; i < template.length(); ++i) {
			char c = template.charAt(i);
			if (!Character.isDigit(c) && !isDelimeter(c)) {
				throw new Error("Invalid template '" + template + "'");
			}
		}

		try {
			super.remove(0, getLength());
			super.insertString(0, mTemplate, null);
		}
		catch (BadLocationException ble) {
			throw new Error(ble.toString());
		}
	}


	private int getNextDigitOffset(int offset) {
		int i;

		if (offset >= mTemplate.length()) {
			return mTemplate.length();
		}

		for (i = offset; i < mTemplate.length(); ++i) {
			if (Character.isDigit(mTemplate.charAt(i))) {
				break;
			}
		}

		return i;
	}


	private int getPreviousDigitOffset(int offset) {
		int i;

		if (offset <= 0) {
			return 0;
		}

		for (i = offset; i > 0; --i) {
			if (Character.isDigit(mTemplate.charAt(i))) {
				break;
			}
		}

		return i;
	}


	private boolean isDelimeter(char c) {
		return ": /".indexOf(c) >= 0;
	}


	private void checkInput(int offset, String str)
			throws BadLocationException {

		if (offset + str.length() > mTemplate.length()) {
			throw new BadLocationException("too long", mTemplate.length());
		}

		if (str.length() == 1 && str.charAt(0) == ' ') {
			return;
		}

		for (int i = 0; i < str.length(); ++i) {
			if (Character.isDigit(mTemplate.charAt(offset + i))) {
				if (!Character.isDigit(str.charAt(i))) {
					throw new BadLocationException("invalid character", i + offset);
				}
			} else {
				if (str.charAt(i) != mTemplate.charAt(offset + i)) {
					throw new BadLocationException("invalid character", i + offset);
				}
			}
		}
	}


	public void insertString(int offset, String str, AttributeSet attr)
			throws BadLocationException {

		if (str.length() == 1 && Character.isDigit(str.charAt(0))) {
			offset = getNextDigitOffset(offset);
		}
		
		checkInput(offset, str);

		if (str.length() == 1) {
			if (Character.isDigit(str.charAt(0))) {
				super.remove(offset, 1);
				super.insertString(offset, str, attr);
			}
			mTextComponent.setCaretPosition(getNextDigitOffset(offset + 1));
		} else {
			super.remove(offset, str.length());
			super.insertString(offset, str, attr);
		}
	}


	public void remove(int offset, int len)
			throws BadLocationException {

		super.remove(offset, len);
		super.insertString(offset,
				mTemplate.substring(offset, offset + len), null);
		mTextComponent.setCaretPosition(offset);
	}
}
