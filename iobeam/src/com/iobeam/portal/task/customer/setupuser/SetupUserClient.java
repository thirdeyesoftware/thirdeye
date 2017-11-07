package com.iobeam.portal.task.customer.setupuser;


import java.text.*;
import java.util.*;
import java.io.*;
import javax.rmi.PortableRemoteObject;
import javax.naming.*;
import javax.activation.*;

import com.iobeam.portal.util.*;
import com.iobeam.portal.model.country.*;
import com.iobeam.portal.model.user.*;
import com.iobeam.portal.security.*;
import com.iobeam.portal.model.billing.*;
import com.iobeam.portal.model.customercontact.*;
import com.iobeam.portal.model.address.*;
import com.iobeam.portal.model.prototype.subscription.*;


/**
*/
public class SetupUserClient {

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
		String mode;
		String command;

		try {
			hostName = args[0];
			port = Integer.parseInt(args[1]);
			user = args[2];
			password = args[3];
			mode = args[4];
			command = args[5];
		}
		catch (Exception e) {
			System.out.println("usage: SetupUserClient " +
					"<host> <port> <user> <password> " +
					"{public | private} {proto | create}");

			throw e;
		}

		InitialContext ic = getInitialContext(hostName, port, user, password);

		SetupUserHome setupUserHome = (SetupUserHome)
				PortableRemoteObject.narrow(
				ic.lookup(SetupUserHome.JNDI_NAME),
				SetupUserHome.class);

		SetupUser setupUser = setupUserHome.create();


		AccessCountryHome accessCountryHome = (AccessCountryHome)
				PortableRemoteObject.narrow(
				ic.lookup(AccessCountryHome.JNDI_NAME),
				AccessCountryHome.class);

		AccessCountry accessCountry = accessCountryHome.create();



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


			System.out.print("User Name>");
			String userName = br.readLine();

			System.out.print("Password>");
			String userPassword = br.readLine();

			System.out.println("What city were you born in? (Used for password " + 
					"reminder)");
			String answer = br.readLine();

			User u = null;

			if (mode.equals("public")) {

				List paymentInstrumentTypes = new ArrayList(
						PaymentInstrumentType.getInstances());
				for (int i = 0; i < paymentInstrumentTypes.size(); ++i) {
					System.out.println("" + i + " " +
					paymentInstrumentTypes.get(i));
				}


				System.out.print("Payment Instrument Type>");
				int piIndex = Integer.parseInt(br.readLine());
				PaymentInstrumentType piType = (PaymentInstrumentType)
						paymentInstrumentTypes.get(piIndex);

				AutomaticPaymentInstrument paymentInstrument = null;

				if (piType.equals(PaymentInstrumentType.CREDIT_CARD)) {
					System.out.print("Card Number>");
					String cardNumber = br.readLine();
					System.out.print("Security Code>");
					String securityCode = br.readLine();
					System.out.print("CardHolder Name>");
					String cardHolder = br.readLine();

					System.out.print("Billing Address Line 1>");
					String bline1 = br.readLine();

					System.out.print("Billing Address Line 2>");
					String bline2 = br.readLine();

					System.out.print("Billing City>");
					String bcity = br.readLine();

					System.out.print("Billing State>");
					String bstate = br.readLine();

					System.out.print("Billing Zipcode>");
					String bzipcode = br.readLine();

					MailingAddress billingMailingAddress = new MailingAddress(
							bline1, bline2, bcity, bstate, bzipcode);
					Address billingAddress = new Address(billingMailingAddress,
							country);

					System.out.println(billingAddress);

					System.out.print("Expiration Date [mm/dd/yy]>");
					SimpleDateFormat df = new SimpleDateFormat("MM/dd/yy");
					Date expirationDate = df.parse(br.readLine());

					paymentInstrument = new CreditCard(cardNumber,
							securityCode, cardHolder, billingAddress,
							expirationDate);

					System.out.println(paymentInstrument.toString());
				} else
				if (piType.equals(PaymentInstrumentType.ELECTRONIC_CHECK)) {
					throw new UnsupportedOperationException("electronic check");
				} else
				if (piType.equals(PaymentInstrumentType.ACH_TRANSFER)) {
					throw new UnsupportedOperationException("ach transfer");
				} else
				if (piType.equals(PaymentInstrumentType.CHECK)) {
					throw new UnsupportedOperationException(
							"automatic payment instrument only");
				} else
				if (piType.equals(PaymentInstrumentType.UNKNOWN)) {
					throw new UnsupportedOperationException(
							"unknown instrument");
				} else {
					throw new UnsupportedOperationException("unknown " +
							"PaymentInstrumentType " + piType);
				}



				Collection protos;
				protos = setupUser.getPublicMemberSubscriptionPrototypes();

				List memberPrototypes = new ArrayList(protos);
				for (int i = 0; i < memberPrototypes.size(); ++i) {
					System.out.println("" + i + " " + memberPrototypes.get(i));
				}

				System.out.print("Subscription Protoype>");
				int prototypeIndex = Integer.parseInt(br.readLine());
				SubscriptionPrototype proto = (SubscriptionPrototype)
						memberPrototypes.get(prototypeIndex);

				u = setupUser.setupPublicMember(userName,
						userPassword.toCharArray(), answer,
						customerContact, paymentInstrument,
						(PublicMemberSubscriptionPrototype) proto);

			} else
			if (mode.equals("private")) {
				System.out.print("Subscription SecureID>");
				String secureID = br.readLine();
				
				u = setupUser.setupPrivateMember(userName,
						userPassword.toCharArray(), answer,
						customerContact, secureID);
			} else {
				throw new Exception("unknown mode: " + mode);
			}

			System.out.println(u);
		} else
		if (command.equals("proto")) {
			Collection c = null;

			if (mode.equals("private")) {
				throw new UnsupportedOperationException("not impl yet");
			} else
			if (mode.equals("public")) {
				c = setupUser.getPublicMemberSubscriptionPrototypes();
			} else {
				throw new IllegalArgumentException("unknown mode " + mode);
			}

			for (Iterator it = c.iterator(); it.hasNext(); ) {
				System.out.println(it.next());
			}
		}
	}
}
