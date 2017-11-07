package com.iobeam.portal.util;

import java.sql.*;
import javax.naming.*;
import javax.ejb.*;
import java.util.*;
import javax.rmi.*;
import java.rmi.*;
import java.io.*;
import java.net.*;
import com.iobeam.portal.task.actor.user.signon.*;
import com.iobeam.portal.task.actor.user.usersession.*;

public class OracleTest {
		static String hostName;
		static int port;
		static String user;
		static String password;
		static String mode;
		static String command;
	
	private static InitialContext getInitialContext(String hostName,
			int port, String user, String password)
			throws Exception {

		Properties p = new Properties();
		p.setProperty(Context.INITIAL_CONTEXT_FACTORY,
				"weblogic.jndi.WLInitialContextFactory");
		p.setProperty(Context.PROVIDER_URL, "t3://" + hostName + ":" + port);

		p.setProperty("java.naming.security.principal", user);
		p.setProperty("java.naming.security.credentials", password);

		InitialContext ic = new InitialContext(p);

		return ic;
	}

	public static void main(String[] args) throws Exception {
		int iterations = 0;

		try {
			hostName = args[0];
			port = Integer.parseInt(args[1]);
			user = args[2];
			password = args[3];
			iterations = Integer.parseInt(args[4]);
		}
		catch (Exception e) {
			System.out.println("usage: SetupUserClient " +
					"<host> <port> <user> <password> " +
					"<# of threads>");

			throw e;
		}
	
		for (int i = 0; i < iterations; i++) {
			TestThread t = new TestThread("iobeamDS");
			System.out.println("starting #" + i);
			t.start();
		}
		
		for (int i = 0; i < iterations; i++) {
			TestThread t = new TestThread("jmmDS");
			System.out.println("starting #" + i);
			t.start();
		}


	}

	static class TestThread extends Thread {
		private String mName;

		public TestThread(String name) {
			mName = name;
		}

		public void run() {
			try {

				/*InitialContext ic = getInitialContext(hostName, port, user, password);
				UserSessionHome home =
					(UserSessionHome)ic.lookup(UserSessionHome.JNDI_NAME);
				UserSession us = home.create();
				SignonUserHome suhome = (SignonUserHome)
						ic.lookup(SignonUserHome.JNDI_NAME);
				SignonUser su = suhome.create();
				su.signOnPortalUser(us, "jeffblau","iobeam".toCharArray());
				*/

				/*Connection c = 
					DBHelper.getConnection(hostName, port, user, password, mName);
				PreparedStatement ps = c.prepareStatement("select * from users");
				ResultSet rs = ps.executeQuery();
				*/

				URL url = new URL("http://www.iobeam.com:7001/loadtest");
				URLConnection connection = url.openConnection();
				connection.connect();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(connection.getInputStream()));
					String result = reader.readLine();

				System.out.println("finished #" + mName);
			}
			catch (Exception ee) {
				ee.printStackTrace();
			}
		}
	}

}

	
