package com.iobeam.portal.service.jms;

import javax.jms.*;
import javax.ejb.EJBException;
import javax.naming.*;
import weblogic.jms.extensions.WLSession;

public class PortalQueue {
	
	private QueueConnectionFactory mConnectionFactory = null;
	private QueueConnection mQueueConnection = null;
	private QueueSession mSession = null;
	private QueueSender mSender = null;

	public static final String MAILER_QUEUE_PROP = 
		"iobeam.portal.jms.mailer";
	public static final String PRINTER_QUEUE_PROP =
		"iobeam.portal.jms.printer";

	public static final String CONNECTION_FACTORY_PROP = 
		"iobeam.portal.jms.connectionfactory";

	protected PortalQueue(String queueProperty) {
		
		String queueName  = System.getProperty(queueProperty);
		String connFactoryName  = System.getProperty(CONNECTION_FACTORY_PROP);
		InitialContext ic = null;
		try {
			ic = new InitialContext();
			mConnectionFactory = 
				(QueueConnectionFactory)ic.lookup(connFactoryName);
			mQueueConnection = mConnectionFactory.createQueueConnection();

			mSession = mQueueConnection.createQueueSession(false,
				WLSession.NO_ACKNOWLEDGE);

			Queue queue = (Queue)ic.lookup(queueName);
			mSender = mSession.createSender(queue);
		}
		catch (Exception ee) {
			throw new EJBException("could not create queue.", ee);
		}
	}

	protected QueueSession getQueueSession() {
		return mSession;
	}

 	protected QueueSender getQueueSender() {
		return mSender;
	}

		
}

