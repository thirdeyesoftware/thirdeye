package com.iobeam.portal.ui.web.user.password;

import org.apache.struts.action.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ChangePasswordForm extends ActionForm {

	private String mOldPassword;
	private String mNewPassword;
	private String mNewPasswordConfirm;

	private static final String[] REQUIRED_FIELDS = {
		"oldPassword",
		"newPassword",
		"newPasswordConfirm"};

	public void setOldPassword(String s) {
		mOldPassword = s;
	}
	public String getOldPassword() {
		return mOldPassword;
	}

	public void setNewPassword(String s) {
		mNewPassword = s;
	}
	public String getNewPassword() {
		return mNewPassword;
	}

	public void setNewPasswordConfirm(String s) {
		mNewPasswordConfirm = s;
	}
	public String getNewPasswordConfirm() {
		return mNewPasswordConfirm;
	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {

		ActionErrors errors = new ActionErrors();
		
		for (int i = 0; i < REQUIRED_FIELDS.length; i++) {
			if (request.getParameter(REQUIRED_FIELDS[i]) == null ||
					request.getParameter(REQUIRED_FIELDS[i]).trim().equals("")) {
				errors.add(REQUIRED_FIELDS[i], new ActionError("error." +
					REQUIRED_FIELDS[i] + ".required"));
			}
		}

		if (errors.size() == 0) {
			if (!request.getParameter("newPasswordConfirm").equals(
						request.getParameter("newPassword"))) {
				errors.add("newPassword",
					new ActionError("iobeam.password.mismatch"));
			}
		}

		return errors;	
	}

}

