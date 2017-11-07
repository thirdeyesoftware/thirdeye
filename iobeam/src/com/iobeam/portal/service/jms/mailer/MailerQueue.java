package com.iobeam.portal.service.jms.mailer;

import com.iobeam.portal.service.jms.*;
import javax.jms.*;

public class MailerQueue extends PortalQueue {

	
	private static final MailerQueue cInstance = 
			new MailerQueue();
	
	private MailerQueue() {

		super(PortalQueue.MAILER_QUEUE_PROP);

	}
	
	public static MailerQueue getInstance() {

		return cInstance;

	}
		
	public void send(MailerMessage message)
			throws MailerServiceException {

		try {

			ObjectMessage objectMessage = 
				getQueueSession().createObjectMessage(message);
			getQueueSender().send(objectMessage);
		}
		catch (Exception ee) {
			throw new MailerServiceException(
				"could not send mailer message.", ee);
		}


	}



}

