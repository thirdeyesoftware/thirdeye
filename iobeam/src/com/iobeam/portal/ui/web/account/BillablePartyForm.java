package com.iobeam.portal.ui.web.account;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;
import com.iobeam.portal.ui.web.user.*;

public final class BillablePartyForm extends ActionForm  {


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

		private String mCreditCardNumber;
		private String mCardHolderName;
		private String mSecurityCode;
		private String mExpirationMonth;
		private String mExpirationYear;

		private static final String[] REQUIRED_FIELDS = {
				"firstName",
				"lastName",
				"address1",
				"city",
				"state",
				"zipcode",
				"countryId",
				"phoneNumber",
				"emailAddress",
				"creditCardNumber",
				"cardHolderName",
				"securityCode",
				"expirationMonth",
				"expirationYear",};


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

		public void setCreditCardNumber(String s) {
			mCreditCardNumber = s;
		}

		public String getCreditCardNumber() {
			return mCreditCardNumber;
		}

		public void setCardHolderName(String s) { 
			mCardHolderName = s;
		}
		public String getCardHolderName() {
			return mCardHolderName;
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


    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {

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

			mCreditCardNumber = null;
			mCardHolderName = null;
			mSecurityCode = null;
			mExpirationMonth = null;
			mExpirationYear = null;
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

			return errors;

    }

}

