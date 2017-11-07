package com.iobeam.portal.util;


public class ContactName implements java.io.Serializable {

	private String mFirstName;
	private String mMiddleInitial;
	private String mLastName;

	public ContactName(String firstName, String middleInitial,
			String lastName) {

		mFirstName = firstName;
		if (firstName == null) {
			throw new NullPointerException("firstName");
		}

		mLastName = lastName;
		if (lastName == null) {
			throw new NullPointerException("lastName");
		}

		if (middleInitial != null) {
			mMiddleInitial = middleInitial.substring(0,1);
		}
	}


	public String getFirstName() {
		return mFirstName;
	}

	public String getLastName() {
		return mLastName;
	}


	public String getMiddleInitial() {
		return mMiddleInitial;
	}


	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("ContactName(");
		sb.append(getFirstName()).append(",");
		sb.append(getMiddleInitial()).append(",");
		sb.append(getLastName());
		sb.append(")");

		return sb.toString();
	}
}
