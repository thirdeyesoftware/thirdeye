   //
   //
   // Joseph A. Dudar, Inc   (C) 1999
// //
////

package pippin.binder;



import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;



/**
*/
public class BinderDialog extends JDialog {

	JButton mApplyButton;
	JButton mCancelButton;
	ButtonAdapter mButtonAdapter = new ButtonAdapter();
	boolean mApplied = false;
	BinderPanel mBinderPanel = null;
	JPanel mButtonPanel;
	Frame mOwner;
	JScrollPane mScrollPane;

	public BinderDialog(Frame owner, String title,
			BinderPanel binderPanel) {
		this(owner, title, binderPanel, "Apply", "Cancel");
	}


	public BinderDialog(Frame owner, String title,
			BinderPanel binderPanel, String applyLabel, String cancelLabel) {
		super(owner, title, true);

		mOwner = owner;

		mBinderPanel = binderPanel;

		JPanel mattePanel = new JPanel() {
			public Dimension getMaximumSize() {
				return new Dimension(super.getMaximumSize().width,
						getPreferredSize().height);
			}
		};

		mScrollPane = new JScrollPane(mBinderPanel) {
			public Dimension getMaximumSize() {
				return new Dimension(super.getMaximumSize().width,
						getPreferredSize().height);
			}
		};
		mScrollPane.getHorizontalScrollBar().setUnitIncrement(20);
		mScrollPane.getVerticalScrollBar().setUnitIncrement(20);
		mScrollPane.setBorder(new CompoundBorder(
				new EmptyBorder(10,10,10,10),
				new EtchedBorder(EtchedBorder.RAISED)));
		mScrollPane.setViewportBorder(new EmptyBorder(5,5,5,5));

		/*
		mattePanel.setBorder(new CompoundBorder(
				new EmptyBorder(10,10,10,10),
				new CompoundBorder(
				new EtchedBorder(EtchedBorder.RAISED),
				new EmptyBorder(5,5,5,5))));
		mattePanel.setLayout(new BorderLayout());
		mattePanel.add(mBinderPanel, BorderLayout.CENTER);
		*/

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		mButtonPanel = new JPanel() {
			public Dimension getMaximumSize() {
				return new Dimension(
					super.getMaximumSize().width,
					super.getPreferredSize().height);
			}
		};
		mButtonPanel.setLayout(new BoxLayout(mButtonPanel, BoxLayout.X_AXIS));

		Container contentPane = getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		// contentPane.add(mattePanel);
		contentPane.add(mScrollPane);
		contentPane.add(Box.createVerticalGlue());
		contentPane.add(mButtonPanel);

		mButtonPanel.setBorder(new CompoundBorder(
				new EtchedBorder(),
				new EmptyBorder(5, 7, 5, 7)));
		mButtonPanel.setBackground(UIManager.getColor(
				"pippin.colors.titlePanelBG"));
		
		mApplyButton = new JButton(applyLabel);
		getRootPane().setDefaultButton(mApplyButton);
		mCancelButton = new JButton(cancelLabel);

		mButtonPanel.add(Box.createHorizontalGlue());
		addButton(mApplyButton);
		addButton(mCancelButton);

		sizeFrame();

		addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent event) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						mBinderPanel.requestFocus();
					}
				});
			}
			public void focusLost(FocusEvent event) {
			}
		});

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				mApplied = false;
				mBinderPanel.cancel();
			}
		});

		// This should usually not be done for any Dialog, since it
		// blocks execution of subclass constructors until after the
		// Dialog is concluded.
		//
		// setVisible(true);
	}


	public void sizeFrame() {
		Dimension s = this.getSize();
		Dimension d = this.getPreferredSize();

		if (d.width < 320) {
			d.width = 320;
		}
		if (d.height < 200) {
			d.height = 200;
		}

		if (!s.equals(d)) {
			Dimension od = mOwner.getSize();
			if (d.width > od.width) {
				d.width = od.width;
			}
			if (d.height > od.height) {
				d.height = od.height;
			}
			setSize(d);

			Point p = new Point((od.width - d.width)/2, (od.height - d.height)/2);
			if (p.x < 0) {
				p.x = 0;
			}
			if (p.y < 0) {
				p.y = 0;
			}
			Point ol = mOwner.getLocation();
			p.translate(ol.x, ol.y);
			setLocation(p);
		}
	}


	public void setColumnHeader(JComponent c) {
		mScrollPane.setColumnHeaderView(c);
	}


	public void addNotify() {
		super.addNotify();
		sizeFrame();
	}


	public void addButton(JButton button) {
		button.addActionListener(mButtonAdapter);
		button.setAlignmentY(.5f);
		mButtonPanel.add(Box.createHorizontalStrut(5));
		mButtonPanel.add(button);
		button.setFont(new Font("SansSerif",
				Font.PLAIN, 10));
		// button.setBackground(mButtonPanel.getBackground());
		button.setOpaque(false);
	}


	public void setVisible(boolean isVisible) {
		if (isVisible && !isVisible()) {
			// Message the BinderPanel so that it can start
			// any needed listeners, etc.
			mBinderPanel.start();
		}

		super.setVisible(isVisible);
	}


	public void dispose() {
		super.dispose();

		// Message the BinderPanel so that it can do any
		// needed cleanup of listeners, etc.
		mBinderPanel.dispose();
	}


	public Envelope getEnvelope() {
		if (mApplied) {
			return mBinderPanel.getEnvelope();
		} else {
			return null;
		}
	}


	protected Envelope getCurrentEnvelope() {
		return mBinderPanel.getEnvelope();
	}


	class ButtonAdapter implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (event.getSource() == mApplyButton) {
				StringBuffer messageBuffer = new StringBuffer();

				Vector v = mBinderPanel.getRequiredNames();
				if (v.size() > 0) {
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < v.size(); ++i) {
						String requiredName = (String) v.elementAt(i);
						String accessibleName = mBinderPanel.getBinder(requiredName)
								.getAccessibleContext().getAccessibleName();
						if (accessibleName != null && accessibleName.length() > 0) {
							sb.append(accessibleName);
						} else {
							sb.append(requiredName);
						}
						if (i < v.size() - 1) {
							sb.append("\n");
						}
					}
					JOptionPane.showMessageDialog(BinderDialog.this,
							"The following fields require input:\n" + sb.toString(),
							"Required Fields", JOptionPane.ERROR_MESSAGE);
				} else
				if (!hasValidData(messageBuffer)) {
					JOptionPane.showMessageDialog(BinderDialog.this,
							"The form cannot be applied:\n" + messageBuffer.toString(),
							"Invalid Data", JOptionPane.ERROR_MESSAGE);
				} else {
					mApplied = true;
					dispose();
				}
			} else
			if (event.getSource() == mCancelButton) {
				mApplied = false;
				mBinderPanel.cancel();
				dispose();
			}
		}
	}


	/**
	* Hook for subclasses to provide a final, customized
	* validation pass on the Dialog's contents.  If something
	* is wrong with the data, message should be filled with
	* an explanation of the problem.
	*/
	protected boolean hasValidData(StringBuffer message) {
		return true;
	}
}
