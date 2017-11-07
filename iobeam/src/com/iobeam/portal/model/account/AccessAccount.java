package com.iobeam.portal.model.account;

import javax.ejb.EJBObject;
import javax.ejb.FinderException;
import java.rmi.RemoteException;
import java.util.Collection;
import com.iobeam.portal.model.EntityAccessor;
import com.iobeam.portal.model.customer.CustomerPK;
import com.iobeam.portal.model.billablecustomer.BillableCustomerPK;

/**
 * account accessor is an entity accessor
 */
public interface AccessAccount extends EntityAccessor, EJBObject {

	public Collection findAllOpenAccounts() 
			throws FinderException, RemoteException;

	public Collection findByCustomerPK(CustomerPK pk) 
			throws FinderException, RemoteException;

	public Collection findByBillableCustomerPK(BillableCustomerPK pk) 
			throws FinderException, RemoteException;

	public AccountEntry findAccountEntry(long id) 
			throws FinderException, RemoteException;

}
