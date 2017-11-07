   //  /////
   //  // ///
   //  /////   /* Jeffrey Blau */
// //  // ///  /* jb-01071101  */
/////  /////   /* 07/11/2001   */


package telesync.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

import java.net.*;
import java.text.DateFormat;
import java.util.Date;


public class ClockPanel extends JPanel  {
	private ClockThread clockThread;
	private JLabel timeLabel;
	
	public ClockPanel() {
		init();	
	}
	public void init() {
		
		setLayout(new BorderLayout());
		
		JPanel centerPanel = new JPanel();
			
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS ) );
			
		timeLabel = new JLabel();
		
		setBorder( new CompoundBorder(BorderFactory.createLoweredBevelBorder(), new EmptyBorder(1,2,1,2)));
		
		timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		timeLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
			
		centerPanel.add( timeLabel );
		
		centerPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
		
		add(centerPanel,BorderLayout.CENTER);
				
		if (clockThread==null) { 
			clockThread = new ClockThread();
			clockThread.start();
		}
	}
	
	public void setVisible(boolean visible) {
		
		if (visible) {
			if (clockThread == null) {
				clockThread = new ClockThread();
				clockThread.start();
				
			}
		} else {
			if (clockThread != null) {
				clockThread.interrupt();
				clockThread = null;
			}
		
		}
		super.setVisible(visible);
		
	}
	/* clock "ticker"  */
	class ClockThread extends Thread {
		public ClockThread() {
			super();
			setPriority(Thread.MIN_PRIORITY);
		}
		public void run() {
			Date dt;
			DateFormat dfTime = DateFormat.getTimeInstance(DateFormat.MEDIUM);
			while (true) {
				dt = new Date();
				timeLabel.setText(dfTime.format(dt));
				try {
					Thread.sleep(500);
				}
				catch (InterruptedException e) {
				}
			}
		}
	}

}
	
