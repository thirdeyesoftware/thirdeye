package com.iobeam.gateway.router.rule;

import java.util.*;

public class Chain  {

	private static final Hashtable mDefaultInstances = new Hashtable();
	private String mName;
	private boolean mIsDefaultChain;

	private Vector mRules = new Vector();
	

	public Chain(String name, boolean def) {
		mName = name;
		mIsDefaultChain = def;
		if (def) {
			mDefaultInstances.put(mName.toLowerCase(), this);
		}
	}

	
	public String getName() {
		return mName;
	}

	public String getCommandString() {
		if (isDefault()) return " " + mName.toUpperCase();
		else return " " + mName;
	}
	
	public boolean isDefault() {
		return mIsDefaultChain;
	}

	public Collection getRules() {
		return mRules;
	}
	
	public String toString() {
		return mName + " def=" + mIsDefaultChain;
	}

	public boolean equals(Object o) {
		if (o instanceof Chain) {
			return (((Chain)o).getName().equals(getName()));
		}
		return false;
	}

	public void addRule(int location, Rule rule) 
			throws ArrayIndexOutOfBoundsException {
		mRules.add(location, rule);
	}

	public void addRule(Rule rule) {
		mRules.addElement(rule);
	}

	public void removeRule(Rule rule) {
		mRules.removeElement(rule);
	}
	
	
}
