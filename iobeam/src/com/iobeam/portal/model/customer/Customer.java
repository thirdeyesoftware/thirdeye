package com.iobeam.portal.model.customer;


import java.rmi.RemoteException;
import javax.ejb.EJBObject;
import java.util.Collection;
import java.util.List;
import com.iobeam.portal.util.*;
import com.iobeam.portal.model.subscription.*;
import com.iobeam.portal.model.customercontact.CustomerContact;


/**
* An individual or business that subscribes to or purchases services or
* products from iobeam.
*/
public interface Customer extends EJBObject {


	/**
	* Returns a Collection of all dependent Customers for this instance.
	* Returns an empty Collection if there are none.
	*
	* @return Collection of CustomerData.
	*/
	public Collection getChildren() throws RemoteException;


	/**
	* Returns a Collection of AccountData for all all Accounts for
	* this Customer.
	*
	* @return Collection of AccountData.
	*/
	public Collection getAccounts() throws RemoteException;


	/**
	* Returns true if this Customer is not a child of another Customer.
	*/
	public boolean isRootCustomer() throws RemoteException;


	/**
	* Returns a Collection of BillableParties that may be assigned
	* to Accounts owned by this Customer.   The underlying rules
	* for this method may vary by Customer implementation, and/or
	* by Customer data.
	*/
	public Collection getAvailableBillableParties()
			throws RemoteException;


	/**
	* Returns the contact information describing the individual who is
	* the primary contact for this Customer.  In the case of
	* individual Customers, the contact is the Customer.
	*/
	public CustomerContact getCustomerContact() throws RemoteException;


	/**
	* Sets the contact information for this Customer.
	*/
	public void setCustomerContact(CustomerContact customerContact)
			throws RemoteException;


	/**
	* Sets the parent customer pk for this customer.
	*/
	public void setParentCustomerPK(CustomerPK pk)
			throws RemoteException;

	/**
	* Returns a Collection of active-status Subscriptions of the specified
	* SubscriptionType, for all Accounts owned by this Customer.
	*/
	public List getActiveSubscriptions(
			SubscriptionType subscriptionType) throws RemoteException;


	/**
	* Returns a Collection of created-status Subscriptions of the specified
	* SubscriptionType, for all Accounts owned by this Customer.
	*/
	public Collection getCreatedSubscriptions(
			SubscriptionType subscriptionType) throws RemoteException;


	/**
	* Returns the CustomerData instance describing this Customer.
	*/
	public CustomerData getData() throws RemoteException;

	/** 
	 * sets the customer data for this customer.
	 */
	public void setData(CustomerData data) throws RemoteException;


}
