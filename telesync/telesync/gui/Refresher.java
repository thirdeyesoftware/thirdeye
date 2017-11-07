package telesync.gui;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import pippin.binder.*;
import java.util.*;

public class Refresher {
	private RefreshThread mRefreshThread;
	
	private Vector mListeners;
	private static final Refresher mInstance = new Refresher();
	private RefreshThread mThread;
	private int mRefreshRate;
	
	private Refresher() {
		mListeners = new Vector();
		mThread = new RefreshThread();
		mThread.start();
		
	}
	
	public static Refresher getRefresher() {
		return mInstance;
	}
	public void setRefreshRate(int rate) {
		mRefreshRate = rate;
	}
	
	public synchronized void addRefreshListener(RefreshListener listener) {
		if (!mListeners.contains(listener)) mListeners.addElement(listener);
	}
	
	public synchronized void removeRefreshListener(RefreshListener listener) {
		mListeners.removeElement(listener);
	}
	public synchronized boolean hasRefreshListener(RefreshListener listener) {
		return mListeners.contains(listener);
	}
	
	
	public void dispose() {
		if (mThread != null && !mThread.isInterrupted()) {
			mThread.interrupt();
			mThread = null;
		}
	}
	
	class RefreshThread extends Thread {
		RefreshThread() {
			super();
			setPriority(Thread.MIN_PRIORITY);
			
		}
		public void run() {
			while (true) {
				
				if (SwingUtilities.isEventDispatchThread()) {
					notifyListeners();
				} else {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							notifyListeners();
						}
					});
				}
		
				try {
					Thread.sleep(3 * 1000);
				}
				catch (InterruptedException ie) {
					break;
				}
			}
		}
		private void notifyListeners() {
			if (mListeners.size() > 0) {
				for (Iterator it = mListeners.iterator();it.hasNext();) {
					RefreshListener listener = (RefreshListener)it.next();
					if (listener != null) listener.refreshNotify();
					
				}
			}
		}
					
	}
	
	
						
}

	
	
			