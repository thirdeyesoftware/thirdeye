   //
   //
   // Joseph A. Dudar, Inc   (C) 2000
// //
////

package pippin.binder;



import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;



public class FormField extends DefaultBinder  {

	public FormField(String title) {
		this(title, null);
	}

	public FormField(String title, int preferredWidth) {
		this(title, null);

		setPreferredFieldWidth(preferredWidth);
	}


	public FormField(String title, JComponent formComponent) {
		super(title, formComponent);
	}

	public static void setPreferredFieldWidth(JComponent c, String sample) {
		Dimension d = c.getPreferredSize();
		FontMetrics fm = c.getFontMetrics(c.getFont());
		d.width = fm.stringWidth(sample) + 6;
		c.setMinimumSize(d);
		c.setPreferredSize(d);
		c.setMaximumSize(d);
	}


	public void setPreferredFieldWidth(String sample) {
		setPreferredFieldWidth(getFormComponent(), sample);
	}
}
