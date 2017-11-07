   //
   //
   // Joseph A. Dudar, Inc   (C) 1999 - 2001
// //
////

package pippin.binder;



import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.border.*;



/**
*/
public class BinderUtilities {
	static private BlockingKeymap cTFKeymap;
	static private BlockingKeymap cPWKeymap;

	static {
		JTextField f = new JTextField();
		KeyStroke cr = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false);
		Keymap km = f.getKeymap();
		// km.removeKeyStrokeBinding(cr);

		cTFKeymap = new BlockingKeymap("no-cr JTextField", km);
		cTFKeymap.blockKeyStrokeBinding(cr);

		JPasswordField pf = new JPasswordField();
		km = pf.getKeymap();
		// km.removeKeyStrokeBinding(cr);

		cPWKeymap = new BlockingKeymap("no-cr JPasswordField", km);
		cPWKeymap.blockKeyStrokeBinding(cr);
	}


	static public Keymap getTextFieldKeymap() {
		return cTFKeymap;
	}

	static public Keymap getPasswordFieldKeymap() {
		return cPWKeymap;
	}


	static public JTextField createNoCRTextField() {
		JTextField tf = new JTextField();
		tf.setKeymap(getTextFieldKeymap());

		return tf;
	}


	public static void initProperties() {
		System.out.println("initProperties()");
		Color c;
		
		if (System.getProperty("java.version").startsWith("1.4")) {
			c = new Color(102,102,153);
		}
		else {
			c = new JLabel().getForeground();
		}
		
			
		try {
			
			UIManager.put("pippin.colors.labelFG", c);
			UIManager.put("pippin.colors.titlePanelBG", c.brighter().brighter());
			UIManager.put("pippin.colors.lpsPurple2", c.brighter().brighter());
			UIManager.put("pippin.colors.lpsPurple1", c.brighter());
			UIManager.put("pippin.colors.lpsPurple0", c);
			UIManager.put("pippin.colors.selectionYellow",
					new Color(255, 255, 204));
			UIManager.put("pippin.colors.selectionBlue",
					new Color(132, 151, 218));
			UIManager.put("pippin.colors.indicatorBlue",
					UIManager.getColor("pippin.colors.selectionBlue").brighter());
			UIManager.put("pippin.colors.alertRed",
					new Color(255, 204, 204));
			UIManager.put("pippin.colors.goGreen",
					new Color(204, 255, 204));
			UIManager.put("pippin.fonts.smaller",
					new Font("SansSerif", Font.PLAIN, 10));
			UIManager.put("pippin.fonts.plain",
					new Font("SansSerif", Font.PLAIN, 12));
			UIManager.put("pippin.fonts.notice",
					new Font("SansSerif", Font.PLAIN, 16));
			UIManager.put("pippin.fonts.herald",
					new Font("SansSerif", Font.BOLD + Font.ITALIC, 18));
			UIManager.put("pippin.fonts.fixed",
					new Font("Monospaced", Font.PLAIN, 11));
			UIManager.put("pippin.fonts.fixed12",
					new Font("Monospaced", Font.PLAIN, 12));
			UIManager.put("pippin.fonts.small",
					new Font("SansSerif", Font.PLAIN, 10));
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
