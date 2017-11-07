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
* Provides default layout characteristics for a Binder.
*
*/
public class DefaultBinder extends JPanel implements Binder, LayoutManager {
	String mTitle;
	protected JComponent mFormComponent;
	protected JComponent mTitleComponent;
	protected BinderSet mBinderSet;
	protected JComponent mIconLabelComponent;
	
	private ImageIcon mBlankIcon;
	
	static private int cGap = 6;

	static private Font cLabelFont = new Font("SansSerif", Font.PLAIN, 12);


	public DefaultBinder(String title) {
		this(title, null);
	}

	
	public DefaultBinder(String title, JComponent formComponent) {
		
		// setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setLayout(this);
		setOpaque(false);
		
		// This is probably for debug only.  See the AccessibleContext
		// for the real NL name for this Binder.
		setName(title);

		mTitle = title;
		
		mIconLabelComponent = new JLabel(" ");
		
		
		setIconLabelComponent(mIconLabelComponent);
		
		JLabel titleLabel = new JLabel(mTitle);
		titleLabel.setFont(getLabelFont());

		getAccessibleContext().setAccessibleName(title);
		setTitleComponent(titleLabel);

		if (formComponent == null) {
			setFormComponent(mFormComponent = createFormComponent());
		} else {
			setFormComponent(mFormComponent = formComponent);
		}

		// Pass focus on to the "form" component.
		addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent event) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						getFormComponent().requestFocus();
					}
				});
			}
			public void focusLost(FocusEvent event) {
			}
		});
	}


	static public Font getLabelFont() {
		return cLabelFont;
	}

	private void setIconLabelComponent(JComponent c) {
		if (mIconLabelComponent != null) {
			remove(mIconLabelComponent);
		}

		Dimension d = c.getPreferredSize();
		d.width = 10;
		c.setMinimumSize(d);
		c.setPreferredSize(d);
		c.setMaximumSize(d);
		mIconLabelComponent = c;
		add(mIconLabelComponent);
	}
	protected JComponent getIconLabelComponent() {
		return mIconLabelComponent;
	}
	
	private void setTitleComponent(JComponent c) {

		if (mTitleComponent != null) {
			remove(mTitleComponent);
		}
		mTitleComponent = c;

		if (mTitleComponent instanceof JLabel) {
			((JLabel) mTitleComponent).setLabelFor(mFormComponent);

			/*
			((JLabel) mTitleComponent).setHorizontalAlignment(
					SwingConstants.RIGHT);
			*/
		}
		add(mTitleComponent);
	}


	/**
	* Called by subclasses to set the formComponent.  This is
	* usually used by subclasses that need to develop the
	* formComponent very late, like in their own constructor.
	* In that case, createFormComponent() should return <code>null</code>.
	*
	* @param c
	* The JComponent that serves as an editor for this DefaultBinder.
	* The JComponent may be <code>null</code>.
	*
	* @see #createFormComponent
	*/
	protected void setFormComponent(JComponent c) {
		if (mFormComponent != null) {
			remove(mFormComponent);
		}
		mFormComponent = c;

		if (c != null) {
			
			if (mTitleComponent instanceof JLabel) {
				((JLabel) mTitleComponent).setLabelFor(mFormComponent);
			}
			mFormComponent.setFont(getLabelFont());

			// This should be done in the subclass on a case by case
			// basis.  Some components will want to be opaque, such
			// as JTextFields.
			//
			// mFormComponent.setOpaque(false);
			
			add(c);
			invalidate();
			validate();
			repaint();
		} else {
			// A subclass must be going the "late" route.
		}
	}


	/**
	* This is a hook into the constructor to allow
	* subclasses to develop their own form components
	* late in the superconstructor.
	*
	* @return
	* The JComponent used as an editor for the bound value.
	* Can return <code>null</code> if the subclass wants
	* to develop the component very very late, like in its
	* own constructor.
	*/
	
	protected JComponent createFormComponent() {
		JTextField tf = BinderUtilities.createNoCRTextField();

		setPreferredFieldWidth(tf);

		return (JComponent) tf;
	}


	public static void setPreferredFieldWidth(JComponent c) {
		Dimension d = c.getPreferredSize();
		d.width = 85;
		c.setMinimumSize(d);
		c.setPreferredSize(d);
		c.setMaximumSize(d);
	}


	public void setPreferredFieldWidth(int width) {
		JComponent c = getFormComponent();

		Dimension d = c.getPreferredSize();
		d.width = width;
		c.setMinimumSize(d);
		c.setPreferredSize(d);
		c.setMaximumSize(d);
	}


	public JComponent getFormComponent() {
		return mFormComponent;
	}


	protected JComponent getTitleComponent() {
		return mTitleComponent;
	}


	public void setHighlight() {
		setHighlight(null);
	}

	public void setHighlight(Color color) {
		if (color == null) {
			color = getBackground().darker();
		}
		getTitleComponent().setOpaque(true);
		getTitleComponent().setBackground(color);
		getTitleComponent().setBorder(new CompoundBorder(
				new LineBorder(Color.gray),
				new EmptyBorder(0,5,0,5)));
	}

	public void clearHighlight() {
		getTitleComponent().setOpaque(false);
		getTitleComponent().setBorder(null);
	}


	public void setBinderSet(BinderSet binderSet) {
		mBinderSet = binderSet;
	}


	public BinderSet getBinderSet() {
		return mBinderSet;
	}


	public Object getBoundValue() {
		String t;

		if (mFormComponent instanceof JTextField) {
			t = ((JTextField) mFormComponent).getText();
		} else
		if (mFormComponent instanceof JComboBox) {
			t = ((JComboBox) mFormComponent).getSelectedItem().toString();
		} else {
			t = mFormComponent.toString();
		}

		if (t != null && t.length() == 0) {
			return null;
		} else {
			return t;
		}
	}


	public void setBoundValue(Object value) {
		if (mFormComponent instanceof JTextField) {
			((JTextField) mFormComponent).setText((String) value);
		} else {
			throw new Error("Subclasses must override this method accordingly.");
		}
	}


	// Subclasses should override this as needed.
	public Class getBoundType() {
		return String.class;
	}


	/**
	* Allow horizontal expansion.  Restrict vertical expansion.
	*/
	public Dimension getMaximumSize() {
		Dimension md = super.getMaximumSize();
		Dimension pd = super.getPreferredSize();

		return new Dimension(md.width, pd.height);
	}


	/**
	* Pass setEnabled() to the "form" component.
	*/
	public void setEnabled(boolean enabled) {
		getFormComponent().setEnabled(enabled);
		getTitleComponent().setEnabled(enabled);
		getFormComponent().setRequestFocusEnabled(enabled);
	}


	public void addLayoutComponent(String name, Component component) {
	}

	public void layoutContainer(Container container) {
		int iconLabelWidth = 10;
		int iconLabelGap = 4;
		
		Insets insets = container.getInsets();
		Dimension size = container.getSize();
		Dimension lieu = new Dimension(
				size.width - insets.left - insets.right,
				size.height - insets.top - insets.bottom);

		int titleX, titleSpace, formX, iconX;
		iconX = insets.left;
		if (getParent() instanceof DefaultBinderPanel) {
			DefaultBinderPanel dbp = (DefaultBinderPanel) getParent();
			
			
			
			formX = lieu.width - dbp.getMaxFormWidth();
			titleX = formX - dbp.getLabelGap() - dbp.getMaxTitleWidth() -
					mTitleComponent.getInsets().left - dbp.getIconLabelGap() - dbp.getMaxIconLabelWidth();
			if (titleX < 0) {
				//accomodate for the new icon label, left-justified.
				titleX = dbp.getIconLabelGap() + dbp.getMaxIconLabelWidth();
			}
			
			titleSpace = formX - dbp.getLabelGap() - titleX +
					mTitleComponent.getInsets().right - dbp.getIconLabelGap() - dbp.getMaxIconLabelWidth();
		} else {
			
			formX = size.width - mFormComponent.getPreferredSize().width;
			titleX = insets.left + mIconLabelComponent.getPreferredSize().width;
			titleSpace = formX - cGap - titleX +
					mTitleComponent.getInsets().right - cGap - iconLabelWidth;
		}

		
		Point p = new Point();
		Dimension d;
		d = mIconLabelComponent.getPreferredSize();
		d.width = 10;
		mIconLabelComponent.setSize(d);
		p.y = insets.top + (lieu.height - d.height) / 2;
		p.x = iconX;
		mIconLabelComponent.setLocation(p);
				
		d = mTitleComponent.getPreferredSize();
		d.width = titleSpace;
		mTitleComponent.setSize(d);

		p.y = insets.top + (lieu.height - d.height) / 2;
		if (titleX < (iconLabelWidth + iconLabelGap)) titleX = (iconLabelWidth+iconLabelGap);
		p.x = insets.left + titleX;
		mTitleComponent.setLocation(p);

		d = mFormComponent.getPreferredSize();
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

		if (getParent() instanceof DefaultBinderPanel) {
			DefaultBinderPanel dbp = (DefaultBinderPanel) getParent();

			size.width = insets.left + dbp.getMaxTitleWidth() +
					dbp.getLabelGap() + dbp.getMaxFormWidth() + insets.right + dbp.getIconLabelGap() + dbp.getMaxIconLabelWidth();
			
		} else {
			size.width = insets.left + insets.right;
			size.width += mIconLabelComponent.getPreferredSize().width + mTitleComponent.getPreferredSize().width +
					cGap + mFormComponent.getPreferredSize().width + cGap + mIconLabelComponent.getPreferredSize().width;
		}

		size.height = Math.max(size.height,
				mTitleComponent.getPreferredSize().height);

		size.height = Math.max(size.height,
				mFormComponent.getPreferredSize().height);

		size.height += insets.top + insets.bottom;


		return size;
	}

	public void removeLayoutComponent(Component component) {
	}


	/**
	* Many Binder implementations will not need this feature.
	* We stub it here but leave it open for subclasses.
	*/
	public void cancel() {
	}


	public void clear() {
		if (mFormComponent instanceof JTextComponent) {
			((JTextComponent) mFormComponent).setText(null);
		} else {
			throw new Error("Subclasses must override this method accordingly.");
		}
	}


	public boolean isValid() {
		return true;
	}


	public void setTitle(String title) {
		if (mTitleComponent instanceof JLabel) {
			((JLabel) mTitleComponent).setText(title);
			invalidate();
			validate();
			repaint();
		}
	}
}
