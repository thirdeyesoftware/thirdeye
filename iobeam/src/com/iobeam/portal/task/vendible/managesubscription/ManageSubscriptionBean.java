package com.iobeam.portal.task.vendible.managesubscription;

import javax.ejb.*;
import javax.naming.*;
import javax.transaction.*;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.logging.Logger;
import com.iobeam.portal.util.Duration;
import com.iobeam.portal.model.subscription.*;
import com.iobeam.portal.model.account.*;
import com.iobeam.portal.model.billing.BillingException;
import com.iobeam.portal.model.prototype.subscription.*;


public class ManageSubscriptionBean implements SessionBean {
	private SessionContext mContext;
	
	public void setSessionContext(SessionContext context) {
		mContext = context;
	}


	private SessionContext getSessionContext() {
		return mContext;
	}


	public void ejbCreate() throws CreateException {
	}

	public void ejbRemove() {
	}

	public void ejbActivate() {
	}

	public void ejbPassivate() {
	}



	/**
	* Creates a new Subscription in the specified Account
	* using the specified SubscriptionPrototype and start Date.
	*
	* @exception BillingException the Subscription would result
	* in a violation of Account billing rules for the specified
	* Account.
	*/
	public Subscription createSubscription(AccountPK accountPK,
			SubscriptionPrototype prototype, Date startDate)
			throws SubscriptionException, BillingException {

		Subscription subscription = null;

		try {
			Account account = getAccount(accountPK);

			SubscriptionBuilder builder = SubscriptionBuilderFactory.
					getFactory().getSubscriptionBuilder();

			subscription = builder.createSubscription(
					prototype, startDate);

			account.addSubscription(subscription);
		}
		catch (SubscriptionException se) {
			throw se;
		}
		catch (BillingException be) {
			throw be;
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		return subscription;
	}


	/**
	* Cancels the specified Subscription.  The Subscription
	* is no longer active after cancellation.
	*/
	public void cancelSubscription(Subscription subscription) {
		throw new UnsupportedOperationException("not impl yet");
	}


	/**
	* Generates count Subscriptions in the Account enclosing the
	* specified generator Subscription.
	*
	* @exception SubscriptionException the specified Subscription
	* is not generative.
	*
	* @exception ExhaustedGeneratorSubscriptionException the specified
	* Subscription has no remaining generation capacity to satisfy
	* the request.
	*/
	public void generateSubscriptions(SubscriptionPK generatorPK,
			int count) throws SubscriptionException {

		Subscription generatorSubscription;
		Account destinationAccount;

		try {
			generatorSubscription = getAccessSubscription().findByPrimaryKey(
					generatorPK);

			if (!generatorSubscription.getSubscriptionType().isGenerative()) {
				throw new SubscriptionException("not generative",
						generatorSubscription);
			}

			SubscriptionGenerator generator = (SubscriptionGenerator)
					generatorSubscription;

			if (generator.getCurrentGenerationCount() + count >
					generator.getMaxGenerationCount()) {

				throw new ExhaustedGeneratorSubscriptionException(
						generatorSubscription);
			}


			Account account = getAccount(generatorSubscription.getAccountPK());

			SubscriptionBuilder builder = SubscriptionBuilderFactory.
					getFactory().getSubscriptionBuilder();


			for (int i = 0; i < count; ++i) {
				Subscription s = builder.createDefaultSubscription(
						generatorSubscription);

				account.addSubscription(s);
			}
		}
		catch (FinderException fe) {
			throw new EJBException(fe);
		}
		catch (RemoteException re) {
			throw new EJBException(re);
		}
		catch (SubscriptionException se) {
			throw se;
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


	/**
	* Transfers the specified Subscription from its current Account
	* to the specified destination Account.
	*
	* @exception SubscriptionException the Subscription is not
	* compatible with the transfer.
	*
	* @exception BillingException the Subscription would result
	* in a violation of Account billing rules in either the source or
	* destination Account.
	*/
	public Subscription transferSubscription(SubscriptionPK subscriptionPK,
			AccountPK destinationAccountPK)
			throws SubscriptionException, BillingException {

		Subscription s;
		Account destinationAccount;

		try {
			s = getAccessSubscription().findByPrimaryKey(subscriptionPK);

			Logger.getLogger(
					"com.iobeam.portal.task.vendible.managesubscription").info(
					"transfer " + s);

			destinationAccount = getAccount(destinationAccountPK);

			s = destinationAccount.addSubscription(s);

			Logger.getLogger(
					"com.iobeam.portal.task.vendible.managesubscription").info(
					"transfered " + s);

			return s;
		}
		catch (FinderException fe) {
			throw new EJBException(fe);
		}
		catch (RemoteException re) {
			throw new EJBException(re);
		}
	}


	/**
	* Marks the specified Subscription as registered to the
	* Account that contains it.
	*/
	public Subscription registerSubscription(SubscriptionPK subscriptionPK)
			throws SubscriptionException {

		Subscription s;

		try {
			Transaction tx =
					weblogic.transaction.TxHelper.getTransaction();
			Logger.getLogger(
					"com.iobeam.portal.task.vendible.managesubscription").info(
					tx.toString() + " " + tx.getStatus());

			s = getAccessSubscription().findByPrimaryKey(subscriptionPK);

			if (s.isRegistered()) {
				throw new RegisteredSubscriptionException(s);
			}

			Logger.getLogger(
					"com.iobeam.portal.task.vendible.managesubscription").info(
					"register " + s);

			s.setRegistered(true);
			getAccessSubscription().update(s);

			Logger.getLogger(
					"com.iobeam.portal.task.vendible.managesubscription").info(
					"registered " + s);

			return s;
		}
		catch (FinderException fe) {
			throw new EJBException(fe);
		}
		catch (RemoteException re) {
			throw new EJBException(re);
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (SubscriptionException se) {
			getSessionContext().setRollbackOnly();
			throw se;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


	/**
	* Marks the specified Subscription as unregistered.
	*/
	public Subscription unregisterSubscription(SubscriptionPK subscriptionPK)
			throws SubscriptionException {

		Subscription s;

		try {
			s = getAccessSubscription().findByPrimaryKey(subscriptionPK);

			if (!s.isRegistered()) {
				throw new UnregisteredSubscriptionException(s);
			}

			s.setRegistered(false);
			getAccessSubscription().update(s);

			return s;
		}
		catch (FinderException fe) {
			throw new EJBException(fe);
		}
		catch (RemoteException re) {
			throw new EJBException(re);
		}
	}


	/**
	* Sets the start date (and expiration date based on duration if the
	* Subscription is not continuous) for the specified Subscription.
	*
	* Marks the Subscription as status active.
	*/
	public Subscription activateSubscription(SubscriptionPK subscriptionPK,
			Date startDate)
			throws SubscriptionException {

		Subscription s;

		try {
			Transaction tx =
					weblogic.transaction.TxHelper.getTransaction();
			Logger.getLogger(
					"com.iobeam.portal.task.vendible.managesubscription").info(
					tx.toString() + " " + tx.getStatus());
			
			s = getAccessSubscription().findByPrimaryKey(subscriptionPK);

			Logger.getLogger(
					"com.iobeam.portal.task.vendible.managesubscription").info(
					"activate " + s);

			if (!s.getSubscriptionStatus().equals(SubscriptionStatus.CREATED)) {
				throw new SubscriptionException("status is " +
						s.getSubscriptionStatus(), s);
			}

			if (s.getStartDate() != null) {
				throw new SubscriptionException("start date already set", s);
			}

			Date endDate = null;

			SubscriptionPrototype proto =
					getAccessSubscriptionPrototype().findByPrimaryKey(
					s.getSubscriptionPrototypePK());

			if (!proto.getDuration().equals(Duration.CONTINUOUS)) {
				endDate = new Date(startDate.getTime() +
						proto.getDuration().getTime());
			}

			s.setStartDate(startDate);
			s.setExpirationDate(endDate);
			s.setSubscriptionStatus(SubscriptionStatus.ACTIVE);

			getAccessSubscription().update(s);

			Logger.getLogger(
					"com.iobeam.portal.task.vendible.managesubscription").info(
					"activated " + s);

			return s;
		}
		catch (FinderException fe) {
			throw new EJBException(fe);
		}
		catch (RemoteException re) {
			throw new EJBException(re);
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (SubscriptionException se) {
			getSessionContext().setRollbackOnly();
			throw se;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}



	private AccessSubscription getAccessSubscription() {
		try {
			InitialContext ic = new InitialContext();

			AccessSubscriptionHome home = (AccessSubscriptionHome)
					ic.lookup(AccessSubscriptionHome.JNDI_NAME);

			return home.create();
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


	private AccessSubscriptionPrototype getAccessSubscriptionPrototype() {
		try {
			InitialContext ic = new InitialContext();

			AccessSubscriptionPrototypeHome home =
					(AccessSubscriptionPrototypeHome)
					ic.lookup(AccessSubscriptionPrototypeHome.JNDI_NAME);

			return home.create();
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


	private Account getAccount(AccountPK accountPK) {
		try {
			InitialContext ic = new InitialContext();

			AccountHome home = (AccountHome)
					ic.lookup(AccountHome.JNDI_NAME);

			return home.findByPrimaryKey(accountPK);
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}
}
