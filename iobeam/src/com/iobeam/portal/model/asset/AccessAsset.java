package com.iobeam.portal.model.asset;


import java.rmi.RemoteException;
import javax.ejb.*;
import java.net.InetAddress;

import com.iobeam.portal.model.EntityAccessor;


public interface AccessAsset extends EntityAccessor, EJBObject {

	/**
	*/
	public Asset findByPrimaryKey(AssetPK assetPK)
			throws FinderException, RemoteException;


	/**
	*/
	public Asset create(Asset asset)
			throws CreateException, RemoteException;


	/**
	*/
	public void remove(AssetPK assetPK) throws RemoteException;


	/**
	*/
	public void update(Asset asset) throws RemoteException;
}
