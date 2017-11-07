package com.iobeam.portal.ui.web.user.password;

import org.apache.struts.action.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class PasswordReminderForm extends ActionForm {

	private String mEmailAddress;
	private String mCity;
	private String mState;
	private String mZipcode;
	private String mPasswordReminderAnswer;

	private static final String[] REQUIRED_FIELDS = {
		"emailAddress",
		"city",
		"state",
		"zipcode",
		"reminderAnswer"};

	public void setEmailAddress(String s) {
		mEmailAddress = s;
	}
	public String getEmailAddress() {
		return mEmailAddress;
	}

	public void setCity(String s) {
		mCity = s;
	}
	public String getCity() {
		return mCity;
	}

	public void setState(String s) {
		mState = s;
	}
	public String getState() {
		return mState;
	}
	
	public void setZipcode(String s) {
		mZipcode = s;
	}
	public String getZipcode() {
		return mZipcode;
	}
	
	public void setReminderAnswer(String s) {
		mPasswordReminderAnswer = s;
	}
	public String getReminderAnswer() {
		return mPasswordReminderAnswer;
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

		return errors;	
	}

}

