package com.iobeam.portal.security;


import java.math.*;
import java.security.*;
import java.util.logging.Logger;
import com.iobeam.boot.*;


public class SecureIDFactory implements BootClient {
	/**
	* Number of bytes in a SecureID random.
	*/
	public static final int ID_LENGTH = 8;
	public static final int KEY_LENGTH = 15;

	private static SecureRandom theSecureRandom;

	private static final String theAlphabet =
			// 20 gives key length 15 on 8 bytes.
			"3479ACEFHJKLMNPQRTWX";

			// 23 gives key length 16 on 9 bytes.
			// "13479ACEFHJKLMNPQRTVWXY";


			// 24 gives key length 14 on 8 bytes.
			// Still, the char-digit mix is confusing.
			// "234679ACEFHJKLMNPQRTVWXY";

			// 24 gives key length 14 on 8 bytes.
			// Still, the char-digit mix is confusing.
			// "234679ACEFHJKLMNPQRTVWXY";

			// 31 gives key length 13 on 8 bytes
			// However, there are confusing similarities among
			// letters and numbers.
			// "123456789ABCDEFHJKLMNPQRTUVWXYZ";

			// 23 with key length 14 on 8 bytes
			// should blow up in test at about 14%.
			// It does.
			// "34679ACEFHJKLMNPQRTVWXY";

	private static final BigInteger theRadix = new BigInteger(
			Integer.toString(theAlphabet.length()));

	private SecureIDFactory() {
		throw new UnsupportedOperationException("do not instantiate");
	}


	public static SecureID createSecureID() {
		byte[] id = new byte[ID_LENGTH];

		theSecureRandom.nextBytes(id);

		BigInteger bid = new BigInteger(1, id);

		return new SecureID(bid);
	}


	private static byte[] getPaddedID(byte[] unpaddedID) {
		byte[] paddedID = new byte[ID_LENGTH + 1];

		paddedID[0] = 0;

		for (int i = 0; i < ID_LENGTH; ++i) {
			paddedID[i + 1] = unpaddedID[i];
		}

		return paddedID;
	}


	public static SecureID createSecureID(String key) {
		BigInteger id = getID(key, ID_LENGTH);

		return new SecureID(id);
	}

	/**
	* Creates a SecureID from unsigned id bits, as might be
	* stored in a database.
	*
	* @param id unsigned bits of secure id.
	*/
	public static SecureID createSecureID(byte[] id) {
		if (id.length != ID_LENGTH) {
			throw new IllegalArgumentException("id.length = " + id.length +
					" but ID_LENTH = " + ID_LENGTH);
		}

		BigInteger bid = new BigInteger(1, id);

		return new SecureID(bid);
	}


	public static SecureID createSecureID(BigInteger bid) {
		byte[] id = bid.toByteArray();

		if (id.length != ID_LENGTH) {
			if (id.length != ID_LENGTH + 1) {
				throw new IllegalArgumentException("id.length = " + id.length +
						" but ID_LENTH = " + ID_LENGTH);
			}

			if (id[0] != (byte) 0) {
				throw new IllegalArgumentException("id.length = " + id.length +
						" but ID_LENTH = " + ID_LENGTH +
						" and id[0] = " + Integer.toHexString(id[0] & 0xff));
			}
		}

		return new SecureID(bid);
	}


	static String getKey(BigInteger id, int length) {
		StringBuffer sb = new StringBuffer();

		BigInteger[] q;

		boolean done = false;

		for (int i = 0; i < length; ++i) {
			q = id.divideAndRemainder(theRadix);
			sb.insert(0, theAlphabet.charAt(q[1].intValue()));
			id = q[0];
		}

		return sb.toString();
	}


	static private BigInteger getID(String key, int idLength) {
		key = key.toUpperCase();
		key = key.replaceAll(" ", "");

		BigInteger bid = BigInteger.ZERO;
		BigInteger rad = BigInteger.ONE;

		for (int i = key.length() - 1; i >= 0; --i) {
			char c = key.charAt(i);
			int d = theAlphabet.indexOf(c);

			if (d < 0) {
				throw new IllegalArgumentException("illegal char " + c +
						" in " + key + " radix " + theRadix);
			}

			bid = bid.add((new BigInteger(Integer.toString(d))).multiply(rad));

			rad = rad.multiply(theRadix);
		}

		return bid;
	}


	public static Bootable getBootable() {
		return new Bootable() {
			public void boot(BootContext context) throws BootException {
				Logger.getLogger("com.iobeam.portal.security").info(
						"SecureID.Bootable.boot: ");

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


	public static void main(String[] args) throws Exception {
		getBootable().boot((BootContext) null);

		SecureID sid;

		if (args.length == 0) {
			sid = createSecureID();

			System.out.println(sid.getID());
			System.out.println(sid.getHexID());
			System.out.println(sid.toString());
		} else
		if (args[0].equals("-key")) {
			BigInteger bigint = new BigInteger(args[1]);
			System.out.println(createSecureID(bigint).getKey());
		} else 
		if (args[0].equals("-ex")) {
			int max = Integer.parseInt(args[1]);

			java.util.Hashtable hash = new java.util.Hashtable();

			for (int i = 0; i < max; ++i) {
				sid = createSecureID();

				if (hash.containsKey(sid)) {
					System.out.println("collision after " + i + " generations");
					System.out.println(sid.getID());
					System.out.println(sid.getHexID());
					System.out.println(sid.toString());

					break;
				}
				hash.put(sid, sid);
				if (i % 1000 == 0) {
					System.out.println(Integer.toString(i) + " generations");
				}

				SecureID rid = createSecureID(sid.toString());
				if (!hash.containsKey(rid)) {
					System.out.println("miss after " + i + " generations");
					System.out.println(sid.getID());
					System.out.println(sid.getHexID());
					System.out.println(sid.toString());

					break;
				}
			}
		}
		else {
			sid = createSecureID(args[0]);

			System.out.println(sid.getID());
			System.out.println(sid.getHexID());
			System.out.println(sid.toString());
		}

	}
}
