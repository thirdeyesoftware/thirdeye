package com.iobeam.portal.model.billablecustomer;


import java.rmi.RemoteException;
import javax.ejb.*;
import java.util.*;
import com.iobeam.portal.util.*;
import com.iobeam.portal.model.customer.*;
import com.iobeam.portal.model.customercontact.*;
import com.iobeam.portal.model.billing.PaymentInstrument;
import com.iobeam.portal.model.subscription.SubscriptionType;


/**
* An individual or business that subscribes to or purchases services or
* products from iobeam.
*/
public class BillableCustomerBean implements EntityBean {

	private BillableCustomerData mData;
	private transient boolean mIsModified;
	private EntityContext mContext;


	public void setEntityContext(EntityContext context) {
		mContext = context;
	}


	public void unsetEntityContext() {
		mContext = null;
	}


	private EntityContext getContext() {
		return mContext;
	}


	private BillableCustomerPK getPK() {
		return (BillableCustomerPK) getContext().getPrimaryKey();
	}


	public BillableCustomerPK ejbCreate(Customer customer,
			PaymentInstrument pi) throws CreateException {

		BillableCustomerData customerData;


		try {
			CustomerData custData = customer.getData();

			customerData = new BillableCustomerData(
					new BillableCustomerPK(custData.getPK()),
					custData.getParentCustomerPK(),
					custData.getCustomerContact(), pi, true);

			customerData = BillableCustomerDAO.update(customerData);
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		mData = customerData;

		return mData.getPK();
	}


	public void ejbPostCreate(Customer customer, PaymentInstrument pi)
			throws CreateException {
		setModified(false);
	}


	public BillableCustomerPK ejbFindByPrimaryKey(BillableCustomerPK pk)
			throws FinderException {

		try {
			if (BillableCustomerDAO.pkExists(pk)) {
				return pk;
			} else {
				throw new ObjectNotFoundException(pk.toString());
			}
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


	public void ejbLoad() {
		try {
			mData = BillableCustomerDAO.select(getPK());
			if (mData == null) {
				throw new NoSuchEntityException(getPK().toString());
			}
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		setModified(false);
	}


	public void ejbRemove() throws RemoveException {
		try {
			BillableCustomerDAO.delete(mData);
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


	public void ejbStore() {
		if (isModified()) {
			return;
		}

		try {
			BillableCustomerDAO.update(mData);
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		setModified(false);
	}


	public void ejbActivate() {
	}

	public void ejbPassivate() {
	}

	private void setModified(boolean isModified) {
		mIsModified = isModified;
	}

	private boolean isModified() {
		return mIsModified;
	}


	/**
	* Returns the Customer instance decorated by this BillableCustomner.
	*/
	private Customer getCustomer() {
		// lookup the Customer using this instance's pk.

		throw new UnsupportedOperationException("not impl yet");
	}



	/**
	* Returns a Collection of all dependent Customers for this instance.
	* Returns an empty Collection if there are none.
	*
	* @return Collection of CustomerData.
	*/
	public Collection getChildren() {
		try {
			return getCustomer().getChildren();
		}
		catch (RemoteException re) {
			throw new EJBException(re);
		}
	}


	/**
	* Returns a Collection of AccountData for all all Accounts for
	* this Customer.
	*
	* @return Collection of AccountData.
	*/
	public Collection getAccounts() {
		try {
			return getCustomer().getAccounts();
		}
		catch (RemoteException re) {
			throw new EJBException(re);
		}
	}


	/**
	* Returns true if this Customer is not a child of another Customer.
	*/
	public boolean isRootCustomer() {
		try {
			return getCustomer().isRootCustomer();
		}
		catch (RemoteException re) {
			throw new EJBException(re);
		}
	}


	/**
	* Returns a Collection of BillableParties that may be assigned
	* to Accounts owned by this Customer.   The underlying rules
	* for this method may vary by Customer implementation, and/or
	* by Customer data.
	*/
	public Collection getAvailableBillableParties() {
		try {
			return getCustomer().getAvailableBillableParties();
		}
		catch (RemoteException re) {
			throw new EJBException(re);
		}
	}


	/**
	* Returns the contact information describing the individual who is
	* the primary contact for this Customer.  In the case of
	* individual Customers, the contact is the Customer.
	*/
	public CustomerContact getCustomerContact() {
		try {
			return getCustomer().getCustomerContact();
		}
		catch (RemoteException re) {
			throw new EJBException(re);
		}
	}

	/**
	* Sets the contact information for this Customer.
	*/
	public void setCustomerContact(CustomerContact customerContact) {
		throw new UnsupportedOperationException("use setBillingContact()");
	}

	/**
	* Sets the contact information for this Customer.
	*/
	public void setBillingContact(CustomerContact customerContact) {
		try {
			getCustomer().setCustomerContact(customerContact);
		}
		catch (RemoteException re) {
			throw new EJBException(re);
		}
	}


	/**
	* Set the contact information used for billing.
	*/
	public void setBillingCustomerContact(CustomerContact billingContact) {

		mData.setCustomerContact(billingContact);

		setModified(true);
	}


	/**
	* Returns the contact information used for billing.
	*/
	public CustomerContact getBillingCustomerContact() {
		return mData.getCustomerContact();
	}


	/**
	* Returns a Collection of active-status Subscriptions of the specified
	* SubscriptionType, for all Accounts owned by this Customer.
	*/
	public List getActiveSubscriptions(
			SubscriptionType subscriptionType) {
		try {
			return getCustomer().getActiveSubscriptions(subscriptionType);
		}
		catch (RemoteException re) {
			throw new EJBException(re);
		}
	}


	/**
	* Returns a Collection of created-status Subscriptions of the specified
	* SubscriptionType, for all Accounts owned by this Customer.
	*/
	public Collection getCreatedSubscriptions(
			SubscriptionType subscriptionType) {
		try {
			return getCustomer().getCreatedSubscriptions(subscriptionType);
		}
		catch (RemoteException re) {
			throw new EJBException(re);
		}
	}


	/**
	* Returns the CustomerData instance describing this Customer.
	*/
	public CustomerData getData() {
		throw new UnsupportedOperationException("not impl yet");
	}

	public void setData(CustomerData data) {
		throw new UnsupportedOperationException("not impl yet.");
	}


	/**
	* Returns the BillableCustomerData instance describing this Customer.
	*/
	public BillableCustomerData getBillableCustomerData() {
		return mData;
	}

	/**
	* returns payment instrument for billable party.
	*/
	public PaymentInstrument getPaymentInstrument() {
		return mData.getPaymentInstrument();
	}

	/**
	 * fix this dependency.
	 */
	public void setParentCustomerPK(CustomerPK pk) {

	}

}
