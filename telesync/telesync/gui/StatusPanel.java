   //  /////
   //  // ///
   //  /////   /* Jeffrey Blau */
// //  // ///  /*              */
/////  /////   /* 06/12/2002   */


package telesync.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

import java.net.*;
import java.text.DateFormat;
import java.util.Date;


public class StatusPanel extends JPanel  {
	
	private JLabel mStatusLabel;
	private ClockPanel mClockPanel;
	private static final ImageIcon ICON_HOURGLASS = new ImageIcon(StatusPanel.class.getResource("icons/hourglass.gif"));
	
	public StatusPanel() {
		super();
		init();	
	}
	public void init() {
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(Box.createHorizontalGlue());
			
		mStatusLabel = new JLabel("",new ImageIcon( StatusPanel.class.getResource("icons/hourglass.gif")), 
															SwingConstants.RIGHT);
		
		mStatusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		mStatusLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
				
		add(mStatusLabel);
		add(Box.createRigidArea(new Dimension(20,10)));
		
		mClockPanel = new ClockPanel();
		
		add(mClockPanel);
		
						
		
	}
	public void showClock(boolean b) {
		mClockPanel.setVisible(b);
	}
	
	public void showStatus(boolean b) {
		mStatusLabel.setVisible(b);
	}
	
	public void setStatus(String s) {
		if (mStatusLabel.isVisible()) {
			if (s == null) {
				mStatusLabel.setVisible(false);
			}
			else {
				mStatusLabel.setIcon(ICON_HOURGLASS);
				mStatusLabel.setText(s);
				mStatusLabel.setVisible(true);
				System.out.println("status msg = " + mStatusLabel.getText());
			}
		}
	}
	
	
	
}
	
