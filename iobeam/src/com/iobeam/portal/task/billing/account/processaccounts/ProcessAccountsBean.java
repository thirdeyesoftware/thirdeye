package com.iobeam.portal.task.billing.account.processaccounts;

import javax.ejb.*;
import java.rmi.RemoteException;
import javax.naming.*;
import java.util.*;
import java.util.logging.Logger;
import com.iobeam.portal.model.billing.BillingException;
import com.iobeam.portal.model.account.*;
import com.iobeam.portal.model.invoice.*;
import com.iobeam.portal.model.billing.BillingPeriod;
import com.iobeam.portal.model.subscription.*;
import com.iobeam.portal.model.prototype.subscription.*;
import com.iobeam.portal.task.billing.BillingController;

public class ProcessAccountsBean implements SessionBean {

	private SessionContext mContext;

	public void ejbCreate() throws CreateException {

	}

	public void setSessionContext(SessionContext context) {
		mContext = context;
	}

	public void unsetSessionContext() {
		mContext = null;
	}

	public void ejbPostCreate() {

	}

	public void ejbActivate() {

	}

	public void ejbPassivate() {

	}

	public void ejbRemove() {

	}

	/**
	 * process accounts for billing purposes.  creates account line
	 * items for balance forwards and subscriptions that are billable
	 * for the specified billing period.
	 */
	public void processAccounts(BillingPeriod period)
			throws BillingException {
		Collection accounts;
		Logger l = Logger.getLogger(
				"com.iobeam.portal.task.billing.accounts.processaccounts");

		boolean trySalesTax = false;

		try {
			accounts = getAllOpenAccounts();

			InitialContext ic = new InitialContext();
			AccountHome home = (AccountHome)ic.lookup(AccountHome.JNDI_NAME);
			Account account;
			AccessSubscriptionHome asHome = 
				(AccessSubscriptionHome)ic.lookup(AccessSubscriptionHome.JNDI_NAME);
			AccessSubscription as = asHome.create();

			l.info("processAccounts(): found " + accounts.size() + " accounts to bill.");

			for (Iterator it = accounts.iterator(); it.hasNext();) {

				AccountData accountData = (AccountData)it.next();
				account = home.findByPrimaryKey(accountData.getPK());

				account.createBalanceForward();

				Collection subscriptions = 
					account.getActiveSubscriptions();
				
				l.info("processAccounts(): found " + 
						subscriptions.size() + " subscription(s) to bill.");

				AccessSubscriptionPrototype asp = getAccessSubscriptionPrototype();
				for (Iterator iter = subscriptions.iterator(); iter.hasNext();) {

					Subscription sub = (Subscription)iter.next();

					if (sub.isBillable(period)) {
						l.info("subscription" + sub.getPK().toString() + " is billable.");
						String memo = asp.findByPrimaryKey(
								sub.getSubscriptionPrototypePK()).getDescription();

						account.createPurchase(sub.getPK(), 
								sub.getCostPerBillingCycle(),
								memo);

						int currentBillingCycleCount = sub.getCurrentBillingCycleCount();
						sub.setCurrentBillingCycleCount(++currentBillingCycleCount);
						
						as.update(sub);	
						
						// we only want to calculate sales tax if there is vendible
						// activity during this period.
						if (!trySalesTax) trySalesTax = true;
					}
					else {
						l.info("subscription " + sub.getPK().toString() + " is not " +
							"billable.");
					}


				} //end for

				if (trySalesTax) 
					account.createSalesTax(period.getStartDate(), period.getEndDate());

				period.updateCurrentStep(BillingController.PROCESS_ACCOUNTS);

			} //end for

		}
		catch (Exception ee) {
			getSessionContext().setRollbackOnly();
			throw new BillingException("could not process accounts", ee);

		}

	}

	/**
	 * returns a collection of AccountData objects for all open
	 * accounts in the system.
	 */
	private Collection getAllOpenAccounts() throws Exception  {

		InitialContext ic = new InitialContext();
		AccessAccountHome aaHome = 
			(AccessAccountHome)ic.lookup(AccessAccountHome.JNDI_NAME);
		AccessAccount aa = aaHome.create();
		return aa.findAllOpenAccounts();
	}


	/**
	 * returns the remote interface to AccessSubscriptionPrototypeBean.
	 */
	private AccessSubscriptionPrototype getAccessSubscriptionPrototype() 
			throws Exception {
		InitialContext ic = new InitialContext();
		AccessSubscriptionPrototypeHome home = 
				(AccessSubscriptionPrototypeHome)ic.lookup(
					AccessSubscriptionPrototypeHome.JNDI_NAME);
		return home.create();
	}

	private SessionContext getSessionContext() {
		return mContext;
	}

}

