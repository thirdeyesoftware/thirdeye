package com.iobeam.portal.model.customernotice;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;
import java.rmi.RemoteException;


public interface AccessCustomerNoticeHome extends EJBHome {

	public static final String JNDI_NAME =
		"iobeam.portal.AccessCustomerNoticeHome";

	public AccessCustomerNotice create() 
			throws CreateException, RemoteException;
	
}
