////
//
//
//
//

package telesync.gui.icons.tree;



import java.awt.*;
import javax.swing.*;



public class RedDotFolder {

	static byte[] sImageArray = new byte[] {
		(byte)0x47, (byte)0x49, (byte)0x46, (byte)0x38, 
		(byte)0x39, (byte)0x61, (byte)0x10, (byte)0x00, 
		(byte)0x12, (byte)0x00, (byte)0xb3, (byte)0x00, 
		(byte)0x00, (byte)0x5f, (byte)0x5f, (byte)0x90, 
		(byte)0x60, (byte)0x60, (byte)0x90, (byte)0x90, 
		(byte)0x90, (byte)0xcf, (byte)0xc0, (byte)0xc0, 
		(byte)0xff, (byte)0x90, (byte)0x90, (byte)0xc0, 
		(byte)0xcf, (byte)0xcf, (byte)0xff, (byte)0xe3, 
		(byte)0x43, (byte)0x21, (byte)0xff, (byte)0xff, 
		(byte)0xff, (byte)0xc0, (byte)0xc0, (byte)0xc0, 
		(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, 
		(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, 
		(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, 
		(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, 
		(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, 
		(byte)0x00, (byte)0x21, (byte)0xf9, (byte)0x04, 
		(byte)0x01, (byte)0x00, (byte)0x00, (byte)0x08, 
		(byte)0x00, (byte)0x2c, (byte)0x00, (byte)0x00, 
		(byte)0x00, (byte)0x00, (byte)0x10, (byte)0x00, 
		(byte)0x12, (byte)0x00, (byte)0x00, (byte)0x04, 
		(byte)0x46, (byte)0x10, (byte)0xc9, (byte)0x49, 
		(byte)0xab, (byte)0xbd, (byte)0xd8, (byte)0x86, 
		(byte)0xcd, (byte)0x33, (byte)0x08, (byte)0x5f, 
		(byte)0x88, (byte)0x24, (byte)0x64, (byte)0x49, 
		(byte)0x0a, (byte)0x04, (byte)0x8a, (byte)0x92, 
		(byte)0x47, (byte)0xeb, (byte)0xb6, (byte)0xa6, 
		(byte)0x79, (byte)0x0c, (byte)0x05, (byte)0x6d, 
		(byte)0xbf, (byte)0x70, (byte)0x72, (byte)0xd4, 
		(byte)0xbc, (byte)0xdd, (byte)0x17, (byte)0xac, 
		(byte)0x81, (byte)0x61, (byte)0x48, (byte)0x24, 
		(byte)0xda, (byte)0x58, (byte)0x85, (byte)0xa2, 
		(byte)0x12, (byte)0x40, (byte)0x0b, (byte)0x2a, 
		(byte)0x8b, (byte)0x80, (byte)0x1a, (byte)0x72, 
		(byte)0x00, (byte)0xa8, (byte)0x5a, (byte)0xad, 
		(byte)0x4d, (byte)0x9d, (byte)0x6f, (byte)0xdb, 
		(byte)0x9b, (byte)0xfe, (byte)0xb8, (byte)0xb1, 
		(byte)0x70, (byte)0x38, (byte)0x43, (byte)0x2e, 
		(byte)0x9b, (byte)0x2d, (byte)0x11, (byte)0x00, 
		(byte)0x3b, 
	};


	static ImageIcon sImageIcon = new ImageIcon(sImageArray);


	public static Image getImage(Toolkit toolkit) {
		return toolkit.createImage(sImageArray);
	}


	public static ImageIcon getImageIcon() {
		return sImageIcon;
	}
}
