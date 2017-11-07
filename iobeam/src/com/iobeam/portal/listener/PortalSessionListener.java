package com.iobeam.portal.listener;

import javax.servlet.http.*;
import javax.servlet.*;
import java.util.logging.Logger;

public final class PortalSessionListener 
		implements HttpSessionListener {

	public void sessionCreated(HttpSessionEvent event) {
		Logger l = Logger.getLogger("com.iobeam.portal.listener");
		l.info("PortalSessionListener.sessionCreated()");
		l.info("PortaSessionListener.sessionId=" + event.getSession().getId());
		l.info("PortalSessionListener.hasUseSession?" +
			(event.getSession().getAttribute("userSessionHandle") != null));


	}

	public void sessionDestroyed(HttpSessionEvent event) {
		Logger l = Logger.getLogger("com.iobeam.portal.listener");
		l.info("PortalSessionListener.sessionDestroyed()");
		l.info("PortalSessionListener.sessionId=" + event.getSession().getId());
	}

}

