package com.iobeam.portal.model.product;


import javax.ejb.*;
import javax.rmi.*;
import java.rmi.*;
import javax.naming.*;
import java.util.Collection;
import com.iobeam.portal.util.*;

/**
*  Entity Accessor Session  for product and ProductCategory
*/

public class AccessProductBean implements SessionBean {

	private SessionContext mContext;

	public void setSessionContext(SessionContext context) {
		mContext = context;
	}

	public void unsetSessionContext() {
		mContext = null;
	}

	private SessionContext getSessionContext() {
		return mContext;
	}

	public void ejbCreate() 
			throws CreateException {
	}

	public void ejbPostCreate()
			throws CreateException {
	}

	public void ejbRemove() {
	
	}

	public void ejbActivate() {
	
	}

	public void ejbPassivate() {

	}

	public Product findProduct(String productNumber) 
			throws FinderException {
		Product product;
		try {
			product = ProductDAO.selectByProductNumber(productNumber);
		}
		catch (DataAccessException dae) {
			throw new FinderException(dae.toString());
		}

		return product;
		
	}

	public Product findByPrimaryKey(ProductPK pk) 
			throws FinderException {
		
		Product product;
		try {
			product = ProductDAO.selectByPK(pk);
		}
		catch (DataAccessException dae) {
			throw new FinderException(dae.toString());
		}
		return product;

	}

	public Collection findProductByCategory(ProductCategoryPK pk) 
			throws FinderException {
		
		Collection c;
		
		try {
			c = ProductDAO.selectByCategory(pk);
		}
		catch (DataAccessException dae) {
			throw new FinderException(dae.toString());
		}
		return c;

	}

	public Collection findAllProducts() 
			throws FinderException {
		Collection c;
		try {
			c = ProductDAO.selectAll();
		}
		catch (DataAccessException dae) {
			throw new FinderException(dae.toString());
		}
		return c;
	}

	public ProductCategory findProductCategoryByPK(ProductCategoryPK pk) 
			throws FinderException {
		ProductCategory category;
		try {
			category = ProductCategoryDAO.selectByPK(pk);
		}
		catch (DataAccessException dae) {
			throw new FinderException(dae.toString());
		}
		return category;
		
	}

	public Collection findAllProductCategories() 
			throws FinderException {
		Collection c;
		try {
			c = ProductCategoryDAO.selectAll();
		}
		catch (DataAccessException dae) {
			throw new FinderException(dae.toString());
		}
		return c;
	}

}
