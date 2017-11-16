package com.iobeam.util;

import weblogic.servlet.logging.CustomELFLogger;
import weblogic.servlet.logging.HttpAccountingInfo;
import weblogic.servlet.logging.FormatStringBuffer;


public class RemoteUserField implements CustomELFLogger {

	public void logField(HttpAccountingInfo info, 
			FormatStringBuffer buffer) {

		buffer.appendValueOrDash(info.getRemoteUser());
	
	}
}


	