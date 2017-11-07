package telesync.gui;

import java.util.*;

public class ErrorEvent extends EventObject {
	
	Object mValue;
	private boolean mIsInit = false;
	
	public ErrorEvent(Object source, Object value) {
		super(source);
		mValue = value;
	}
	public void setIsInit( boolean init ) {
		mIsInit = init;
	}
	public boolean isInit() {
		return mIsInit;
	}
	
	public Object getValue() {
		return mValue;
	}
	public String toString() {
		return mValue.toString();
	}
}

