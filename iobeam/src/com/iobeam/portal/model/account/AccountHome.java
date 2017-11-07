package com.iobeam.portal.model.account;


import java.util.Collection;
import java.util.Date;
import java.rmi.RemoteException;
import javax.ejb.*;
import com.iobeam.portal.util.*;
import com.iobeam.portal.model.customer.*;
import com.iobeam.portal.model.billablecustomer.*;
import com.iobeam.portal.model.invoice.*;


/**
*/
public interface AccountHome extends EJBHome {


	public static final String JNDI_NAME = "iobeam.portal.AccountHome";


	/**
	* Creates an Account, owned by a Customer, and paid by a BillableParty.
	*/
	public Account create(Customer customer, BillableCustomer billableParty)
			throws CreateException, RemoteException;


	/**
	* Creates an Account owned by the specified Customer.  There is
	* no BillableParty on the Account.
	*
	* This type of Account is unbillable, and cannot accept
	* Subscriptions that result in charges to the Account.
	*/
	public Account create(Customer customer)
			throws CreateException, RemoteException;


	public Account findByPrimaryKey(AccountPK pk)
			throws FinderException, RemoteException;


	public Account findByAccountNumber(AccountNumber accountNumber)
			throws FinderException, RemoteException;


	/**
	* Returns the Account for the specified Invoice.
	*/
	public Account findByInvoice(InvoicePK invoicePK)
			throws FinderException, RemoteException;


	/**
	* Returns a Collection of Accounts having activity occurring
	* within the specified time period.
	*/
	public Collection findByActivity(Date startDate, Date endDate)
			throws FinderException, RemoteException;

}
