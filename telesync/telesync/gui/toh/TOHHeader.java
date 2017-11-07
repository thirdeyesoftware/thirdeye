////
//
// Telesync 5320 Project
//
//

package telesync.gui.toh;



import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import telesync.pui.tsi5320.LineSpeed;



public class TOHHeader extends JPanel implements LayoutManager {
	
	private int lineSpeed = LineSpeed.OC48;
		
	public TOHHeader(int lineSpeed) {
		setLayout(this);
		setBorder(new EmptyBorder(0,1,0,1));
		setOpaque(false);
		
		this.lineSpeed = lineSpeed;
		
		for (int i = 1; i <= (lineSpeed / 3); ++i) {
			JLabel l = new JLabel(Integer.toString(i));
			l.setOpaque(false);

			add(l);
		}
	}
	public TOHHeader() {
		this( LineSpeed.OC48 );
	}
	
	public void addLayoutComponent(String name, Component component) {
	}

	public void layoutContainer(Container container) {
		Insets insets = container.getInsets();
		Dimension size = container.getSize();
		Dimension lieu = new Dimension(
				size.width - insets.left - insets.right,
				size.height - insets.top - insets.bottom);
		Component[] components = container.getComponents();

		Point p = new Point(0, 0);
		double dx = lieu.width / (new Integer(lineSpeed / 3)).doubleValue();
		Dimension ps;

		for (int i = 0; i < components.length; ++i) {
			Component c = components[i];
			ps = c.getPreferredSize();
			c.setSize(ps);
			p.y = insets.top + (lieu.height - ps.height) / 2;
			p.x = (int) (insets.left + 3 + i * dx);
			c.setLocation(p);
		}
	}

	public Dimension minimumLayoutSize(Container container) {
		return preferredLayoutSize(container);
	}

	public Dimension preferredLayoutSize(Container container) {
		Insets insets = container.getInsets();
		Dimension size = new Dimension(0, 0);
		Component[] components = container.getComponents();

		size.width = insets.left + insets.right;

		Dimension ps;
		for (int i = 0; i < components.length; ++i) {
			ps = components[i].getPreferredSize();
			size.width += ps.width;
			size.height = Math.max(size.height, ps.height);
		}

		size.height += insets.top + insets.bottom;

		return size;
	}

	public void removeLayoutComponent(Component component) {
	}
}
