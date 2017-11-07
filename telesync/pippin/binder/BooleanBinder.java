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



public class BooleanBinder extends FormField {
	JCheckBox mJCheckBox;
	private String mOnText = "On";
	private String mOffText = "Off";
	private boolean mIsInverted = false;

	public BooleanBinder(String title) {
		super(title);
	}


	public BooleanBinder(String title, String onText, String offText) {
		super(title);
		mOnText = onText;
		mOffText = offText;
	}


	public void setOnText(String onText) {
		mOnText = onText;
	}


	public void setOffText(String offText) {
		mOffText = offText;
	}


	public void setInverted(boolean isInverted) {
		mIsInverted = isInverted;
	}


	public boolean isInverted() {
		return mIsInverted;
	}


	protected JComponent createFormComponent() {
		mJCheckBox = new JCheckBox();
		mJCheckBox.setAlignmentY(.5f);
		mJCheckBox.setFocusPainted(false);
		setBoxText("Off");

		mJCheckBox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				if (mJCheckBox.isSelected()) {
					setBoxText(mOnText);
				} else {
					setBoxText(mOffText);
				}
			}
		});

		mJCheckBox.setOpaque(false);

		return mJCheckBox;
	}


	private void setBoxText(String text) {
		mJCheckBox.setText(text);
	}


	public Object getBoundValue() {
		if (mIsInverted) {
			return new Boolean(!mJCheckBox.isSelected());
		} else {
			return new Boolean(mJCheckBox.isSelected());
		}
	}


	public void setBoundValue(Object value) {
		boolean b = ((Boolean) value).booleanValue();
		if (mIsInverted) {
			mJCheckBox.setSelected(!b);
		} else {
			mJCheckBox.setSelected(b);
		}
	}


	public void clear() {
		setBoundValue(Boolean.FALSE);
	}


	public Class getBoundType() {
		return Boolean.class;
	}


	public void setIcon(Icon icon) {
		mJCheckBox.setIcon(icon);
	}
}
