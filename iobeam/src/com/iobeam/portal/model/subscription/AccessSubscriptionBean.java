package com.iobeam.portal.model.subscription;

import javax.ejb.*;
import javax.naming.*;
import java.util.*;

import com.iobeam.portal.security.*;
import com.iobeam.portal.model.account.*;
import com.iobeam.portal.util.DataAccessException;
import com.iobeam.portal.util.DataNotFoundException;


public class AccessSubscriptionBean implements SessionBean {

	private SessionContext mContext;


	public void ejbCreate() throws CreateException {
	}


	public void ejbActivate() {
	}


	public void ejbPassivate() {
	}


	public void setSessionContext(SessionContext ctx) {
		mContext = ctx;
	}


	public void ejbRemove() {
	}


	public void ejbPostCreate() {
	}


	public Subscription findByPrimaryKey(SubscriptionPK pk) 
			throws FinderException {

		Subscription s = null;

		try {
			s = SubscriptionDAO.selectByPK(pk);

			if (s == null) {
				throw new ObjectNotFoundException(pk.toString());
			}
		}
		catch (FinderException fe) {
			throw fe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		return s;
	}


	/**
	* Returns the Subscription for the specified SecureID.
	* Throws FinderException if there is none.
	* The returned value is cast to a type specific
	* to the SubscriptionType.
	*/
	public Subscription findBySecureID(SecureID subscriptionKey)
			throws FinderException {

		Subscription s = null;

		try {
			s = SubscriptionDAO.selectBySecureID(subscriptionKey);

			if (s == null) {
				throw new ObjectNotFoundException("Subscription Key " +
						subscriptionKey.toString());
			}
		}
		catch (DataAccessException dae) {
			throw new EJBException(dae);
		}

		return s;
	}




	public Collection findBySubscriptionType(
			AccountPK accountPK, SubscriptionType type)
			throws FinderException {

		try {
			return SubscriptionDAO.selectBySubscriptionType(
					accountPK, type);
		}
		catch (DataAccessException dae) {
			throw new EJBException(dae);
		}
	}
	

	public Collection findBySubscriptionStatus(AccountPK accountPK,
			SubscriptionStatus status)
			throws FinderException {

		try {
			return SubscriptionDAO.selectBySubscriptionStatus(
				accountPK, status);
		}
		catch (DataAccessException dae) {
			throw new EJBException(dae);
		}
	}


	public Collection findBySubscriptionStatus(AccountPK accountPK,
			SubscriptionType stype,
			SubscriptionStatus status) 
			throws FinderException {


		try {
			return SubscriptionDAO.selectBySubscriptionStatus(
				accountPK, stype, status);
		}
		catch (DataAccessException dae) {
			throw new EJBException(dae);
		}
	}


	/**
	* Returns a Collection of all Subscriptions for a specified Account.
	*/
	public Collection findAllSubscriptions(AccountPK accountPK) 
			throws FinderException {

		try {
			return SubscriptionDAO.selectByAccountPK(accountPK);
		}
		catch (DataAccessException dae) {
			throw new EJBException(dae);
		}
	}


	/**
	* Stores the specified Subscription in the system under the
	* specified Account.
	*
	* Returns a Subscription populated with a SubscriptionPK and the
	* specified AccountPK, as well as a unique SecureID.
	*/
	public Subscription create(AccountPK accountPK, Subscription subscription) 
		throws CreateException {

		try {
			subscription.setAccountPK(accountPK);
			subscription.setSecureID(getAvailableSecureID());

			return SubscriptionDAO.create(subscription);
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	
	}


	private SecureID getAvailableSecureID()
			throws DataAccessException {
		SecureID sid = null;

		do {
			sid = SecureIDFactory.createSecureID();

			if (SubscriptionDAO.selectBySecureID(sid) != null) {
				sid = null;
			}
		} while (sid == null);

		return sid;
	}


	public void update(Subscription subscription) {
		try {
			SubscriptionDAO.update(subscription);
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

	}


	public void delete(SubscriptionPK subscriptionPK) {
		try {
			SubscriptionDAO.delete(subscriptionPK);
		}
		catch (DataAccessException dae) {
			throw new EJBException(dae);
		}
	}
}
