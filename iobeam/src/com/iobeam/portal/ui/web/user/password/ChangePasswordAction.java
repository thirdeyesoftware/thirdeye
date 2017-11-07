package com.iobeam.portal.ui.web.user.password;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;

import javax.naming.*;

import com.iobeam.portal.ui.web.user.UserSessionHelper;
import com.iobeam.portal.task.actor.user.usersession.UserSession;
import com.iobeam.portal.task.customer.manageuser.ManageUserHome;
import com.iobeam.portal.task.customer.manageuser.ManageUser;
import com.iobeam.portal.security.*;

public class ChangePasswordAction extends Action {


	public ActionForward execute(ActionMapping mapping, 
			ActionForm _form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ActionErrors errors = new ActionErrors();

		HttpSession httpSession = request.getSession();
		ChangePasswordForm form = (ChangePasswordForm)_form;

		UserSession us = UserSessionHelper.getUserSession(httpSession);
		if (us == null) {
			return mapping.findForward("portalsignon");
		}

		try {
			getManageUser().setPassword(us.getUser(),
				form.getOldPassword().toCharArray(),
				form.getNewPassword().toCharArray());
		}
		catch (InvalidPasswordException ipe) {
			errors.add("password",
				new ActionError("iobeam.password.mismatch"));
		}
		catch (MalformedPasswordException mpe) {
			errors.add("password",
				new ActionError("iobeam.password.invalid"));
		}
		catch (Exception e) {
			errors.add("appication",
				new ActionError("app.exception"));
			saveErrors(request, errors);
			return mapping.findForward("error");
		}

		if (errors.size() > 0) {
			saveErrors(request, errors);
			return new ActionForward(mapping.getInput());
		}

		return mapping.findForward("success");

	}

	private ManageUser getManageUser() throws Exception {
		InitialContext ic = new InitialContext();
		ManageUserHome home = 
			(ManageUserHome)ic.lookup(ManageUserHome.JNDI_NAME);
		return home.create();
	}

}

