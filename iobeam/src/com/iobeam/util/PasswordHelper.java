package com.iobeam.util;

import java.security.*;

public class PasswordHelper {
	public static final String MD5 = "MD5";
	public static final String SHA = "SHA";

	public static byte[] getHash(String inString) {
		return getHash(MD5, inString);
	}

	public static byte[] getHash(String algo, String inString) {
		MessageDigest mg;
		try {
			mg = MessageDigest.getInstance(algo);
			mg.update(inString.getBytes());
		}
		catch (Exception e) {
			return null;
		}
		return mg.digest();
	}
	public static String getHashString(String inString) {
		String retVal = "";
		byte[] in = getHash(inString);

		for (int i=0;i<in.length;i++) {
			retVal +=	byteToHex(in[i]);
		}
		return retVal;
	}

	public static String byteToHex(byte b) {
				// Returns hex String representation of byte b
				char hexDigit[] = {
					 '0', '1', '2', '3', '4', '5', '6', '7',
					 '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
				};
				char[] array = { hexDigit[(b >> 4) & 0x0f], hexDigit[b & 0x0f] };
				return new String(array);
  }

	public static void main( String args[] ) {

		System.out.println("hash = " + getHashString(args[0]));
		System.out.println("size=" + getHashString(args[0]).length());

	}

}
