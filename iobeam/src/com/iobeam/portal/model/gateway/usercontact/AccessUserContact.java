package com.iobeam.portal.model.gateway.usercontact;


import java.rmi.RemoteException;
import javax.ejb.*;
import java.net.InetAddress;
import java.util.*;
import com.iobeam.util.MACAddress;
import com.iobeam.portal.model.EntityAccessor;
import com.iobeam.portal.model.user.UserPK;
import com.iobeam.portal.model.venue.VenuePK;


public interface AccessUserContact extends EntityAccessor, EJBObject {

	/**
	*/
	public UserContact findByContactID(ContactID contactID)
			throws FinderException, RemoteException;


	/**
	*/
	public UserContact findByUserPK(UserPK userPK)
			throws FinderException, RemoteException;
	
	/**
	 * returns a collection of user contact instances specified
	 * by VenuePK
	 */
	public Collection findAllByVenuePKByDate(VenuePK VenuePK, Date startdate, 
			Date enddate) throws FinderException, RemoteException;
	/**


	/**
	 * returns a collection of user contact instances specified
	 * by UserPK
	 */
	public Collection findAllByUserPKByDate(UserPK userPK, Date startdate, 
			Date enddate) throws FinderException, RemoteException;
	/**

	/**
	 * returns a collection of user contact instances specified
	 * by UserPK
	 */
	public Collection findAllByUserPK(UserPK userPK)
			throws FinderException, RemoteException;

	/**
	 * selects collection of dates which relate to 
	 * usage months for a specified user
	 */
	public Collection findUsageMonthsByUserPK(UserPK userpk)
			throws FinderException, RemoteException;

	/**
	*/
	public UserContact create(VenuePK venuePK, InetAddress userIPAddress,
			MACAddress userMACAddress)
			throws CreateException, RemoteException;


	/**
	* Removes all unbound UserContacts with Timestamp older
	* than the specified cutoff time.
	*/
	public void removeUnboundStaleContacts(Date cutoffTime)
			throws RemoteException;


	/**
	* Replaces an existing UserContact with the specified UserContact data.
	*/
	public void update(UserContact userContact) throws RemoteException;
}
