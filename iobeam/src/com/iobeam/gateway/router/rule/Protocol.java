package com.iobeam.gateway.router.rule;

import java.util.*;

public class Protocol  {

	private static final Hashtable mInstanceHash = new Hashtable();
	
	public static final Protocol TCP = new Protocol(6,"tcp");
	public static final Protocol UDP = new Protocol(17,"udp");
	public static final Protocol ICMP = new Protocol(1, "icmp");
	public static final Protocol ANY = new Protocol(0, "any");

	private int mProtocolNumber;
	private String mName;
	
	private Protocol(int proto, String name) {
		mProtocolNumber = proto;
		mName = name;
		mInstanceHash.put(name, this);
	}
	public static Protocol getInstanceFor(int proto) {
		switch (proto)
		{
			case 0:
				return ANY;

			case 1:
				return ICMP;

			case 6:
				return TCP;

			case 17:
				return UDP;

			default:
				return ANY;
		}
	}

	public static Protocol getInstanceFor(String name) {
		return (Protocol)mInstanceHash.get(name);
	}
	public String toString() {
		return "Protocol:" + mName;
	}
	public boolean equals(Object o) {
		if (o instanceof Protocol) {
			return (((Protocol)o).getProtocol() == getProtocol());
		}
		return false;
	}

	public String getCommandString() {
		return "-p " + getName();
	}
	
	public int getProtocol() {
		return mProtocolNumber;
	}
	public String getName() {
		return mName;
	}
	
}

