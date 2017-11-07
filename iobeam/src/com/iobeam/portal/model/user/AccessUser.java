package com.iobeam.portal.model.user;


import java.util.Collection;
import java.rmi.RemoteException;
import javax.ejb.*;
import com.iobeam.portal.model.EntityAccessor;
import com.iobeam.portal.security.*;
import com.iobeam.portal.model.customer.*;


public interface AccessUser extends EntityAccessor, EJBObject {


	/**
	* Returns the User with the specified pk.
	* Throws FinderException if there is none.
	*/
	public User findByPrimaryKey(UserPK userPK)
			throws FinderException, RemoteException;


	/**
	* Returns the User with the specified name.
	* Throws FinderException if there is none.
	*/
	public User findByName(String userName)
			throws FinderException, RemoteException;


	/**
	* Returns true if the specifed userName exists in the system.
	*/
	public boolean userExists(String userName) throws RemoteException;


	/**
	* Creates a new User with the specified userName, linked to the
	* specified Customer.
	*
	* Throws DuplicateKeyException if the userName already
	* exists.
	*
	* Throws BusinessLogicError if there is another User or
	* Venue with the same CustomerPK.
	*
	* @exception MalformedPasswordException the specified password
	* does not adhere to system rules for well formed passwords.
	*/
	public User createUser(String userName, char[] password,
			String passwordReminderAnswer, Customer customer)
			throws CreateException, PasswordException, RemoteException;


	/**
	* Replaces an existing User with the specified user data.
	* Throws BusinessLogicError if there is no user with the
	* specified userName.
	*/
	public User updateUser(User user) throws RemoteException;


	/**
	* Returns the User who is associated with the specified Customer.
	*/
	public User findByCustomer(CustomerPK customerPK)
			throws FinderException, RemoteException;


	/**
	 * returns user who is associated with this password reminder
	 * info (email address, city, state, zipcode and reminder answer).
	 */
	public User findByReminderInfo(String email,
			String city, String state, String zipcode,
			String answer) throws FinderException, RemoteException;

}
