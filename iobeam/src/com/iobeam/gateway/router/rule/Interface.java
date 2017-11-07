package com.iobeam.gateway.router.rule;

public class Interface  {
	private int mType;
	private String mName;
	private String mAddress;
	private String mSubnet;
	
	public Interface(String alias, int type, String address, String subnet) {
		mName = alias;
		mType = type;
		mAddress = address;
		mSubnet = subnet;
	}

	public Interface(String alias, int type) {
		this(alias, type, null, null);
	}
	
	public String getName() {
		return mName;
	}
	public int getType() {
		return mType;
	}
	public String getAddress() {
		return mAddress;
	}
	public String getSubnet() {
		return mSubnet;
	}

	public String getCommandString() {
		return " -" + 
			((mType==Type.INTERNAL) ? "i" : "o") +
			" "  + getName();
	}
	
	public boolean equals(Object o) {
		if (o instanceof Interface) {
			Interface iface = (Interface)o;
			if (iface.getName().equals(getName()) &&
					iface.getType() == getType() &&
					iface.getAddress().equals(getAddress()) &&
					iface.getSubnet().equals(getSubnet())) return true;
		}
		return false;
	}
				
	public interface Type {
		public int INTERNAL = 0;
		public int EXTERNAL = 1;
	}
}