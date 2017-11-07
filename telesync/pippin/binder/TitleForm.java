   //
   //
   // Joseph A. Dudar, Inc   (C) 2000
// //
////

package pippin.binder;



import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;



public class TitleForm extends BasicForm {

	BasicForm mMainForm;
	JScrollPane mScrollPane;
	Container mMainContainer;
	BorderMaker.TitleBorder mTitleBorder;
	String mTitle;
	JPanel mTitlePanel,mInnerPanel;
	JLabel mTitleLabel;
	int mLabelComponents = 0;
	
	public TitleForm(String title, TypeSet typeSet) {
		this(title, typeSet, true);
	}


	public TitleForm(String title, TypeSet typeSet, boolean indent) {
		super(typeSet, false);
		mTitle = title;
		
		mMainForm = new BasicForm(typeSet);
		mMainForm.setOpaque(true);
		mMainForm.setBackground(UIManager.getColor("TextArea.background"));

		setLayout(new BorderLayout());
		mTitlePanel = new JPanel();
		mTitlePanel.setAlignmentX(0);

		mTitleLabel = new JLabel(title);
		
		mTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		mTitlePanel.setLayout(new BoxLayout(mTitlePanel,BoxLayout.X_AXIS));
		mTitlePanel.setBorder(new EmptyBorder(2,2,2,2));
		mTitlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		mTitlePanel.add(mTitleLabel);
		mTitlePanel.add(Box.createHorizontalGlue());


		mInnerPanel = new JPanel();
		mInnerPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		mInnerPanel.setLayout( new FlowLayout(FlowLayout.RIGHT, 1,1) );
		mTitlePanel.add(Box.createHorizontalGlue());
		mTitlePanel.add(mInnerPanel);
		mLabelComponents = 0;
		
		if (indent) {
			JPanel p = new JPanel(new BorderLayout());
			p.add(mTitlePanel,BorderLayout.NORTH);	
			p.add(mScrollPane = new JScrollPane(mMainForm), BorderLayout.CENTER);
			//p.setBorder(mTitleBorder = (BorderMaker.TitleBorder)
			//		BorderMaker.createTitleBorder(title));
			setBorder(new EmptyBorder(5,5,5,5));
			add(p, BorderLayout.CENTER);
			mMainContainer = p;
		} else {
			mScrollPane = new JScrollPane(mMainForm);
			add(mTitlePanel,BorderLayout.NORTH);
			add(mScrollPane, BorderLayout.CENTER);
			//setBorder(mTitleBorder = (BorderMaker.TitleBorder)
			//		BorderMaker.createTitleBorder(title, false));
			setBorder(new EmptyBorder(3,3,3,3));
			mMainContainer = this;
		}

		mScrollPane.getHorizontalScrollBar().setUnitIncrement(20);
		mScrollPane.getVerticalScrollBar().setUnitIncrement(20);

		// This guy doesn't have any Binders yet, or else
		// we'd have to do this.
		// addBinderSet((BinderSet) mMainForm);

		mMainForm.setBorder(new EmptyBorder(5,5,5,5));
	}


	public BasicForm getMainForm() {
		return mMainForm;
	}


	public JScrollPane getScrollPane() {
		return mScrollPane;
	}


	public void setColumnHeader(JComponent c) {
		mScrollPane.setColumnHeaderView(c);
	}


	protected Container getMainContainer() {
		return mMainContainer;
	}
	
	public void addField(String name, DefaultBinder field, boolean requestsEvents) 
		throws BinderException {
		addField(name, field, requestsEvents, true);
	}
	

	public void addField(String name, DefaultBinder field, boolean requestsEvents, 
		boolean isVisible) throws BinderException {
			
			mMainForm.addField(name, field, requestsEvents, isVisible);
		
			if (!hasBinder(name)) {
				addBinder(name, (Binder) field, isVisible);
			}
	}
		
	public void addField(String name, DefaultBinder field)
			throws BinderException {
		addField(name, field, true, true);
	}
		
	protected void removeField(String name)
			throws BinderException {

		mMainForm.removeField(name);

		if (hasBinder(name)) {
			removeBinder(name);
		}
	}


	public void setTitle(String title) {
		//mTitleBorder.setTitle(title);
		mTitleLabel.setText(title);
		invalidate();
		validate();
		repaint();
	}
	
	public String getTitle() {
		return mTitleLabel.getText();
	}
	
	public void addTitlePanel(JPanel c) {
		if (mLabelComponents == 0) {
			mTitlePanel.add(Box.createHorizontalGlue());
		}
		mInnerPanel.add(Box.createHorizontalStrut(2));
		mInnerPanel.add(c);
		mLabelComponents ++;
		
		
	}
	
	
}
