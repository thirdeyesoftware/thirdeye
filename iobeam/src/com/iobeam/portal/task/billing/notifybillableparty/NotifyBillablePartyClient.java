package com.iobeam.portal.task.billing.notifybillableparty;

import com.iobeam.portal.client.PortalClient;
import com.iobeam.portal.model.account.*;
import com.iobeam.portal.model.billing.*;
import com.iobeam.portal.util.Money;

import javax.ejb.*;
import javax.naming.*;

public class NotifyBillablePartyClient {

	public static final void main(String[] args) {
		if (args.length < 1) {
			System.out.println(getUsage());
			return;
		}
		try {
			InitialContext ic = PortalClient.getInitialContext();
			NotifyBillablePartyHome home = 
				(NotifyBillablePartyHome)ic.lookup(
					NotifyBillablePartyHome.JNDI_NAME);
			NotifyBillableParty nbp = home.create();
		}
		catch (Exception ee) {
			System.out.println(ee.toString());
			ee.printStackTrace();
		}
		System.out.println("\ndone.");

	}

	private static String getUsage() {
		StringBuffer sb = new StringBuffer(0);
		sb.append("NotifyBillableParty <invoiceid>");
		sb.append("\n");
		sb.append(" only single invoices are supported through this interface.");
		return sb.toString();
	}
}

