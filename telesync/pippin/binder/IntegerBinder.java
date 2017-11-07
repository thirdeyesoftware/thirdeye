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
import javax.swing.text.*;



public class IntegerBinder extends FormField {
	JTextField mJTextField;
	
	JPanel mJPanel;
	
	
	private boolean hasUnits = false;
	private String unitLabel = "";
	
	
	public IntegerBinder(String title) {
			super(title);
	}
	
	protected JComponent createFormComponent() {
		mJPanel = new JPanel();
		mJPanel.setOpaque( false );
		mJPanel.setLayout( new BoxLayout(mJPanel, BoxLayout.X_AXIS) );
		
		
		mJTextField = new JTextField();
		mJTextField.setDocument(new IntegerDocument());
		setPreferredFieldWidth( mJTextField, "888" );
		
		mJPanel.add( mJTextField );
		

		setPreferredFieldWidth( mJPanel);		
		return mJPanel;
	}

	
	/**
	 * @author jeff blau (jb-01052001)
	 * 
	 * adds units to the right of the integer binder field.
	 * Note: this method will resize binder to include the width 
	 * of the unit label.
	 *
	 * @param		units unit abbreviation.
	 * 
	 */
	 
	public void setUnits(String units) {
		
		mJPanel.add(Box.createHorizontalStrut(3));
		
		JLabel unitLabel = new JLabel(units);
		
		//revisit this font issue... want to make font plain...currently bold.
		//unitLabel.setFont( new Font(unitLabel.getFont().getName(),
		//									 unitLabel.getFont().getSize(),Font.PLAIN) );
		mJPanel.add( unitLabel );
		setPreferredFieldWidth( mJPanel);
		
	}
	/**
	 * @author jeff blau (jb-01052001)
	 * overrides default setEnabled() behavior
	 * 
	 * @param enabled boolean flag
	 *
	 */
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		mJTextField.setEnabled( enabled );
	}
	
	public Object getBoundValue() {
		Integer i = null;
		String s = mJTextField.getText();
		
		if (s.length() > 0) {
			try {
				i = new Integer(mJTextField.getText());
			}
			catch (NumberFormatException nfe) {
				throw new Error(nfe.toString());
			}
		}

		return i;
	}


	public void setBoundValue(Object value) {
		mJTextField.setText(value.toString());
	}


	public Class getBoundType() {
		return Integer.class;
	}


	/*
	class IntegerDocument extends PlainDocument {
		public void insertString(int offset, String insertString,
				AttributeSet attributeSet) 
				throws BadLocationException {
			
			if (insertString == null) {
				return;
			}

			for (int i = 0; i < insertString.length(); ++i) {
				char c = insertString.charAt(i);

				if (!Character.isDigit(c)) {
					throw new BadLocationException("illegal character '" +
							c + "'", offset + i);
				}
			}

			super.insertString(offset, insertString, attributeSet);
		}
	}
	*/
}
