package com.iobeam.portal.service.contact;


import java.net.InetAddress;
import com.iobeam.util.*;


public class Contact implements java.io.Serializable {

	private long mContactID;
	private InetAddress mVenueInetAddress;
	private String mVenueName;
	private InetAddress mUserInetAddress;
	private MACAddress mUserMACAddress;


	Contact(long contactID,
			String venueName,
			InetAddress venueInetAddress,
			InetAddress userInetAddress,
			MACAddress userMACAddress) {

		mContactID = contactID;
		mVenueInetAddress = venueInetAddress;
		mVenueName = venueName;
		mUserInetAddress = userInetAddress;
		mUserMACAddress = userMACAddress;
	}


	public long getID() {
		return mContactID;
	}


	public String getVenueName() {
		return mVenueName;
	}


	public InetAddress getVenueInetAddress() {
		return mVenueInetAddress;
	}


	public InetAddress getUserInetAddress() {
		return mUserInetAddress;
	}


	public MACAddress getUserMACAddress() {
		return mUserMACAddress;
	}


	public String toString() {
		StringBuffer sb = new StringBuffer("Contact(");

		sb.append(mContactID).append(",");
		sb.append("venue=").append(getVenueName()).append(":");
		sb.append(getVenueInetAddress().getHostAddress()).append(",");
		sb.append("user=");
		sb.append(getUserInetAddress().getHostAddress()).append(" ");
		sb.append(getUserMACAddress());

		sb.append(")");

		return sb.toString();
	}
}
