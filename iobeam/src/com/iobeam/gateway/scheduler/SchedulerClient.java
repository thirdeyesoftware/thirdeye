package com.iobeam.gateway.scheduler;


import java.net.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import java.text.*;


public class SchedulerClient {

	private static final DateFormat cDateFormat = new SimpleDateFormat(
			"MM/dd/yy HH:mm:ss");

	private SchedulerClient() {
	}


	private static final class TestTask implements ScheduledTask,
			java.io.Serializable {

		public void doTask() {
			System.out.println("TestTask.doTask:");
		}
	}


	public static void main(String[] args) throws Exception {
		if (args.length != 3) {
			System.out.println(
					"Usage: SchedulerClient <host> <port> <mm/dd/yy hh:mm:ss");
		}


		int port = Integer.parseInt(args[1]);

		Date trigger = cDateFormat.parse(args[2]);

		ScheduledEvent se = new ScheduledEvent(trigger, new TestTask());

		Socket s = new Socket(args[0], port);

		ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());

		oos.writeObject(se);

		s.close();
	}
}
