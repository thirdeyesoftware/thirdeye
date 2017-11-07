package com.iobeam.gateway.router;


import java.net.InetAddress;
import java.util.Date;
import com.iobeam.util.MACAddress;
import java.text.*;


public class RouteLease implements java.io.Serializable {

	public static final long serialVersionUID = 2003021201L;

	private static final DateFormat cDateFormat =
			new SimpleDateFormat("MM/dd/yy HH:mm:ss");
	
	private ClientState mClientState;
	private InetAddress mInetAddress;
	private MACAddress mMACAddress;
	private long mContactID;

	private Date mExpirationDate;

	public RouteLease(ClientState clientState,
			Date expirationDate,
			InetAddress inetAddress, MACAddress macAddress, long contactID) {

		mClientState = clientState;
		if (mClientState == null) {
			throw new NullPointerException("clientState");
		}

		mExpirationDate = expirationDate;
		if (mExpirationDate == null) {
			throw new NullPointerException("expirationDate");
		}


		if (mExpirationDate.getTime() < System.currentTimeMillis()) {
			throw new IllegalArgumentException("expiration before now");
		}

		mInetAddress = inetAddress;
		if (mInetAddress == null) {
			throw new NullPointerException("inetAddress");
		}

		mMACAddress = macAddress;
		if (mMACAddress == null) {
			throw new NullPointerException("macAddress");
		}

		mContactID = contactID;
	}
	
	public ClientState getClientState() {
		return mClientState;
	}

	public MACAddress getMACAddress() {
		return mMACAddress;
	}


	public long getContactID() {
		return mContactID;
	}

	public InetAddress getInetAddress() {
		return mInetAddress;
	}

	public Date getExpirationDate() {
		return mExpirationDate;
	}


	public String toString() {
		StringBuffer sb = new StringBuffer("RouteLease(");

		sb.append(getClientState()).append(",");
		sb.append(getInetAddress()).append(",");
		sb.append(getMACAddress()).append(",");
		sb.append("contact=").append(getContactID()).append(",");
		sb.append(cDateFormat.format(getExpirationDate()));

		sb.append(")");

		return sb.toString();
	}
}
