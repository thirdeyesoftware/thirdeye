package com.iobeam.portal.service.jms.mailer;

import com.iobeam.portal.service.jms.MessageServiceException;

public class MailerServiceException extends MessageServiceException {
	
	public MailerServiceException(String msg, Throwable t) {
		super(msg, t);
	}

	public MailerServiceException(String msg) {
		super(msg);
	}

	public String toString() {
		return "MailerServiceException: " + super.toString();
	}

}

