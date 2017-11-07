package com.iobeam.gateway.util;

import java.util.*;
import java.text.SimpleDateFormat;

public class GatewaySchedule {

	private static final String START_PROP = 
			"iobeam.gateway.schedule.start";
	private static final String STOP_PROP = 
			"iobeam.gateway.schedule.stop";

	private GregorianCalendar mStart;
	private GregorianCalendar mEnd;
	private static SimpleDateFormat cFormat = 
		new SimpleDateFormat("HH:mm:ss");

	private GatewaySchedule(String start, String end) {
		try {
			mStart = new GregorianCalendar();
			mEnd = new GregorianCalendar();
			mStart.setTime(cFormat.parse(start));
			mEnd.setTime(cFormat.parse(end));
		}
		catch (Exception pe ) {
			throw new IllegalArgumentException(pe.toString());
		}
	}

	public static GatewaySchedule getInstance() {
		GatewayConfiguration config = 
				GatewayConfiguration.getInstance();
		return new
			GatewaySchedule(
				config.getProperty(START_PROP), config.getProperty(STOP_PROP));
	}

	public Date getStart() {
		return mStart.getTime();
	}

	public Date getEnd() {
		return mEnd.getTime();
	}

	public boolean allowService(Date date) {
		GregorianCalendar target = new GregorianCalendar();
		target.setTime(date);

		mStart.set(Calendar.MONTH, target.get(Calendar.MONTH));
		mStart.set(Calendar.DAY_OF_MONTH, target.get(Calendar.DAY_OF_MONTH));
		mStart.set(Calendar.YEAR, target.get(Calendar.YEAR));
		
		mEnd.set(Calendar.MONTH, target.get(Calendar.MONTH));
		mEnd.set(Calendar.DAY_OF_MONTH, target.get(Calendar.DAY_OF_MONTH));
		mEnd.set(Calendar.YEAR, target.get(Calendar.YEAR));

		if (mStart.equals(target) || mEnd.equals(target)) {
			return true;
		}
		else if (mStart.before(target) && mEnd.after(target)) {
			return true;
		}

		else return false;

	}			
	public static void main(String args[]) {
		GatewaySchedule s = GatewaySchedule.getInstance();
		System.out.println(s.allowService(new Date()));
	}
}

				
