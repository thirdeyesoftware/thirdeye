package com.iobeam.portal.model.gateway;


import java.util.Date;
import java.text.*;
import com.iobeam.util.*;
import java.net.InetAddress;
import com.iobeam.portal.model.venue.VenuePK;


public class Gateway implements java.io.Serializable {

	private static DateFormat cDateFormat = new SimpleDateFormat(
			"yy/MM/dd HH:mm:ss");

	private GatewayPK mPK = null;
	private VenuePK mVenuePK;
	private MACAddress mMACAddress;
	private InetAddress mPublicIPAddress;
	private InetAddress mPrivateIPAddress;
	private int mNotifyPort;
	private Date mTimestamp;
	

	public Gateway(VenuePK venuePK, MACAddress mac) {
		this(venuePK, mac, null, -1);
	}


	public Gateway(VenuePK venuePK, MACAddress macAddress,
			InetAddress publicIPAddress, int notifyPort) {

		mVenuePK = venuePK;
		if (venuePK == null) {
			throw new NullPointerException("venuePK");
		}

		mMACAddress = macAddress;
		if (macAddress == null) {
			throw new NullPointerException("macAddress");
		}

		mPublicIPAddress = publicIPAddress;
		mPrivateIPAddress = null;

		mNotifyPort = notifyPort;

		mTimestamp = new Date();
	}


	Gateway(GatewayPK pk, VenuePK venuePK, MACAddress macAddress,
			InetAddress publicIPAddress, InetAddress privateIPAddress,
			int notifyPort, Date timestamp) {

		mPK = pk;
		if (pk == null) {
			throw new NullPointerException("pk");
		}

		mVenuePK = venuePK;
		if (venuePK == null) {
			throw new NullPointerException("venuePK");
		}

		mMACAddress = macAddress;
		if (macAddress == null) {
			throw new NullPointerException("macAddress");
		}

		mPublicIPAddress = publicIPAddress;
		mPrivateIPAddress = privateIPAddress;

		mNotifyPort = notifyPort;

		mTimestamp = timestamp;
		if (timestamp == null) {
			throw new NullPointerException("timestamp");
		}
	}


	public GatewayPK getPK() {
		return mPK;
	}


	void setPK(GatewayPK pk) {
		mPK = pk;
		if (pk == null) {
			throw new NullPointerException("pk");
		}
	}


	/**
	* Returns the pk of the Venue where this Gateway is located.
	*/
	public VenuePK getVenuePK() {
		return mVenuePK;
	}


	/**
	* Returns the MACAddress of the nic attached to the ISP
	* for this Gateway.
	*/
	public MACAddress getMACAddress() {
		return mMACAddress;
	}


	/**
	* Sets the IP address for the ISP-side of this Gateway
	* at Gateway registration time.
	*/
	public void setPublicIPAddress(InetAddress ipAddress) {
		mPublicIPAddress = ipAddress;
	}


	/**
	* Returns the current IP address for the ISP-side of this Gateway,
	* or null if the Gateway has not yet made contact with the Portal.
	*/
	public InetAddress getPublicIPAddress() {
		return mPublicIPAddress;
	}


	/**
	* Sets the IP address for the WiFi-side of this Gateway
	* at Gateway registration time.
	*/
	public void setPrivateIPAddress(InetAddress ipAddress) {
		mPrivateIPAddress = ipAddress;
	}


	/**
	* Returns the current IP address for the WiFi-side of this Gateway,
	* or null if the Gateway has not yet made contact with the Portal.
	*/
	public InetAddress getPrivateIPAddress() {
		return mPrivateIPAddress;
	}


	/**
	* Sets the notifyPort for the ISP-side of this Gateway, as used
	* by portal for sending unsolicited messages to the Gateway.
	*/
	public void setNotifyPort(int notifyPort) {
		mNotifyPort = notifyPort;
	}


	/**
	* Returns the port on the current IP address for communication
	* with this Gateway.  This value is undefined if the Gateway has
	* not yet made contact with the Portal.
	*/
	public int getNotifyPort() {
		return mNotifyPort;
	}


	/**
	* Returns time of last modification to this Gateway record.  This
	* is either the time of creation, or last registration.
	*/
	public Date getTimestamp() {
		return mTimestamp;
	}


	/**
	* Brings the timestamp up to date.
	*/
	void updateTimestamp() {
		mTimestamp = new Date();
	}


	public String toString() {
		StringBuffer sb = new StringBuffer(0);

		sb.append("Gateway(");
		sb.append(getPK()).append(",");
		sb.append(getVenuePK()).append(",");
		sb.append(getMACAddress()).append(",");
		if (getPublicIPAddress() != null) {
			sb.append("public=");
			sb.append(getPublicIPAddress().getHostAddress()).append(",");
		}
		if (getPrivateIPAddress() != null) {
			sb.append("private=");
			sb.append(getPrivateIPAddress().getHostAddress()).append(",");
		}
		sb.append("notifyPort=").append(getNotifyPort()).append(",");
		sb.append(cDateFormat.format(getTimestamp()));
		sb.append(")");

		return sb.toString();

	}
}
