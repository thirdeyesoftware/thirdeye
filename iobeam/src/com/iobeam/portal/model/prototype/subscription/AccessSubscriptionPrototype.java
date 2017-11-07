package com.iobeam.portal.model.prototype.subscription;


import java.util.Collection;
import java.rmi.RemoteException;
import javax.ejb.*;
import com.iobeam.portal.model.EntityAccessor;
import com.iobeam.portal.model.subscription.SubscriptionType;


public interface AccessSubscriptionPrototype extends EntityAccessor, EJBObject {

	public SubscriptionPrototype findByPrimaryKey(
			SubscriptionPrototypePK pk)
			throws FinderException, RemoteException;


	/**
	* Returns a Collection of SubscriptionPrototypes for the
	* specified SubscriptionType.  The returned instances
	* are isAvailable() == true.
	*/
	public Collection findBySubscriptionType(
			SubscriptionType subscriptionType)
			throws FinderException, RemoteException;


	/**
	* Returns a Collection of SubscriptionPrototypes for the
	* specified SubscriptionType and isAvailable state.
	*/
	public Collection findBySubscriptionType(
			SubscriptionType subscriptionType, boolean isAvailable)
			throws FinderException, RemoteException;
}
