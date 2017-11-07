 ////
//
// Telesync 5320 Project
//
//

package telesync.gui;



import java.net.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.help.*;



public class HelpDialog extends JDialog {
	GClient mGClient;
	JHelp mHelp;
	static String cHelpSetName = "TSI5320";

	public HelpDialog(GClient gClient) {
		super(gClient, "TSI5320 Help");
		mGClient = gClient;

		HelpSet helpSet = null;
		URL helpSetURL;

		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		helpSetURL = HelpSet.findHelpSet(
				getClass().getClassLoader(), cHelpSetName);
		if (helpSetURL != null) {
			try {
				helpSet = new HelpSet(getClass().getClassLoader(), helpSetURL);
			}
			catch (HelpSetException hse) {
				hse.printStackTrace();
				mGClient.showErrorDialog(hse);
				dispose();
			}
		} else {
			mGClient.showErrorDialog("HelpSet " + cHelpSetName + " not found.");
			dispose();
		}

		contentPane.add(mHelp = new JHelp(helpSet), BorderLayout.CENTER);
		mHelp.setCurrentID("about");

		setSize(700, 600);

		centerFrame();

		setVisible(true);
	}


	public JHelp getHelp() {
		return mHelp;
	}


	private void centerFrame() {
		Dimension d = this.getSize();

		Dimension od = mGClient.getSize();
		Point p = new Point((od.width - d.width)/2, (od.height - d.height)/2);
		if (p.x < 0) {
			p.x = 0;
		}
		if (p.y < 0) {
			p.y = 0;
		}
		Point ol = mGClient.getLocation();
		p.translate(ol.x, ol.y);
		setLocation(p);
	}
}
