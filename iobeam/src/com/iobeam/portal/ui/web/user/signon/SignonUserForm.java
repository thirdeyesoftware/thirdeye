package com.iobeam.portal.ui.web.user.signon;


import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.iobeam.portal.ui.web.user.*;

public final class SignonUserForm extends ActionForm  {

    private String mPassword;
    private String mUsername;
		private String mSecureId;
		private String mSignonType;

		public static final String SECURE_ID = "secureid";
		public static final String CREDENTIALS = "credentials";
		public static final String PORTAL_CREDENTIALS = "portalcredentials";

		private String SECURE_FIELDS_REQUIRED[] = {
			"secureId"};

		private String CREDENTIAL_FIELDS_REQUIRED[] = {
			"username",
			"password"};

		public void setSignonType(String type) {
			mSignonType = type;
		}
		public String getSignonType() {
			return mSignonType;
		}

    public String getPassword() {
			return (mPassword);
    }
    public void setPassword(String password) {
        mPassword = password;
    }
    
    public String getUsername() {
			return (mUsername);
    }
    public void setUsername(String username) {
        mUsername = username;
    }

		public void setSecureId(String secureid) {
			mSecureId = secureid;
		}

		public String getSecureId() {
			return mSecureId;
		}


    public void reset(ActionMapping mapping, HttpServletRequest request) {
        
				mPassword = null;
        mUsername = null;
				mSecureId = null;

    }

    public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest request) {

    	ActionErrors errors = new ActionErrors();
			String[] requiredFields = new String[0];

			if (SECURE_ID.equals(request.getParameter("type"))) {
				requiredFields = SECURE_FIELDS_REQUIRED;
			}
			else {
				requiredFields = CREDENTIAL_FIELDS_REQUIRED;
			}
			
			for (int i = 0; i < requiredFields.length; i++) {

				if (request.getParameter(requiredFields[i]) == null ||
						request.getParameter(requiredFields[i]).trim().equals("")) {

					errors.add(requiredFields[i], new ActionError("error." +
						requiredFields[i] + ".required"));
				}
			}

			return errors;

    }

}

