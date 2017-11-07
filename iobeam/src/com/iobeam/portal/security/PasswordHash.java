package com.iobeam.portal.security;


import java.security.*;
import java.util.logging.Logger;
import sun.misc.BASE64Encoder;
import com.iobeam.boot.*;


public class PasswordHash implements BootClient {
	private static SecureRandom theSecureRandom;

	private PasswordHash() {
		throw new UnsupportedOperationException("do not instantiate");
	}


	public static String getHash(char[] password, long salt) {

		MessageDigest md = null;
		byte[] saltBytes = new byte[8];
		byte[] passwordBytes = new byte[password.length];
		String hash = null;
		int shift = 0;

		for (int i = 0; i < password.length; ++i) {
			passwordBytes[i] = (byte) password[i];
		}

		long mask = 0xff;
		for (int i = 0; i < 8; ++i, shift += 8) {
			saltBytes[i] = (byte) ((salt & mask) >> shift);
			mask <<= 8;
		}

		try {
			md = MessageDigest.getInstance("MD5");

			md.update(saltBytes);
			md.update(passwordBytes);

			byte[] digest = md.digest();

			BASE64Encoder b64 = new BASE64Encoder();
			hash = b64.encode(digest);
		}
		catch (Exception e) {
			Logger.getLogger("com.iobeam.portal.security").throwing(
					PasswordHash.class.getName(), "getHash", e);

			throw new RuntimeException(e);
		}

		return hash;
	}


	public static long getSalt() {
		return theSecureRandom.nextLong();
	}


	public static long getSiteKey() {
		return theSecureRandom.nextLong();
	}


	public static Bootable getBootable() {
		return new Bootable() {
			public void boot(BootContext context) throws BootException {
				Logger.getLogger("com.iobeam.portal.security").info(
						"PasswordHash.Bootable.boot: ");

				try {
					theSecureRandom = SecureRandom.getInstance("SHA1PRNG");
					theSecureRandom.setSeed(theSecureRandom.getSeed(8));
				}
				catch (Throwable t) {
					throw new BootException(t);
				}
			}
		};
	}
}
