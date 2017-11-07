package com.iobeam.gateway.router.rule;

public class Address  {
	private String mAddress;
	private boolean mIsSubnet;
	private boolean mIsExcept;

	/**
	 * Simple Address Object that represents either a dot decimal address or an
	 * address & mask (ie. 172.16.0.0/16).
	 */
	public Address(String addr, boolean subnet) {
		this(addr, subnet, false);	
	}
	
	public Address(String addr, boolean subnet, boolean isExcept) {
		mAddress = addr;
		mIsSubnet = subnet;
		mIsExcept = isExcept;
	}

		
	public String getAddress() {
		return mAddress;
	}

	public boolean isSubnet() {
		return mIsSubnet;
	}

	public String toString() {
		return mAddress;
	}

	public boolean isExcept() {
		return mIsExcept;
	}

	public boolean equals(Object o) {
		if (o instanceof Address) {
			Address addr = (Address)o;
			if (addr.getAddress().equals(getAddress()) &&
				isExcept() == addr.isExcept()) return true;
		}
		return false;
	}
	
}
