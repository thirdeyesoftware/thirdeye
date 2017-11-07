package com.iobeam.portal.task.gateway.leasenotify;


import java.util.Date;
import java.rmi.RemoteException;
import javax.ejb.*;
import com.iobeam.portal.task.UseCaseController;
import com.iobeam.portal.security.*;
import com.iobeam.portal.model.gateway.usercontact.*;


public interface NotifyGateway extends UseCaseController, EJBObject {

	/**
	* Enables the specified User for internet access
	* at the Gateway where the User is currently located.
	*
	* @param expirationDate the date and time at which the access
	* rights must expire on the Gateway, or null if there is no
	* expiration date.
	*/
	public void enableUser(User user, Date expirationDate)
			throws RemoteException;


	/**
	* Enables the member associated with the specified contact
	* for internet access at the Gateway where the contact
	* was established.
	*
	* @param expirationDate the date and time at which the access
	* rights must expire on the Gateway, or null if there is no
	* expiration date.
	*/
	public void enableAnonymousContact(ContactID contactID,
			Date expirationDate)
			throws RemoteException;


	/**
	* Disables the specified User's internet connection
	* at the Gateway where the User is currently located.
	*/
	public void disableUser(User user) throws RemoteException;


	/**
	* Disables the internet connection of the member associated
	* with the specified contact at the Gateway where the contact
	* was established.
	*/
	public void disableAnonymousContact(ContactID contactID)
			throws RemoteException;
}
