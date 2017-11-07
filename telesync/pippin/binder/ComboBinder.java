   //
   //
   // Joseph A. Dudar, Inc   (C) 2000
// //
////

package pippin.binder;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.lang.reflect.*;
import java.util.*;
import java.text.DateFormat;



public class ComboBinder extends FormField implements StateChangeBinder {
	JComboBox mJComboBox;
	Class mTypeClass;
	Vector mActionListeners = new Vector();
	
	private boolean mEnableIndicator = false;
	
	public ComboBinder(String title, Class commandTypeClass,
			Vector choices) {
		this(title, commandTypeClass);
		
		
		setChoices(choices);
	}
	

	/**
	* Constructor for subclasses who want to develop their
	* own set of choices.
	*/
	protected ComboBinder(String title, Class commandTypeClass) {
		super(title);
		mTypeClass = commandTypeClass;
	}

	
	public void setChoices(Vector choices) {
		Object boundValue = null;
		if (mJComboBox != null) {
			boundValue = getBoundValue();
			for (Enumeration en = mActionListeners.elements();
					en.hasMoreElements(); ) {
				mJComboBox.removeActionListener((ActionListener) en.nextElement());
			}
		}

		mJComboBox = new JComboBox();
		for (Enumeration en = mActionListeners.elements();
				en.hasMoreElements(); ) {
			mJComboBox.addActionListener((ActionListener) en.nextElement());
		}

		Enumeration en = choices.elements();
		
		while (en.hasMoreElements()) {
			mJComboBox.addItem(en.nextElement());
		}

		if (boundValue != null) {
			setBoundValue(boundValue);
		}

		setFormComponent(mJComboBox);
	}


	protected JComponent createFormComponent() {
		return null;
	}


	public Object getBoundValue() {
		return mJComboBox.getSelectedItem();
	}


	public void setBoundValue(Object value) {
		mJComboBox.setSelectedItem(value);
	}


	public void clear() {
		mJComboBox.setSelectedIndex(0);
		if (mJComboBox.getItemCount() > 0) {
			mJComboBox.setSelectedIndex(0);
		}
	}


	public Class getBoundType() {
		return mTypeClass;
	}


	public void addActionListener(ActionListener actionListener) {
		if (!mActionListeners.contains(actionListener)) {
			mActionListeners.addElement(actionListener);
			mJComboBox.addActionListener(actionListener);
		}
	}


	public void removeActionListener(ActionListener actionListener) {
		mActionListeners.removeElement(actionListener);
		mJComboBox.removeActionListener(actionListener);
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
