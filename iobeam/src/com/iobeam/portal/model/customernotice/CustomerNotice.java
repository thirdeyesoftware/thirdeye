package com.iobeam.portal.model.customernotice;

import java.io.Serializable;

public class CustomerNotice implements Serializable, Comparable {

	private StringBuffer mNoticeBuffer;
	private long mID;
	private boolean mIsActive;
	private long mVenueID;
	private int mOrderBit;

	
	public CustomerNotice(long id,  long venueid,
			String notice, boolean active, int orderbit) {
		mNoticeBuffer = new StringBuffer(notice);
		mID = id;
		mIsActive = active;
		mOrderBit = orderbit;
		mVenueID = venueid;

	}

	public CustomerNotice(String notice) {
		mNoticeBuffer = new StringBuffer(notice);
	}

	public long getID() {
		return mID;
	}

	public long getVenueID() {
		return mVenueID;
	}

	public void setVenueID(long l) {
		mVenueID = l;
	}

	public boolean isActive() {
		return mIsActive;
	}

	public void setIsActive(boolean b) {
		mIsActive = b;
	}

	public void setOrderBit(int bit) {
		mOrderBit = bit;
	}

	public int getOrderBit() {
		return mOrderBit;
	}

	public void append(String s) {
		mNoticeBuffer.append(s);
	}

	public String getNotice() {
		return mNoticeBuffer.toString();
	}

	public int compareTo(Object o) {

		CustomerNotice notice = (CustomerNotice)o;
		if (notice.getOrderBit() > getOrderBit()) {
			return 1;
		}
		else if (notice.getOrderBit() == getOrderBit()) {
			return 0;
		}
		else {
			return -1;
		}

	}

}


