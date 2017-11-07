package com.iobeam.portal.task.gateway.manageusercontact;


import java.rmi.RemoteException;
import javax.ejb.*;
import java.net.InetAddress;
import com.iobeam.util.MACAddress;
import com.iobeam.portal.security.NoSuchVenueException;
import com.iobeam.portal.security.GatewayException;
import com.iobeam.portal.model.gateway.usercontact.*;
import com.iobeam.portal.model.user.*;
import com.iobeam.portal.model.venue.*;
import com.iobeam.portal.task.UseCaseController;


public interface ManageUserContact extends UseCaseController, EJBObject {

	/**
	* Records a user-contact at the specified Venue.  The resulting
	* user-contact describes an unknown client machine making network
	* contact with a Gateway.
	*
	* @exception NoSuchVenueException the specified VenuePK is not
	* known to the system.
	*/
	public ContactID logInitialContact(VenuePK venuePK,
			InetAddress venueIP, InetAddress userIP,
			MACAddress userMAC, long siteKey)
			throws NoSuchVenueException, GatewayException,
			RemoteException;


	/**
	* Associates the specified User with the specified Contact.
	* This association occurs at User signon time.
	*/
	public void bindUser(UserPK userPK, ContactID contactID)
			throws RemoteException;


	/**
	* Associates the specified UserContact with an anonymous signon.
	* This association occurs at user signon time.
	*/
	public void bindAnonymous(ContactID contactID)
			throws RemoteException;
}
