package com.iobeam.portal.model.gateway.usercontact;


import javax.ejb.*;
import java.util.*;
import java.net.InetAddress;
import com.iobeam.util.MACAddress;
import com.iobeam.portal.util.DataAccessException;
import com.iobeam.portal.model.venue.VenuePK;
import com.iobeam.portal.model.user.UserPK;


public class AccessUserContactBean implements SessionBean {

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
	*/
	public UserContact findByContactID(ContactID contactID)
			throws FinderException {

		UserContact userContact = null;

		try {
			userContact = UserContactDAO.select(contactID);

			if (userContact == null) {
				throw new ObjectNotFoundException(contactID.toString());
			}
		}
		catch (FinderException fe) {
			throw fe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		return userContact;
	}

	/**
	*/
	public Collection findAllByVenuePKByDate(VenuePK venuePK, 
			Date startdate, Date enddate) throws FinderException {

		Collection c;

		try {
			c = UserContactDAO.selectAllByVenueByDate(venuePK, startdate, enddate);

		}
		catch (DataAccessException de) {
			throw new FinderException(de.toString());
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		return c;
	}

	/**
	*/
	public Collection findAllByUserPKByDate(UserPK userPK, 
			Date startdate, Date enddate) throws FinderException {

		Collection c;

		try {
			c = UserContactDAO.selectAllByDate(userPK, startdate, enddate);

		}
		catch (DataAccessException de) {
			throw new FinderException(de.toString());
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		return c;
	}

	/**
	* selects collection of months specified by userpk
	*/
	public Collection findUsageMonthsByUserPK(UserPK userPK) 
			throws FinderException {

		Collection c;

		try {
			c = UserContactDAO.selectUsageMonthsByUserPK(userPK); 

		}
		catch (DataAccessException de) {
			throw new FinderException(de.toString());
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		return c;
	}

	/**
	*/
	public Collection findAllByUserPK(UserPK userPK)
			throws FinderException {

		Collection c;

		try {
			c = UserContactDAO.selectAll(userPK);

		}
		catch (DataAccessException de) {
			throw new FinderException(de.toString());
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		return c;
	}

	/**
	*/
	public Collection findAll() throws FinderException {

		Collection c;

		try {
			c = UserContactDAO.selectAll();

		}
		catch (DataAccessException de) {
			throw new FinderException(de.toString());
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		return c;
	}

	/**
	*/
	public UserContact findByUserPK(UserPK userPK)
			throws FinderException {

		UserContact userContact = null;

		try {
			userContact = UserContactDAO.select(userPK);

			if (userContact == null) {
				throw new ObjectNotFoundException(userPK.toString());
			}
		}
		catch (FinderException fe) {
			throw fe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		return userContact;
	}


	/**
	*/
	public UserContact create(VenuePK venuePK, InetAddress userIPAddress,
			MACAddress userMACAddress)
			throws CreateException {

		UserContact userContact = new UserContact(venuePK, userIPAddress,
				userMACAddress);

		try {
			userContact = UserContactDAO.create(userContact);
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		return userContact;
	}


	/**
	* Removes all unbound UserContacts with Timestamp older
	* than the specified cutoff time.
	*/
	public void removeUnboundStaleContacts(Date cutoffTime) {
		throw new UnsupportedOperationException("not impl yet");
	}


	/**
	* Replaces an existing UserContact with the specified UserContact data.
	*/
	public void update(UserContact userContact) {
		try {
			userContact = UserContactDAO.update(userContact);
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}
}
