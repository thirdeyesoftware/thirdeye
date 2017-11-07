package com.iobeam.portal.model.product;


import java.rmi.RemoteException;
import javax.ejb.*;
import java.util.Collection;
import com.iobeam.portal.util.*;
import com.iobeam.portal.model.EntityAccessor;



/**
* describes a physical product for sale.
*/
public interface AccessProduct extends EntityAccessor, EJBObject {

	public Product findProduct(String productNumber) 
			throws FinderException, RemoteException;

	public Collection findProductByCategory(ProductCategoryPK pk) 
			throws FinderException, RemoteException;

	public Product findByPrimaryKey (ProductPK productPK)
			throws FinderException, RemoteException;

	public Collection findAllProducts() 
			throws FinderException, RemoteException;

	public Collection findAllProductCategories()
			throws FinderException, RemoteException;
	
	public ProductCategory findProductCategoryByPK(ProductCategoryPK pk)
			throws FinderException, RemoteException;
	
}
