package com.iobeam.portal.model.customer;


import javax.ejb.*;
import javax.naming.*;
import java.util.*;
import java.util.logging.*;
import com.iobeam.portal.util.*;
import com.iobeam.portal.model.account.*;
import com.iobeam.portal.model.subscription.*;
import com.iobeam.portal.model.customercontact.*;


/**
* An individual or business that subscribes to or purchases services or
* products from iobeam.
*/
public class CustomerBean implements EntityBean {

	private CustomerData mData;
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


	private CustomerPK getPK() {
		return (CustomerPK) getContext().getPrimaryKey();
	}


	public CustomerPK ejbCreate(CustomerContact customerContact)
			throws CreateException {

		CustomerData customerData;

		customerData = new CustomerData(customerContact, null, true);

		try {
			customerData = CustomerDAO.create(customerData);
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		mData = customerData;

		return mData.getPK();
	}


	public void ejbPostCreate(CustomerContact customerContact)
			throws CreateException {
		setModified(false);
	}


	public CustomerPK ejbFindByPrimaryKey(CustomerPK pk)
			throws FinderException {

		try {
			if (CustomerDAO.pkExists(pk)) {
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
			mData = CustomerDAO.select(getPK());
			if (mData == null) {
				throw new NoSuchEntityException(getPK().toString());
			}
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}


		setModified(false);
	}


	public void ejbRemove() throws RemoveException {
		try {
			CustomerDAO.delete(mData);
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


	public void ejbStore() {
		if (!isModified()) {
			return;
		}
		Logger l = Logger.getLogger("com.iobeam.portal.model.customer");
		l.info("CustomerBean.ejbStore():<start>");

		try {
			CustomerDAO.update(mData);
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
		l.info("CustomerBean.ejbStore():<end>");
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
	* Returns a Collection of all dependent Customers for this instance.
	* Returns an empty Collection if there are none.
	*
	* @return Collection of CustomerData.
	*/
	public Collection getChildren() {
		throw new UnsupportedOperationException("not impl yet");
	}


	/**
	* Returns a Collection of AccountData for all all Accounts for
	* this Customer.
	*
	* @return Collection of AccountData.
	*/
	public Collection getAccounts() {
		try {
			return getAccessAccount().findByCustomerPK(getPK());
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


	private AccessAccount getAccessAccount() {

		AccessAccountHome h = null;
		AccessAccount b = null;
		InitialContext ic = null;

		try {
			ic = new InitialContext();

			h = (AccessAccountHome) ic.lookup(AccessAccountHome.JNDI_NAME);

			b = h.create();
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		return b;
	}



	/**
	* Returns true if this Customer is not a child of another Customer.
	*/
	public boolean isRootCustomer() {
		throw new UnsupportedOperationException("not impl yet");
	}


	/**
	* Returns a Collection of BillableParties that may be assigned
	* to Accounts owned by this Customer.   The underlying rules
	* for this method may vary by Customer implementation, and/or
	* by Customer data.
	*/
	public Collection getAvailableBillableParties() {
		throw new UnsupportedOperationException("not impl yet");
	}


	/**
	* Returns the contact information describing the individual who is
	* the primary contact for this Customer.  In the case of
	* individual Customers, the contact is the Customer.
	*/
	public CustomerContact getCustomerContact() {
		return mData.getCustomerContact();
	}


	/**
	* Sets the contact information for this Customer.
	*/
	public void setCustomerContact(CustomerContact customerContact) {
		mData.setCustomerContact(customerContact);

		setModified(true);
	}


	public void setParentCustomerPK(CustomerPK pk) {
		mData.setParentCustomerPK(pk);
		setModified(true);
	}

	/**
	* Returns a Collection of active-status Subscriptions of the specified
	* SubscriptionType, for all Accounts owned by this Customer.
	*/
	public List getActiveSubscriptions(
			SubscriptionType subscriptionType) {

		Collection accounts = getAccounts();
		List activeSubscriptions = new ArrayList();
		Logger logger = Logger.getLogger(
				"com.iobeam.portal.model.customer");

		try {
			for (Iterator it = accounts.iterator(); it.hasNext(); ) {
				Account account = getAccount(
						((AccountData) it.next()).getPK());

				activeSubscriptions.addAll(account.getActiveSubscriptions(
						subscriptionType));
			}
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		return activeSubscriptions;
	}


	/**
	* Returns a Collection of created-status Subscriptions of the specified
	* SubscriptionType, for all Accounts owned by this Customer.
	*/
	public Collection getCreatedSubscriptions(
			SubscriptionType subscriptionType) {

		Collection accounts = getAccounts();
		Collection createdSubscriptions = new ArrayList();
		Logger logger = Logger.getLogger(
				"com.iobeam.portal.model.customer");

		try {
			for (Iterator it = accounts.iterator(); it.hasNext(); ) {
				Account account = getAccount(
						((AccountData) it.next()).getPK());

				createdSubscriptions.addAll(account.getSubscriptions(
						SubscriptionStatus.CREATED, subscriptionType));
			}
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		return createdSubscriptions;
	}


	private AccountHome getAccountHome() {
		AccountHome h = null;
		InitialContext ic = null;

		try {
			ic = new InitialContext();

			h = (AccountHome) ic.lookup(AccountHome.JNDI_NAME);
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		return h;
	}


	private Account getAccount(AccountPK accountPK) {
		AccountHome h = getAccountHome();

		try {
			return h.findByPrimaryKey(accountPK);
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


	/**
	* Returns the CustomerData instance describing this Customer.
	*/
	public CustomerData getData() {
		return mData;
	}

	public void setData(CustomerData data) {

		mData = data;
		setModified(true);
		ejbStore();
	}

}
