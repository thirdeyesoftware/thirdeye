package com.iobeam.util;

import weblogic.servlet.logging.CustomELFLogger;
import weblogic.servlet.logging.HttpAccountingInfo;
import weblogic.servlet.logging.FormatStringBuffer;
import java.util.*;


/**
 * used to return user agent http header for extended
 * logging feature in weblogic.
 */
public class UserAgentField implements CustomELFLogger {

	public void logField(HttpAccountingInfo info, 
			FormatStringBuffer buffer) {
		buffer.appendValueOrDash("\"" + info.getHeader("User-Agent") + "\"");
	
	}
}


	
