package com.iobeam.portal.task.billing.account.manageaccount;

import javax.ejb.EJBObject;
import javax.ejb.CreateException;
import java.rmi.RemoteException;
import com.iobeam.portal.model.customer.Customer;
import com.iobeam.portal.model.billablecustomer.BillableCustomer;
import com.iobeam.portal.model.account.Account;
import com.iobeam.portal.model.account.AccountStatus;

public interface ManageAccount extends EJBObject {
	
	public Account createAccount(
			Customer customer, BillableCustomer billableCustomer, 
			AccountStatus status) throws CreateException, RemoteException;
	
}

