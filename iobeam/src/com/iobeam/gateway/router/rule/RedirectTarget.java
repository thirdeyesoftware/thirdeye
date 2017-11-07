package com.iobeam.gateway.router.rule;

public class RedirectTarget extends Target {
	int mPort;
	String mAddress;

	public RedirectTarget(int port) {
		super("REDIRECT", false);
		mPort = port;
	}

	public RedirectTarget(String address, int port) {
		super("REDIRECT", false);
		mPort = port;
		mAddress = address;
	}

	public int getPort() {
		return mPort;
	}
	public String getAddress() {
		return mAddress;
	}

	public String getCommandString() {
		if (mAddress == null) {
			return super.getCommandString() + " --to-port " + mPort;
		}
		else {
			return "-j DNAT --to " + mAddress + ":" + mPort;
		}

	}
}
