   //
   //
   // Joseph A. Dudar, Inc   (C) 2000
// //
////

package pippin.binder;



import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;



public class BorderMaker {

	static Border createTitleBorder(String title) {
		return createTitleBorder(title, true);
	}


	static Border createTitleBorder(String title, boolean indent) {
		if (indent) {
			return new IndentTitleBorder(title);
			/*
			return new CompoundBorder(
					new TitledBorder(title),
					new CompoundBorder(
					new EmptyBorder(4,4,4,4),
					new BevelBorder(BevelBorder.LOWERED)));
			*/
		} else {
			// return new TitledBorder(new EmptyBorder(4,0,0,0), title);
			return new BasicTitleBorder(title);
		}
	}


	public static interface TitleBorder extends Border {
		public void setTitle(String title);
	}


	private static class IndentTitleBorder implements Border, TitleBorder {
		TitledBorder mTitledBorder;
		CompoundBorder mCompoundBorder;

		private IndentTitleBorder(String title) {
			mCompoundBorder = new CompoundBorder(
					mTitledBorder = new TitledBorder(title),
					new CompoundBorder(
					new EmptyBorder(4,4,4,4),
					new BevelBorder(BevelBorder.LOWERED)));
		}

		public Insets getBorderInsets(Component c) {
			return mCompoundBorder.getBorderInsets(c);
		}

		public boolean isBorderOpaque() {
			return mCompoundBorder.isBorderOpaque();
		}

		public void paintBorder(Component c, Graphics g, int x, int y,
				int w, int h) {
			mCompoundBorder.paintBorder(c,g,x,y,w,h);
		}

		public void setTitle(String title) {
			mTitledBorder.setTitle(title);
		}
	}

	private static class BasicTitleBorder extends TitledBorder
			implements TitleBorder {

		private BasicTitleBorder(String title) {
			super(new EmptyBorder(4,0,0,0), title);
		}
	}
}
