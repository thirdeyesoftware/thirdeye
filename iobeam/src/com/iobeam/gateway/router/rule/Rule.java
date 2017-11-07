package com.iobeam.gateway.router.rule;

import java.io.Serializable;

public class Rule implements Serializable {

	public static final long serialVersionUID = 2003020601L;
	
	private Address mSourceAddress;
	private Address mDestinationAddress;
	private Match mMatch;
	private String mChain;
	private Target mTarget;
	private String mTable;
	private String mProtocol;
	private int mSourcePort;
	private int mDestinationPort;
	private String mInternalInterface;
	private String mExternalInterface;

	public static final int FLG_SOURCE_ADDRESS = 1;
	public static final int FLG_DESTINATION_ADDRESS = 2;
	public static final int FLG_MATCH = 4;
	public static final int FLG_PROTOCOL = 8;
	public static final int FLG_SOURCE_PORT = 16;
	public static final int FLG_DESTINATION_PORT = 32;
	public static final int FLG_INTERNAL_INTERFACE = 64;
	public static final int FLG_EXTERNAL_INTERFACE = 128;
	
	public Rule(Address src, Address dest, String table, Target target, 
			String chain, String protocol, Match match, int srcPort, int dstPort,
			String internalInterface, String externalInterface) {

		mSourceAddress = src;
		mDestinationAddress = dest;
		mTable = table;
		mTarget = target;
		mChain = chain;
		mProtocol = protocol;
		mMatch = match;
		mSourcePort = srcPort;
		mDestinationPort = dstPort;
		mInternalInterface = internalInterface;
		mExternalInterface = externalInterface;
	}

	public Rule(Address src, Address dest, String table, Target target, 
			String chain, Match match) {
		this(src,dest,table,target,chain,null,match,0,0,null,null);
	}

	public Address getSourceAddress() {
		return mSourceAddress;
	}
	public Address getDestinationAddress() {
		return mDestinationAddress;
	}
	public String getTable() {
		return mTable;
	}
	public Target getTarget() {
		return mTarget;
	}
	public String getChain() {
		return mChain;
	}
	public int getSourcePort() {
		return mSourcePort;
	}
	public int getDestinationPort() {
		return mDestinationPort;
	}
	public String getProtocol() {
		return mProtocol;
	}
	public String getInternalInterface() {
		return mInternalInterface;
	}
	public String getExternalInterface() {
		return mExternalInterface;
	}
	public Match getMatch() {
		return mMatch;
	}

	public String toString() {
		try {
			return "Rule: " + 
				"table = " + fixNull(getTable()) + 
				"\n\tchain = " + fixNull(getChain()) +
				"\n\tsrc = " + fixNull(getSourceAddress()) + 
				"\n\tdst = " + fixNull(getDestinationAddress()) + 
				"\n\tmatch = " + fixNull(getMatch()) + 
				"\n\ttarget = " + fixNull(getTarget()) + 
				"\n\tdPort = " + getDestinationPort() + 
				"\n\tsPort = " + getSourcePort() + 
				"\n\tiFace = " + fixNull(getInternalInterface()) + 
				"\n\teFace = " + fixNull(getExternalInterface());
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

	private Object fixNull(Object s) {
		if (s == null) return "<null>";
		else return s;
	}
	
	public int getFlags() {
		int flags = 0;
		if (mSourceAddress != null) {
			flags |= FLG_SOURCE_ADDRESS;
		}
		if (mDestinationAddress != null) {
			flags |= FLG_DESTINATION_ADDRESS;
		}
		if (mSourcePort > 0) {
			flags |= FLG_SOURCE_PORT;
		}
		if (mDestinationPort > 0) {
			flags |= FLG_DESTINATION_PORT;
		}
		if (mMatch != null) {
			flags |= FLG_MATCH;
		}
		if (mProtocol != null && (flags & FLG_MATCH) != FLG_MATCH) {
			flags |= FLG_PROTOCOL;
		}
		if (mInternalInterface != null) {
			flags |= FLG_INTERNAL_INTERFACE;
		}
		if (mExternalInterface != null) {
			flags |= FLG_EXTERNAL_INTERFACE;
		}
		return flags;
	}
	
	public String getCommandString() {
		StringBuffer sb = new StringBuffer();
		int flags = getFlags();
		if ((flags & FLG_SOURCE_ADDRESS) == FLG_SOURCE_ADDRESS) {
			sb.append("-s ");
			if (mSourceAddress.isExcept()) sb.append("! ");
			sb.append( mSourceAddress.getAddress());
			sb.append(" ");
		}
		if ((flags & FLG_DESTINATION_ADDRESS) == FLG_DESTINATION_ADDRESS) {
			sb.append("-d ");
			if (mDestinationAddress.isExcept()) sb.append("! ");
			sb.append(mDestinationAddress.getAddress());
			sb.append(" ");
		}
		if ((flags & FLG_MATCH) == FLG_MATCH) {
			sb.append(mMatch.getCommandString());
			sb.append(" ");
		}
		else {
			if ((flags & FLG_PROTOCOL) == FLG_PROTOCOL) {
				sb.append("-p " + mProtocol);
				sb.append(" ");
			}
			if ((flags & FLG_SOURCE_PORT) == FLG_SOURCE_PORT) {
				sb.append("--sport ").append(mSourcePort);
				sb.append(" ");
			}
			if ((flags & FLG_DESTINATION_PORT) == FLG_DESTINATION_PORT) {
				sb.append("--dport ").append(mDestinationPort);
				sb.append(" ");
			}
		}
		if ((flags & FLG_INTERNAL_INTERFACE) == FLG_INTERNAL_INTERFACE) {
			sb.append("-i ").append(mInternalInterface);
			sb.append(" ");
		}
		if ((flags & FLG_EXTERNAL_INTERFACE) == FLG_EXTERNAL_INTERFACE) {
			sb.append("-o ").append(mExternalInterface);
			sb.append(" ");
		}
		sb.append(mTarget.getCommandString());
		return sb.toString();
	}
	
		
		
	public boolean equals(Object o) {
		if (o instanceof Rule) {
			Rule rule = (Rule)o;
			if (rule.getTable().equals(getTable()) &&
					rule.getChain().equals(getChain()) &&
					rule.getSourceAddress().equals(getSourceAddress()) &&
					rule.getDestinationAddress().equals(getDestinationAddress()) &&
					rule.getTarget().equals(getTarget()) &&
					rule.getSourcePort() == getSourcePort() &&
					rule.getDestinationPort() == getDestinationPort() &&
					rule.getInternalInterface().equals(getInternalInterface()) &&
					rule.getExternalInterface().equals(getExternalInterface()) &&
					rule.getMatch().equals(getMatch())) return true;
		}
		return false;
	}
	
	public interface Action {
		public int APPEND = 0;
		public int INSERT = 1;
		public int DELETE = 2;
	}
	
		
		
	
}
