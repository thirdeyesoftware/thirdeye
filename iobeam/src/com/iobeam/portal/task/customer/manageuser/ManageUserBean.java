package com.iobeam.portal.task.customer.manageuser;


import javax.ejb.*;
import javax.naming.*;
import java.util.logging.Logger;
import java.rmi.RemoteException;
import com.iobeam.portal.util.ContactName;
import com.iobeam.portal.model.customercontact.CustomerContact;
import com.iobeam.portal.model.customer.*;
import com.iobeam.portal.model.address.Address;
import com.iobeam.portal.security.*;
import com.iobeam.portal.task.customer.managecustomer.*;
import com.iobeam.portal.model.customer.CustomerData;
import com.iobeam.portal.model.user.*;


public class ManageUserBean implements SessionBean {
	private SessionContext mContext;

	public void ejbCreate() throws CreateException {
	}

	public void ejbActivate() {

	}
	public void ejbPassivate() {
	}
	
	public void ejbRemove() {
	}
	
	public void setSessionContext(SessionContext context) {
		mContext = context;
	}
	
	public SessionContext getSessionContext() {
		return mContext;
	}




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
			throws DuplicateUserException, PasswordException {

		try {
			Customer customer = getManageCustomer().createCustomer(
					customerContact);

			if (getAccessUser().userExists(userName)) {
				getSessionContext().setRollbackOnly();
				throw new DuplicateUserException(userName);
			}

			return getAccessUser().createUser(userName, password, 
				passwordReminderAnswer, customer);
		}
		catch (RemoteException re) {
			Logger logger = Logger.getLogger("com.iobeam.portal.task");
			logger.throwing(ManageUserBean.class.getName(), "createUser", 
					(Throwable)re);
			throw new EJBException(re);
		}
		catch (DuplicateUserException due) {
			throw due;
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (PasswordException pe) {
			throw pe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


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
			throws PasswordException {

		String oldPasswordHash = 
			PasswordHash.getHash(oldPassword, user.getPasswordSalt());

		if (!oldPasswordHash.equals(user.getPasswordHash())) {
			throw new InvalidPasswordException("invalid old password");
		}

		user.setPassword(newPassword);
		
		try {
			getAccessUser().updateUser(user);
		}
		catch (RemoteException re) { 
			throw new EJBException(re);
		}


	}


	/**
	* Returns the CustomerData for the specified User.
	*/
	public CustomerData getCustomerData(User user) {
		throw new UnsupportedOperationException("no impl yet");
	}


	/**
	*/
	public void changeContact(User user, ContactName contactName) {
		throw new UnsupportedOperationException("no impl yet");
	}


	/**
	*/
	public void changeAddress(User user, Address address) {
		throw new UnsupportedOperationException("no impl yet");
	}


	private ManageCustomer getManageCustomer() {
		try {
			InitialContext ic = new InitialContext();
			ManageCustomerHome mcHome = 
				(ManageCustomerHome)ic.lookup(ManageCustomerHome.JNDI_NAME);
			return mcHome.create();
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (Exception e) {
			throw new EJBException(e.toString());
		}
	}


	private AccessUser getAccessUser() {

		try {
			InitialContext ic = new InitialContext();

			AccessUserHome h =
					(AccessUserHome)ic.lookup(AccessUserHome.JNDI_NAME);

			return h.create();
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}
}

