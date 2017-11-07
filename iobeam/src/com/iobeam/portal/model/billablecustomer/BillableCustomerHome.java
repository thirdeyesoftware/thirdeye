package com.iobeam.portal.model.billablecustomer;


import java.rmi.RemoteException;
import javax.ejb.*;
import com.iobeam.portal.util.*;
import com.iobeam.portal.model.customer.*;
import com.iobeam.portal.model.billing.*;


/**
*/
public interface BillableCustomerHome extends EJBHome {
	public static final String JNDI_NAME = "iobeam.portal.BillableCustomerHome";
	

	public BillableCustomer create(Customer customer, PaymentInstrument pi)
			throws CreateException, RemoteException;


	public BillableCustomer findByPrimaryKey(BillableCustomerPK pk)
			throws FinderException, RemoteException;
}
