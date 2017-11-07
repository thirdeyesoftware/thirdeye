   // ////
   // // // Jeff Blau (so there!)
   // /////
// // // //
///// ////

package telesync.gui;




import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.text.*;
import pippin.binder.*;
import java.text.*;
import java.lang.Math;
import java.util.Date;

public class OpticalPowerBinder extends FormField implements StateChangeBinder {
	
	private boolean hasUnits = false;
	private String unitLabel = "";
	private JTextField mJTextField;
	private JPanel mJPanel;
	private String mBinderKey = "";
	private boolean mEnableIndicator = false;
	
	public OpticalPowerBinder(String title, String binderKey) {
			this(title);
			mBinderKey = binderKey;
	}
	
	public OpticalPowerBinder(String title) {
		super(title);
	}
	
	public Object getBoundValue() {
		return null;
	}
	

	public JComponent createFormComponent() {
		mJPanel = new JPanel();
		mJPanel.setOpaque( false );
		mJPanel.setLayout( new BoxLayout(mJPanel, BoxLayout.X_AXIS) );


		mJTextField = new JTextField();
		setPreferredFieldWidth( mJTextField, "888.00" );
		mJPanel.add( mJTextField );

		
		setPreferredFieldWidth( mJPanel);		
		return mJPanel;
		
		
	}
	
	public void setUnits(String units) {
			if (mJPanel==null) return;
			
			mJPanel.add(Box.createHorizontalStrut(3));
			
			JLabel unitLabel = new JLabel(units);
			
			//revisit this font issue... want to make font plain...currently bold.
			//unitLabel.setFont( new Font(unitLabel.getFont().getName(),
			//									 unitLabel.getFont().getSize(),Font.PLAIN) );
			mJPanel.add( unitLabel );
			setPreferredFieldWidth( mJPanel );
			hasUnits = true;
			
	}
	
	public void setBoundValue(Object value) {
		if (value != null) {
			DecimalFormat df = new DecimalFormat("#0.0");
			
			double dVal = ((Integer)value).doubleValue() / 100;
			
			mJTextField.setText(df.format( dVal ));
			
		}
		else mJTextField.setText(null);
		
		
	}


	public Class getBoundType() {
		return Integer.class;
	}
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		mJTextField.setEnabled( enabled );
		
	}
	
	public String getBinderKey() {
		return mBinderKey;
	}
	
	/* StateChangeBinder Interface Methods */
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
