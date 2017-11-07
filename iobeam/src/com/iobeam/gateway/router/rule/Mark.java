package com.iobeam.gateway.router.rule;

import com.iobeam.gateway.router.ClientState;

public class Mark extends Target  {

	
	private int mType; //unsigned int
	
	public Mark(int type) {
		super("MARK",false);
		mType = type;
	}

	public String toString() {
		return getCommandString();
	}

	public String getCommandString() {
		return " -j MARK --set-mark " + mType;
	}
	
	public int getType() {
		return mType;
	}

	public boolean equals(Object o) {
		if (o instanceof Mark) {
			if (((Mark)o).getType() == getType()) return true;
		}
		return false;
	}

	public static Mark getMark(ClientState state) {
		return new Mark(state.getID());

	}

}
