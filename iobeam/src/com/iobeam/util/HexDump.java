package com.iobeam.util;


import java.io.*;


public class HexDump {
	private static String cPunctuation = "?!`~@#$%^&*()-_+={}[]\\|:;\"'<>,./";

	public static void dump(InputStream is) throws IOException {
		int n;
		byte[] b = new byte[4096];

		while ((n = is.read(b)) >= 0) {
			if (n > 0) {
				dump(b, 0, n);
			}
		}
	}


	public static String toString(short srt) {
		StringBuffer sb = new StringBuffer();

		String s = Integer.toHexString(((int) srt) & 0xffff);
		sb.append(s);
		for (int i = s.length(); i < 4; ++i) {
			sb.insert(0, '0');
		}

		return sb.toString();
	}


	public static String toString(byte b) {
		StringBuffer sb = new StringBuffer();

		String s = Integer.toHexString(((int) b) & 0xff);
		sb.append(s);
		if (s.length() == 1) {
			sb.insert(0, '0');
		}

		return sb.toString();
	}


	public static String toString(byte[] b, int offset, int length) {
		StringBuffer sb = new StringBuffer();

		for (int i = offset, j = 0; i < length; ++i) {
			String s = Integer.toHexString(((int) b[i]) & 0xff);
			sb.append(s);
			if (s.length() == 1) {
				sb.insert(j, '0');
			}
			j += 2;
		}

		return sb.toString();
	}


	public static String toDecodedString(byte[] b, int offset, int length) {
		StringBuffer sb = new StringBuffer();

		for (int i = offset; i < length; ++i) {

			char c = (char) (((int) b[i]) & 0xff);
			if (Character.isLetterOrDigit(c) || Character.isSpaceChar(c) ||
					isPunctuation(c)) {
				sb.append(c);
			} else {
				sb.append('.');
			}
		}

		return sb.toString();
	}


	public static String toString(byte[] b) {
		return toString(b, 0, b.length);
	}


	public static String toDecodedString(byte[] b) {
		return toDecodedString(b, 0, b.length);
	}


	public static void dump(byte[] b) {
		dump(b, 0, b.length, System.out);
	}


	public static void dump(byte[] b, int offset, int length) {
		dump(b, offset, length, System.out);
	}


	public static void dump(byte[] b, PrintStream out) {
		dump(b, 0, b.length, out);
	}


	public static void dump(byte[] b, int offset, int length, PrintStream out) {
		StringBuffer hexLine = new StringBuffer();
		StringBuffer charLine = new StringBuffer();
		StringBuffer sb = new StringBuffer();
		char c;
		int i;

		for (i = 0; i < offset % 16; ++i) {
			hexLine.append("   ");
			charLine.append(" ");
		}

		for (i = offset; i < length; ++i) {
			sb.setLength(0);
			sb.append(Integer.toHexString(((int) b[i]) & 0xff));
			if (sb.length() == 1) {
				sb.insert(0, '0');
			}
			hexLine.append(sb);
			hexLine.append(" ");

			c = (char) (((int) b[i]) & 0xff);
			if (Character.isLetterOrDigit(c) || Character.isSpaceChar(c) ||
					isPunctuation(c)) {
				charLine.append(c);
			} else {
				charLine.append('.');
			}
			if ((i + 1) % 8 == 0) {
				if ((i + 1) % 16 == 0) {
					out.print(hexLine.toString());
					hexLine.setLength(0);
					out.print("   ");
					out.println(charLine.toString());
					charLine.setLength(0);
				} else {
					hexLine.append(" ");
				}
			}
		}

		if (hexLine.length() > 0) {
			for (i = i % 16; i < 16; ++i) {
				hexLine.append("   ");
				if (i == 7) {
					hexLine.append(" ");
				}
			}
			out.print(hexLine.toString());
			out.print("   ");
			out.println(charLine.toString());
		}
	}


	static private boolean isPunctuation(char c) {
		return cPunctuation.indexOf(c) >= 0;
	}


	public static void main(String[] args) throws Exception {
		HexDump.dump(new FileInputStream(args[0]));
	}
}
