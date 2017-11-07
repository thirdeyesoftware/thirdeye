package com.iobeam.portal.model.billablecustomer;


import java.rmi.RemoteException;
import javax.ejb.EJBObject;
import com.iobeam.portal.model.customer.Customer;
import com.iobeam.portal.model.billing.*;
import com.iobeam.portal.model.customercontact.CustomerContact;


/**
* The only allowed implementation of BillableParty is also a Customer.
* All realized BillableParty instances are Customers.
*/
public interface BillableCustomer extends Customer, BillableParty, EJBObject {

	/**
	* Set the contact information used for billing.
	*/
	public void setBillingContact(CustomerContact billingContact)
			throws RemoteException;


	public BillableCustomerData getBillableCustomerData()
			throws RemoteException;


	/**
	* Returns payment instrument for BillableParty.
	*/
	public PaymentInstrument getPaymentInstrument() throws RemoteException;
}
