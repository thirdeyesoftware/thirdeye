package com.iobeam.portal.service.jms.mailer;

import java.util.Collection;
import java.io.Serializable;

public interface MailerMessage extends Serializable {
	
	public Object getBody();

	public Collection getAttachments();

	public String getSubject();

	public String getSenderAddress();

	public String getRecipientAddress();

	abstract int hashCode();



}

