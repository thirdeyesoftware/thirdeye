package com.iobeam.util.logging;


import java.util.Date;
import java.util.logging.*;
import java.text.*;


public class BriefFormatter extends Formatter {

	public BriefFormatter() {
	}

	private static final DateFormat DATE_FORMAT =
			new SimpleDateFormat("MM/dd HH:mm:ss");

	public String format(LogRecord record) {
		StringBuffer sb = new StringBuffer();

		sb.append("<<<");
		sb.append(record.getSequenceNumber());
		sb.append(" ").append(record.getLevel()).append(" ");
		sb.append(record.getLoggerName());
		sb.append(" ");
		sb.append(DATE_FORMAT.format(new Date(record.getMillis())));
		sb.append("\n");

		sb.append("   ");
		sb.append(record.getSourceClassName());
		sb.append(".");
		sb.append(record.getSourceMethodName());
		sb.append(": ");
		sb.append(record.getMessage());

		Throwable t = record.getThrown();

		if (t != null) {
			sb.append("\n");

			sb.append("   ");
			sb.append(t.toString()).append("\n");
			StackTraceElement[] e = t.getStackTrace();

			for (int i = 0; i < e.length; ++i) {
				sb.append("   ");
				sb.append("   at ");
				sb.append(e[i].toString()).append("\n");
			}
			sb.append(record.getSequenceNumber());
		} else {
			sb.append(" ");
		}
		sb.append(">>>\n");



		return sb.toString();
	}
}
