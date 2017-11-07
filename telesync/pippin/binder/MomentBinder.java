   //
   //
   // Joseph A. Dudar, Inc   (C) 2000
// //
////

package pippin.binder;



import java.util.*;
import javax.swing.*;
import pippin.binder.*;
import pippin.util.Moment;



public class MomentBinder extends FormField {

	public MomentBinder(String title) {
		super(title);
	}


	protected JComponent createFormComponent() {
		return (JComponent) new MomentComponent();
	}


	public Object getBoundValue() {
		MomentComponent c  = (MomentComponent) getFormComponent();

		return c.getValue();
	}


	public void setBoundValue(Object value) {
		MomentComponent c  = (MomentComponent) getFormComponent();

		c.setValue((Moment) value);
	}


	public Class getBoundType() {
		return Moment.class;
	}


	public void clear() {
		setBoundValue(new Moment());
	}


	protected class MomentComponent extends JTextField {

		MomentComponent() {
			setDocument(new TemplateDocument(this, "00/00/0000 00:00:00"));
			setKeymap(BinderUtilities.getTextFieldKeymap());
		}


		void setValue(Moment moment) {
			setText(moment.getDateString() + " " + moment.getTimeString());
		}


		Moment getValue() {
			StringTokenizer st = new StringTokenizer(getText());

			String d = st.nextToken();
			String t = st.nextToken();

			return new Moment(d, t);
		}
	}
}
