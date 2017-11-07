   //
   //
   // Joseph A. Dudar, Inc   (C) 2001
// //
////

package pippin.binder;



import java.awt.*;
import javax.swing.*;
import java.text.DateFormat;
import java.util.Date;


public class ProgressBinder extends FormField implements StateChangeBinder {
	JProgressBar mProgressBar;
	int mStepMax;
	private boolean mEnableIndicator = false;
	

	public ProgressBinder(String title, int stepMax) {
		super(title);
		mStepMax = stepMax;

		setFormComponent(createProgressBar());
	}


	protected JComponent createFormComponent() {
		return null;
	}

	protected JProgressBar createProgressBar() {
		mProgressBar = new JProgressBar(0, mStepMax);
		mProgressBar.setAlignmentY(.5f);
		mProgressBar.setStringPainted(true);

		// mProgressBar.setOpaque(false);

		return mProgressBar;
	}


	public Object getBoundValue() {
		return mProgressBar.getString();
	}


	public void setBoundValue(Object value) {
		mProgressBar.setString((String) value);
	}


	public void setStepCount(int stepCount) {
		mProgressBar.setValue(stepCount);
		//if (stepCount >= mStepMax) mProgressBar.setString("Complete.");
	}


	public void setStepMax(int stepMax) {
		mProgressBar.setMaximum(stepMax);
		mStepMax = stepMax;
	}


	public Class getBoundType() {
		return String.class;
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
