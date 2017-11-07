package com.iobeam.portal.model.user;


import com.iobeam.portal.security.User;
import com.iobeam.portal.security.PasswordHash;
import com.iobeam.portal.security.PasswordException;
import com.iobeam.portal.model.customer.CustomerPK;


public class UserData implements User, java.io.Serializable {

	private UserPK mPK;
	private String mUserName;
	private CustomerPK mCustomerPK;
	private String mPasswordHash;
	private long mPasswordSalt;
	private String mPasswordReminderAnswer;


	UserData(String userName,
			char[] password, String passwordReminderAnswer, CustomerPK customerPK)
			throws PasswordException {

		mUserName = userName;
		if (userName == null) {
			throw new NullPointerException("userName");
		}

		setPassword(password);

		setPasswordReminderAnswer(passwordReminderAnswer);
		mCustomerPK = customerPK;
		if (customerPK == null) {
			throw new NullPointerException("customerPK");
		}
	}


	UserData(UserPK pk, String userName,
			String passwordHash, long salt,
			CustomerPK customerPK, String passwordReminderAnswer) {

		mPK = pk;
		if (pk == null) {
			throw new NullPointerException("pk");
		}

		mUserName = userName;
		if (userName == null) {
			throw new NullPointerException("userName");
		}

		mPasswordHash = passwordHash;
		if (passwordHash == null) {
			throw new NullPointerException("passwordHash");
		}

		mPasswordSalt = salt;

		mCustomerPK = customerPK;
		if (customerPK == null) {
			throw new NullPointerException("customerPK");
		}

		setPasswordReminderAnswer(passwordReminderAnswer);

	}


	public UserPK getPK() {
		return mPK;
	}


	public UserPK getUserPK() {
		return mPK;
	}


	void setPK(UserPK pk) {
		mPK = pk;
		if (pk == null) {
			throw new NullPointerException("pk");
		}
	}


	public String getUserName() {
		return mUserName;
	}


	/**
	* Returns the pk for the Customer instance associated with this
	* User.  All Users are also Customers.
	*/
	public CustomerPK getCustomerPK() {
		return mCustomerPK;
	}


	/**
	* Returns true if the specified password matches the
	* stored password for this User.
	*/
	public boolean isValidPassword(char[] password) {
		return getPasswordHash().equals(
				PasswordHash.getHash(password, getPasswordSalt()));
	}


	/**
	*/
	public void setPassword(char[] password) throws PasswordException {
		if (password == null) {
			throw new NullPointerException("password");
		}

		mPasswordSalt = PasswordHash.getSalt();
		mPasswordHash = PasswordHash.getHash(password, mPasswordSalt);
	}


	/**
	* Returns the random salt used to perturb the password hash
	* at the time it is first created.
	*/
	public long getPasswordSalt() {
		return mPasswordSalt;
	}


	/**
	* Returns the hashed representation of the User's password.
	*/
	public String getPasswordHash() {
		return mPasswordHash;
	}

	public void setPasswordReminderAnswer(String s) {
		mPasswordReminderAnswer = s;
	}
	public String getPasswordReminderAnswer() {
		return mPasswordReminderAnswer;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("(");

		sb.append(getPK()).append(",");
		sb.append(getUserName()).append(",");
		sb.append(getCustomerPK());

		sb.append(")");

		return sb.toString();
	}

	public boolean equals(Object o) {
		if (o instanceof UserData) {
			UserData ud = (UserData)o;
			if (ud.getPK().equals(getPK()) &&
					ud.getUserName().equals(getUserName()) &&
					ud.getCustomerPK().equals(getCustomerPK())) {
				return true;
			}
		}
		return false;
	}

}
