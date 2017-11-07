package com.iobeam.portal.model.asset;


import java.util.logging.Logger;
import javax.ejb.*;

import com.iobeam.portal.util.DataNotFoundException;


public class AccessAssetBean implements SessionBean {

	public static final String LOGGER = "com.iobeam.portal.model.asset";

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




	/**
	*/
	public Asset findByPrimaryKey(AssetPK assetPK)
			throws FinderException {

		Asset asset = null;

		try {
			asset = AssetDAO.select(assetPK);

			if (asset == null) {
				throw new ObjectNotFoundException(assetPK.toString());
			}
		}
		catch (FinderException fe) {
			throw fe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		return asset;
	}


	/**
	*/
	public Asset create(Asset asset) throws CreateException {

		try {
			asset = AssetDAO.create(asset);
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		return asset;
	}


	/**
	*/
	public void remove(AssetPK assetPK) {
		try {
			AssetDAO.delete(assetPK);
		}
		catch (DataNotFoundException dnfe) {
			throw new NoSuchEntityException(dnfe);
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}


	/**
	*/
	public void update(Asset asset) {
		try {
			asset = AssetDAO.update(asset);
		}
		catch (DataNotFoundException dnfe) {
			throw new NoSuchEntityException(dnfe);
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}
}
