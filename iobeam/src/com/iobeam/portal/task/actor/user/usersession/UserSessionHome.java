package com.iobeam.portal.task.actor.user.usersession;


import javax.ejb.EJBHome;
import javax.ejb.CreateException;
import java.rmi.RemoteException;
import com.iobeam.portal.security.NoSuchVenueException;
import com.iobeam.portal.model.gateway.usercontact.*;
import com.iobeam.portal.model.venue.VenuePK;


public interface UserSessionHome extends EJBHome {

	public static final String JNDI_NAME = "iobeam.portal.UserSessionHome";

	/**
	* Creates a new UserSession that is not yet bound to a User.
	*
	* @exception NoSuchVenueException if the venue is not 
	* known to the system.
	*/
	public UserSession create(ContactID contactID, VenuePK venuePK)
			throws NoSuchVenueException, CreateException, RemoteException;
	

	/**
	* Creates a new portal-only UserSession that is not yet bound to a User.
	*
	* The resulting session is marked portal-only, and cannot be
	* used for operations pertaining only to venue-based UserSessions.
	*/
	public UserSession create() throws CreateException, RemoteException;


	/**
	* Creates a new portal-only UserSession that is not yet bound to a User.
	* This UserSession has originated at the specified Venue.
	*
	* The resulting session is marked portal-only, and cannot be
	* used for operations pertaining only to venue-based UserSessions.
	*
	* @exception NoSuchVenueException if the venue is not 
	* known to the system.
	*/
	public UserSession create(VenuePK venuePK)
			throws NoSuchVenueException, CreateException, RemoteException;
}
