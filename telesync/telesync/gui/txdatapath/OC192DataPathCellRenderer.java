////
//
// Telesync 5320 Project
//
//

package telesync.gui.txdatapath;



import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;
import telesync.gui.icons.tree.*;



public class OC192DataPathCellRenderer extends DefaultTreeCellRenderer {
	public OC192DataPathCellRenderer() {
	}


	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {

		Component c = super.getTreeCellRendererComponent(tree, value,
				selected, expanded, leaf, row, hasFocus);
		
		if (value instanceof OC192DataPathNode) {
			OC192DataPathNode node = (OC192DataPathNode) value;
			
			if (node.isConcatenated()) {
				if (c instanceof JLabel) {
					if (!node.isLeaf()) {
						((JLabel) c).setIcon(GreenDotFolder.getImageIcon());
					} else {
						((JLabel) c).setIcon(GreenDotDocument.getImageIcon());
					}
				}
			}
		}

		return c;
	}
}
