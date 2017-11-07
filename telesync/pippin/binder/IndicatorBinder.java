   //
   //
   // Joseph A. Dudar, Inc   (C) 2000
// //
////

package pippin.binder;



import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import pippin.binder.*;
import pippin.util.*;




public class IndicatorBinder extends FormField {

	public IndicatorBinder(String title) {
		super(title);
	}


	protected JComponent createFormComponent() {
		return (JComponent) new IndicatorComponent();
	}


	public Object getBoundValue() {
		IndicatorComponent c  = (IndicatorComponent) getFormComponent();

		return c.getValue();
	}


	public void setBoundValue(Object value) {
		IndicatorComponent c  = (IndicatorComponent) getFormComponent();

		c.setValue((IndicatorStatus) value);
	}


	public Class getBoundType() {
		return IndicatorStatus.class;
	}


	public void clear() {
		setBoundValue(new IndicatorStatus());
	}


	protected class IndicatorComponent extends JPanel {
		JLabel mLabelError;
		JLabel mLabelHistory;
		private ImageIcon ERROR_ICON = ErrorIconWhite.getImageIcon();
		private ImageIcon NO_INDICATOR_ICON = NoIndicatorIconWhite.getImageIcon();
		private ImageIcon ERROR_HISTORY_ICON = ErrorHistoryIconWhite.getImageIcon();
		
		private IndicatorStatus mStatus;
		
		IndicatorComponent() {
			init();
			
		}

		void init() {
			mLabelError = new JLabel(NO_INDICATOR_ICON);
			mLabelError.setToolTipText("Error Indicator");
			mLabelError.setAlignmentX(Component.CENTER_ALIGNMENT);

			mLabelHistory = new JLabel(NO_INDICATOR_ICON);
			mLabelHistory.setAlignmentX(Component.CENTER_ALIGNMENT);
			mLabelHistory.setToolTipText("Error History Indicator");
			this.setLayout( new BoxLayout(this, BoxLayout.X_AXIS) );
			this.setAlignmentX(Component.CENTER_ALIGNMENT);
			this.add( mLabelError );
			this.add( mLabelHistory );
			this.setBackground(new Color(255,255,255));
		}
		
		void setValue(IndicatorStatus status) {
			mStatus = status;
			mLabelError.setIcon( (mStatus.getError()) ? ERROR_ICON : NO_INDICATOR_ICON);
			if (!mStatus.getError()) {
				if (mStatus.getHistory()) {
					mLabelHistory.setIcon(ERROR_HISTORY_ICON);
				}
				else {
					mLabelHistory.setIcon(NO_INDICATOR_ICON);	
				}
			} else {
				mLabelHistory.setIcon(NO_INDICATOR_ICON);
			}
			
		}


		IndicatorStatus getValue() {
			return mStatus;
		}
	}
	
	
	
}
