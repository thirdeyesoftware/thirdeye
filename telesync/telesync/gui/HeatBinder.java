   //
   //
   // Joseph A. Dudar, Inc   (C) 2000
// //
////

package telesync.gui;



import java.awt.*;
import java.text.*;
import javax.swing.*;
import pippin.binder.*;



public class HeatBinder extends FormField {
	JTextField mCelsius;
	JTextField mFahrenheit;

	public HeatBinder(String title) {
		super(title);
	}


	public Object getBoundValue() {
		return null;
	}


	protected JComponent createFormComponent() {
		JPanel p = new JPanel();
		p.setOpaque(false);

		mCelsius = new JTextField();
		setPreferredFieldWidth(mCelsius, "000.0");
		mCelsius.setHorizontalAlignment(JTextField.RIGHT);

		mFahrenheit = new JTextField();
		setPreferredFieldWidth(mFahrenheit, "000.0");
		mFahrenheit.setHorizontalAlignment(JTextField.RIGHT);

		JLabel l;

		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		p.add(mCelsius);
		l = new JLabel("C");
		l.setOpaque(false);
		p.add(Box.createHorizontalStrut(3));
		p.add(l);

		p.add(Box.createHorizontalStrut(15));
		p.add(mFahrenheit);
		l = new JLabel("F");
		l.setOpaque(false);
		p.add(Box.createHorizontalStrut(3));
		p.add(l);

		return p;
	}


	public void setBoundValue(Object value) {
		if (value != null) {
			DecimalFormat df = new DecimalFormat("#.0");
			int t = ((Integer) value).intValue();

			float f = ((float) t) / 2;

			mCelsius.setText(df.format(f));
			mFahrenheit.setText(df.format(f * 1.8f + 32));
		} else {
			mCelsius.setText(null);
			mFahrenheit.setText(null);
		}
	}


	public Class getBoundType() {
		return Integer.class;
	}


	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		mFahrenheit.setEnabled(enabled);
		mCelsius.setEnabled(enabled);
	}
}
