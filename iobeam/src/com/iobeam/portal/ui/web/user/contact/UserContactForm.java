package com.iobeam.portal.ui.web.user.contact;

import java.text.SimpleDateFormat;
import java.util.logging.Logger;
import org.apache.struts.action.*;
import javax.naming.*;
import javax.ejb.*;
import java.rmi.*;

import javax.servlet.http.*;
import javax.servlet.*;

import com.iobeam.portal.model.venue.*;
import com.iobeam.portal.model.gateway.usercontact.*;

public class UserContactForm extends ActionForm {

	private String mDate;
	private String mVenueName;
	private String mIPAddress;
	private String mContactID;
	private String mMACAddress;
	private String mVenueID;

	private static final Logger cLogger = 
		Logger.getLogger("com.iobeam.portal.ui.web.user.contact");

	public UserContactForm() {

	}

	public UserContactForm(UserContact contact) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

		mDate = sdf.format(contact.getTimestamp());
		mVenueName = contact.getVenueName();

		mIPAddress = (contact.getUserIPAddress() != null) ? 
				contact.getUserIPAddress().getHostAddress() : "";
		mMACAddress = (contact.getUserMACAddress() != null) ?
				contact.getUserMACAddress().toString() : "";
		mContactID = String.valueOf(contact.getContactID());
		mVenueID = String.valueOf(contact.getVenuePK().getID());

	}

	public String getMACAddress() {
		return mMACAddress;
	}
	public void setMACAddress(String mac) {
		mMACAddress = mac;
	}

	public String getIPAddress() {
		return mIPAddress;
	}
	public void setIPAddress(String ip) {
		mIPAddress = ip;
	}
	
	public void setVenueID(String s) {
		mVenueID = s;
	}

	public String getVenueID() {
		return mVenueID;
	}

	public void setVenueName(String name) {
		mVenueName = name;
	}
	public String getVenueName() {
		return mVenueName;
	}

	public void setContactID(String s) {
		mContactID = s;
	}
	public String getContactID() {
		return mContactID;
	}

	public void setDate(String s) {
		mDate = s;
	}
	public String getDate() {
		return mDate;
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest req) {
		return new ActionErrors();
	}
	
	private AccessVenue getAccessVenue() throws Exception {
		AccessVenueHome home = 
				(AccessVenueHome)(new InitialContext()).lookup(
					AccessVenueHome.JNDI_NAME);
		return home.create();
	}

}


