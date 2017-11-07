package com.iobeam.portal.model.product;

import java.io.Serializable;

public class ProductCategory implements Serializable {
	public static final long serialVersionUID = 200304180128L;
	private ProductCategoryPK mPK;
	private String mDescription;
	private ProductCategoryPK  mParentCategoryPK;
	private boolean mIsActive;
	
	public ProductCategory(ProductCategoryPK pk) {
		this(pk, null, null);
	}

	public ProductCategory(ProductCategoryPK pk, String desc) {
		this(pk, desc, null);
	}

	public ProductCategory(ProductCategoryPK pk, String desc, 
			ProductCategoryPK parentPK) {
		
		mPK = pk;
		mDescription = desc;
		mParentCategoryPK = parentPK;
	}

	public ProductCategoryPK getPK() {
		return mPK;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String desc) {
		mDescription = desc;
	}

	public ProductCategoryPK getParentCategoryPK() {
		return mParentCategoryPK;
	}

	public void setParentCategoryPK(ProductCategoryPK pk) {
		mParentCategoryPK = pk;
	}

	public void setIsActive(boolean b) {
		mIsActive = b;
	}

	public boolean isActive() {
		return mIsActive;
	}
	
}





		
