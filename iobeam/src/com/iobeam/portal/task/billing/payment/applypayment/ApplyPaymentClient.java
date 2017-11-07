package com.iobeam.portal.task.billing.payment.applypayment;

import com.iobeam.portal.client.PortalClient;
import com.iobeam.portal.model.account.*;
import com.iobeam.portal.model.billing.*;
import com.iobeam.portal.util.Money;

import javax.ejb.*;
import javax.naming.*;

public class ApplyPaymentClient {

	public static final void main(String[] args) {
		if (args.length < 3) {
			System.out.println(getUsage());
			return;
		}
		try {
			InitialContext ic = PortalClient.getInitialContext();

			AccountPK pk = new AccountPK(Long.parseLong(args[0]));
			Check check = new Check(args[1]);
			Money money = new Money(Double.parseDouble(args[2]));
			ApplyPaymentHome apHome = 
				(ApplyPaymentHome)ic.lookup(ApplyPaymentHome.JNDI_NAME);
			ApplyPayment ap = apHome.create();
			ap.applyPayment(pk, check, money);
		}
		catch (Exception ee) {
			System.out.println(ee.toString());
			ee.printStackTrace();
		}
		System.out.println("\ndone.");

	}

	private static String getUsage() {
		StringBuffer sb = new StringBuffer(0);
		sb.append("ApplyPayment <accountid> <checknumber> <amount>");
		sb.append("\n");
		sb.append(" only checks are supported through this interface.");
		return sb.toString();
	}
}

