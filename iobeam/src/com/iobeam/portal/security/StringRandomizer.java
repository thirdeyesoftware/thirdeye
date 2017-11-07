package com.iobeam.portal.security;

import java.security.SecureRandom;
import com.iobeam.boot.*;

public class StringRandomizer implements BootClient {
	private static SecureRandom cRandom;
	private static final int DEFAULT_LENGTH = 8;


	private static final char[] ALPHABET = {
		'A','B','C','D','E','F','G','H','I','J',
		'K','L','M','N','O','P','Q','R','S','T',
		'U','V','W','X','Y','Z','0','1','2','3',
		'4','5','6','7','8','9'};

	/**
	 * returns a random string of the specified length
	 */
	public static String getRandomString(int length) {
		StringBuffer sb = new StringBuffer(0);
		for (int i = 0; i < length; i++) { 
			sb.append(
				ALPHABET[Math.abs(cRandom.nextInt() % ALPHABET.length)]);
		}
		return sb.toString();
	}

	/**
	 * returns a random string of the default length.
	 */
	public static String getRandomString() {

		return getRandomString(DEFAULT_LENGTH);

	}

	public static Bootable getBootable() {
		return new Bootable() {
			public void boot(BootContext context) throws BootException {
				try {
					cRandom = SecureRandom.getInstance("SHA1PRNG");
					cRandom.setSeed(cRandom.getSeed(8));
				}
				catch (Throwable t) {
					throw new BootException(t);
				}
			}
		};
	}

}



