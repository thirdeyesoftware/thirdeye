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



/**
*/
public class DefaultBinderPanel extends BinderPanel {

	static final int LABEL_GAP = 13;
	static final int ICON_LABEL_GAP = 7;
	
	/**
	* The width of the widest titleComponent.
	*/
	private int mMaxTitleWidth = 0;

	/**
	* The width of the widest formComponent.
	*/
	private int mMaxFormWidth = 0;


	private int mMaxIconLabelWidth = 0;
	
	private boolean mUseSeparators = true;


	public DefaultBinderPanel(TypeSet typeSet) {
		super(typeSet);
	}

	public void addBinder(String name, Binder b, boolean isVisible) {
		mBinderSetSupport.addBinder(name, b);
		
		if (isVisible) {
			invalidateBinders();
			recalcWidths();
		}
	}
	
	public void addBinder(String name, Binder binder) {
		addBinder(name,binder,true);	
	}

	/**
	* Hook providing an opportunity for subclasses to
	* create automatic Binder instances for addBinder(String).
	*/
	protected Binder createBinderImpl(TypeSetElement element) {
		Binder b = super.createBinderImpl(element);

		if (b == null) {
			if (element.getType().equals(String.class)) {
				b = new DefaultBinder(element.getLabel());
			}
		}

		return b;
	}


	public void replaceBinder(String name, Binder binder) {
		mBinderSetSupport.replaceBinder(name, binder);

		invalidateBinders();
		recalcWidths();
	}


	public void removeBinder(String name) {
		mBinderSetSupport.removeBinder(name);

		invalidateBinders();
		recalcWidths();
	}


	private void recalcWidths() {
		mMaxTitleWidth = calcMaxTitleWidth();
		mMaxFormWidth = calcMaxFormWidth();
		mMaxIconLabelWidth = calcMaxIconLabelWidth();
	}


	public int getMaxTitleWidth() {
		return mMaxTitleWidth;
	}
	
	public int getMaxIconLabelWidth() {
		return mMaxIconLabelWidth;
	}
	

	public int getMaxFormWidth() {
		return mMaxFormWidth;
	}

	protected int calcMaxIconLabelWidth() {
		int max = 0;
		for (Enumeration en = getBinders(); en.hasMoreElements(); ) {
			Binder b = (Binder) en.nextElement();

			if (b instanceof DefaultBinder) {
				DefaultBinder db = (DefaultBinder) b;

				max = Math.max(max,
						db.getIconLabelComponent().getPreferredSize().width);
			}
		}
		return max;
	}
	
	protected int calcMaxTitleWidth() {
		int max = 0;
		
		for (Enumeration en = getBinders(); en.hasMoreElements(); ) {
			Binder b = (Binder) en.nextElement();

			if (b instanceof DefaultBinder) {
				DefaultBinder db = (DefaultBinder) b;

				// The insets.left thing corresponds to DefaultBinder's
				// layout characteristic in which the labels
				// line up despite their borders.  This facilitates
				// using borders as annunciators.
				max = Math.max(max,
						db.getTitleComponent().getPreferredSize().width -
						db.getTitleComponent().getInsets().left);
			}
		}

		return max;
	}


	protected int calcMaxFormWidth() {
		int max = 0;
					
			for (Enumeration en = getBinders(); en.hasMoreElements(); ) {
				Binder b = (Binder) en.nextElement();
				
				
				if (b instanceof DefaultBinder) {
					DefaultBinder db = (DefaultBinder) b;
					
					max = Math.max(max,
							db.getFormComponent().getPreferredSize().width);
				}
			}
		
		
		return max;
	}


	public int getLabelGap() {
		return LABEL_GAP;
	}
	
	public int getIconLabelGap() {
		return ICON_LABEL_GAP;
	}
	

	public JComponent createHorizontalSeparator(int height) {

		JComponent hs;
		
		hs = new JComponent() {
			public void paint(Graphics g) {
				super.paint(g);

				if (mUseSeparators) {
					Color c = g.getColor();

					g.setColor(DefaultBinderPanel.this.getBackground().darker());
					Insets insets = getInsets();
					Dimension d = getSize();
					g.drawLine(insets.left, d.height/2, d.width - insets.right,
							d.height/2);

					g.setColor(c);
				}
			}
		};

		hs.setMaximumSize(new Dimension(65535, height));
		hs.setPreferredSize(new Dimension(
				hs.getPreferredSize().width, height));
		hs.setMinimumSize(new Dimension(
				hs.getMinimumSize().width, height));

		return hs;
	}


	public void invalidateBinders() {
		for (Enumeration en = getBinders(); en.hasMoreElements(); ) {
			Binder b = (Binder) en.nextElement();

			if (b instanceof DefaultBinder) {
				DefaultBinder db = (DefaultBinder) b;

				db.invalidate();
			}
		}
	}


	public void setUseSeparators(boolean useSeparators) {
		mUseSeparators = useSeparators;
	}
	
	
}
