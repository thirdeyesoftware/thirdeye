package com.iobeam.portal.task.customer.manageuser;


import java.rmi.RemoteException;
import javax.ejb.EJBObject;
import com.iobeam.portal.util.ContactName;
import com.iobeam.portal.task.UseCaseController;
import com.iobeam.portal.security.*;
import com.iobeam.portal.model.customercontact.CustomerContact;
import com.iobeam.portal.model.customer.CustomerData;
import com.iobeam.portal.model.address.Address;


public interface ManageUser extends UseCaseController, EJBObject {

	/**
	* Creates a new User with the specified name, password,
	* and contact information.
	*
	* @exception MalformedPasswordException the specified newPassword
	* breaks the rules for proper password composition.
	*
	* @exception DuplicateUserException the specified userName
	* is already in use.
	*/
	public User createUser(String userName, char[] password,
			String passwordReminderAnswer, CustomerContact customerContact)
			throws DuplicateUserException, PasswordException, RemoteException;


	/**
	* Changes the password on the specified User.
	*
	* @exception InvalidPasswordException the specified oldPassword
	* is incorrect.
	*
	* @exception MalformedPasswordException the specified newPassword
	* breaks the rules for proper password composition.
	*/
	public void setPassword(User user,
			char[] oldPassword, char[] newPassword)
			throws PasswordException, RemoteException;


	/**
	* Returns the CustomerData for the specified User.
	*/
	public CustomerData getCustomerData(User user)
			throws RemoteException;


	/**
	*/
	public void changeContact(User user, ContactName contactName)
			throws RemoteException;


	/**
	*/
	public void changeAddress(User user, Address address)
			throws RemoteException;
}
