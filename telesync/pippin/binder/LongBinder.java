   //
   //
   // Joseph A. Dudar, Inc   (C) 2000
// //
////

package pippin.binder;

import java.awt.*;
import java.awt.event.*;
import java.math.BigInteger;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.text.DateFormat;
import java.util.Date;




public class LongBinder extends FormField implements StateChangeBinder {
	protected JTextField mJTextField;
	protected boolean mEnableIndicator = false;
	
	public LongBinder(String title) {
		super(title);
	}


	protected JComponent createFormComponent() {
		mJTextField = new JTextField();
		mJTextField.setDocument(new IntegerDocument());
		setPreferredFieldWidth(mJTextField, "888888888888888888888");

		return mJTextField;
	}


	public Object getBoundValue() {
		Long i = null;
		String s = mJTextField.getText();
		
		if (s.length() > 0) {
			try {
				i = new Long((new BigInteger(mJTextField.getText())).longValue());
			}
			catch (NumberFormatException nfe) {
				throw new Error(nfe.toString());
			}
		}

		return i;
	}


	public void setBoundValue(Object value) {
		long l = ((Long) value).longValue();
		byte[] b = new byte[8];
		long mask = 0xff << 7 * 8;

		for (int i = 0; i < 8; ++i) {
			b[i] = (byte) ((l & mask) >>> (8 - i - 1) * 8);
			mask >>>= 8;
		}
				
		mJTextField.setText((new BigInteger(1, b)).toString());
	}


	public Class getBoundType() {
		return Long.class;
	}
	
	public void setBoundValue(Object o, long eventTime) {
		JLabel iconLabel = (JLabel)getIconLabelComponent();

		if (iconLabel.getIcon()==null) {
			iconLabel.setIcon(BinderIcon.ICON_EVENT);
		}

		DateFormat df = DateFormat.getDateTimeInstance();
		Date date = new Date(eventTime);

		iconLabel.setToolTipText(df.format(date));

		setBoundValue(o);

	}
				
	/**
		* resets the "flag" icon and tooltip
		*/
	public void resetChangeIndicator() {
		JLabel label = (JLabel)getIconLabelComponent();
		label.setIcon(null);
		label.setToolTipText(null);
	}
	
	/**
		* enables/disables the flag indicator
		*/
	public void setEnableIndicator( boolean enableFlag ) {
		mEnableIndicator = enableFlag;
	}
	
	/**
		* returns if the indicator is enabled.
		*/
	public boolean indicatorEnabled() {
		return mEnableIndicator;
	}
	
	/**
		* returns if the indicator is currently raised.
		*/
	public boolean isIndicatorRaised() {
		return (((JLabel)getIconLabelComponent()).getIcon() != null );
	}
	
	
}
