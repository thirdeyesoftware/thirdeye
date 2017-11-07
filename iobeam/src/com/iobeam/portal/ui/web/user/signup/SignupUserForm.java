package com.iobeam.portal.ui.web.user.signup;

import com.iobeam.portal.util.CreditCardValidator;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import com.iobeam.portal.ui.web.user.*;

public final class SignupUserForm extends ActionForm  {

		private String mPrototypeId;
		private String mSecureId;
		private boolean mDisclaimer;

		private String mUsername;
		private String mPassword;
		private String mPasswordConfirm;
		private String mPasswordReminderAnswer;
	
		private String mFirstName;
		private String mLastName;

		private String mEmailAddress;
		private String mPhoneNumber;
		private String mFaxNumber;

		private String mAddress1;
		private String mAddress2;
		private String mCity;
		private String mState;
		private String mZipcode;
		private String mCountryId;

		private String mBillingAddress1;
		private String mBillingAddress2;
		private String mBillingCity;
		private String mBillingState;
		private String mBillingZipcode;
		private String mBillingCountryId;

		private String mCreditCardNumber;
		private String mCardHolderName;
		private String mSecurityCode;
		private String mExpirationMonth;
		private String mExpirationYear;

		private static final String[] REQUIRED_FIELDS = {
				"username",
				"password",
				"passwordConfirm",
				"passwordReminderAnswer",
				"firstName",
				"lastName",
				"address1",
				"city",
				"state",
				"zipcode",
				"countryId",
				"disclaimer",
		};

		private static final String[] REQUIRED_BILLING_FIELDS = {
				"billingAddress1",
				"billingCity",
				"billingState",
				"billingZipcode",
				"creditCardNumber",
				"securityCode",
				"cardHolderName",
				"expirationMonth",
				"expirationYear",
				};

		public String getUsername() {
			return mUsername;
		}
		public void setUsername(String s) {
			mUsername = s;
		}

		public String getPassword() {
			return mPassword;
		}
		public void setPassword(String s) {
			mPassword = s;
		}
		
		public String getPasswordConfirm() {
			return mPasswordConfirm;
		}
		public void setPasswordConfirm(String s) {
			mPasswordConfirm = s;
		}
		
		public String getPasswordReminderAnswer() {
			return mPasswordReminderAnswer;
		}
		public void setPasswordReminderAnswer(String s) {
			mPasswordReminderAnswer = s;
		}

		public String getFirstName() {
			return mFirstName;
		}
		public void setFirstName(String s) {
			mFirstName = s;
		}

		public String getLastName() {
			return mLastName;
		}

		public void setLastName(String s) {
			mLastName = s;
		}

		public String getAddress1() {
			return mAddress1;
		}
		public void setAddress1(String s) {
			mAddress1 = s;
		}

		public String getAddress2() {
			return mAddress2;
		}
		public void setAddress2(String s) {
			mAddress2 = s;
		}

		public String getCity() {
			return mCity;
		}
		public void setCity(String s) {
			mCity = s;
		}

		public String getState() {
			return mState;
		}
		public void setState(String s) {
			mState = s;
		}

		public String getZipcode() {
			return mZipcode;
		}

		public void setZipcode(String s) {
			mZipcode = s;
		}
		
		public String getCountryId() {
			return mCountryId;
		}
		public void setCountryId(String s) {
			mCountryId = s;
		}

		public String getEmailAddress() {
			return mEmailAddress;
		}

		public void setEmailAddress(String s) {
			mEmailAddress = s;
		}

		public String getPhoneNumber() {
			return mPhoneNumber;
		}
		public void setPhoneNumber(String s) {
			mPhoneNumber = s;
		}

		public String getFaxNumber() {
			return mFaxNumber;
		}
		public void setFaxNumber(String s) {
			mFaxNumber = s;
		}

		public String getBillingAddress1() {
			return mBillingAddress1;
		}
		
		public void setBillingAddress1(String s) {
			mBillingAddress1 = s;
		}

		public String getBillingAddress2() {
			return mBillingAddress2;
		}
		public void setBillingAddress2(String s) {
			mBillingAddress2 = s;
		}

		public String getBillingCity() {
			return mBillingCity;
		}
		public void setBillingCity(String s) {
			mBillingCity = s;
		}

		public String getBillingState() {
			return mBillingState;
		}
		public void setBillingState(String s) {
			mBillingState = s;
		}

