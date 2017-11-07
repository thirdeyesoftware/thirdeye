package com.iobeam.portal.model.customernotice;

import javax.ejb.EJBObject;
import javax.ejb.FinderException;
import java.rmi.RemoteException;
import java.util.Collection;
import com.iobeam.portal.model.EntityAccessor;

/**
 * Customer Notice accessor is an entity accessor
 */
public interface AccessCustomerNotice extends EntityAccessor, EJBObject {
	
	public static final long ALL_VENUES = -1;

	public Collection findAll() 
			throws FinderException, RemoteException;

	public Collection findActiveByVenueId(long venueid)
			throws FinderException, RemoteException;

	public Collection findAllActive()
			throws FinderException, RemoteException;

	public Collection findActiveAllVenues() 
			throws FinderException, RemoteException;
			
}
