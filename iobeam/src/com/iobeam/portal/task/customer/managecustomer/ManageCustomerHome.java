package com.iobeam.portal.task.customer.managecustomer;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import java.rmi.RemoteException;

public interface ManageCustomerHome extends EJBHome {

	public static final String JNDI_NAME = 
		"iobeam.portal.ManageCustomerHome";

	public ManageCustomer create() throws CreateException, RemoteException;

}

