   //
   //
   // Joseph A. Dudar, Inc   (C) 2000
// //
////

package telesync.gui.toh;



import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import javax.swing.event.*;

import java.util.Date;
import java.text.DateFormat;

import pippin.binder.*;
import telesync.pui.tsi5320.LineSpeed;



public class TOHBinder extends FormField implements StateChangeBinder {
	JTextField mTextField[] = new JTextField[3];
	private boolean mEnableIndicator = false;
	
	static int lineSpeed = LineSpeed.OC48; //default to OC48
	
	public TOHBinder(String title) {
		super(title);
		JComponent c = createTOHComponent();
		
		
		setFormComponent(c);
		clear();
	}
	public TOHBinder(String title, int lineSpeed) {
		super( title );
		this.lineSpeed = lineSpeed;
		System.out.println(lineSpeed);
	}
	

	protected JComponent createFormComponent() {
		return null;
	}


	protected JComponent createTOHComponent() {
		Font f = new Font("Monospaced", Font.PLAIN, 11);

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < (lineSpeed / 3); ++i) {
			if (i != 0) {
				sb.append(" ");
			}
			sb.append("00");
		}

		for (int i = 0; i < 3; ++i) {
			mTextField[i] = new JTextField();
			mTextField[i].setFont(f);
			mTextField[i].setDocument(new ByteArrayDocument(mTextField[i],
					(lineSpeed / 3), (lineSpeed / 3)));
			setPreferredFieldWidth(mTextField[i], sb.toString());
			
		}
		

		JPanel p = new JPanel();
		p.setOpaque(false);
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.add(mTextField[0]);
		p.add(Box.createVerticalStrut(1));
		p.add(mTextField[1]);
		p.add(Box.createVerticalStrut(1));
		p.add(mTextField[2]);

		return p;
	}


	public Object getBoundValue() {
		byte[] b = new byte[lineSpeed];
		byte[] a;

		for (int i = 0; i < 3; ++i) {
			a = ((ByteArrayDocument) mTextField[i].getDocument()).getBytes();
			System.arraycopy(a, 0, b, i * (lineSpeed / 3), (lineSpeed / 3));
		}

		return b;
	}


	public void setBoundValue(Object value) {
		byte[] b = (byte[]) value;
		byte[] a = new byte[(lineSpeed / 3)];

		if (b.length != lineSpeed) {
			throw new ArrayIndexOutOfBoundsException("length " + b.length +
					" != " + lineSpeed);
		}

		for (int i = 0; i < 3; ++i) {
			System.arraycopy(b, i * (lineSpeed / 3), a, 0, (lineSpeed / 3));
			((ByteArrayDocument) mTextField[i].getDocument()).setBytes(a);
		}
	}


	// Subclasses should override this as needed.
	public Class getBoundType() {
		return byte[].class;
	}


	public void clear() {
		byte[] b = new byte[lineSpeed];

		setBoundValue(b);
	}


	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		for (int i = 0; i < 3; ++i) {
			mTextField[i].setEnabled(enabled);
		}
	}


	private static class TestListener implements ActionListener {
		TOHBinder mBinder;
		TestListener(TOHBinder b) {
			mBinder = b;
		}
		public void actionPerformed(ActionEvent event) {
			byte[] b = new byte[lineSpeed];
			for (int i = 0; i < lineSpeed; ++i) {
				b[i] = (byte) i;
			}
			mBinder.setBoundValue(b);
		}
	}


	public static void main(String[] args) {
		JFrame f = new JFrame("TOHBinder Test") {
			public void dispose() {
				super.dispose();
				System.exit(0);
			}
		};
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setSize(300, 200);

		Container c = f.getContentPane();
		c.setLayout(new FlowLayout());

		TOHBinder binder;
		c.add(binder = new TOHBinder("TOH"));

		JButton b = new JButton("Update");
		b.addActionListener(new TestListener(binder));
		c.add(b);

		f.setVisible(true);
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
