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

public class SecondsBinder extends FormField {
	
	public SecondsBinder(String title) {
		super(title);
	}
	public Class getBoundType() {
		return Moment.class;
	}
	
	public Object getBoundValue() {
		SecondsComponent c  = (SecondsComponent) getFormComponent();

		return c.getValue();
	}
	public void setBoundValue(Object value) {
		SecondsComponent c  = (SecondsComponent) getFormComponent();

		c.setValue((Moment) value);
	}
	
	public void clear() {
		setBoundValue(null);
	}
	
	protected JComponent createFormComponent() {
			return (JComponent) new SecondsComponent();
	}
	class SecondsComponent extends JTextField {
	
		SecondsComponent() {
			setDocument(new TemplateDocument(this, "0000:00:00"));
			setKeymap(BinderUtilities.getTextFieldKeymap());
		}


		void setValue(Moment moment) {
			if (moment == null) setText(null);
			else 
				setText(moment.toDurationString(false));
		}

		Moment getValue() {
			return new Moment(getText());
		}

		
	
	}
}
