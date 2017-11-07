   //
   //
   // Joseph A. Dudar, Inc   (C) 2000
// //
////

package pippin.binder;



import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;



public class ButtonForm extends TitleForm {
	Vector mButtons = new Vector();
	JPanel mButtonPanel;

	public ButtonForm(String title, TypeSet typeSet) {
		this(title, typeSet, true);
	}


	public ButtonForm(String title, TypeSet typeSet, boolean indent) {
		super(title, typeSet, indent);

		mButtonPanel = ButtonMaker.createButtonPanel();

		/*
		JPanel p = new JPanel(new BorderLayout());
		p.add(mButtonPanel, BorderLayout.CENTER);

		p.setBorder(new EmptyBorder(5,0,0,0));
		*/

		getMainContainer().add(mButtonPanel, BorderLayout.SOUTH);
	}


	public void addButton(JButton button) {
		if (mButtons.size() == 0) {
			mButtonPanel.add(Box.createHorizontalGlue());
		} else {
			mButtonPanel.add(Box.createHorizontalStrut(5));
		}
		mButtons.addElement(button);
		mButtonPanel.add(button);
	}
	
}
