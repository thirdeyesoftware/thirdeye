package com.iobeam.util;


import java.util.StringTokenizer;


public class MACAddress implements java.io.Serializable {
	
	private byte[] mMACBytes = new byte[6];
	private String mToString = null;

	public MACAddress(byte[] mac) {
		mMACBytes = mac;
	}


	/**
	* @param mac A String representation of the 6 bytes of the mac
	* address, of the form xx:xx:xx:xx:xx:xx, where x is a hex digit.
	*/
	public MACAddress(String mac) {
		StringTokenizer st = new StringTokenizer(mac, ":");

		for (int i = 0; i < 6; ++i) {
			mMACBytes[i] = (byte) (Integer.parseInt(st.nextToken(), 16) &
					0xff);
		}
	}


	public boolean equals(Object o) {
		if (o instanceof MACAddress) {
			MACAddress addr = (MACAddress)o;
			byte[] bytes = addr.getBytes();
			for (int i = 0; i < bytes.length; i++) {
				if (bytes[i] != mMACBytes[i]) return false;
			}
			return true;
		}
		return false;
	}


	public int hashCode() {
		return toString().hashCode();
	}


	public String toString() {
		if (mToString == null) {
			StringBuffer sb = new StringBuffer();

			for (int i = 0; i < mMACBytes.length; ++i) {
				String hex = Integer.toHexString(mMACBytes[i] & 0xff);
				if (hex.length() == 1) {
					sb.append("0");
				}
				sb.append(hex);
				if (i < (mMACBytes.length-1)) sb.append(":");
			}

			mToString = sb.toString();
		}

		return mToString;
	}


	public byte[] getBytes() {
		byte[] b = new byte[6];

		for (int i = 0; i < 6; ++i) {
			b[i] = mMACBytes[i];
		}

		return b;
	}
}
