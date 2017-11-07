package com.iobeam.portal.service.jms.mailer;

import javax.ejb.*;
import javax.jms.*;
import javax.naming.*;
import java.util.logging.Logger;
import javax.mail.*;
import javax.mail.internet.*;

public class MailerServiceBean implements MessageDrivenBean, MessageListener {

	private static final String MAIL_SESSION_JNDI_NAME =
		"iobeam.portal.MailSession";
	
	private MessageDrivenContext mContext;

	public void setMessageDrivenContext(MessageDrivenContext ctx) {
		mContext = ctx;
	}

	public void ejbCreate() throws CreateException {

	}

	public void ejbRemove() {
	}

	public void onMessage(javax.jms.Message msg) {
		try {
			if (msg instanceof ObjectMessage) {
				Object target = ((ObjectMessage)msg).getObject();
				MailerMessage mailMessage = (MailerMessage)target;
				dispatch(mailMessage);
			}
		}
		catch (Exception ee) {
			Logger.getLogger(
				"com.iobeam.portal.service.jms.mailer").throwing(
					MailerServiceBean.class.getName(), "onMessage()",
					(Throwable)ee);
		}

	}

	/** 
	 * send email for specified mail message.
	 */
	private void dispatch(MailerMessage msg) throws Exception {
		InitialContext ic = new InitialContext();

		javax.mail.Session session = 
			(javax.mail.Session)ic.lookup(MAIL_SESSION_JNDI_NAME);

		Address[] recipients = new Address[1];

		recipients[0] = new InternetAddress(msg.getRecipientAddress());

		MimeMessage message = new MimeMessage(session);

		message.setFrom(
			new InternetAddress(msg.getSenderAddress()));

		message.setRecipients(javax.mail.Message.RecipientType.TO, recipients);

		message.setSubject(msg.getSubject());

		message.setContent(msg.getBody(), "text/plain");
		
		Transport.send(message);


	}

}



