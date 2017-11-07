package com.iobeam.gateway.router.rule;

import java.util.Hashtable;
import java.util.Collection;

public class Table  {
	private Hashtable mChains = new Hashtable();
	private String mName;

	public static final Table MANGLE = new Table("mangle", new String[] {
																												"prerouting",
																												"postrouting",
																												"input",
																												"forward",
																												"output"});

	
	public static final Table NAT = new Table("nat", new String[] {
																										"prerouting",
																										"postrouting",
																										"output"});

																										
	public static final Table FILTER = new Table("filter", new String[] {
																												"input",
																												"forward",
																												"output"});
	
	private Table(String name, String[] defaultChains) {
		this(name);
		for (int i = 0;i < defaultChains.length;i++) {
//			System.out.println(i + " " + defaultChains[i]);
			Chain chain = new Chain(defaultChains[i].toUpperCase(),true);
			addChain(chain);
		}
	}
	
	private Table(String name) {
		mName = name;
	}
	public String getName() {
		return mName;
	}
	public String toString() {
		return mName;
	}
	public boolean equals(Object o) {
		if (o instanceof Table) {
			return ((Table)o).getName().equals(getName());
		}
		return false;
	}

	public String getCommandString() {
		return "-t " + getName().toLowerCase();
	}
	
	public void addChain(Chain chain) {
		mChains.put(chain.getName(),chain);
	}
	
	public void removeChain(Chain chain) {
		mChains.remove(chain.getName());
	}
	
	public Chain getChain(String chainName) {
		return (Chain)mChains.get(chainName);
	}
	
	public boolean containsChain(String chain) {
		return (mChains.containsKey(chain));
	}
	
	public Collection getChains() {
		return mChains.values();
	}

	public static Table getInstanceFor(String name) {
		if (name.equalsIgnoreCase("nat")) {
			return NAT;
		}
		else if (name.equalsIgnoreCase("mangle")) {
			return MANGLE;
		}
		else if (name.equalsIgnoreCase("filter")) {
			return FILTER;
		}
		else return null;
	}
	

}