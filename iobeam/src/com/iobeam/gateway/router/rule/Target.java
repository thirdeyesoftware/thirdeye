package com.iobeam.gateway.router.rule;

import java.util.Hashtable;
import java.util.Collection;

public class Target  {
	protected String mName;
	private boolean mIsDefault;
		
	private static Hashtable mDefaultTargets = new Hashtable();
	
	public static final Target ACCEPT = new Target("ACCEPT",true);
	public static final Target DROP = new Target("DROP",true);
	public static final Target RETURN = new Target("RETURN",true);
	public static final Target QUEUE = new Target("QUEUE",true);	
	public static final Target MASQUERADE = new Target("MASQUERADE",true);	

	public String getName() {
		return mName;
	}
	public boolean isDefault() {
		return mIsDefault;
	}

	public Target(String name, boolean def) {
		mName = name;
		mIsDefault = def;
		if (def) {
			mDefaultTargets.put(mName, this);
		}
	}

	public String getCommandString() {
		return " -j " + getName();
	}
	
	public static Target getDefaultInstanceFor(String name) {
		if (mDefaultTargets.containsKey(name)) {
			return (Target)mDefaultTargets.get(name);
		} else return null;
	}
	
	public String toString() {
		return mName;
	}
	
	public boolean equals(Object o) {
		if (o instanceof Target) {
			return (((Target)o).getName().equals(getName()));
		}
		return false;
	}


	
}

