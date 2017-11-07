package pippin.binder;

import javax.swing.*;
import java.util.*;
import java.text.*;

/* work in progress - will be abstract decorator */
public class FlagDecider extends ErrorBinder {
	private StateChangeBinder mBinder;
	
	public FlagDecider( String title ) {
		super(title);
		
		
	}
	public void setBoundValue(Object o, long time) {
		JLabel iconLabel = (JLabel)getIconLabelComponent();
		if (!flagRaisable(o)) {
			setBoundValue(o);
			iconLabel.setIcon( null );
			return;
		}
		if (iconLabel.getIcon()==null) {
			iconLabel.setIcon(BinderIcon.ICON_EVENT);
		}

		DateFormat df = DateFormat.getDateTimeInstance();
		Date date = new Date(time);

		iconLabel.setToolTipText(df.format(date));

		setBoundValue(o);
	}
	public void resetChangeIndicator() {
		super.resetChangeIndicator();
	}
	public void setEnableIndicator( boolean b ) {
		super.setEnableIndicator(b);
	}
	public boolean indicatorEnabled() {
		return super.indicatorEnabled();
	}
	public boolean flagRaisable(Object o) {
		long value = ((Long)o).longValue();
		if (value == 0) {
			return false;
		}
		return true;
	}
	
}
