package com.iobeam.gateway.router.rule;

import com.iobeam.util.MACAddress;
import java.io.Serializable;

public class Match  implements Serializable {
	public static final long serialVersionUID = 2003021101L;
	
	private Protocol mProtocol;
	private int mDPort;
	private int mSPort;
	private Mark mMark;
	private MACAddress mMACAddress;
	private int mFlags;

	public static final int MARK = 0x01;
	public static final int DESTINATION_ADDRESS = 0x02;
	public static final int SOURCE_ADDRESS = 0x04;
	public static final int SOURCE_PORT = 0x08;
	public static final int DESTINATION_PORT = 0x16;
	public static final int PROTOCOL = 0x32;
	public static final int MAC_ADDRESS = 0x64;
	
	public Match() {
		mFlags = 0;
	}
	public void setProtocol(Protocol proto) {
		mProtocol = proto;
	}
	
	public void setMark(Mark mark) {
		mMark = mark;
	}
	public void setMACAddress(MACAddress mac) {
		mMACAddress = mac;
	}
	public void setDestinationPort(int port) {
		mDPort = port;
	}
	public void setSourcePort(int port) {
		mSPort = port;
	}
	
	public Mark getMark() {
		return mMark;
	}
	public MACAddress getMACAddress() {
		return mMACAddress;
	}
	
	public int getDestinationPort() {
		return mDPort;
	}

	public int getSourcePort() {
		return mSPort;
	}

	public Protocol getProtocol() {
		return mProtocol;
	}

	public int getFlags() {
		if (mMark != null) {
			mFlags |= MARK;
		}
		if (mProtocol != null) {
			mFlags |= PROTOCOL;
		}
		if (mSPort > 0) {
			mFlags |= SOURCE_PORT;
		}
		if (mDPort > 0) {
			mFlags |= DESTINATION_PORT;
		}
		if (mMACAddress != null) {
			mFlags |= MAC_ADDRESS;
		}
		
		return mFlags;
	}

	public String getCommandString() {
		int flags = getFlags();
		StringBuffer sb = new StringBuffer("-m ");
		if ((flags & MARK) == MARK) {
			sb.append(" mark --mark ").append(getMark().getType());
			sb.append(" " );
		}
		if ((flags & PROTOCOL) == PROTOCOL) {
			sb.append(getProtocol().getCommandString());
			sb.append(" " );
		}
		if ((flags & SOURCE_PORT) == SOURCE_PORT) {
			sb.append("--sport ").append(getSourcePort());
			sb.append(" " );
		}
		if ((flags & DESTINATION_PORT) == DESTINATION_PORT) {
			sb.append("--dport ").append(getDestinationPort());
			sb.append(" " );
		}
		if ((flags & MAC_ADDRESS) == MAC_ADDRESS) {
			sb.append(" mac --mac-source ").append(getMACAddress());
			sb.append(" ");
		}
		return sb.toString();
	}
	
	public String toString() {
		return getCommandString();
	}
	
	public boolean equals(Object o) {
		if (o instanceof Match) {
			Match match = (Match)o;
			if (match.getProtocol().equals(getProtocol()) &&
					match.getDestinationPort()==getDestinationPort() &&
					match.getMACAddress().equals(getMACAddress()) &&
					match.getMark().equals(getMark())) return true;
		}
		return false;
	}
	private Object fixNull(Object s) {
		if (s == null) return "<null>";
		else return s;
	}		
}
