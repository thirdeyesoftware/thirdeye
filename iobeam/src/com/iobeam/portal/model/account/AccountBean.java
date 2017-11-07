package com.iobeam.portal.model.account;


import javax.ejb.*;
import javax.naming.*;
import java.util.*;
import java.util.logging.*;
import com.iobeam.portal.util.*;
import com.iobeam.portal.model.billing.*;
import com.iobeam.portal.model.subscription.*;
import com.iobeam.portal.model.product.*;
import com.iobeam.portal.model.customer.*;
import com.iobeam.portal.model.billablecustomer.*;
import com.iobeam.portal.model.invoice.*;

/**
*/
public class AccountBean implements EntityBean {

	private AccountData mData;
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


	private AccountPK getPK() {
		return (AccountPK) getContext().getPrimaryKey();
	}


	public AccountPK ejbCreate(Customer customer,
			BillableCustomer billableCustomer) throws CreateException {
		try {
			mData  = AccountDAO.create(customer.getData().getPK(),
					billableCustomer.getBillableCustomerData().getPK());
			AccountDAO.createBalanceForward(mData.getPK(), new Money(0));

			setModified(false);
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		return mData.getPK();
	}


	public void ejbPostCreate(Customer customer,
			BillableCustomer billableCustomer) throws CreateException {
	}


	public AccountPK ejbCreate(Customer customer) throws CreateException {

		try {
			mData  = AccountDAO.create(customer.getData().getPK(), null);
			AccountDAO.createBalanceForward(mData.getPK(), new Money(0));

			setModified(false);
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		return mData.getPK();
	}


	public void ejbPostCreate(Customer customer) throws CreateException {
	}


	public AccountPK ejbFindByPrimaryKey(AccountPK pk)
			throws FinderException {

		try {
			AccountDAO.checkPrimaryKey(pk);
		}
		catch (DataAccessException dae) {
			throw new EJBException(dae.toString());
		}
		return pk;
		
	}

	/**
	* Returns the AccountPK for the specified Invoice.
	*/
	public AccountPK ejbFindByInvoice(InvoicePK invoicePK)
			throws FinderException {
			
		AccountPK pk = null;
		try {
			pk = AccountDAO.selectByInvoicePK(invoicePK);
		}
		catch (DataAccessException dae) { 
			throw new FinderException(dae.toString());
		}
		return pk;
		
	}

	/**
	* Returns a Collection of Accounts having activity occurring
	* within the specified time period.
	*/
	public Collection ejbFindByActivity(Date startDate, Date endDate)
			throws FinderException {

		Collection c;
		try {
			c = AccountDAO.selectByActivity(startDate, endDate);
		}
		catch (DataAccessException dae) { 
			throw new FinderException(dae.toString());
		}
		return c;
	
	}


	public AccountPK ejbFindByAccountNumber(AccountNumber accountNumber)
			throws FinderException {

		AccountPK pk = null;

		try {
			pk = AccountDAO.selectByAccountNumber(accountNumber);
		}
		catch (DataNotFoundException dae) { 
			throw new FinderException(accountNumber.toString());
		}
		catch (DataAccessException dae) { 
			throw new EJBException(dae);
		}

		return pk;
	}


	public void ejbLoad() {

		mData = new AccountData(getPK());
			
		try {
			mData = AccountDAO.load(mData);
		}
		catch (DataAccessException dae) {
			throw new EJBException(dae.toString());
		}

		setModified(false);
	}


	public void ejbRemove() throws RemoveException {

		// We should consider removing all associated
		// subscriptions, account entries, etc
		// when removing an Account.
		//
		// Also, there may be some rules to prevent certain Accounts
		// from being removed, in which case we throw RemoveException
		// with an explanation.

		throw new UnsupportedOperationException("not impl yet");
	}


	public void ejbStore() {
		if (!isModified()) {
			return;
		}

		try {
			AccountDAO.update(mData);
			setModified(false);
		}
		catch (DataAccessException dae) {
			throw new EJBException(dae.toString());
		}

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
	* Returns true if this is an active Account.
	* An open Account is subject to regular invoice processing.
	*/
	public boolean isOpen() {

		return mData.getAccountStatus().equals(AccountStatus.OPEN);
		
	}

	/**
	* Returns the BillableParty responsible for paying on this Account.
	*/
	public BillableParty getBillableParty() {
		if (mData.getBillableCustomerPK() == null ||
				mData.getBillableCustomerPK().getID() == 0) return null;

		BillableCustomer bc = null;
		BillableCustomerHome bcHome = null;
		InitialContext ic = null;
		try {
			ic = new InitialContext();
			bcHome = (BillableCustomerHome)ic.lookup(
				BillableCustomerHome.JNDI_NAME);
			bc = bcHome.findByPrimaryKey(mData.getBillableCustomerPK());
		}
		catch (Exception ee) {
			throw new EJBException(ee.toString());
		}
		finally {
			try {
				ic.close();
			}
			catch (Exception ez) {}
		}
		return bc;
	}


	/**
	* Returns the Customer who owns this Account.
	*/
	public Customer getCustomer() {
		Customer customer = null;
		CustomerHome custHome = null;
		InitialContext ic = null;
		try {
			ic = new InitialContext();
			custHome = (CustomerHome)ic.lookup(
				CustomerHome.JNDI_NAME);
			customer = custHome.findByPrimaryKey(
				mData.getCustomerPK());
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
		finally {
			try {
				ic.close();
			}
			catch (Exception ez) { }
		}
		return customer;
		
	}


	/**
	* Returns the balance on this Account at this moment.
	*/
	public Money getCurrentBalance() {
		Money money = null;
		try {
			money = AccountDAO.getCurrentBalance(getPK());
		}
		catch (DataAccessException dae) {
			throw new EJBException(dae.toString());
		}
		return money;
			
	}


	public void createBalanceForward() throws BillingException {
		try {
			AccountDAO.createBalanceForward(getPK(), getCurrentBalance());
		}
		catch (DataAccessException dae) {
			getEntityContext().setRollbackOnly();
			throw new BillingException(dae.toString());
		}
	}

	/**
	* Applies the specified Money as payment on this Account.
	* Returns a Payment instance which is the AccountEntry resulting from the
	* new payment on the Account.
	*/
	public Payment applyPayment(PaymentInstrument instrument, Money amount,
			String memo) throws BillingException {
		Payment payment = null;
		try {
			payment = AccountDAO.createPayment(getPK(), instrument, amount, memo);
		}
		catch (DataAccessException dae) {
			getEntityContext().setRollbackOnly();
			throw new BillingException("could not create payment.\n" + dae.toString());
		}
		return payment;
			
	}

	public Purchase createPurchase(ProductPK productPK, Money amount, String memo) 
			throws BillingException {
		Purchase purchase = null;
		try {
			purchase = AccountDAO.createPurchase(
					getPK(), amount, productPK, memo);
		}
		catch (DataAccessException dae) {
			getEntityContext().setRollbackOnly();
			throw new BillingException(dae.toString());
		}
		return purchase;
	}
	
	public Purchase createPurchase(
			SubscriptionPK subPK, Money amount, String memo) 
			throws BillingException {
		Purchase purchase = null;
		try {
			purchase = AccountDAO.createPurchase(getPK(), amount, subPK, memo);
		}
		catch (DataAccessException dae) {
			getEntityContext().setRollbackOnly();
			throw new BillingException(dae.toString());
		}
		return purchase;
	}

	/**
	* Returns a Collection of Subscriptions of the specified type
	* managed under this Account.
	*/
	public Collection getSubscriptions(SubscriptionType stype) {
		AccessSubscriptionHome asHome = null;
		Collection c;
		InitialContext ic = null;
		try {
			ic = new InitialContext();
			asHome =
				(AccessSubscriptionHome)ic.lookup(AccessSubscriptionHome.JNDI_NAME);
			AccessSubscription as = asHome.create();
			c = as.findBySubscriptionType(getPK(), stype);
		}
		catch (Exception fe) {
			throw new EJBException(fe.toString());
		}
		return c;
	}

	public Collection getAllSubscriptions() {
		AccessSubscriptionHome asHome = null;
		Collection c;
		try {
			InitialContext ic = new InitialContext();
			asHome =
				(AccessSubscriptionHome)ic.lookup(AccessSubscriptionHome.JNDI_NAME);
			AccessSubscription as = asHome.create();
			c = as.findAllSubscriptions(getPK());
		}
		catch (Exception fe) {
			throw new EJBException(fe.toString());
		}
		return c;
	}

	/**
	* Returns a Collection of active Subscriptions
	* managed under this Account.
	*/
	public Collection getActiveSubscriptions() {
		AccessSubscriptionHome asHome = null;
		AccessSubscription as = null;
		InitialContext ic = null;
		Collection c;
		Logger logger = Logger.getLogger(
			"com.iobeam.portal.model.account");

		try {
			logger.info("AccountBean.getActiveSubscriptions():<start>");
			ic = new InitialContext();
			asHome = 
				(AccessSubscriptionHome)ic.lookup(AccessSubscriptionHome.JNDI_NAME);
			as = asHome.create();
			c = as.findBySubscriptionStatus(getPK(), SubscriptionStatus.ACTIVE);
			logger.info("AccountBean.getActiveSubscriptions(): subscription count=" + 
				c.size());

		}
		catch (Exception fe) {
			throw new EJBException(fe.toString());
		}

		return c;

	}

	/**
	* Returns a Collection of active Subscriptions of the specified type
	* managed under this Account.
	*/
	public Collection getActiveSubscriptions(SubscriptionType type) {

		return getSubscriptions(SubscriptionStatus.ACTIVE, type);
	}


	/**
	* Returns a Collection of all Subscriptions managed under this Account
	* with the specified SubscriptionStatus and SubscriptionType.
	*/
	public Collection getSubscriptions(SubscriptionStatus status,
			SubscriptionType type) {

		AccessSubscriptionHome asHome = null;
		AccessSubscription as = null;
		InitialContext ic = null;
		Collection c = null;;
		Logger logger = Logger.getLogger(
				"com.iobeam.portal.model.account");

		try {
			ic = new InitialContext();
			asHome = (AccessSubscriptionHome)
					ic.lookup(AccessSubscriptionHome.JNDI_NAME);
			as = asHome.create();
			c = as.findBySubscriptionStatus(getPK(), type, status);

		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		return c;
	}




	/**
	* Returns the Subscription on this Account with the specified
	* SubscriptionPK.
	*
	* Throws BusinessLogicError if there is no Subscription on
	* this Account with the specified PK.
	*/
	public Subscription getSubscription(SubscriptionPK pk)
			throws BusinessLogicException {
		AccessSubscription as = null;
		AccessSubscriptionHome asHome = null;
		InitialContext ic = null;
		Subscription subscription = null;

		try {
			ic = new InitialContext();
			asHome = 
				(AccessSubscriptionHome)ic.lookup(AccessSubscriptionHome.JNDI_NAME);
			as = asHome.create();
			subscription = as.findByPrimaryKey(pk);
			if (!subscription.getAccountPK().equals(getPK())) {
					throw new BusinessLogicException("subscription does not exist for" + 
						" this account");
			}

		}
		catch (Exception fe) {
			throw new EJBException(fe.toString());
		}
		finally {
			try {
				ic.close();
			}
			catch (Exception eze) { }
		}

		return subscription;

	}


	/**
	* Adds the specified Subscription to the set of Subscriptions
	* managed under this Account.
	*
	* @exception SubscriptionException the specified Subscription cannot
	* be added to this Account.  There may be a problem with the type of
	* Subscription, and the type of Account.
	*
	* @exception BillingException the specified Subscription has a non-zero
	* price and this Account has no BillableParty.
	*/
	public Subscription addSubscription(Subscription subscription)
			throws SubscriptionException, BillingException {

		Subscription newSub;
		if (subscription == null) {
			throw new NullPointerException("subscription");
		}

		Logger.getLogger(
			"com.iobeam.portal.model.account").info(
				"addSubscription: " + subscription);

		if (subscription.getPrice().getAmount() != 0.0 &&
				mData.getBillableCustomerPK() == null) {
			getEntityContext().setRollbackOnly();

			throw new BillingException("Subscription has a price and " +
					"account has no BillableParty " + subscription +
					" " + mData);
		}



		AccessSubscriptionHome asHome = null;
		AccessSubscription as = null;
		InitialContext ic = null;

		try {
			ic = new InitialContext();
			asHome = (AccessSubscriptionHome)
					ic.lookup(AccessSubscriptionHome.JNDI_NAME);

			as = asHome.create();

			if (subscription.getPK() == null) {
				newSub = as.create(getPK(), subscription);
			} else {
				// Transfer the Subscription to this Account.
				subscription.setAccountPK(getPK());
				as.update(subscription);
				newSub = subscription;
			}
		}
		catch (Exception fe) {
			throw new EJBException("could not add subscription.\n" + 
				fe.toString());
		}

		return newSub;
	}


	/**
	* Replaces the Subscription on this Account with the same PK
	* as the specified Subscription.
	*
	* Throws BusinessLogicError if the new Subscription is of a
	* different SubscriptionType than the original, or if there
	* is no Subscription with the same PK.
	*/
	public void updateSubscription(Subscription subscription) {
		if (subscription == null) 
			throw new NullPointerException("subscription");
		
		AccessSubscriptionHome asHome = null;
		AccessSubscription as = null;
		InitialContext ic = null;

		try {
			ic = new InitialContext();
			asHome = 
				(AccessSubscriptionHome)ic.lookup(AccessSubscriptionHome.JNDI_NAME);
			as = asHome.create();
			as.update(subscription);
		}
		catch (Exception e) {
			throw new EJBException(
				"could not update subscription.\n" + e.toString());
		}
		
	}


	/**
	* Returns a Sortedset of AccountEntries for the
	* current BillingPeriod.   AccountEntries are presented
	* in ascending chronological order.
	*/
	public SortedSet getAccountEntries() {
		SortedSet ss;
		try {
			BillingPeriod bp = BillingPeriod.getInstanceFor(new Date());
			ss = AccountDAO.selectAccountEntries(
				(AccountPK)mContext.getPrimaryKey(),
				bp.getStartDate(), bp.getEndDate());
		}
		catch (ObjectNotFoundException ofe) {
			throw new EJBException("could not find current billing period.");
		}
		catch (DataAccessException dae) {
			getEntityContext().setRollbackOnly();
			throw new EJBException(dae.toString());
		}
		return ss;
	}

	/**
	* Returns a Sortedset of AccountEntries for the
	* specified time period.   AccountEntries are presented
	* in ascending chronological order.
	*/
	public SortedSet getAccountEntries(Date startDate,
			Date endDate) {
		SortedSet ss;
		try {
			ss = AccountDAO.selectAccountEntries(
				(AccountPK)mContext.getPrimaryKey(),
				startDate, endDate);
		}
		catch (DataAccessException dae) {
			getEntityContext().setRollbackOnly();
			throw new EJBException(dae.toString());
		}
		return ss;
	}

	public SortedSet getAccountEntries(AccountEntryType type, Date startDate,
			Date endDate) {
		SortedSet ss;
		try {
			ss = AccountDAO.selectAccountEntries(
				(AccountPK)mContext.getPrimaryKey(), type, 
				startDate, endDate);
		}
		catch (DataAccessException dae) {
			getEntityContext().setRollbackOnly();
			throw new EJBException(dae.toString());
		}
		return ss;
	}

	public SalesTax createSalesTax(Date startDate, Date endDate) throws
			BillingException {
		double tax = 0.0d;
		double taxRate = 0.0;
		Collection activity;
		try {
			taxRate = Double.parseDouble(
					System.getProperty("iobeam.portal.billing.salestaxrate"));

			activity = getAccountEntries(
					AccountEntryType.PURCHASE, 
					startDate, endDate);

			Logger.getLogger(
				"com.iobeam.portal.model.account").info(
					"createSalesTax(): found " + activity.size() + " vendibles");

			for (Iterator it = activity.iterator();it.hasNext();) {
				Purchase purchase = (Purchase)it.next();
				Vendible vendible = getVendible(purchase);
				
				if (vendible.isTaxable()) {
					tax += (vendible.getPrice().getAmount()  * taxRate);
				}
			}
			return AccountDAO.createSalesTax(getPK(), new Money(tax));
		}
		catch (Exception ee) {
			getEntityContext().setRollbackOnly();
			throw new BillingException(
					"could not create sales tax.", ee);
		}
	}

	/**
	* Returns the AccountData instance describing this Account.
	*/
	public AccountData getData() {
		return mData;
	}

	public void setAccountStatus(AccountStatus status) {
		mData.setAccountStatus(status);
		setModified(true);
	}
	public void setBillableCustomerPK(BillableCustomerPK pk) {
		mData.setBillableCustomerPK(pk);
		setModified(true);
	}

	private EntityContext getEntityContext() {
		return mContext;
	}

	private Vendible getVendible(Purchase purchase) throws Exception {
		if (purchase.getSubscriptionPK() != null) {
			return getAccessSubscription().findByPrimaryKey(
				purchase.getSubscriptionPK());
		}
		else {
			return getAccessProduct().findByPrimaryKey(
				purchase.getProductPK());
		}
	}
	
	private AccessSubscription getAccessSubscription() throws Exception {
		InitialContext ic = new InitialContext();
		AccessSubscriptionHome home = 
			(AccessSubscriptionHome)ic.lookup(AccessSubscriptionHome.JNDI_NAME);
		return home.create();
	}

	private AccessProduct getAccessProduct() throws Exception {
		InitialContext ic = new InitialContext();
		AccessProductHome home = 
			(AccessProductHome)ic.lookup(AccessProductHome.JNDI_NAME);
		return home.create();
	}



}
