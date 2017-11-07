package com.iobeam.portal.security;


import java.math.*;
import java.security.*;
import com.iobeam.util.HexDump;


public class SecureID implements java.io.Serializable {
	private BigInteger mID;
	private String mKey;

	/**
	* The number of characters per a group in a formatted key.
	*/
	public static final int GROUP_SIZE = 4;


	SecureID(BigInteger id) {
		mID = id;
		if (id == null) {
			throw new NullPointerException("id");
		}

		mKey = SecureIDFactory.getKey(mID, SecureIDFactory.KEY_LENGTH);
	}


	public boolean equals(Object o) {
		if (o instanceof SecureID) {
			SecureID sid = (SecureID) o;
			return sid.getID().equals(getID());
		} else {
			return false;
		}
	}


	public int hashCode() {
		return getID().hashCode();
	}


	public BigInteger getID() {
		return mID;
	}


	/**
	* Returns unsigned bits of the underlying id.
	*/
	public byte[] toByteArray() {
		byte[] b = mID.toByteArray();

		if (b.length > SecureIDFactory.ID_LENGTH) {
			byte[] bb = new byte[SecureIDFactory.ID_LENGTH];

			for (int i = 0; i < bb.length; ++i) {
				bb[i] = b[i + 1];
			}

			b = bb;
		}

		return b;
	}


	public String getHexID() {
		return HexDump.toString(toByteArray());
	}


	/**
	* Returns an unformatted key.  This is the human-readable String
	* representation of the ID, without any spaces or other separators.
	*/
	public String getKey() {
		return mKey;
	}


	/**
	* Returns a formatted key.
	*/
	public String toString() {
		StringBuffer sb = new StringBuffer();

		for (int i = mKey.length(); i > 0; i -= GROUP_SIZE) {
			int p = i - GROUP_SIZE;
			if (p < 0) {
				p = 0;
			}

			sb.insert(0, mKey.substring(p, i));
			if (p > 0) {
				sb.insert(0, " ");
			}
		}

		return sb.toString();
	}
}
