   //
   //
   // Joseph A. Dudar, Inc   (C) 2000
// //
////

package pippin.binder;



import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import javax.swing.event.*;



public class ByteArrayBinder extends FormField {
	JTextField mTextField;
	int mMinimumBytes  = 0;
	int mMaximumBytes  = 0;

	public ByteArrayBinder(String title) {
		this(title, 0, 0);
	}

	public ByteArrayBinder(String title, int min, int max) {
		super(title);

		mMinimumBytes = min;
		mMaximumBytes = max;

		setFormComponent(createByteArrayComponent());
		clear();
	}


	protected JComponent createFormComponent() {
		return null;
	}


	protected JComponent createByteArrayComponent() {
		mTextField = new JTextField();
		mTextField.setDocument(new ByteArrayDocument(mTextField,
				mMinimumBytes, mMaximumBytes));
		
		if (mMaximumBytes > 0) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < mMaximumBytes; ++i) {
				if (i != 0) {
					sb.append(" ");
				}
				sb.append("00");
			}
			setPreferredFieldWidth(mTextField, sb.toString());
		} else {
			setPreferredFieldWidth(mTextField);
		}

		return mTextField;
	}


	public Object getBoundValue() {
		byte[] b = ((ByteArrayDocument) mTextField.getDocument()).getBytes();

		if (b != null && b.length == 0) {
			return null;
		} else {
			return b;
		}
	}


	public void setBoundValue(Object value) {
		byte[] b = (byte[]) value;

		((ByteArrayDocument) mTextField.getDocument()).setBytes(b);
	}


	// Subclasses should override this as needed.
	public Class getBoundType() {
		return byte[].class;
	}


	public void clear() {
		byte[] b = new byte[mMinimumBytes];

		setBoundValue(b);
	}


	private static class TestListener implements ActionListener {
		ByteArrayBinder mBinder;
		TestListener(ByteArrayBinder b) {
			mBinder = b;
		}
		public void actionPerformed(ActionEvent event) {
			byte[] b = new byte[7];
			for (int i = 0; i < 7; ++i) {
				b[i] = (byte) i;
			}
			mBinder.setBoundValue(b);
		}
	}


	public static void main(String[] args) {
		JFrame f = new JFrame("ByteArrayBinder Test") {
			public void dispose() {
				super.dispose();
				System.exit(0);
			}
		};
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setSize(300, 200);

		Container c = f.getContentPane();
		c.setLayout(new FlowLayout());

		ByteArrayBinder binder;
		c.add(binder = new ByteArrayBinder("Byte Array", 5, 10));

		JButton b = new JButton("Update");
		b.addActionListener(new TestListener(binder));
		c.add(b);

		f.setVisible(true);
	}
}
