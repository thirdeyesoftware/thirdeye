   //
   //
   // Joseph A. Dudar, Inc   (C) 2000
// //
////

package pippin.binder;



import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;



public class FixedIntBinder extends FormField {
	JTextField mJTextField;
	int mMinValue;
	int mMaxValue;
	int mLength;

	public FixedIntBinder(String title,
			int length, int minValue, int maxValue) {
		super(title);

		mLength = length;
		mMinValue = minValue;
		mMaxValue = maxValue;

		mJTextField = new JTextField();

		StringBuffer template = new StringBuffer();
		for (int i = 0; i < mLength; ++i) {
			template.append('0');
		}
		mJTextField.setDocument(
				new TemplateDocument(mJTextField, template.toString()));
		setPreferredFieldWidth(mJTextField, template.toString());
		setFormComponent(mJTextField);

		mJTextField.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent event) {
			}

			public void focusLost(FocusEvent event) {
				int i = ((Integer) getBoundValue()).intValue();
				int j = i;
				if (i > mMaxValue) {
					j = mMaxValue;
				} else
				if (i < mMinValue) {
					j = mMinValue;
				}

				if (j != i) {
					setBoundValue(new Integer(j));
				}
			}
		});
	}


	protected JComponent createFormComponent() {
		return null;
	}


	public Object getBoundValue() {
		Integer i = new Integer(0);
		
		try {
			i = new Integer(mJTextField.getText());
		}
		catch (NumberFormatException nfe) {
			throw new Error(nfe.toString());
		}

		return i;
	}


	public void setBoundValue(Object value) {
		Integer v = (Integer) value;

		int i = v.intValue();

		if (i > mMaxValue) {
			i = mMaxValue;
		} else
		if (i < mMinValue) {
			i = mMinValue;
		}


		StringBuffer sb = new StringBuffer(Integer.toString(i));
		if (sb.length() > mLength) {
			throw new Error("Field length should allow enough " +
					"space for max value.");
		} else {
			while (sb.length() < mLength) {
				sb.insert(0, '0');
			}
		}

		mJTextField.setText(sb.toString());
	}


	public Class getBoundType() {
		return Integer.class;
	}


	public void clear() {
		setBoundValue(new Integer(mMinValue));
	}
}
