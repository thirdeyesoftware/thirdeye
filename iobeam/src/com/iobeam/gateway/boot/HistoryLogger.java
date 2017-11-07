package com.iobeam.gateway.boot;

import java.io.*;
import java.util.*;
import java.text.*;
import com.iobeam.boot.*;
import com.iobeam.gateway.util.*;
import com.iobeam.gateway.router.*;

public class HistoryLogger implements BootClient {


	private static FileWriter mWriter;
	private static HistoryLogger theHistoryLogger = null;
	
	private static final String HISTORY_LOG_FILE_PROP = 
			"iobeam.gateway.lease.history.file";
	
	private static SimpleDateFormat cFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

	private HistoryLogger() {
		openLog();
	}


	private void openLog() {
		try {
			mWriter = new FileWriter(
					GatewayConfiguration.getInstance().
							getProperty(HISTORY_LOG_FILE_PROP), true);

			writeLog("<<< OPENED LOG " + cFormat.format(new Date()) + " >>>\n");

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeLog(RouteLease lease) {
		writeLog(formatRecord(lease));
	}

	public static void writeLog(String entry) {

		try {
			mWriter.write(entry.toCharArray());
			mWriter.flush();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	public HistoryLogger getInstance() {
		return theHistoryLogger;
	}


	public static Bootable getBootable() {
		return new HistoryBootable();	

	}

	private static class HistoryBootable implements Bootable {
		public void boot(BootContext bootContext)
				throws BootException {

			try {
				theHistoryLogger = new HistoryLogger();
			}
			catch (Exception e) {
				throw new BootException(e);
			}
		}
	}

	private static String formatRecord(RouteLease lease) {
		StringBuffer sb = new StringBuffer(0);
		Date now = new Date();
		sb.append(cFormat.format(now));
		sb.append("\t");
		sb.append(lease.getContactID());
		sb.append("\t");
		sb.append(lease.getMACAddress().toString());
		sb.append("\t");
		sb.append(lease.getInetAddress());
		sb.append("\t");
		sb.append(lease.getClientState());
		sb.append("\t");
		sb.append(cFormat.format(lease.getExpirationDate()));
		sb.append("\n");
		return sb.toString();
	}


}

