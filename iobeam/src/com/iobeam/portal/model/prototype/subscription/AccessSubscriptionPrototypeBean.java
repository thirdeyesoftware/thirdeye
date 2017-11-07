package com.iobeam.portal.model.prototype.subscription;


import java.util.Collection;
import java.rmi.RemoteException;
import javax.ejb.*;
import com.iobeam.portal.model.subscription.SubscriptionType;


public class AccessSubscriptionPrototypeBean implements SessionBean {

	private SessionContext mSessionContext;


	public void setSessionContext(SessionContext context) {
		mSessionContext = context;
	}


	public void ejbCreate() throws CreateException {
	}


	public void ejbActivate() {
	}


	public void ejbPassivate() {
	}


	public void ejbRemove() {
	}



	public SubscriptionPrototype findByPrimaryKey(
			SubscriptionPrototypePK pk)
			throws FinderException {

		SubscriptionPrototype p = null;

		try {
			p = SubscriptionPrototypeDAO.selectByPK(pk);

			if (p == null) {
				throw new ObjectNotFoundException(pk.toString());
			}
		}
		catch (FinderException fe) {
			throw fe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		return p;
	}


	/**
	* Returns a Collection of SubscriptionPrototypes for the
	* specified SubscriptionType.  The returned instances
	* are isAvailable() == true.
	*/
	public Collection findBySubscriptionType(
			SubscriptionType subscriptionType)
			throws FinderException {

		try {
			return SubscriptionPrototypeDAO.selectBySubscriptionType(
					subscriptionType, true);
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


	/**
	* Returns a Collection of SubscriptionPrototypes for the
	* specified SubscriptionType.  The returned instances
	* are isAvailable() == true.
	*/
	public Collection findBySubscriptionType(
			SubscriptionType subscriptionType,
			boolean isAvailable)
			throws FinderException {

		try {
			return SubscriptionPrototypeDAO.selectBySubscriptionType(
					subscriptionType, isAvailable);
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}
}
