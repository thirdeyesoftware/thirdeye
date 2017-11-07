package com.iobeam.portal.util;

/**
 * ReadableNumberGenerator creates a readable number based on a long
 * key.  
 */
public class ReadableNumberGenerator {

	private static final int DEFAULT_LENGTH = 5;

	public static String getReadableNumber(long key) {
		return getReadableNumber(key, DEFAULT_LENGTH);
	}


	public static String getReadableNumber(long key, int length) {
		
		String sKey = Long.toString(key);

		int n = length - sKey.length();
		if (n > 0) {
			StringBuffer sb = new StringBuffer();

			for (int i = 0; i < n; ++i) {
				sb.append("0");
			}
			sb.append(sKey);

			sKey = sb.toString();
		}

		return sKey;
	}
}


