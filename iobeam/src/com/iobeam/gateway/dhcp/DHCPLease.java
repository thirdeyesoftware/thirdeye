package com.iobeam.gateway.dhcp;


import java.util.*;
import java.text.*;
import java.net.InetAddress;
import com.iobeam.util.MACAddress;



public class DHCPLease implements java.io.Serializable {
	private static DateFormat cDateFormat =
			new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	private InetAddress mInetAddress;
	private MACAddress mMACAddress;
	private Date mStartTime;
	private Date mEndTime;
	private boolean mIsBound;
	private String mClientHostname;
	private boolean mAbandoned;

	DHCPLease(InetAddress inetAddress,
			MACAddress macAddress,
			Date startTime, Date endTime,
			boolean isBound, String clientHostname,
			boolean abandoned) {

		setBound(isBound);

		setInetAddress(inetAddress);
		setMACAddress(macAddress);
		setStartTime(startTime);
		setEndTime(endTime);
		setClientHostname(clientHostname);
		setAbandoned(abandoned);
	}


	private void setInetAddress(InetAddress inetAddress) {
		mInetAddress = inetAddress;

		if (inetAddress == null) {
			throw new NullPointerException("inetAddress");
		}
	}

	public InetAddress getInetAddress() {
		return mInetAddress;
	}

	private void setAbandoned(boolean b) {
		mAbandoned = b;
	}

	public boolean isAbandoned() {
		return mAbandoned;
	}

	private void setMACAddress(MACAddress macAddress) {
		mMACAddress = macAddress;

		if (isBound() && macAddress == null) {
			throw new NullPointerException("macAddress");
		}
	}

	public MACAddress getMACAddress() {
		return mMACAddress;
	}


	private void setStartTime(Date startTime) {
		mStartTime = startTime;
	}

	public Date getStartTime() {
		return mStartTime;
	}


	private void setEndTime(Date endTime) {
		mEndTime = endTime;
	}

	public Date getEndTime() {
		return mEndTime;
	}


	private void setBound(boolean isBound) {
		mIsBound = isBound;
	}

	public boolean isBound() {
		return mIsBound;
	}


	private void setClientHostname(String clientHostname) {
		mClientHostname = clientHostname;
	}

	public String getClientHostname() {
		return mClientHostname;
	}


	public int hashCode() {
		return getInetAddress().hashCode();
	}


	public boolean equals(Object o) {
		if (o instanceof DHCPLease) {
			DHCPLease l = (DHCPLease) o;

			return getInetAddress().equals(l.getInetAddress());
		} else {
			return false;
		}
	}


	public String toString() {
		StringBuffer sb = new StringBuffer("DHCPLease(");

		sb.append(getInetAddress()).append(",");
		sb.append(getMACAddress()).append(",");
		sb.append(cDateFormat.format(getStartTime())).append(",");
		sb.append(cDateFormat.format(getEndTime())).append(",");

		sb.append(isBound() ? "bound" : "free").append(",");

		sb.append(getClientHostname());

		sb.append(")");

		return sb.toString();
	}
}
