package telesync.gui;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

public class BorderPanel extends JPanel {
		JLabel mTitleLabel;
		
		public BorderPanel(String title) {
			super();
			init(title);
		}
		public BorderPanel() {
			this("");
		}
			
		private void init(String title) {
			setLayout( new BorderLayout() );
			mTitleLabel = new JLabel(title);
			mTitleLabel.setFont(UIManager.getFont("pippin.fonts.smaller"));	
			JPanel panelTitle= new JPanel();
			panelTitle.setLayout(new BorderLayout());
			panelTitle.add(mTitleLabel, BorderLayout.NORTH);
			panelTitle.setBorder( new EtchedBorder());
			add(panelTitle, BorderLayout.NORTH);
			
		}
		
		public void setTitle(final String title) {
			if (SwingUtilities.isEventDispatchThread()) {
					mTitleLabel.setText(title);	
			} else {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						setTitle(title);
					}
				});
			}
			
		}
		public String getTitle() {
			return mTitleLabel.getText();
		}
		
	}