package com.iobeam.portal.service.jms.mailer;

import com.iobeam.portal.client.PortalClient;
import com.iobeam.portal.service.jms.*;
import com.iobeam.portal.task.billing.notifybillableparty.BillingMessage;

import javax.jms.*;
import javax.ejb.*;

public class MailerServiceClient {

	public static void main(String[] args) throws Exception {

		if (args.length < 3) {
			System.out.println(getUsage());
			return;
		}
		MailerQueue queue = QueueFactory.getMailerQueue();
		BillingMessage msg = new BillingMessage(
			args[0], args[1], args[2]);
		queue.send(msg);

	}

	private static String getUsage() {
		return "MailerServiceClient <recipient> <subject> <body>";
	}

}


