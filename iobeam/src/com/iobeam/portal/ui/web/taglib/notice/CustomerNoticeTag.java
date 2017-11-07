package com.iobeam.portal.ui.web.taglib.notice;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import java.util.*;
import javax.ejb.FinderException;
import java.rmi.RemoteException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import javax.naming.*;

import com.iobeam.portal.model.billing.BillingPeriod;
import com.iobeam.portal.model.customernotice.*;
import com.iobeam.portal.task.actor.user.usersession.UserSession;
import com.iobeam.portal.ui.web.user.UserSessionHelper;

/**
 * this taglib displays customer notices in a 
 * table, one notice per row.
 * @styleClass - optional parameter determines css style class to use when
 * rendering customer notices.
 */
public class CustomerNoticeTag extends TagSupport {

	private String mStyleClass;

	
	private static final String DEFAULT_STYLE_CLASS = "newsbody";

	public void setStyleClass(String s) {
		mStyleClass = s;
	}
	public String getStyleClass() {
		return mStyleClass;
	}

	/**
	 * renders current billing period based on system date.
	 */
	public int doStartTag() throws JspException {

		StringBuffer results = new StringBuffer(0);
		
		AccessCustomerNotice acn = getAccessCustomerNotice();
		try {
			Collection allNotices = acn.findActiveAllVenues();

			UserSession us =
					UserSessionHelper.getUserSession(pageContext.getSession());
			
			
			results.append("<TABLE border=0 cellpadding=0 cellspacing=0>\n");
			Iterator it;
			for (it = allNotices.iterator(); it.hasNext(); ) {
				CustomerNotice notice = (CustomerNotice)it.next();	
				addNotice(results, notice);
			}

			if (!us.isPortalSession()) {
				Collection venueNotices = acn.findActiveByVenueId(
						us.getVenue().getPK().getID());
				for (it = venueNotices.iterator(); it.hasNext(); ) {
					CustomerNotice notice = (CustomerNotice)it.next();	
					addNotice(results, notice);
				}
			}

			results.append("</table>\n");

			write(pageContext, results.toString());
					
			return EVAL_BODY_INCLUDE;
		}
		catch (FinderException fe) {
			throw new JspException(fe);
		}
		catch (RemoteException re) {
			throw new JspException(re);
		}
	}


	private void addNotice(StringBuffer sb, CustomerNotice notice) {
		String style = getStyleClass();
		if (style == null) {
			style = DEFAULT_STYLE_CLASS;
		}

		sb.append("<TR><TD align=\"left\">");
		sb.append("<font class=\"").append(style).append("\">");
		sb.append(notice.getNotice());
		sb.append("</font>");
		sb.append("</TD></TR>\n");
	}

	private void write(PageContext context, String buffer) 
			throws JspException {
		try {
			JspWriter writer = context.getOut();
			writer.print(buffer);
		}
		catch (IOException ioe) {
			throw new JspException(ioe);
		}

	}

	private AccessCustomerNotice getAccessCustomerNotice() throws JspException {
		try {
			InitialContext ic = new InitialContext();
			AccessCustomerNoticeHome home =
				(AccessCustomerNoticeHome)ic.lookup(AccessCustomerNoticeHome.JNDI_NAME);
			return home.create();
		}
		catch (Exception e) {
			throw new JspException(e);
		}
	}

}



