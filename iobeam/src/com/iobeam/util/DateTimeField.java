package com.iobeam.util;

import weblogic.servlet.logging.CustomELFLogger;
import weblogic.servlet.logging.HttpAccountingInfo;
import weblogic.servlet.logging.FormatStringBuffer;
import java.util.*;
import java.text.SimpleDateFormat;


/**
 * used to return user agent http header for extended
 * logging feature in weblogic.
 */
public class DateTimeField implements CustomELFLogger {
		static private SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	
	public void logField(HttpAccountingInfo info, 
			FormatStringBuffer buffer) {
		Date d = new Date();
		buffer.appendValueOrDash(sdf.format(d));
	
	}
}


	
