package com.iobeam.portal.model.employee;


public class EmployeePK implements java.io.Serializable {
	
	public static final long serialVersionUID = 2003041801L;

	private long mID;

	public EmployeePK(long id) {

		mID = id;
	}


	public long getEmployeeID() {

		return mID;

	}

	public boolean equals(Object o) {
		if (o instanceof EmployeePK) {
			EmployeePK pk = (EmployeePK) o;
			return pk.getEmployeeID() == mID;
		} else {
			return false;
		}
	}

	public String toString() {
		return "EmployeePK(" + getEmployeeID() + ")";
	}
}
