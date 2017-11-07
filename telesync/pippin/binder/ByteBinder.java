   //
   //
   // Joseph A. Dudar, Inc   (C) 2000
// //
////

package pippin.binder;



import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import javax.swing.event.*;

import java.util.Date;
import java.text.DateFormat;


public class ByteBinder extends FormField implements StateChangeBinder {
	JTextField mTextField;
	private boolean mEnableIndicator = false;
	
	public ByteBinder(String title) {
		super(title);
		setFormComponent(createByteComponent());
		clear();
	}


	protected JComponent createFormComponent() {
		return null;
	}


	protected JComponent createByteComponent() {
		mTextField = new JTextField();
		mTextField.setDocument(new ByteDocument(mTextField));
		setPreferredFieldWidth(mTextField, "00");

		return mTextField;
	}


	public Object getBoundValue() {
		Integer b = ((ByteDocument) mTextField.getDocument()).getByte();

		return b;
	}


	public void setBoundValue(Object value) {
		((ByteDocument) mTextField.getDocument()).setByte((Integer) value);
	}

	public void setBoundValue(Object value, long eventTime) {
		JLabel iconLabel = (JLabel)getIconLabelComponent();
		
		if (iconLabel.getIcon()==null) {
			iconLabel.setIcon(BinderIcon.ICON_EVENT);
		}

		DateFormat df = DateFormat.getDateTimeInstance();
		Date date = new Date(eventTime);

		iconLabel.setToolTipText(df.format(date));

		setBoundValue(value);
	}
	
	// Subclasses should override this as needed.
	public Class getBoundType() {
		return Integer.class;
	}


	public void clear() {
		setBoundValue(new Integer(0));
	}


	public static void main(String[] args) {
		JFrame f = new JFrame("ByteBinder Test") {
			public void dispose() {
				super.dispose();
				System.exit(0);
			}
		};
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setSize(300, 200);

		Container c = f.getContentPane();
		c.setLayout(new FlowLayout());

		c.add(new ByteBinder("Byte"));

		f.setVisible(true);
	}
	
	public void resetChangeIndicator() {
		JLabel label = (JLabel)getIconLabelComponent();
		label.setIcon(null);
		label.setToolTipText(null);
	}
	public void setEnableIndicator( boolean enableFlag ) {
		mEnableIndicator = enableFlag;
	}
	public boolean indicatorEnabled() {
		return mEnableIndicator;
	}
	public boolean isIndicatorRaised() {
			if (((JLabel)getIconLabelComponent()).getIcon() != null) return true;
			else return false;
	}
}
