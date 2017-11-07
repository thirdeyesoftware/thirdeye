/* LoggerWindow */
/* Utility GUI Window - will display output line by line */
/* Output limited to SIZE_LIMIT # of rows */

package telesync.qov.rmi;

import javax.swing.*;
import javax.swing.event.*;
import java.util.Date;
import java.text.*;
import java.awt.*;



import java.awt.event.*;

public class LoggerWindow extends JFrame {
	private JTextArea mTextArea;
	private static int SIZE_LIMIT = 20;
	private DateFormat df;
	
	
	public LoggerWindow(String title) {
		super(title);
		init();
	}
	
	private void init() {
		
		setSize(250,100);
		
		mTextArea = new JTextArea();
			
		getContentPane().setLayout(new BorderLayout());
		
		getContentPane().add(new JScrollPane(mTextArea), BorderLayout.CENTER);
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent event ) {
				clear();
				
			}
		});
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.add(btnClear, BorderLayout.WEST);
		
		getContentPane().add(btnClear, BorderLayout.SOUTH);
		setVisible(true);
		
	}
	public void add( String entry ) {
		if (mTextArea.getLineCount() >= SIZE_LIMIT) {
			clear();
		}
		mTextArea.append( new Date() + ": " + entry + "\n");
		
	}
	public void clear() {
		mTextArea.setText("");
	}
	
	public static void main( String args[] ) {
		LoggerWindow lw = new LoggerWindow("Log");
		lw.addWindowListener( new WindowAdapter() {
			public void windowClosed( WindowEvent event ) {
				System.exit(0);
			}
		});
		
	}
}

		