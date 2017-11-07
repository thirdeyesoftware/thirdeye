package com.iobeam.portal.model.customer;


import java.rmi.RemoteException;
import javax.ejb.*;
import com.iobeam.portal.model.customercontact.*;


/**
*/
public interface CustomerHome extends EJBHome {
	public static final String JNDI_NAME = "iobeam.portal.CustomerHome";

	public Customer create(CustomerContact customerContact)
			throws CreateException, RemoteException;


	public Customer findByPrimaryKey(CustomerPK pk)
			throws FinderException, RemoteException;
	

}
