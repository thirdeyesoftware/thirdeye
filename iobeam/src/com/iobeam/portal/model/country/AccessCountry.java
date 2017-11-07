package com.iobeam.portal.model.country;


import java.util.*;
import java.rmi.RemoteException;
import javax.ejb.*;
import com.iobeam.portal.model.EntityAccessor;
import com.iobeam.portal.model.venue.VenuePK;


public interface AccessCountry extends EntityAccessor, EJBObject {

	/**
	* Returns the Country instance for the specified pk.
	*/
	public Country findByPrimaryKey(CountryPK countryPK)
			throws FinderException, RemoteException;


	/**
	* Returns a Collection of all Country instances known to the system.
	*/
	public Collection findAll() throws RemoteException;
}
