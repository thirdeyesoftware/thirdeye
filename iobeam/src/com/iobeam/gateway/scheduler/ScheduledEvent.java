package com.iobeam.gateway.scheduler;


import java.text.*;
import java.util.Date;
import java.util.logging.*;


/**
* Associates a ScheduledTask with a triggerTime, at which the
* task is to be run.
*/
public class ScheduledEvent implements Comparable, Runnable,
		java.io.Serializable {

	public static final String LOGGER = "com.iobeam.gateway.scheduler";

	private static final DateFormat cDateFormat = new SimpleDateFormat(
			"MM/dd/yy HH:mm:ss");

	private Date mTriggerTime;
	private ScheduledTask mScheduledTask;

	private boolean mIsPermanent;
	private long mIntervalMillis;

	private static long cID = 0;
	private long mID;


	/**
	* @param triggerTime the moment in time when scheduledTask is to be run.
	*
	* @param scheduledTask the task to be run.
	*/
	public ScheduledEvent(Date triggerTime, ScheduledTask scheduledTask) {
		mID = cID++;
		mTriggerTime = triggerTime;
		mScheduledTask = scheduledTask;

		if (mTriggerTime == null) {
			throw new NullPointerException("triggerTime");
		}
		if (mScheduledTask == null) {
			throw new NullPointerException("scheduledTask");
		}

	}

	public ScheduledEvent(Date triggerTime, ScheduledTask task,
			boolean permanent, long millis) {
		this(triggerTime, task);
		mIsPermanent = permanent;
		mIntervalMillis = millis;
	}

	public long getID() {
		return mID;
	}


	public Date getTriggerTime() {
		return mTriggerTime;
	}

	public void setTriggerTime(Date d) {
		mTriggerTime = d;
	}

	public ScheduledTask getTask() {
		return mScheduledTask;
	}


	public int compareTo(Object o) {
		
		ScheduledEvent se = (ScheduledEvent) o;
		int i = getTriggerTime().compareTo(se.getTriggerTime());
		if (i == 0) {
			if (getID() < se.getID()) {
				return -1;
			}
			else if (getID() == se.getID()) {
				return 0;
			}
			else {
				return 1;
			}
		}
		else {
			return i;
		}

	}


	public int hashCode() {
		return (int) ((mID >> 32) ^ mID);
	}


	public boolean equals(Object o) {
		System.out.println("ScheduledEvent.equals():this=" + 
			toString() + "\no=" + o.toString());

		if (o != null && o instanceof ScheduledEvent) {
			ScheduledEvent se = (ScheduledEvent) o;
			return getID() == se.getID();
		} else {
			return false;
		}
	}


	public void run() {
		try {
			Logger.getLogger(LOGGER).info(this.toString());
			getTask().doTask();
		}
		catch (SchedulerException se) {
			Logger.getLogger(LOGGER).log(Level.WARNING, se.toString(), se);
		}
	}


	protected boolean isPermanent() {
		return mIsPermanent;
	}

	protected long getIntervalMillis() {
		return mIntervalMillis;
	}

	protected boolean isDue() {
		return mTriggerTime.getTime() <= System.currentTimeMillis();
	}


	protected long getMillisUntilDue() {
		long m = mTriggerTime.getTime() - System.currentTimeMillis();
		if (m < 0) {
			m = 0;
		}

		return m;
	}


	public String toString() {
		StringBuffer sb = new StringBuffer("ScheduledEvent(");

		sb.append(getID());
		sb.append(",");
		sb.append(cDateFormat.format(getTriggerTime()));
		sb.append(",");
		sb.append(getTask().toString());

		sb.append(")");

		return sb.toString();
	}
}
