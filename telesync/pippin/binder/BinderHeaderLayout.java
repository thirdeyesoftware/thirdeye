   //
   //
   // Joseph A. Dudar, Inc   (C) 1999
// //
////

package pippin.binder;



import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.swing.border.*;



/**
*/
public abstract class BinderHeaderLayout implements LayoutManager {

	JComponent mFormComponent = null;


	public BinderHeaderLayout(Container c, JComponent formComponent) {
		mFormComponent = formComponent;
		c.add(formComponent);
	}


	/**
	* @return
	* The desired width of the form component.
	*/
	abstract protected int getFormWidth();

	abstract protected int getTitleWidth();

	abstract protected int getGapWidth();


	public void addLayoutComponent(String name, Component component) {
	}

	public void layoutContainer(Container container) {
		Insets insets = container.getInsets();
		Dimension size = container.getSize();
		Dimension lieu = new Dimension(
				size.width - insets.left - insets.right,
				size.height - insets.top - insets.bottom);

		int titleX, titleSpace, formX;


		formX = lieu.width - getFormWidth();


		Point p = new Point();
		Dimension d;

		d = mFormComponent.getPreferredSize();
		d.width = getFormWidth();
		mFormComponent.setSize(d);
		p.y = insets.top + (lieu.height - d.height) / 2;
		p.x = insets.left + formX;
		mFormComponent.setLocation(p);
	}

	public Dimension minimumLayoutSize(Container container) {
		return preferredLayoutSize(container);
	}

	public Dimension preferredLayoutSize(Container container) {
		Insets insets = container.getInsets();
		Dimension size = new Dimension(0, 0);

		size.width = insets.left + getTitleWidth() +
				getGapWidth() + getFormWidth() + insets.right;

		size.height = mFormComponent.getPreferredSize().height;

		size.height += insets.top + insets.bottom;

		return size;
	}

	public void removeLayoutComponent(Component component) {
	}
}
