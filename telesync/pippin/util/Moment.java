////
// //
// PippinSoft
//
//

package pippin.util;



import java.util.*;
import java.io.Serializable;



/**
* A moment is expressed in terms of milliseconds since the 1970
* epoch, and is represented in string form as hh:mm:ss.ms where the
* "ms" field can be up to three digits.
*/
public class Moment implements Serializable {
	private static long serialVersionUID = 1999061401L;
	// MomentCalendar mCalendar = new MomentCalendar();
	private Long mMillis;

	public Moment() {
		MomentCalendar mc = new MomentCalendar();
		setMillis(mc);
	}


	/**
	* @param seconds
	* Seconds since the 1970 epoch.
	*/
	public Moment(int seconds) {
		setMillis(seconds * 1000L);
	}


	/**
	* @param milliseconds
	* Milliseconds since the 1970 epoch.
	*/
	public Moment(long milliseconds) {
		// mCalendar.setTimeInMillis(milliseconds);
		setMillis(milliseconds);
	}


	/**
	* @param s
	* hh:mm:ss.ms of the current day.
	*/
	public Moment(String s) throws NumberFormatException {
		MomentCalendar mc = new MomentCalendar();

		setTime(s, mc);
		mc.computeTime();

		setMillis(mc);
	}


	/**
	* @param date
	* mm:dd:[yy]yy date for this Moment.  Y2K windowing assigns year 0-69
	* to 2000, 70-99 to 1999.
	*
	* @param time
	* hh:mm:ss.ms within the specified date.
	*/
	public Moment(String date, String time) throws NumberFormatException {
		System.out.println("Moment.<init>: " + date + " " + time);
		MomentCalendar mc = new MomentCalendar();
		mc.setTimeInMillis(0);
		setDate(date, mc);
		setTime(time, mc);
		mc.computeTime();

		setMillis(mc);
	}


	private static void setDate(String s, MomentCalendar mc)
			throws NumberFormatException {
		StringTokenizer st = new StringTokenizer(s, "/");
		int day = 0, month = 0, year = 0;

		if (st.hasMoreTokens()) {
			month = Integer.parseInt(st.nextToken()) - 1;
		}

		if (st.hasMoreTokens()) {
			day = Integer.parseInt(st.nextToken());
		}

		if (st.hasMoreTokens()) {
			year = Integer.parseInt(st.nextToken());

			// Y2k windowing for 1567s.
			if (year < 70) {
				year += 2000;
			} else
			if (year >= 70 && year <= 99) {
				year += 1900;
			}
		}

		mc.set(Calendar.MONTH, month);
		mc.set(Calendar.DAY_OF_MONTH, day);
		mc.set(Calendar.YEAR, year);
	}


	private static void setTime(String s, MomentCalendar mc)
			throws NumberFormatException {
		StringTokenizer st = new StringTokenizer(s, ":.");
		int hours = 0, minutes = 0, seconds = 0, milliseconds = 0;

		if (st.hasMoreTokens()) {
			hours = Integer.parseInt(st.nextToken());
		}

		if (st.hasMoreTokens()) {
			minutes = Integer.parseInt(st.nextToken());
		}

		if (st.hasMoreTokens()) {
			seconds = Integer.parseInt(st.nextToken());
		}

		if (st.hasMoreTokens()) {
			milliseconds = Integer.parseInt(st.nextToken());
		}

		mc.set(Calendar.HOUR_OF_DAY, hours);
		mc.set(Calendar.MINUTE, minutes);
		mc.set(Calendar.SECOND, seconds);
		mc.set(Calendar.MILLISECOND, milliseconds);
	}


	/**
	* Make this Moment occur within a day before the specified Moment.
	*/
	public void normalizeTo(Moment now) {
		Calendar c = now.getCalendar();
		MomentCalendar mc = getCalendar();

		while (mc.after(c)) {
			mc.add(Calendar.DATE, -1);
		}

		setMillis(mc);
	}


	public String toString() {
		return getDateString() + " " + getMillisString();
	}


	public String getDateString() {
		MomentCalendar mc = getCalendar();

		mc.setTimeZone(TimeZone.getDefault());
		int month = mc.get(Calendar.MONTH) + 1;
		int day = mc.get(Calendar.DAY_OF_MONTH);
		int year = mc.get(Calendar.YEAR);

		return format(month, 2) + "/" + format(day, 2) + "/" +
				format(year, 4);
	}


	public String getTimeString() {
		MomentCalendar mc = getCalendar();

		return getTimeString(mc);
	}


	protected String getTimeString(MomentCalendar mc) {
		mc.setTimeZone(TimeZone.getDefault());
		int hours = mc.get(Calendar.HOUR_OF_DAY);
		int minutes = mc.get(Calendar.MINUTE);
		int seconds = mc.get(Calendar.SECOND);

		return format(hours, 2) + ":" + format(minutes, 2) + ":" +
				format(seconds, 2);
	}


	public String getMillisString() {
		MomentCalendar mc = getCalendar();

		int milliseconds = mc.get(Calendar.MILLISECOND);

		return getTimeString(mc) + "." + format(milliseconds, 3);
	}


