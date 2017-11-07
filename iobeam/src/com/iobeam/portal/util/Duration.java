package com.iobeam.portal.util;


public class Duration implements java.io.Serializable {

	private long mMillis;
	private boolean mIsContinuous;

	private static final int HOUR_MILLIS = 60 * 60 * 1000;
	private static final int MINUTE_MILLIS = 60 * 1000;

	public static final Duration CONTINUOUS = new Duration(true);


	public Duration(long millis) {

		mMillis = millis;
		if (millis < 0) {
			throw new IllegalArgumentException("negative millis");
		}


		if (mMillis == Long.MAX_VALUE) {
			mIsContinuous = true;
		} else {
			mIsContinuous = false;
		}
	}


	private Duration(boolean isContinuous) {
		mIsContinuous = isContinuous;
		mMillis = Long.MAX_VALUE;
	}


	public long getTime() {
		return mMillis;
	}


	public int getHours() {
		return (int) (getTime() / HOUR_MILLIS);
	}

	public int getMinutes() {
		return (int) ((getTime() % HOUR_MILLIS) / MINUTE_MILLIS);
	}

	public int getSeconds() {
		return (int) ((getTime() % MINUTE_MILLIS) / 1000);
	}

	public int getMilliseconds() {
		return (int) (getTime() % 1000);
	}


	public boolean isContinuous() {
		return mIsContinuous;
	}


	public boolean equals(Object o) {
		if (o instanceof Duration) {
			Duration d = (Duration) o;
			return getTime() == d.getTime();
		} else {
			return false;
		}
	}


	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("Duration(");

		if (isContinuous()) {
			sb.append("CONTINUOUS");
		} else {
			sb.append(getHours()).append(",");
			sb.append(getMinutes()).append(",");
			sb.append(getSeconds()).append(",");
			sb.append(getMilliseconds());
		}

		sb.append(")");

		return sb.toString();
	}
}
