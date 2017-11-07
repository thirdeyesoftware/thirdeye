package com.iobeam.portal.service.jms;

import javax.jms.*;
import javax.naming.*;
import javax.ejb.ObjectNotFoundException;

import com.iobeam.portal.service.jms.mailer.MailerQueue;

/**
 * factory that creates and maintains queue
 * senders.
 */
public class QueueFactory {

	public static final String MAILER_QUEUE_PROP = 
		"iobeam.portal.jms.mailer";
	
	public static final String PRINTER_QUEUE_PROP = 
		"iobeam.portal.jms.printer";

	public static MailerQueue getMailerQueue() {
		return MailerQueue.getInstance();
	}

	//public PrinterQueue getPrinterQueue() {
	//		return PrinterQueue.getInstance();
	//}

}

