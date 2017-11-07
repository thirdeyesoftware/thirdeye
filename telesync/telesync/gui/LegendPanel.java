   //  /////
   //  // ///
   //  /////   /* Jeffrey Blau */
// //  // ///  /* jb-01052009  */
/////  /////   /* 06/06/2001   */


package telesync.gui;


import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

import java.net.*;


public class LegendPanel extends JPanel {
	
	public LegendPanel() {
		init();	
	}
	public void init() {
		setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );
		URL url;
		JLabel alert = null;
		JLabel alarm = null;
		JLabel event = null;
		
		url = ClientView.class.getResource("icons/cards/event_small.gif");
					
		ImageIcon icon;
		icon = new ImageIcon(url);
		//this will work for all labels
		Font font = new Font("Arial",Font.PLAIN,10); //dumb compiler...
		if (icon != null) {
			event = new JLabel( "Status Changed", icon, SwingConstants.LEFT );
			font = new Font(event.getFont().getFontName(), Font.PLAIN, event.getFont().getSize() - 1);
			event.setFont( font );
		}
		
		url = ClientView.class.getResource("icons/cards/alarm_small.gif");
		
		
		icon = new ImageIcon(url);
		if (icon != null) {
			alarm = new JLabel( "Alarm", icon, SwingConstants.LEFT );
			alarm.setFont( font );
		}
		
		url = ClientView.class.getResource("icons/cards/alert_small.gif");
		icon = new ImageIcon(url);
		if (icon != null) {
			alert = new JLabel( "Configuration Problem", icon, SwingConstants.LEFT );
			alert.setFont( font );
		}
		
		event.setVisible( true );
		alert.setVisible( true );
		alarm.setVisible( true );
		
		setBorder( new TitledBorder("Legend") );
		add( event );
		add(Box.createVerticalStrut(5));
		add( alert );
		add(Box.createVerticalStrut(5));
		add( alarm );
		
	}
	
		
	
}

