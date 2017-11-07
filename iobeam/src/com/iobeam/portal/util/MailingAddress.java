package com.iobeam.portal.util;


public class MailingAddress implements java.io.Serializable {

	private String mLine1;
	private String mLine2;
	private String mCity;
	private String mState;
	private String mZipcode;
	private String mZip4;

	public MailingAddress(String line1, String line2,
			String city, String state, String zipcode) {

		mLine1 = line1;
		if (line1 == null) {
			throw new NullPointerException("line1");
		}

		mLine2 = line2;

		mCity = city;
		if (city == null) {
			throw new NullPointerException("city");
		}

		mState = state.substring(0, 2);
		
		mZipcode = zipcode;
		if (zipcode == null) {
			throw new NullPointerException("zipcode");
		}
	}


	public String getLine1() {
		return mLine1;
	}

	public String getLine2() {
		return mLine2;
	}


	public String getCity() {
		return mCity;
	}


	public String getState() {
		return mState;
	}


	public String getZipcode() {
		return mZipcode;
	}


	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("MailingAddress(");
		sb.append(getLine1()).append(",");
		sb.append(getLine2()).append(",");
		sb.append(getCity()).append(",");
		sb.append(getState()).append(",");
		sb.append(getZipcode());
		sb.append(")");

		return sb.toString();
	}
}
