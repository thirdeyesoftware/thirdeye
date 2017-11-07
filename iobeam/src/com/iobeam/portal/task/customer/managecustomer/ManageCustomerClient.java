package com.iobeam.portal.task.customer.managecustomer;


import java.util.*;
import java.io.*;
import javax.rmi.PortableRemoteObject;
import javax.naming.*;
import javax.activation.*;

import com.iobeam.portal.util.*;
import com.iobeam.portal.model.country.*;
import com.iobeam.portal.model.venue.*;
import com.iobeam.portal.model.customercontact.*;
import com.iobeam.portal.model.customer.*;
import com.iobeam.portal.model.address.*;


/**
*/
public class ManageCustomerClient {

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
			System.out.println("usage: ManageCustomerClient " +
					"<host> <port> <user> <password> {create | get}");

			throw e;
		}

		InitialContext ic = getInitialContext(hostName, port, user, password);

		ManageCustomerHome manageCustomerHome = (ManageCustomerHome)
				PortableRemoteObject.narrow(
				ic.lookup(ManageCustomerHome.JNDI_NAME),
				ManageCustomerHome.class);

		ManageCustomer manageCustomer = manageCustomerHome.create();


		AccessCountryHome accessCountryHome = (AccessCountryHome)
				PortableRemoteObject.narrow(
				ic.lookup(AccessCountryHome.JNDI_NAME),
				AccessCountryHome.class);

		AccessCountry accessCountry = accessCountryHome.create();

		CustomerHome customerHome = (CustomerHome)
				PortableRemoteObject.narrow(
				ic.lookup(CustomerHome.JNDI_NAME),
				CustomerHome.class);



		BufferedReader br = new BufferedReader(
				new InputStreamReader(System.in));


		if (command.equals("create")) {
			System.out.print("Contact First Name>");
			String firstName = br.readLine();

			System.out.print("Contact Last Name>");
			String lastName = br.readLine();

			System.out.print("Contact Middle Initial>");
			String mi = br.readLine();

			ContactName contactName = new ContactName(firstName, mi, lastName);

			System.out.println(contactName);


			System.out.print("Contact Address Line 1>");
			String line1 = br.readLine();

			System.out.print("Contact Address Line 2>");
			String line2 = br.readLine();

			System.out.print("Contact City>");
			String city = br.readLine();

			System.out.print("Contact State>");
			String state = br.readLine();

			System.out.print("Contact Zipcode>");
			String zipcode = br.readLine();

			MailingAddress mailingAddress = new MailingAddress(line1, line2,
					city, state, zipcode);

			System.out.println(mailingAddress);


			System.out.print("Contact Phone>");
			String phone = br.readLine();

			System.out.print("Contact Fax>");
			String fax = br.readLine();

			System.out.print("Contact Email>");
			String email = br.readLine();

			List countries = new ArrayList(accessCountry.findAll());
			for (int i = 0; i < countries.size(); ++i) {
				System.out.println("" + i + " " + countries.get(i));
			}

			System.out.print("Contact Country>");
			int countryIndex = Integer.parseInt(br.readLine());
			Country country = (Country) countries.get(countryIndex);

			Address address = new Address(mailingAddress, country);

			System.out.println(address);


			CustomerContact customerContact = new CustomerContact(contactName,
					address, phone, fax, email);

			System.out.println(customerContact);


			Customer customer = manageCustomer.createCustomer(customerContact);

			System.out.println(customer.getData());
		} else
		if (command.equals("get")) {
			System.out.print("Customer ID>");
			CustomerPK pk = new CustomerPK(Integer.parseInt(br.readLine()));

			Customer customer = customerHome.findByPrimaryKey(pk);

			System.out.println(customer.getData());
		}
	}
}
