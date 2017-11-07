package com.iobeam.portal.task.customer.managevenue;


import java.util.*;
import java.io.*;
import java.net.InetAddress;
import javax.rmi.PortableRemoteObject;
import javax.naming.*;
import javax.activation.*;

import com.iobeam.portal.util.*;
import com.iobeam.portal.model.country.*;
import com.iobeam.portal.model.venue.*;
import com.iobeam.portal.model.gateway.*;
import com.iobeam.portal.model.customercontact.*;
import com.iobeam.portal.model.address.*;
import com.iobeam.portal.model.subscription.*;
import com.iobeam.portal.task.customer.managevenue.*;
import com.iobeam.portal.task.actor.gateway.register.*;
import com.iobeam.util.MACAddress;


/**
*/
public class ManageVenueClient {

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
			System.out.println("usage: ManageVenueClient " +
					"<host> <port> <user> <password> " +
					"{create | get | remove | generate | list | install | register}");

			throw e;
		}

		InitialContext ic = getInitialContext(hostName, port, user, password);

		ManageVenueHome manageVenueHome = (ManageVenueHome)
				PortableRemoteObject.narrow(
				ic.lookup(ManageVenueHome.JNDI_NAME),
				ManageVenueHome.class);

		ManageVenue manageVenue = manageVenueHome.create();


		AccessCountryHome accessCountryHome = (AccessCountryHome)
				PortableRemoteObject.narrow(
				ic.lookup(AccessCountryHome.JNDI_NAME),
				AccessCountryHome.class);

		AccessCountry accessCountry = accessCountryHome.create();

		AccessVenueHome accessVenueHome = (AccessVenueHome)
				PortableRemoteObject.narrow(
				ic.lookup(AccessVenueHome.JNDI_NAME),
				AccessVenueHome.class);

		AccessVenue accessVenue = accessVenueHome.create();

		AccessSubscriptionHome accessSubscriptionHome = (AccessSubscriptionHome)
				PortableRemoteObject.narrow(
				ic.lookup(AccessSubscriptionHome.JNDI_NAME),
				AccessSubscriptionHome.class);

		AccessSubscription accessSubscription =
				accessSubscriptionHome.create();


		AccessGatewayHome accessGatewayHome = (AccessGatewayHome)
				PortableRemoteObject.narrow(
				ic.lookup(AccessGatewayHome.JNDI_NAME),
				AccessGatewayHome.class);

		AccessGateway accessGateway = accessGatewayHome.create();


		RegisterGatewayHome registerGatewayHome = (RegisterGatewayHome)
				PortableRemoteObject.narrow(
				ic.lookup(RegisterGatewayHome.JNDI_NAME),
				RegisterGatewayHome.class);

		RegisterGateway registerGateway = registerGatewayHome.create();


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


			System.out.print("Venue Name>");
			String venueName = br.readLine();

			List venueTypes = VenueType.getVenueTypes();
			for (int i = 0; i < venueTypes.size(); ++i) {
				System.out.println("" + i + " " + venueTypes.get(i));
			}

			System.out.print("Venue Type>");
			int venueTypeIndex = Integer.parseInt(br.readLine());
			VenueType venueType = (VenueType) venueTypes.get(venueTypeIndex);


			Venue venue = manageVenue.createVenue(
					venueName, venueType, customerContact);

			System.out.println(venue);
		} else
		if (command.equals("get")) {
			System.out.print("Subscription or Venue? [s/v]>");

			String userLine = br.readLine();

			if ("v".equalsIgnoreCase(userLine)) {
				System.out.print("Venue ID>");
				VenuePK pk = new VenuePK(Integer.parseInt(br.readLine()));

				Venue venue = accessVenue.findByPrimaryKey(pk);

				System.out.println(venue);
			} else
			if ("s".equalsIgnoreCase(userLine)) {
				System.out.print("Subscription ID>");
				SubscriptionPK pk = new SubscriptionPK(
						Integer.parseInt(br.readLine()));

				Subscription subscription =
						accessSubscription.findByPrimaryKey(pk);

				System.out.println(subscription);
			} else {
				throw new IllegalArgumentException(userLine);
			}
		} else
		if (command.equals("remove")) {

			Map nameMap = manageVenue.getVenueNameMap();
			List venueList = new ArrayList(nameMap.entrySet());
			for (int i = 0; i < venueList.size(); ++i) {
				System.out.println("" + i + " " + venueList.get(i));
			}

			System.out.print("Venue Index>");
			int venueIndex = Integer.parseInt(br.readLine());
			Map.Entry mapEntry = (Map.Entry) venueList.get(venueIndex);

			VenuePK pk = (VenuePK) mapEntry.getValue();
			manageVenue.removeVenue(pk);
		} else
		if (command.equals("generate")) {
			System.out.print("Subscription ID>");
			SubscriptionPK subscriptionPK = new SubscriptionPK(
					Integer.parseInt(br.readLine()));

			System.out.print("count>");
			int count = Integer.parseInt(br.readLine());

			manageVenue.generateSubscriptions(subscriptionPK, count);
		} else
		if (command.equals("list")) {
			VenuePK venuePK;
			SubscriptionType subscriptionType;

			ArrayList subscriptionTypes = new ArrayList(
					SubscriptionType.getInstances());

			ArrayList opts = new ArrayList();
			opts.add("Venues");
			opts.add("Active Subscriptions");
			opts.add("Created Subscriptions");

			for (int i = 0; i < opts.size(); ++i) {
				System.out.println("" + i + " " + opts.get(i));
			}

			System.out.print("list option>");
			int optionIndex = Integer.parseInt(br.readLine());

			switch (optionIndex) {
			case 0:
				Map nameMap = manageVenue.getVenueNameMap();
				List venueList = new ArrayList(nameMap.entrySet());
				for (int i = 0; i < venueList.size(); ++i) {
					System.out.println("" + i + " " + venueList.get(i));
				}
				break;
			case 1:
				System.out.print("Venue ID>");
				venuePK = new VenuePK(Integer.parseInt(br.readLine()));

				for (int i = 0; i < subscriptionTypes.size(); ++i) {
					System.out.println("" + i + " " + subscriptionTypes.get(i));
				}
				System.out.print("Subscription Type>");
				subscriptionType = (SubscriptionType) subscriptionTypes.get(
						Integer.parseInt(br.readLine()));

				Collection activeSubscriptions =
						manageVenue.getActiveSubscriptions(venuePK,
						subscriptionType);

				for (Iterator it = activeSubscriptions.iterator();
						it.hasNext(); ) {
					System.out.println(it.next());
				}
				break;
			case 2:
				System.out.print("Venue ID>");
				venuePK = new VenuePK(Integer.parseInt(br.readLine()));

				for (int i = 0; i < subscriptionTypes.size(); ++i) {
					System.out.println("" + i + " " + subscriptionTypes.get(i));
				}
				System.out.print("Subscription Type>");
				subscriptionType = (SubscriptionType) subscriptionTypes.get(
						Integer.parseInt(br.readLine()));

				System.out.print("SecureID only? [y/n]>");
				boolean listSecureIDOnly = "y".equalsIgnoreCase(br.readLine());

				Collection createdSubscriptions =
						manageVenue.getCreatedSubscriptions(venuePK,
						subscriptionType);

				for (Iterator it = createdSubscriptions.iterator();
						it.hasNext(); ) {
					Subscription createdSub = (Subscription) it.next();
					if (listSecureIDOnly) {
						System.out.println(createdSub.getSecureID());
					} else {
						System.out.println(createdSub);
					}
				}
				break;
			}
		} else
		if (command.equals("install")) {
			System.out.print("Venue ID>");
			VenuePK pk = new VenuePK(Integer.parseInt(br.readLine()));

			Venue venue = accessVenue.findByPrimaryKey(pk);

			System.out.println(venue);

			System.out.print("Gateway backhaul MAC>");
			MACAddress mac = new MACAddress(br.readLine());
			Gateway gateway = new Gateway(pk, mac);

			manageVenue.installGateway(gateway);

			gateway = accessGateway.findByVenuePK(pk);

			System.out.println(gateway);
		} else
		if (command.equals("register")) {
			System.out.print("Venue ID>");
			VenuePK pk = new VenuePK(Integer.parseInt(br.readLine()));

			Venue venue = accessVenue.findByPrimaryKey(pk);

			System.out.println(venue);

			Gateway gateway = accessGateway.findByVenuePK(pk);
			System.out.println(gateway);

			System.out.print("Gateway public IP>");
			InetAddress publicIP = InetAddress.getByName(br.readLine());
			System.out.print("Gateway notify port>");
			int notifyPort = Integer.parseInt(br.readLine());
			System.out.print("Gateway private IP>");
			InetAddress privateIP = InetAddress.getByName(br.readLine());

			registerGateway.register(pk, gateway.getMACAddress(),
					publicIP, notifyPort, privateIP);

			gateway = accessGateway.findByVenuePK(pk);

			System.out.println(gateway);
		}
	}
}
