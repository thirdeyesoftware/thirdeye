package com.iobeam.portal.ui.web.user.contact;

import org.apache.struts.action.*;

import java.text.SimpleDateFormat;
import java.util.logging.Logger;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.ejb.*;
import javax.naming.*;
import java.rmi.*;
import java.net.*;
import java.text.SimpleDateFormat;

import com.iobeam.portal.model.gateway.usercontact.*;
import com.iobeam.portal.model.user.*;
import com.iobeam.portal.ui.web.user.UserSessionHelper;

import com.iobeam.portal.task.actor.user.usersession.UserSession;

/**
 * ui controller retrieves user usage specified by the following parameters:
 *@parameter userid - user id to retrieve usage 
 *@parameter startdate - start date to retrieve usage 
 *@parameter enddate - end date to retrieve usage
 */

public class GetUserContactAction extends Action {

	public ActionForward perform(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Logger logger = Logger.getLogger("com.iobeam.portal.ui.web.user.contact");
		
		ActionErrors errors = new ActionErrors();

		long userid = 0;
		Date startdate = null;
		Date enddate = null;
		try {
			UserSession usersession = 
				UserSessionHelper.getUserSession(request.getSession());
			if (usersession == null) {
				return mapping.findForward("portalsignon");
			}

			userid = Long.parseLong(request.getParameter("userid"));

			/* ensure requested userid is the currently logged in user */
			if (userid != UserSessionHelper.getUserSession(
					request.getSession()).getUser().getUserPK().getID()) {
				errors.add("application", new ActionError("app.exception"));
				saveErrors(request, errors);
				return mapping.findForward("error");
			}
			
			GregorianCalendar calendar = (GregorianCalendar)Calendar.getInstance();

			if (request.getParameter("startdate") != null) {

				startdate = sdf.parse(
					URLDecoder.decode(request.getParameter("startdate"), "UTF-8"));
				
				logger.info("startdate= " + startdate.toString());

				calendar.setTime(startdate);
			
				calendar.roll(Calendar.MONTH, true);
				enddate = calendar.getTime();

				logger.info("enddate = " + enddate.toString());


			}
			else {
				int month = calendar.get(Calendar.MONTH) + 1;
				int year = calendar.get(Calendar.YEAR);

				String ds = month + "/" + calendar.getMinimum(Calendar.DAY_OF_MONTH) +
					"/" + year;
				logger.info("start date = " + ds);
			
				startdate =  sdf.parse(ds);

				ds = (month + 1) + "/01/" + year;
				
				logger.info("end date = " + ds);

				enddate = sdf.parse(ds);
			}


		}
		catch (Exception e) {
			e.printStackTrace();
			errors.add("parameter", new ActionError("app.exception"));
			saveErrors(request, errors);
			return mapping.findForward("error");
		}

		Vector usage = new Vector();
		Collection contacts;
		try {
			AccessUserContact auc = getAccessUserContact();
			UserPK pk = new UserPK(userid);

			if (startdate == null) {
				contacts = auc.findAllByUserPK(pk);
			}
			else {
				contacts = auc.findAllByUserPKByDate(
						pk, startdate, enddate);
			}

			for (Iterator it = contacts.iterator(); it.hasNext(); ) { 
				UserContact contact = (UserContact)it.next();
				usage.addElement(new UserContactForm(contact));
			}
			Collection months = auc.findUsageMonthsByUserPK(pk);
			request.setAttribute("months", months);

		}
		catch (Exception ee) {
			ee.printStackTrace();	
			errors.add("application", new ActionError("app.exception"));
			saveErrors(request, errors);
			return mapping.findForward("error");
			
		}

		request.setAttribute("usage", usage);
		return mapping.findForward("success");

	}

	private AccessUserContact getAccessUserContact() throws Exception {
		AccessUserContactHome home = 
				(AccessUserContactHome)(new InitialContext()).lookup(
					AccessUserContactHome.JNDI_NAME);
		return home.create();
	}


}