		public void setBillingZipcode(String s) {
			mBillingZipcode = s;
		}
		public String getBillingZipcode() {
			return mBillingZipcode;
		}
		
		public String getBillingCountryId() {
			return mCountryId;
		}
		public void setBillingCountryId(String s) {
			mCountryId = s;
		}


		public String getCreditCardNumber() {
			return mCreditCardNumber;
		}
		public void setCreditCardNumber(String s) {
			mCreditCardNumber = s;
		}
		public String getCardHolderName() {
			return mCardHolderName;
		}
		public void setCardHolderName(String s) {
			mCardHolderName = s;
		}
		public String getSecurityCode() {
			return mSecurityCode;
		}
		public void setSecurityCode(String s) {
			mSecurityCode = s;
		}
		public String getExpirationMonth() {
			return mExpirationMonth;
		}
		public void setExpirationMonth(String s) {
			mExpirationMonth= s;
		}
		public String getExpirationYear() {
			return mExpirationYear;
		}
		public void setExpirationYear(String s) {
			mExpirationYear = s;
		}


		public void setSubscriptionPrototypeId(String s) {
			mPrototypeId = s;
		}
		public String getSubscriptionPrototypeId() {
			return mPrototypeId;
		}
		public void setSecureId(String s) {
			mSecureId = s;
		}
		public String getSecureId() {
			return mSecureId;
		}
   
	 	public boolean getDisclaimer() {
			return mDisclaimer;
		}
		public void setDisclaimer(boolean s) {
			mDisclaimer = s;
		}



    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {

			mPrototypeId = null;
			mUsername = null;
			mPassword = null;
			mPasswordConfirm = null;

			mFirstName = null;
			mLastName = null;

			mPhoneNumber = null;
			mFaxNumber = null;
			mEmailAddress = null;

			mAddress1 = null;
			mAddress2 = null;
			mCity = null;
			mState = null;
			mZipcode = null;
			mCountryId = null;

			mBillingAddress1 = null;
			mBillingAddress2 = null;
			mBillingCity = null;
			mBillingState = null;
			mBillingZipcode = null;
			mBillingCountryId = null;

			mCreditCardNumber = null;
			mCardHolderName = null;
			mSecurityCode = null;
			mExpirationMonth = null;
			mExpirationYear = null;
			mSecureId = null;
			mDisclaimer = false;

    }


    /**
     * Validate the properties that have been set from this HTTP request,
     * and return an <code>ActionErrors</code> object that encapsulates any
     * validation errors that have been found.  If no errors are found, return
     * <code>null</code> or an <code>ActionErrors</code> object with no
     * recorded error messages.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest request) {

    	ActionErrors errors = new ActionErrors();

			for (int i = 0; i < REQUIRED_FIELDS.length; i++) {
				if (request.getParameter(REQUIRED_FIELDS[i]) == null ||
						request.getParameter(REQUIRED_FIELDS[i]).trim().equals("")) {
					errors.add(REQUIRED_FIELDS[i],
						new ActionError("error." + REQUIRED_FIELDS[i] + ".required"));
				}
			}

			if (request.getParameter("secureId") == null ||
					request.getParameter("secureId").trim().equals("")) {
				
				for (int i = 0; i < REQUIRED_BILLING_FIELDS.length; i++) {
					if (request.getParameter(REQUIRED_BILLING_FIELDS[i]) == null ||
							request.getParameter(REQUIRED_BILLING_FIELDS[i]).trim().equals("")) {
						errors.add(REQUIRED_BILLING_FIELDS[i],
							new ActionError("error." + REQUIRED_BILLING_FIELDS[i] + ".required"));
					}
				}

				if (errors.size() == 0) {
					if (request.getParameter("subscriptionPrototypeId") == null ||
							request.getParameter("subscriptionPrototypeId").trim().equals("")) {
						errors.add("subscriptionPrototype",
							new ActionError("error.subscription.required"));
					}					
					String ccnum = request.getParameter("creditCardNumber");
					if (!CreditCardValidator.validate(ccnum)) {
						errors.add("creditCardNumber",
							new ActionError("error.creditcardnumber.invalid"));
					}

				}	
			}
			if (!request.getParameter("passwordConfirm").equals(
				request.getParameter("password"))) {
					errors.add("password", new ActionError("error.password.match"));
			}



			return errors;

    }

}

