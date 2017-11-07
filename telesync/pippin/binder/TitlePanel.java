   //
   //
   // Joseph A. Dudar, Inc   (C) 2000
// //
////

package pippin.binder;



import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;



public class TitlePanel extends JPanel {

	JComponent mContent;
	JScrollPane mScrollPane;

	public TitlePanel(String title, JComponent content) {
		this(title, content, true, true);
	}

	public TitlePanel(String title, JComponent content,
			boolean indent, boolean scroll) {
		mContent = content;

		setLayout(new BorderLayout());

		if (indent) {
			JPanel p = new JPanel(new BorderLayout());
			if (scroll) {
				p.add(mScrollPane = new JScrollPane(mContent), BorderLayout.CENTER);
			} else {
				p.add(mContent, BorderLayout.CENTER);
			}
			p.setBorder(BorderMaker.createTitleBorder(title));
			setBorder(new EmptyBorder(5,5,5,5));
			add(p, BorderLayout.CENTER);
		} else {
			if (scroll) {
				add(mScrollPane = new JScrollPane(mContent), BorderLayout.CENTER);
			} else {
				add(mContent, BorderLayout.CENTER);
			}
			setBorder(BorderMaker.createTitleBorder(title));
		}

		if (scroll) {
			mScrollPane.getHorizontalScrollBar().setUnitIncrement(20);
			mScrollPane.getVerticalScrollBar().setUnitIncrement(20);
		}

		// mContent.setBorder(new EmptyBorder(5,5,5,5));
	}


	public JComponent getContent() {
		return mContent;
	}


	public JScrollPane getScrollPane() {
		return mScrollPane;
	}
}
