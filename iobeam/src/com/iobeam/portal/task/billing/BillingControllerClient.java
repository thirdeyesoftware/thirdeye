package com.iobeam.portal.task.billing;

import javax.naming.*;
import javax.ejb.*;
import java.rmi.*;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;

import com.iobeam.portal.util.DataSources;
import com.iobeam.portal.client.*;
import com.iobeam.portal.model.billing.BillingPeriod;

public class BillingControllerClient extends PortalClient {

	public static void main(String[] args) {
		if (args.length < 4) {
			System.out.println(getUsage());
			return;
		}
		
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		
		String hostname;
		String user;
		String password;
		int port;
		Date start = null;
		int step = 0;

		hostname = args[0];
		user = args[2];
		password = args[3];

		try {

			port = Integer.parseInt(args[1]);
			
			BufferedReader br = new BufferedReader(
					new InputStreamReader(System.in));
		
			System.out.println("Please enter a billing step.");
			System.out.println(getSteps());
			
			step = Integer.parseInt(br.readLine());

			if (step < 0 || step > 5) {
				System.out.println(getSteps());
				return;
			}
			BillingPeriod current = BillingPeriod.getInstanceFor(hostname, port, user,
					password, DataSources.PRIMARY_NAME, new Date());

			System.out.println("The current billing period is " + 
				format.format(current.getStartDate()) + " to " + format.format(
					current.getEndDate()));
			
			System.out.println(
				"If you would like to run during the current billing period, press " + 
				" enter.  Otherwise, enter a date within the billing period you wish to " +
				"process.");

			String dt = br.readLine();

			if (dt == null || dt.trim().equals("")) { 
				start = current.getStartDate();
			}
			else {
				start = format.parse(dt);
			}

			if (start == null) {
				System.out.println("could not parse date.");
				return;
			}

			InitialContext ic = getInitialContext(
				hostname, String.valueOf(port), user, password);

			BillingControllerHome home = 	
				(BillingControllerHome)ic.lookup(BillingControllerHome.JNDI_NAME);
			BillingController bc = home.create();
			bc.run(step, start);
		}
		catch (Exception e) {
			e.printStackTrace();
		}


	}

	private static String getUsage() {
		StringBuffer sb = new StringBuffer(0);
		sb.append(
			"usage: BillingControllerClient <host> <port> <user> <password>\n");
		sb.append(getSteps());
		return sb.toString();
	}

	private static String getSteps() {
		StringBuffer sb = new StringBuffer(0);
		sb.append(" step must be 1-5.\n");
		sb.append(" 1 - process accounts\n");
		sb.append(" 2 - create invoices \n");
		sb.append(" 3 - deliver invoices \n");
		sb.append(" 4 - process invoices \n");
		sb.append(" 5 - process statements\n");
		return sb.toString();
	}

}

