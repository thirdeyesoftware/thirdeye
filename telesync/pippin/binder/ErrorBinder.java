   //
   //
   // Joseph A. Dudar, Inc   (C) 2002
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
import java.text.*;
import java.util.Date;

/**
	* accomodates Error counts (long value) as well
	* as Error Rates.
	*/
public class ErrorBinder extends LongBinder {
	private JTextField mErrorRateField;
	private JLabel mUnitsLabel;
	
	private JTextField mErrorSecondsField;
	
	/** 
	 * These members are updated with errorRateNotify()
	 */
	private int mLastRateSeconds;
	private long mLastErrorCount;
	private double mLastErrorRate;
	private int mNumTribs;
	private ErrorRateCalculator mCalculator;
	
	private static final DecimalFormat rateFormat = new DecimalFormat("#####");
	private static final DecimalFormat rateFormatFraction = new DecimalFormat("0.#####E0");
	
	public ErrorBinder(String title) {
		super(title);
		
	}
	
	protected JComponent createFormComponent() {
		JPanel panel = new JPanel();
		panel.setOpaque( false );
		panel.setLayout( new BoxLayout(panel, BoxLayout.X_AXIS) );
		mJTextField = new JTextField();
		mJTextField.setDocument(new IntegerDocument());
		setPreferredFieldWidth(mJTextField, "88888888888888888");
		panel.add(mJTextField);
		panel.add(Box.createHorizontalStrut(1));
		
		mErrorSecondsField = new JTextField();
		setPreferredFieldWidth(mErrorSecondsField, "888888");
		mErrorSecondsField.setToolTipText("Number of seconds during which at least one error occured");
		panel.add(mErrorSecondsField);
		panel.add(Box.createHorizontalStrut(1));
		
		mErrorRateField = new JTextField();
		mErrorRateField.setToolTipText("Average error count over time");
		setPreferredFieldWidth(mErrorRateField, "8888888888");
		panel.add(mErrorRateField);
		
		
		
		panel.add(Box.createHorizontalStrut(2));
		//mUnitsLabel = new JLabel("E / F");
		mUnitsLabel = new JLabel("");
		mUnitsLabel.setToolTipText("Errors per Frame");
		panel.add(Box.createHorizontalStrut(4));
		
		panel.add(mUnitsLabel);
	
		return panel;
	}
	
	public void setBoundValue(Object value) {
		
		super.setBoundValue(value);
		
	}
	
	public void setErrorRate(double rate) {
		rate = Math.abs(rate);
		
		if (rate > 0 && rate < 1 ) {
			
			mErrorRateField.setText(rateFormatFraction.format(rate));
		}
		else {
			mErrorRateField.setText(rateFormat.format(rate));
		}
		
	}
	
	public void setCalculator(ErrorRateCalculator calc) {
		mCalculator = calc;
	}
	
		
	public void setEnabled(boolean enabled) {
		mJTextField.setEnabled(enabled);
		mErrorRateField.setEnabled(enabled);
		mErrorSecondsField.setEnabled(enabled);
	}
	
	/**
	 * sets error rate visible
	 */
	public void setErrorRateVisible(boolean vis) {
		mErrorRateField.setVisible(vis);
	}
	
	public void calculateErrorRate(int time) {
		
		
		long count = 0;

		if (getBoundValue() != null) {
			count = ((Long)getBoundValue()).longValue();
		}
		
		//10/25/2002 - change rate to average rate instead of 
		//instantaneous rate.
		//int dT = time - mLastRateSeconds;
		//long dE = count - mLastErrorCount;
		long dT = time;
		long dE = count;
		
		double rate = 0.0;
		if (dT == 0) rate = mLastErrorRate;
		else {
			if (mCalculator != null) 
				rate = mCalculator.calculateErrorRate(mNumTribs, 
		 																	dT,
																		 	dE);
		}
		
		setErrorRate(rate);
		mLastErrorCount = count;
		mLastRateSeconds = time;
		mLastErrorRate = rate;
		
	}
	
	public void setUnits(String s, String toolTip) {
		mUnitsLabel.setText(s);
		mUnitsLabel.setToolTipText(toolTip);
	}
	
	public void resetErrorRate() {
		mLastRateSeconds = 0;
		mLastErrorCount = 0;
		mLastErrorRate = 0.0;
		setErrorRate(0);
	}
	public void setTributaryCount(int count) {
		mNumTribs = count;
	}
	public int getTributaryCount() {
		return mNumTribs;
	}
	
	public void setErrorSeconds(int val) {
		mErrorSecondsField.setText(String.valueOf(val));
	}
	
	public void resetErrorSeconds() {
		mErrorSecondsField.setText(String.valueOf(0));
	}
	
}

			
	
	
		