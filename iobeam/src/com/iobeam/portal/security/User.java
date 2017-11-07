package com.iobeam.portal.security;


import com.iobeam.portal.model.user.UserPK;
import com.iobeam.portal.model.customer.CustomerPK;


/**
* A user represents an individual who may be authenticated.
* Authentication describes a process through which a user's
* identity is proved.
*/
public interface User {

	public String getUserName();

	public UserPK getUserPK();


	/**
	* Returns the pk for the Customer instance associated with this
	* User.  All Users are also Customers.
	*/
	public CustomerPK getCustomerPK();


	/**
	* Returns true if the specified password matches the
	* stored password for this User.
	*/
	public boolean isValidPassword(char[] password);


	/**
	*/
	public void setPassword(char[] password) throws PasswordException;


	/**
	* Returns the hashed representation of the user's password,
	* according to the current password salt.
	*/
	public String getPasswordHash();


	/**
	* Returns the salt used to perturb the password hash.
	*/
	public long getPasswordSalt();

	/**
	 * Returns the password answer for this user used for
	 * password reminder.
	 */
	public String getPasswordReminderAnswer();

}
