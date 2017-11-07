package com.iobeam.portal.model.account;


import java.rmi.RemoteException;
import javax.ejb.EJBObject;
import java.util.*;
import com.iobeam.portal.util.*;
import com.iobeam.portal.model.billing.*;
import com.iobeam.portal.model.subscription.*;
import com.iobeam.portal.model.customer.*;
import com.iobeam.portal.model.billablecustomer.BillableCustomerPK;
import com.iobeam.portal.model.product.ProductPK;


/**
* An Account is a container for subscriptions, as well as AccountEntries.
*
* An Account can be used for billing by navigating from the Account to
* the associated BillableParty.
*/
public interface Account extends EJBObject {


	/**
	* Returns true if this is an active Account.
	* An open Account is subject to regular invoice processing.
	*/
	public boolean isOpen() throws RemoteException;


	/**
	* Returns the BillableParty responsible for paying on this Account.
	*/
	public BillableParty getBillableParty() throws RemoteException;


	/**
	* Returns the Customer who owns this Account.
	*/
	public Customer getCustomer() throws RemoteException;


	/**
	* Returns the balance on this Account at this moment.
	*/
	public Money getCurrentBalance() throws RemoteException;

	/**
	 * creates a sales tax entry for the current balance
	 * on this account.
	 */
	public SalesTax createSalesTax(Date startDate, Date endDate)
			throws BillingException, RemoteException;

	/**
	 * creates a balance forward entry for the current balance
	 * on this account.
	 */
	public void createBalanceForward() 
			throws BillingException, RemoteException;

	/**
	* Applies the specified Money as payment on this Account.
	* Returns a Payment instance which is the AccountEntry resulting from the
	* new payment on the Account.
	*/
	public Payment applyPayment(PaymentInstrument instrument, Money payment, 
			String memo) throws BillingException, RemoteException;

	/**
	 * create purchase for product pk and amount.
	 */
	public Purchase createPurchase(ProductPK pk, Money amount, String memo)
			throws BillingException, RemoteException;

	/**
	 * create purchase for subscription pk and amount amount
	 */
	public Purchase createPurchase(SubscriptionPK pk, Money amount, String memo)
			throws BillingException, RemoteException;
	
	/**
	* Returns a Collection of Subscriptions of the specified type
	* managed under this Account.
	*/
	public Collection getSubscriptions(SubscriptionType stype)
			throws RemoteException;

	/**
	 * Returns a collection of all subscriptions for this account.
	 */
	public Collection getAllSubscriptions() throws RemoteException;

	/**
	* Returns a Collection of active Subscriptions 
	* managed under this Account.
	*/
	public Collection getActiveSubscriptions() 
			throws RemoteException;


	public Collection getActiveSubscriptions(SubscriptionType type)
			throws RemoteException;


	/**
	* Returns a Collection of all Subscriptions managed under this Account
	* with the specified SubscriptionStatus and SubscriptionType.
	*/
	public Collection getSubscriptions(SubscriptionStatus status,
			SubscriptionType type)
			throws RemoteException;

	/**
	* Returns the Subscription on this Account with the specified
	* SubscriptionPK.
	*
	* Throws BusinessLogicError if there is no Subscription on
	* this Account with the specified PK.
	*/
	public Subscription getSubscription(SubscriptionPK pk)
			throws BusinessLogicException, RemoteException;


	/**
	* Adds the specified Subscription to the set of Subscriptions
	* managed under this Account.
	*
	* @exception SubscriptionException the SubscriptionType or
	* category is not compatible with this Account.
	*
	* @exception BillingException the specified Subscription is
	* billable, and this Account has no BillableParty.
	*/
	public Subscription addSubscription(Subscription subscription)
			throws RemoteException, SubscriptionException, BillingException;


	/**
	* Replaces the Subscription on this Account with the same PK
	* as the specified Subscription.
	*
	* Throws BusinessLogicError if the new Subscription is of a
	* different SubscriptionType than the original, or if there
	* is no Subscription with the same PK.
	*/
	public void updateSubscription(Subscription subscription)
			throws RemoteException;


	/**
	* Returns a Sortedset of AccountEntries for the
	* current BillingPeriod.   AccountEntries are presented
	* in ascending chronological order.
	*/
	public SortedSet getAccountEntries() throws RemoteException;


	/**
	 * returns a sorted set of account entries for the
	 * specified account entry type and date range.
	 */
	public SortedSet getAccountEntries(AccountEntryType type, 
			Date startDate, Date endDate) throws RemoteException;

	/**
	* Returns a Sortedset of AccountEntries for the
	* specified time period.   AccountEntries are presented
	* in ascending chronological order.
	*/
	public SortedSet getAccountEntries(Date startDate,
			Date endDate) throws RemoteException;


	/**
	* Returns the AccountData instance describing this Customer.
	*/
	public AccountData getData() throws RemoteException;

	public void setAccountStatus(AccountStatus status) 
			throws RemoteException;

	public void setBillableCustomerPK(BillableCustomerPK pk)
			throws RemoteException;

}
