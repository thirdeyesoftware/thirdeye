package pippin.util;

import java.io.*;
import java.util.*;

public class Sniffer {
	private static final Sniffer mInstance = new Sniffer();
	private Vector mListeners;
	private boolean mEnabled = false;
	
	private Sniffer () {
		mListeners = new Vector();	
	}
	
	public static Sniffer getSniffer() {
		if (mInstance == null) return new Sniffer();
		else return mInstance;
	}
	
	public void setEnabled(boolean b) {
		mEnabled = b;
	}
	public boolean isEnabled() {
		return mEnabled;
	}
	public void addSnifferListener(SnifferListener listener) {
		if (!mEnabled) return;
		if (!mListeners.contains(listener)) {
			System.out.println("Sniffer addSnifferListener");
			mListeners.addElement(listener);
		}
	}
	
	public void removeSnifferListener(SnifferListener listener) {
		if (!mEnabled) return;
		if (mListeners.contains(listener)) {
			System.out.println("Sniffer removeSnifferListener");
			mListeners.remove(listener);
		}
	}
	
	public void dispatch(String name, Object buffer) {
		if (!mEnabled) return;
		for (Enumeration en = mListeners.elements();en.hasMoreElements();) {
			((SnifferListener)en.nextElement()).snifferEventNotify(name, buffer.toString());
		}
	}
	
	
	
}
