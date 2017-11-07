package com.iobeam.portal.task.billing.payment.processautomaticpayment;


import java.util.*;
import java.io.*;
import javax.rmi.PortableRemoteObject;
import javax.naming.*;
import javax.activation.*;
import com.iobeam.portal.util.*;
import com.iobeam.portal.model.billablecustomer.*;
import com.iobeam.portal.model.customer.*;
import com.iobeam.portal.model.billing.*;


/**
*/
public class ProcessAutomaticPaymentClient {

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

		String hostName;
		int port;
		String user;
		String password;
		String command;

		try {
			hostName = args[0];
			port = Integer.parseInt(args[1]);
			user = args[2];
			password = args[3];
			command = args[4];
		}
		catch (Exception e) {
			System.out.println("usage: ProcessAutomaticPaymentClient " +
					"<host> <port> <user> <password> " +
					"{version | process}");

			throw e;
		}

		InitialContext ic = getInitialContext(hostName, port, user, password);

		ProcessAutomaticPaymentHome processAutomaticPaymentHome =
				(ProcessAutomaticPaymentHome)
				PortableRemoteObject.narrow(
				ic.lookup(ProcessAutomaticPaymentHome.JNDI_NAME),
				ProcessAutomaticPaymentHome.class);

		ProcessAutomaticPayment processAutomaticPayment =
				processAutomaticPaymentHome.create();


		BillableCustomerHome billableCustomerHome =
				(BillableCustomerHome)
				PortableRemoteObject.narrow(
				ic.lookup(BillableCustomerHome.JNDI_NAME),
				BillableCustomerHome.class);



		BufferedReader br = new BufferedReader(
				new InputStreamReader(System.in));


		if (command.equals("version")) {

			String version =
					processAutomaticPayment.getTransactionClientVersion();

			System.out.println(version);
		} else
		if (command.equals("process")) {
			System.out.print("BillableCustomer ID>");
			BillableCustomerPK pk = new BillableCustomerPK(Integer.parseInt(
					br.readLine()));

			BillableCustomer bc = billableCustomerHome.findByPrimaryKey(pk);
			CreditCard cc = (CreditCard) bc.getPaymentInstrument();

			System.out.println(cc.toString());

			System.out.print("Amount>");
			Money amount = new Money(Double.parseDouble(br.readLine()));

			ProcessPaymentResponse resp =
					processAutomaticPayment.processAutomaticPayment(cc,
					amount);

			System.out.println(resp);

		} else {
			throw new IllegalArgumentException(command);
		}
	}
}
