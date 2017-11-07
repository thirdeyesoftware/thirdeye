package com.iobeam.portal.model.gateway.usercontact;


import java.net.InetAddress;
import java.text.*;
import java.util.Date;
import com.iobeam.util.*;
import com.iobeam.portal.model.venue.VenuePK;
import com.iobeam.portal.model.user.UserPK;


public class UserContact implements java.io.Serializable {

	private static DateFormat cDateFormat = new SimpleDateFormat(
			"yy/MM/dd HH:mm:ss");

	private ContactID mContactID;
	private VenuePK mVenuePK;
	private InetAddress mUserIPAddress;
	private MACAddress mUserMACAddress;
	private Date mTimestamp;
	private UserPK mUserPK;
	private boolean mIsAnonymous;
	private String mVenueName;


	UserContact(VenuePK venuePK,
			InetAddress userIPAddress,
			MACAddress userMACAddress) {

		mVenuePK = venuePK;
		if (venuePK == null) {
			throw new NullPointerException("venuePK");
		}

		mUserIPAddress = userIPAddress;
		if (userIPAddress == null) {
			throw new NullPointerException("userIPAddress");
		}

		mUserMACAddress = userMACAddress;
		if (userMACAddress == null) {
			throw new NullPointerException("userMACAddress");
		}

		mIsAnonymous = false;

		mTimestamp = new Date();
	}
	

	UserContact(ContactID contactID, VenuePK venuePK,
			InetAddress userIPAddress,
			MACAddress userMACAddress) {

		this(contactID, venuePK, userIPAddress, userMACAddress,
				false, new Date(), null, null);
	}


	UserContact(ContactID contactID, VenuePK venuePK,
			InetAddress userIPAddress,
			MACAddress userMACAddress, boolean isAnonymous,
			Date timestamp, UserPK userPK, String venueName) {

		mContactID = contactID;
		if (contactID == null) {
			throw new NullPointerException("contactID");
		}

		mVenuePK = venuePK;
		if (venuePK == null) {
			throw new NullPointerException("venuePK");
		}

		mUserIPAddress = userIPAddress;
		if (userIPAddress == null) {
			throw new NullPointerException("userIPAddress");
		}

		mUserMACAddress = userMACAddress;
		if (userMACAddress == null) {
			throw new NullPointerException("userMACAddress");
		}

		mIsAnonymous = isAnonymous;

		mTimestamp = timestamp;
		if (timestamp == null) {
			throw new NullPointerException("timestamp");
		}

		mUserPK = userPK;

		if (mIsAnonymous && mUserPK != null) {
			throw new IllegalStateException("anonymous with " + mUserPK);
		}

		mVenueName = venueName;

	}


	public ContactID getContactID() {
		return mContactID;
	}


	void setContactID(ContactID contactID) {
		mContactID = contactID;
		if (contactID == null) {
			throw new NullPointerException("contactID");
		}
	}


	public VenuePK getVenuePK() {
		return mVenuePK;
	}

	public String getVenueName() {
		return mVenueName;
	}

	public InetAddress getUserIPAddress() {
		return mUserIPAddress;
	}


	public MACAddress getUserMACAddress() {
		return mUserMACAddress;
	}


	public Date getTimestamp() {
		return mTimestamp;
	}


	/**
	* Returns the pk of the User bound to this UserContact, or null
	* if no binding has yet occurred.
	* Binding happens at User signon time.
	*
	* @exception IllegalStateException this is an anonymous contact.
	*/
	public UserPK getUserPK() {
		if (isAnonymous()) {
			throw new IllegalStateException("anonymous contact");
		}

		return mUserPK;
	}


	/**
	* This method is used as part of the process of binding
	* a User to this UserContact.   Binding happens at User signon time.
	* No binding is complete until the UserContact instance is
	* written to the permanent store via AccessUserContact.
	*/
	public void setUserPK(UserPK userPK) {
		mUserPK = userPK;
	}


	/**
	* Binds this UserContact as anonymous.  Binding happens at User
	* signon time.
	*
	* @exception IllegalStateException there is already a bound User
	* for this UserContact.
	*/
	public void setAnonymous(boolean isAnonymous) {
		if (getUserPK() != null) {
			throw new IllegalStateException("already bound to " + getUserPK());
		}

		mIsAnonymous = isAnonymous;
	}


	/**
	* Returns true if this is an anonynous contact, that is, there
	* will never be an explicit User associated with it.
	*
	* The getUserPK will throw IllegalStateException for anonymous contacts.
	*/
	public boolean isAnonymous() {
		return mIsAnonymous;
	}


	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("UserContact(");
		sb.append(getContactID()).append(",");
		sb.append(getVenuePK()).append(",");
		sb.append(getUserIPAddress().getHostAddress()).append(",");
		sb.append(getUserMACAddress()).append(",");
		sb.append(cDateFormat.format(getTimestamp())).append(",");
		sb.append(getUserPK());
		if (isAnonymous()) {
			sb.append(",");
			sb.append("anonymous");
		}
		sb.append(")");

		return sb.toString();
	}
}
