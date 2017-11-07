package com.iobeam.gateway.scheduler;


import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.*;
import com.iobeam.boot.*;


public class Scheduler implements Runnable, BootClient {
	public static final String LOGGER = "com.iobeam.gateway.scheduler";

	static private Scheduler theScheduler = null;
	private Thread mThread;
	private TreeSet mEvents = new TreeSet();

	private Scheduler() {
		mThread = new Thread(this);
		mThread.setDaemon(true);

		mThread.start();
	}

	static public Scheduler getScheduler() {
		if (theScheduler == null) {
			throw new RuntimeException("not initialized");
		}

		return theScheduler;
	}

	public Thread getThread() {
		return mThread;
	}

	public synchronized void addEvent(ScheduledEvent event) {
		System.out.println("addEvent():event=" + event.toString());
		if (!mEvents.add(event)) {
			throw new IllegalArgumentException("event already scheduled");
		}

		Logger.getLogger(LOGGER).info(event.toString());

		notify();
	}

	public synchronized void removeEvent(ScheduledEvent event) {
		if (mEvents.remove(event)) {
			Logger.getLogger(LOGGER).info("removed " + event.toString());
		} else {
			Logger.getLogger(LOGGER).info("ignored " + event.toString());
		}

		notify();
	}


	/**
	* Returns a Collection of ScheduledEvents not yet processed by
	* the Scheduler.
	*/
	public Collection getEvents() {
		return new TreeSet(mEvents);
	}


	public void run() {
		ScheduledEvent se;

		for (;;) {
			synchronized (this) {
				se = null;

				while (mEvents.isEmpty()) {
					try {
						wait();
					}
					catch (InterruptedException ie) {
						Logger.getLogger(LOGGER).log(Level.WARNING,
								ie.toString(), ie);
					}
				}

				se = (ScheduledEvent) mEvents.first();
				if (se.isDue()) {

					if (se.isPermanent()) {
						mEvents.remove(se);
						Date d = se.getTriggerTime();
						se.setTriggerTime(
							new Date(d.getTime() + se.getIntervalMillis()));
						mEvents.add(se);
					}
					else {
						mEvents.remove(se);
					}

				} else {
					long m = se.getMillisUntilDue();
					se = null;
					try {
						wait(m);
					}
					catch (InterruptedException ie) {
						Logger.getLogger(LOGGER).log(Level.WARNING,
								ie.toString(), ie);
					}
				}
			}

			if (se != null) {
				(new Thread(se)).start();
			}
		}
	}




	public static Bootable getBootable() {
		return new SchedulerBootable();
	}


	private static class SchedulerBootable implements Bootable {
		public void boot(BootContext bootContext)
				throws BootException {

			try {
				theScheduler = new Scheduler();
			}
			catch (Throwable t) {
				throw new BootException(t);
			}
		}
	}


	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.out.println("Usage: Scheduler <listen port>");
		}

		theScheduler = new Scheduler();

		int listenPort = Integer.parseInt(args[0]);

		ServerSocket ss = new ServerSocket(listenPort);


		for ( ; ; ) {
			Socket s = ss.accept();

			ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

			ScheduledEvent se = (ScheduledEvent) ois.readObject();

			getScheduler().addEvent(se);

			s.close();
		}
	}
}
