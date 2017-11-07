package com.iobeam.portal.model.billing;


import com.iobeam.portal.model.customercontact.CustomerContact;
import java.rmi.RemoteException;


/**
* A entity capable of paying iobeam for services or products.
* This is not necessarily the same entity that bought the products.
* BillableParty is an abstraction that describes properties and behaviors
* needed for payment to iobeam.
*/
public interface BillableParty {

	/**
	* Returns the PaymentInstrument used by this BillableParty for
	* making a Payment on an Account.
	*/
	public PaymentInstrument getPaymentInstrument() throws RemoteException;


	/**
	* Returns the contact information used for billing purposes.
	*/
	public CustomerContact getBillingCustomerContact() throws RemoteException;
}
