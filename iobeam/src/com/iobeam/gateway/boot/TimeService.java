package com.iobeam.gateway.boot;

import java.io.*;
import java.util.*;
import java.text.*;
import com.iobeam.boot.*;
import com.iobeam.gateway.util.*;
import com.iobeam.gateway.web.AdminHelper;
import com.iobeam.gateway.router.*;
import com.iobeam.gateway.scheduler.*;

public class TimeService implements BootClient {


	private static FileWriter mWriter;
	private static TimeService theTimeService = null;
	private static final long INTERVAL_MILLIS = 1000l * 3600l;

	private static final String HISTORY_LOG_FILE_PROP = 
			"iobeam.gateway.lease.history.file";
	
	private static SimpleDateFormat cFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

	private TimeService() {
		init();
	}


	private void init() {
		try {
			Date trigger = new Date();

			ScheduledEvent event = new ScheduledEvent(
					trigger, new TimeServiceTask(), true, INTERVAL_MILLIS);

			Scheduler.getScheduler().addEvent(event);

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static final class TimeServiceTask implements ScheduledTask, 
			java.io.Serializable {

		public void doTask() {
			System.out.println("TimeServiceTask:doTask()");
			long servertime = AdminHelper.getServerTime();
			GatewayConfiguration.getInstance().setSystemTime(servertime);
			
		}
	}

	public TimeService getInstance() {
		return theTimeService;
	}


	public static Bootable getBootable() {
		return new TimeServiceBootable();	

	}

	private static class TimeServiceBootable implements Bootable {
		public void boot(BootContext bootContext)
				throws BootException {

			try {
				theTimeService = new TimeService();
			}
			catch (Exception e) {
				throw new BootException(e);
			}
		}
	}


}


