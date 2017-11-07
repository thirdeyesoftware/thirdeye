package com.iobeam.portal.task.customer.managecustomer;

import javax.ejb.EJBObject;
import javax.ejb.CreateException;
import java.rmi.RemoteException;

import com.iobeam.portal.model.customer.*;
import com.iobeam.portal.model.billablecustomer.*;
import com.iobeam.portal.model.customercontact.*;
import com.iobeam.portal.model.billing.PaymentInstrument;

public interface ManageCustomer extends EJBObject {
		
	public Customer createCustomer(CustomerContact contact) 
			throws CreateException, RemoteException;
	

	public BillableCustomer createBillableCustomer(Customer customer,
			PaymentInstrument instrument)
			throws CreateException, RemoteException;


	/**
	* Removes the specified Customer and its unreferenced
	* dependencies from the system.
	*/
	public void removeCustomer(CustomerPK customerPK)
			throws RemoteException;
}
