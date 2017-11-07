////
// //
// PippinSoft
//
//

package pippin.util;



import java.awt.*;
import java.io.*;



/**
*/
public class ImageMaker {

	public static void createImageClass(String imageFile, String imageClass)
			throws IOException {
		createImageClass(imageFile, imageClass, null);
	}


	public static void createImageClass(String imageFile, String imageClass,
			String packageName) throws IOException {
		
		int n;
		byte[] bytes = new byte[1024];
		StringBuffer sb = new StringBuffer();;

		FileInputStream inputStream = new FileInputStream(imageFile);
		PrintWriter printWriter = new PrintWriter(
				new FileWriter(imageClass + ".java"));

		printWriter.println("////");
		printWriter.println("//");
		printWriter.println("//");
		printWriter.println("//");
		printWriter.println("//");
		printWriter.println("\npackage " + packageName + ";");
		printWriter.println("\n\n\nimport java.awt.*;");
		printWriter.println("import javax.swing.*;");
		printWriter.println("\n\n\npublic class " + imageClass + " {");

		printWriter.println("\n\tstatic byte[] sImageArray = new byte[] {");

		while ((n = inputStream.read(bytes)) > -1) {
			for (int i = 0; i < n; i += 4) {
				sb.setLength(0);
				for (int j = i; j < i + 4 && j < n; ++j) {
					sb.append("(byte)" + toHexString(bytes[j]) + ", ");
				}
				printWriter.println("\t\t" + sb.toString());
			}
		}

		printWriter.println("\t};");

		printWriter.println("\n\n\tstatic ImageIcon sImageIcon =" +
				" new ImageIcon(sImageArray);");

		printWriter.println("\n\n\tpublic static " +
				"Image getImage(Toolkit toolkit) {");
		printWriter.println("\t\treturn toolkit.createImage(sImageArray);");
		printWriter.println("\t}");

		printWriter.println("\n\n\tpublic static ImageIcon getImageIcon() {");
		printWriter.println("\t\treturn sImageIcon;");
		printWriter.println("\t}");
		printWriter.println("}");

		printWriter.close();
		inputStream.close();
	}


	static private String toHexString(byte b) {
		StringBuffer sb = new StringBuffer();

		sb.append(Integer.toHexString((int) b));
		while (sb.length() < 2) {
			sb.insert(0, '0');
		}

		String hexString = sb.toString();

		if (hexString.length() > 2) {
			hexString = hexString.substring(hexString.length() - 2);
		}

		return "0x" + hexString;
	}


	public static void main(String[] args) {
		if (args.length < 2 || args.length > 3) {
			System.out.println("usage: ImageMaker <imagefile> <classname> " +
					"[package]");
		} else {
			try {
				if (args.length == 2) {
					createImageClass(args[0], args[1]);
				} else {
					createImageClass(args[0], args[1], args[2]);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
