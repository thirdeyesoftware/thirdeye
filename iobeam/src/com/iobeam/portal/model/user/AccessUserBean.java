package com.iobeam.portal.model.user;


import java.util.Collection;
import java.rmi.RemoteException;
import javax.ejb.*;
import com.iobeam.portal.util.*;
import com.iobeam.portal.security.*;
import com.iobeam.portal.model.customer.*;
import com.iobeam.portal.model.venue.*;


public class AccessUserBean implements SessionBean {

	private SessionContext mSessionContext;


	public void setSessionContext(SessionContext context) {
		mSessionContext = context;
	}


	public void ejbCreate() throws CreateException {
	}


	public void ejbActivate() {
	}


	public void ejbPassivate() {
	}


	public void ejbRemove() {
	}



	/**
	* Returns the User with the specified pk.
	* Throws FinderException if there is none.
	*/
	public User findByPrimaryKey(UserPK userPK)
			throws FinderException {

		UserData userData = null;

		try {
			userData = UserDAO.select(userPK);

			if (userData == null) {
				throw new ObjectNotFoundException(userPK.toString());
			}
		}
		catch (FinderException fe) {
			throw fe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		return userData;
	}


	/**
	* Returns the User with the specified name.
	* Throws FinderException if there is none.
	*/
	public User findByName(String userName) throws FinderException {
		UserData userData = null;

		try {
			userData = UserDAO.select(userName);

			if (userData == null) {
				throw new ObjectNotFoundException("no user with name " +
						userName);
			}
		}
		catch (FinderException fe) {
			throw fe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		return userData;
	}


	/**
	* Returns true if the specifed userName exists in the system.
	*/
	public boolean userExists(String userName) {
		UserData userData = null;

		try {
			userData = UserDAO.select(userName);

			if (userData != null) {
				return true;
			} else {
				return false;
			}
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


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
				throws CreateException, PasswordException {

		UserData userData = null;


		try {
			CustomerPK customerPK = (CustomerPK) customer.getPrimaryKey();

			if (UserDAO.userExists(customerPK)) {
				throw new BusinessLogicError("user exists for " + customerPK);
			}

			if (VenueDAO.venueExists(customerPK)) {
				throw new BusinessLogicError("user exists for " + customerPK);
			}

			if (UserDAO.userExists(userName)) {
				throw new DuplicateKeyException("user exists with name " +
						userName);
			}

			userData = new UserData(userName, password, passwordReminderAnswer,
					customerPK);

			userData = UserDAO.create(userData);
		}
		catch (DuplicateKeyException dke) {
			throw dke;
		}
		catch (PasswordException pe) {
			throw pe;
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		return userData;
	}


	/**
	* Replaces an existing User with the specified user data.
	* Throws BusinessLogicError if there is no user with the
	* specified pk.
	*/
	public User updateUser(User user) {
		UserData userData = null;

		try {
			userData = new UserData(user.getUserPK(),
					user.getUserName(), user.getPasswordHash(),
					user.getPasswordSalt(), user.getCustomerPK(),
					user.getPasswordReminderAnswer());

			userData = UserDAO.update(userData);
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		return userData;
	}


	/**
	* Returns the User who is associated with the specified Customer.
	*/
	public User findByCustomer(CustomerPK customerPK)
			throws FinderException {

		UserData userData = null;

		try {
			userData = UserDAO.select(customerPK);

			if (userData == null) {
				throw new ObjectNotFoundException(customerPK.toString());
			}
		}
		catch (FinderException fe) {
			throw fe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		return userData;
	}

	/**
	 * returns User who is associated with this email address, city,
	 * state, zipcode and password reminder answer.
	 */
	public User findByReminderInfo(String email,
			String city, String state, String zipcode, String answer) 
				throws FinderException {
		UserData userData = null;
		try {
			userData = UserDAO.selectByReminderInfo(email, city, 
				state, zipcode, answer);
			if (userData == null) {
				throw new ObjectNotFoundException(email);
			}
		}
		catch (FinderException fe) {
			throw fe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
		return userData;
	}

}
