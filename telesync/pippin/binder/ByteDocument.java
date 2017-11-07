   //
   //
   // Joseph A. Dudar, Inc   (C) 2000
// //
////

package pippin.binder;



import java.util.*;
import javax.swing.*;
import javax.swing.text.*;



public class ByteDocument extends PlainDocument {
	JTextComponent mTextComponent;


	public ByteDocument(JTextComponent textComponent) {
		mTextComponent = textComponent;
		mTextComponent.setText("00");
	}


	private boolean isHexString(String s) {
		for (int i = 0; i < s.length(); ++i) {
			if (!isHexChar(s.charAt(i))) {
				return false;
			}
		}

		return true;
	}


	private boolean isHexChar(char c) {
		char ch = Character.toLowerCase(c);
		return Character.isDigit(ch) || ch >= 'a' && ch <= 'f';
	}


	private void checkInput(int offset, String str)
			throws BadLocationException {

		for (int i = 0; i < str.length(); ++i) {
			if (!isHexChar(str.charAt(i))) {
				throw new BadLocationException("invalid character", i + offset);
			}
		}
	}


	public void insertString(int offset, String str, AttributeSet attr)
			throws BadLocationException {

		checkInput(offset, str);
		if (str.length() == 1) {
			if (!isHexChar(str.charAt(0))) {
				throw new BadLocationException("non-hex char", offset);
			}
			if (offset < 2) {
				super.remove(offset, 1);
				super.insertString(offset, str, attr);
			} else {
				throw new BadLocationException("too long", offset);
			}
		} else {
			if (offset + str.length() <= 2) {
				if (getLength() > 0) {
					super.remove(offset, getLength());
				}
				super.insertString(offset, str, attr);
			} else {
				throw new BadLocationException("too long", offset);
			}
		}
	}


	public void remove(int offset, int len)
			throws BadLocationException {

		super.remove(offset, len);
		super.insertString(offset,
				"00".substring(offset, offset + len), null);

		mTextComponent.setCaretPosition(offset);
	}


	public Integer getByte() {
		try {
			return new Integer(Integer.parseInt(getText(0, getLength()), 16));
		}
		catch (BadLocationException ble) {
			throw new Error(ble.toString());
		}
	}


	public void setByte(Integer b) {
		String s = "0" + Integer.toHexString(b.intValue());
		mTextComponent.setText(s.substring(s.length() - 2, s.length()));
	}
}
