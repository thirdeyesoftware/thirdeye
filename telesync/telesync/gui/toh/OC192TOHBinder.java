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
import pippin.binder.*;



public class OC192TOHBinder extends FormField {
	static int BIT_WIDTH = 64; // for 192 impl
	//static int BIT_WIDTH = 16; //for 48 impl
	static int OC_SPEED = 192;
	
	JTextField mTextField[] = new JTextField[3];
	
	/* precursor to complete line speed extraction... */
	
	/* ********************************************** */
	
	public OC192TOHBinder(String title) {
		super(title);

		setFormComponent(createTOHComponent());
		clear();
	}


	protected JComponent createFormComponent() {
		return null;
	}


	protected JComponent createTOHComponent() {
		Font f = new Font("Monospaced", Font.PLAIN, 11);

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < BIT_WIDTH; ++i) {
			if (i != 0) {
				sb.append(" ");
			}
			sb.append("00");
		}

		for (int i = 0; i < 3; ++i) {
			mTextField[i] = new JTextField();
			mTextField[i].setFont(f);
			mTextField[i].setDocument(new ByteArrayDocument(mTextField[i],
					BIT_WIDTH, BIT_WIDTH));
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
		byte[] b = new byte[OC_SPEED];
		byte[] a;

		for (int i = 0; i < 3; ++i) {
			a = ((ByteArrayDocument) mTextField[i].getDocument()).getBytes();
			System.arraycopy(a, 0, b, i * BIT_WIDTH, BIT_WIDTH);
		}

		return b;
	}


	public void setBoundValue(Object value) {
		byte[] b = (byte[]) value;
		byte[] a = new byte[BIT_WIDTH];
		
		if (b.length != OC_SPEED) {
			throw new ArrayIndexOutOfBoundsException("length " + b.length +
			" != 192");
		}

		for (int i = 0; i < 3; ++i) {
			System.arraycopy(b, i * BIT_WIDTH, a, 0, BIT_WIDTH);
			
			((ByteArrayDocument) mTextField[i].getDocument()).setBytes(a);
		}
	}


	// Subclasses should override this as needed.
	public Class getBoundType() {
		return byte[].class;
	}


	public void clear() {
		byte[] b = new byte[OC_SPEED];

		setBoundValue(b);
	}


	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		for (int i = 0; i < 3; ++i) {
			mTextField[i].setEnabled(enabled);
		}
	}


	private static class TestListener implements ActionListener {
		OC192TOHBinder mBinder;
		TestListener(OC192TOHBinder b) {
			mBinder = b;
		}
		public void actionPerformed(ActionEvent event) {
			byte[] b = new byte[OC_SPEED];
			for (int i = 0; i < OC_SPEED; ++i) {
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

		OC192TOHBinder binder;
		c.add(binder = new OC192TOHBinder("TOH"));

		JButton b = new JButton("Update");
		b.addActionListener(new TestListener(binder));
		c.add(b);

		f.setVisible(true);
	}
}
