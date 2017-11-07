   //
   //
   // Joseph A. Dudar, Inc   (C) 2000
// //
////

package pippin.binder;



import java.util.*;
import javax.swing.*;
import javax.swing.text.*;



public class ByteArrayDocument extends PlainDocument {
	int mMinSize;
	int mMaxSize;
	JTextComponent mTextComponent;
	private boolean mSizingEnabled = true;


	/**
	* Constructs a ByteArrayDocument with no minimum or maximum
	* number of bytes.
	*/
	public ByteArrayDocument(JTextComponent textComponent) {
		this(textComponent, 0, 0);
	}


	/**
	* @param min
	* Minimum number of bytes.
	*
	* @param max
	* Maximum number of bytes.  A value of 0 indicates no limit.
	*/
	public ByteArrayDocument(JTextComponent textComponent, int min, int max) {
		mTextComponent = textComponent;
		mMinSize = min;
		mMaxSize = max;

		if (mMinSize > 0) {
			StringBuffer sb = new StringBuffer();

			for (int i = 0; i < mMinSize; ++i) {
				if (i != 0) {
					sb.append(" ");
				}
				sb.append("00");
			}
			mTextComponent.setText(sb.toString());
		}
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


	private boolean isDelimeterChar(char c) {
		return c == ' ';
	}


	private void normalizeText() {
		int delimCount = 0;
		int fieldCount = 0;
		int hexCount = 0;

		int i;
		try {
			for (i = 0; i < getLength(); ++i) {
				char c;
				c = getText(i, 1).charAt(0);

				if (isHexChar(c)) {
					delimCount = 0;
					if (hexCount == 2) {
						super.insertString(i, " ", null);
						delimCount = 1;
						hexCount = 0;
					} else {
						if (hexCount == 0) {
							++fieldCount;
						}
						++hexCount;
					}
				} else
				if (isDelimeterChar(c)) {
					if (fieldCount == 0 || i == getLength() - 1) {
						super.remove(i, 1);
						--i;
					} else {
						if (hexCount == 1) {
							super.insertString(i, "0", null);
							++hexCount;
						} else {
							hexCount = 0;
							if (delimCount > 0) {
								super.remove(i, 1);
								--i;
							} else {
								++delimCount;
							}
						}
					}
				}
			}

			if (hexCount == 1) {
				super.insertString(i++, "0", null);
			}

			if (isSizingEnabled()) {
				if (mMinSize > fieldCount) {
					StringBuffer sb = new StringBuffer();

					for (int j = fieldCount; j < mMinSize; ++j) {
						if (j != 0) {
							sb.append(" ");
						}
						sb.append("00");
					}
					super.insertString(i, sb.toString(), null);
				}
			}
		}
		catch (BadLocationException ble) {
			throw new Error(ble.toString());
		}
	}


	private int getByteCount() {
		boolean needDelim = false;
		int byteCount = 0;

		try {
			for (int i = 0; i < getLength(); ++i) {
				if (!needDelim && isHexChar(getText(i, 1).charAt(0))) {
					needDelim = true;
					++byteCount;
				}

				if (isDelimeterChar(getText(i, 1).charAt(0))) {
					needDelim = false;
				}
			}
		}
		catch (BadLocationException ble) {
			throw new RuntimeException(ble.toString());
		}

		return byteCount;
	}


	private void checkInput(int offset, String str)
			throws BadLocationException {

		for (int i = 0; i < str.length(); ++i) {
			if (!isHexChar(str.charAt(i)) && !isDelimeterChar(str.charAt(i))) {
				throw new BadLocationException("invalid character", i + offset);
			}
		}
	}


	public void insertString(int offset, String str, AttributeSet attr)
			throws BadLocationException {
		
		checkInput(offset, str);
		if (str.length() == 1 && isHexChar(str.charAt(0))) {
			if (offset < getLength()) {
				if (isHexString(getText(offset, 1))) {
					super.remove(offset, 1);
					super.insertString(offset, str, attr);
				} else {
					if (mMaxSize > 0 && getByteCount() >= mMaxSize) {
						mTextComponent.setCaretPosition(offset + 1);
						insertString(offset + 1, str, attr);
					} else {
						super.insertString(offset, str + "0", attr);
						mTextComponent.setCaretPosition(offset + 1);
					}
				}
			} else {
				if (mMaxSize > 0 && getByteCount() >= mMaxSize) {
					throw new BadLocationException("too long", offset);
				}
				super.insertString(offset, str + "0", attr);
				mTextComponent.setCaretPosition(offset + 1);
			}
		} else {
			super.insertString(offset, str, attr);
		}
		normalizeText();
	}


	public void remove(int offset, int len)
			throws BadLocationException {

		if (len == 1) {
			if (offset < getLength() - 1 &&
					isHexChar(getText(offset, 1).charAt(0)) &&
					getText(offset + 1, 1).charAt(0) == '0' &&
					(mMinSize == 0 || getByteCount() > mMinSize)) {
				super.remove(offset, 2);
			} else
			if (isHexChar(getText(offset, 1).charAt(0))) {
				super.remove(offset, 1);
				super.insertString(offset, "0", null);
				mTextComponent.setCaretPosition(offset);
			} else {
				mTextComponent.setCaretPosition(offset);
			}
		} else {
			super.remove(offset, len);
		}
		normalizeText();
	}


	public byte[] getBytes() {
		Vector v = new Vector();
		int l = getLength();
		byte[] b = null;

		try {
			String t = getText(0, l);

			for (int i = 0; i < l; i += 3) {
				if (i < l - 1) {
					v.addElement(
							new Byte(
							(byte) Integer.parseInt(t.substring(i, i + 2), 16)));
				}
			}

			if (v.size() > 0) {
				b = new byte[v.size()];
				for (int i = 0; i < v.size(); ++i) {
					b[i] = ((Byte) v.elementAt(i)).byteValue();
				}
			}
		}
		catch (Exception e) {
			throw new RuntimeException(e.toString());
		}

		return b;
	}


	public void setBytes(byte[] b) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < b.length; ++i) {
			String s = "0" + Integer.toHexString(b[i]);
			if (i != 0) {
				sb.append(" ");
			}
			sb.append(s.substring(s.length() - 2, s.length()));
		}

		setSizingEnabled(false);
		mTextComponent.setText(sb.toString());
		setSizingEnabled(true);
	}


	private void setSizingEnabled(boolean sizingEnabled) {
		mSizingEnabled = sizingEnabled;
	}

	private boolean isSizingEnabled() {
		return mSizingEnabled;
	}
}
