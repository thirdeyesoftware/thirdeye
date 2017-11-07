////
//
// Telesync 5320 Project
//
//

package telesync.gui.rxdatapath;



import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;
import telesync.gui.icons.tree.*;



public class DataPathCellRenderer extends DefaultTreeCellRenderer {
	public DataPathCellRenderer() {
	}


	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {

		Component c = super.getTreeCellRendererComponent(tree, value,
				selected, expanded, leaf, row, hasFocus);
		
		if (value instanceof DataPathNode) {
			DataPathNode node = (DataPathNode) value;
			boolean isLeaf = node.isLeaf();
			
			if (c instanceof JLabel) {
				if (node.getSetupLock()) { // this means, we have extra work to do, autoscan is off...
					if (node.isConcatenated()) {
						if (node.isUserRateEnabled()) {
							if (isLeaf) ((JLabel)c).setIcon(GreenDotDocument.getImageIcon());
							else 
								((JLabel)c).setIcon(GreenDotFolder.getImageIcon());
							
						}
						else {
							if (isLeaf) ((JLabel)c).setIcon(EnhancedDotDocument.getGreenRedImageIcon());
							else 
								((JLabel)c).setIcon(EnhancedDotFolder.getGreenRedImageIcon());
						}
					}
					else {
						if (node.isUserRateEnabled()) {
							if (isLeaf) ((JLabel)c).setIcon(EnhancedDotDocument.getRedGreenImageIcon());
							else 
								((JLabel)c).setIcon(EnhancedDotFolder.getRedGreenImageIcon());

						}
						else {
							/* this case is rateEnabled = false, rateEnabledUser = false */
							/*if (isLeaf) ((JLabel)c).setIcon(RedDotDocument.getImageIcon());
							else 
								((JLabel)c).setIcon(RedDotFolder.getImageIcon());
							*/
						}
					}
						
					
				}
				else {
					if (node.isConcatenated()) {
						if (!node.isLeaf()) {
							((JLabel) c).setIcon(GreenDotFolder.getImageIcon());
						} else {
							((JLabel) c).setIcon(GreenDotDocument.getImageIcon());
						}

					}
				}
			}
			
		}

		return c;
	}
}
