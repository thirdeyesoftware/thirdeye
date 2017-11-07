package com.iobeam.portal.model.asset;


import java.rmi.RemoteException;
import javax.ejb.*;


public interface AccessAssetHome extends EJBHome {

	public static final String JNDI_NAME =
			"iobeam.portal.AccessAssetHome";

	public AccessAsset create()
			throws CreateException, RemoteException;
}
