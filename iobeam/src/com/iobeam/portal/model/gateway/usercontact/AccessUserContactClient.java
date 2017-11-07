package com.iobeam.portal.model.gateway.usercontact;

import java.util.*;
import javax.ejb.*;
import javax.naming.*;
import javax.rmi.*;
import java.text.SimpleDateFormat;
import com.iobeam.portal.model.venue.VenuePK;
import javax.activation.*;

public class AccessUserContactClient {

	public static void main(String[] args) {

		if (args.length < 6) {
			System.out.println(getUsage());
			System.exit(1);
		}

		printContactByVenue(args[0], args[1], 
			args[2], args[3], args[4], args[5]);

	}

	private static String getUsage() {
		return "AccessUserContactClient <url> <user> <password> " + 
			"<venueid> <startdate> <enddate>";
	}

	private static void printContactByVenue(String url, String user, 
			String password, String venueid, String startdate, String enddate) {
		
		try {
			InitialContext ic = getInitialContext(url, user, password);
			AccessUserContactHome home = 
				(AccessUserContactHome)PortableRemoteObject.narrow(
					ic.lookup(AccessUserContactHome.JNDI_NAME), 
					AccessUserContactHome.class);
			AccessUserContact auc = home.create();
			
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

			Collection c = auc.findAllByVenuePKByDate(
					new VenuePK(Integer.parseInt(venueid)), 
					sdf.parse(startdate), sdf.parse(enddate));

			for (Iterator it = c.iterator(); it.hasNext();) {
				UserContact contact = (UserContact)it.next();	
				System.out.println(
					sdf.format(contact.getTimestamp()) + "\t" + 
					contact.getUserMACAddress().toString() + "\t" + 
					contact.getUserIPAddress().toString() + "\t" + 
					contact.getContactID().getID());
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private static InitialContext getInitialContext(String url,
			String user, String password)
			throws Exception {

		System.out.println("user = " + user);
		System.out.println("password = " + password);
		System.out.println("url = " + url);

		Properties p = new Properties();
		p.setProperty(Context.INITIAL_CONTEXT_FACTORY,
				"weblogic.jndi.WLInitialContextFactory");
		p.setProperty(Context.PROVIDER_URL, url);

		p.setProperty("java.naming.security.principal", user);
		p.setProperty("java.naming.security.credentials", password);

		InitialContext ic = new InitialContext(p);

		return ic;
	}



}


