package com.iobeam.portal.task.billing.notifybillableparty;

import com.iobeam.portal.service.jms.mailer.MailerMessage;
import com.iobeam.portal.service.jms.mailer.MailerQueue;
import com.iobeam.portal.service.jms.QueueFactory;

import java.util.Collection;
import java.util.Vector;
import java.io.Serializable;

public class BillingMessage implements MailerMessage, Serializable  {

	private String mRecipient;
	private String mSubject;
	private String mBody;
	private Collection mAttachments;

	private static final String BILLING_EMAIL_PROP = 	
		"iobeam.portal.billing.emailaddress";

	public BillingMessage(String recipient, 
		String subject, 
		String body,
		Collection attachments) {

		mRecipient = recipient;
		mSubject = subject;
		mBody = body;
		mAttachments = attachments;
	}

	public BillingMessage(String recipient,
			String subject,
			String body) {
		this(recipient, subject, body, new Vector());
	}

	public String getSenderAddress() {

		return System.getProperty(BILLING_EMAIL_PROP);

	}

	public String getRecipientAddress() {
		return mRecipient;
	}

	public String getSubject() {
		return mSubject;
	}

	public Object getBody() {
		return mBody;
	}

	public Collection getAttachments() {
		return mAttachments;
	}

	public int hashCode() {
		int h = 17;
		h = (h * 37) * mBody.hashCode();
		return h;
	}

}
		
