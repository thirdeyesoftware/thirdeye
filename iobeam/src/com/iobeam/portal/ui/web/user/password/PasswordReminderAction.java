package com.iobeam.portal.ui.web.user.password;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;

import javax.naming.*;

import com.iobeam.portal.ui.web.user.UserSessionHelper;
import com.iobeam.portal.model.user.*;
import com.iobeam.portal.task.actor.user.usersession.UserSession;
import com.iobeam.portal.security.*;

public class PasswordReminderAction extends Action {

	public ActionForward execute(ActionMapping mapping,
			ActionForm _form, 
			HttpServletRequest request, HttpServletResponse response)
				throws Exception {

		ActionErrors errors = new ActionErrors();

		PasswordReminderForm form = (PasswordReminderForm)_form;
		try {
			AccessUser au = getAccessUser();

			User user = au.findByReminderInfo(
					form.getEmailAddress().toLowerCase(),
					form.getCity().toLowerCase(),
					form.getState().toLowerCase(),
					form.getZipcode().toLowerCase(),
					form.getReminderAnswer().toLowerCase());
			
			String newPassword = 
					StringRandomizer.getRandomString(6).toLowerCase();

			user.setPassword(newPassword.toCharArray());
			au.updateUser(user);

			request.setAttribute("newpassword", newPassword);
			return mapping.findForward("success");
		}
		catch (Exception ee) { 
			errors.add("application", new ActionError("app.exception"));
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
	}
	
	private AccessUser getAccessUser() throws Exception {
		AccessUserHome home = (AccessUserHome)
				(new InitialContext()).lookup(AccessUserHome.JNDI_NAME);
		return home.create();
	}


}

