   //
   //
   // Joseph A. Dudar, Inc   (C) 2000
// //
////

package pippin.binder;



import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;



public class ButtonMaker {

	public static JButton createPButton(String name) {
		return createPButton(name, false);
	}


	public static JButton createPButton(String name, boolean narrow) {
		JButton b = new JButton(name);
		b.setFont(UIManager.getFont("pippin.fonts.plain"));
		b.setOpaque(false);

		if (narrow) {
			Insets insets = (Insets) b.getMargin().clone();
			insets.left = 7;
			insets.right = 7;
			b.setMargin(insets);
		}

		return b;
	}
	public static JButton createSmallPButton(String name, boolean narrow) {
			JButton b = new JButton(name);
			b.setFont(UIManager.getFont("pippin.fonts.plain"));
			b.setOpaque(false);
	
			if (narrow) {
				Insets insets = (Insets) b.getMargin().clone();
				insets.left = 2;
				insets.right = 2;
				b.setMargin(insets);
			}
	
			return b;
	}

	public static JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel() {
			public Dimension getMaximumSize() {
				return new Dimension(super.getMaximumSize().width,
						super.getPreferredSize().height);
			}
		};
		buttonPanel.setBackground(UIManager.getColor(
				"pippin.colors.titlePanelBG"));
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.setBorder(new CompoundBorder(
				new EtchedBorder(),
				new EmptyBorder(5,5,5,5)));

		return buttonPanel;
	}
	public static JButton makeSmaller(JButton button) {
		Insets insets = (Insets)button.getMargin().clone();
		insets.left = 2;
		insets.right = 2;
		button.setMargin(insets);
		return button;
	}
	
}