	private String format(int n, int digits) {
		StringBuffer sb = new StringBuffer(Integer.toString(n));

		while (sb.length() < digits) {
			sb.insert(0, '0');
		}

		return sb.toString();
	}


	private void setMillis(long millis) {
		mMillis = new Long(millis);
	}


	private void setMillis(MomentCalendar mc) {
		mMillis = new Long(mc.getTimeInMillis());
	}


	public long getMilliseconds() {
		return mMillis.longValue();
	}


	public void addMilliseconds(long millis) {
		mMillis = new Long(mMillis.longValue() + millis);
	}


	public long getSeconds() {
		return getMilliseconds() / 1000;
	}


	public Date getTimeStamp() {
		return new Date(getMilliseconds());
	}


	public MomentCalendar getCalendar() {
		MomentCalendar mc = new MomentCalendar();
		mc.setTimeInMillis(getMilliseconds());

		return mc;
	}


	public long getDeltaMillis(Moment m) {
		return m.getMilliseconds() - getMilliseconds();
	}


	public TimePeriod getDeltaPeriod(Moment m) {
		return new TimePeriod(getDeltaMillis(m));
	}


	static public long stringToMillis(String s) throws NumberFormatException {
		StringTokenizer st = new StringTokenizer(s, ":");
		int hours = 0, minutes = 0, seconds = 0, milliseconds = 0;

		if (st.hasMoreTokens()) {
			hours = Integer.parseInt(st.nextToken());
		}

		if (st.hasMoreTokens()) {
			minutes = Integer.parseInt(st.nextToken());
		}

		if (st.hasMoreTokens()) {
			seconds = Integer.parseInt(st.nextToken("."));
		}

		if (st.hasMoreTokens()) {
			milliseconds = Integer.parseInt(st.nextToken());
		}

		return hours * 3600000 + minutes * 60000 + seconds * 1000 +
				milliseconds;
	}

	static public String millisToString(long millis, boolean usedays) {
		int sec = 1000;
		int min = 60 * sec;
		int hour = 60 * min;
		int day = 24 * hour;
		int days = 0;

		StringBuffer sb = new StringBuffer();

		days = (int) (millis / day);
		if (days > 0) {
			if (usedays) {
				sb.append(Integer.toString(days) + "d ");
			}
		}
		int hours = (int)((millis % day) / hour);
		if (!usedays) {
			hours += (24*days);
			sb.append(formatInt(hours,4));
		}
		else {
			sb.append(formatInt(hours,2));
		}
		sb.append(':');

		sb.append(formatInt((int) (millis % hour) / min, 2));
		sb.append(':');
		sb.append(formatInt((int) (millis % min) / sec, 2));
		
		return sb.toString();
	}
	
	static public String millisToString(long millis) {
		return millisToString(millis,true);
	}


	static public String millisToLongString(long millis) {
		int sec = 1000;

		StringBuffer sb = new StringBuffer();

		sb.append(millisToString(millis));
		sb.append('.');
		sb.append(formatInt((int) (millis % sec), 3));

		return sb.toString();
	}


	static private String formatInt(int i, int length) {
		StringBuffer sb = new StringBuffer(Integer.toString(i));
		while (sb.length() < length) {
			sb.insert(0, '0');
		}

		return sb.toString();
	}


	public String toDurationString() {
		return millisToString(getMilliseconds(),true);
	}
	public String toDurationString(boolean useDays) {
		return millisToString(getMilliseconds(),useDays);
	}
	

	public String toLongDurationString() {
		return millisToLongString(getMilliseconds());
	}


	public boolean equals(Object o) {
		if (o instanceof Moment) {
			Moment m = (Moment) o;

			return getMilliseconds() == m.getMilliseconds();
		} else {
			return false;
		}
	}


	public int hashCode() {
		return mMillis.hashCode();
	}


	static public String getCurrentTimeString() {
		GregorianCalendar gc = new GregorianCalendar();

		StringBuffer sb = new StringBuffer(
				formatInt(gc.get(Calendar.MONTH) + 1, 2));
		sb.append('/');
		sb.append(formatInt(gc.get(Calendar.DAY_OF_MONTH), 2));
		sb.append(' ');
		sb.append(formatInt(gc.get(Calendar.HOUR_OF_DAY), 2));
		sb.append(':');

		sb.append(formatInt(gc.get(Calendar.MINUTE), 2));
		sb.append(':');
		sb.append(formatInt(gc.get(Calendar.SECOND), 2));

		return sb.toString();
	}


	class MomentCalendar extends GregorianCalendar {
		MomentCalendar() {
		}

		public void setTimeInMillis(long millis) {
			super.setTimeInMillis(millis);
			computeFields();
		}

		public long getTimeInMillis() {
			return super.getTimeInMillis();
		}

		public void setTimeZone(TimeZone timeZone) {
			super.setTimeZone(timeZone);
			computeFields();
		}


		public void computeTime() {
			super.computeTime();
		}
	};
	
	public static void main(String[] args) {
		System.out.println((new Moment(Long.parseLong(args[0])).toDurationString(false)));
	}
	
}
