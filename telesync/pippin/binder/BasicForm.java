   //
   //
   // Joseph A. Dudar, Inc   (C) 2000
// //
////

package pippin.binder;


import java.util.*;
import java.awt.*;
import javax.swing.*;



public class BasicForm extends DefaultBinderPanel {
					
	Vector mComponents = new Vector();
	Vector mFormComponents = new Vector();


	public BasicForm(TypeSet typeSet) {
		this(typeSet, true);
	}


	public BasicForm(TypeSet typeSet, boolean setLayout) {
		super(typeSet);

		if (setLayout) {
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		}
	}


	public void addField(String name, DefaultBinder field)
			throws BinderException {
		addField(name,field, Binder.RECEIVE_EVENTS, Binder.SHOW_COMPONENT);
		
	}
	public void addField(String name, DefaultBinder field, boolean requestsEvents) {
		
		addField(name, field, requestsEvents, Binder.SHOW_COMPONENT);
		
	}
	
	public void addField(String name, DefaultBinder field, boolean requestsEvents, boolean isVisible) {
		if (!hasBinder(name)) {
			super.addBinder(name, (Binder) field, isVisible);
			
			if (isVisible) {
				addComponent(field);
				validate();
				repaint();
				if (requestsEvents) {
					if (field instanceof StateChangeBinder) {
						((StateChangeBinder)field).setEnableIndicator( true );
					}
				}
				
			}
			
		}
	}
	
	protected void removeField(String name)
			throws BinderException {

		if (hasBinder(name)) {
			DefaultBinder field = (DefaultBinder) getBinder(name);
			super.removeBinder(name);

			removeComponent(field);
			validate();
			repaint();
		}
	}


	private void addComponent(JComponent component) {
		if (!mFormComponents.isEmpty()) {
			Component s = createHorizontalSeparator(5);

			add(s);
			mComponents.addElement(s);
		}

		add(component);

		mComponents.addElement(component);
		mFormComponents.addElement(component);
	}


	private void removeComponent(JComponent component) {
		int index = mComponents.indexOf(component);

		remove(component);

		mComponents.removeElement(component);
		mFormComponents.removeElement(component);

		if (index > 0) {
			remove(index - 1);
			mComponents.removeElementAt(index - 1);
		}
	}


	// This isn't strictly needed, since in most cases where we
	// need to restrict vertical expansion, the BasicForm will
	// be placed within a matte JPanel that needs to restrict
	// vertical expansion itself.
	/*
	public Dimension getMaximumSize() {
		return new Dimension(super.getMaximumSize().width,
				getPreferredSize().height);
	}
	*/
	
	
}
