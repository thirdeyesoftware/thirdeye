   //
   //
   // Joseph A. Dudar, Inc   (C) 2000
// //
////

package pippin.binder;


import java.awt.*;
import javax.swing.*;
import java.text.DateFormat;
import java.util.Date;



public class HighlightBooleanBinder extends BooleanBinder implements StateChangeBinder {
	boolean mHighlightState = true;
	Color mHighlightColor;
	Color mRestColor = null;
	private String mBinderKey = "";
	private boolean mEnableIndicator = false;
	
	public HighlightBooleanBinder(String title, String binderKey, boolean highlightState) {
		this(title, binderKey, highlightState,
				UIManager.getColor("pippin.colors.titlePanelBG"));
		
	}

	public HighlightBooleanBinder(String title, String binderKey, Color highlightColor) {
		this(title, binderKey, true, highlightColor);
		
	}

	public HighlightBooleanBinder(String title, String binderKey, boolean highlightState,
			Color highlightColor) {
		super(title);
		mBinderKey = binderKey;
		mHighlightState = highlightState;
		mHighlightColor = highlightColor;
	}

	public void setBoundValue(Object value) {
		boolean state = ((Boolean) value).booleanValue();

		if (isInverted()) {
			state = !state;
		}

		if (shouldHighlight(state)) {
			setHighlight(mHighlightColor);
		} else {
			if (mRestColor != null) {
				setHighlight(mRestColor);
			} else {
				clearHighlight();
			}
		}

		super.setBoundValue(value);
	}

	protected boolean shouldHighlight(boolean state) {
		return state == mHighlightState;
	}

	/**
	* Sets the resting highlight color.  <code>null</code> clears
	* the resting highlight (no highlight in rest state).
	*/
	public void setRestColor(Color c) {
		mRestColor = c;
	}
	
	
	public String getBinderKey() {
		return mBinderKey;
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
