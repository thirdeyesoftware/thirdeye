package com.iobeam.portal.model.asset;


import com.iobeam.portal.model.product.*;


public class Asset implements java.io.Serializable {

	private AssetPK mPK;
	private ProductPK mProductPK;
	private String mDescription;
	private String mSerialNumber;
	

	public Asset(ProductPK productPK, String description, String serialNumber) {
		mProductPK = productPK;
		if (productPK == null) {
			throw new NullPointerException("productPK");
		}

		mDescription = description;
		mSerialNumber = serialNumber;
	}


	Asset(AssetPK assetPK, ProductPK productPK,
			String description, String serialNumber) {

		mPK = assetPK;
		if (assetPK == null) {
			throw new NullPointerException("assetPK");
		}

		mProductPK = productPK;
		if (productPK == null) {
			throw new NullPointerException("productPK");
		}

		mDescription = description;
		mSerialNumber = serialNumber;
	}


	public AssetPK getPK() {
		return mPK;
	}


	void setPK(AssetPK pk) {
		mPK = pk;
		if (pk == null) {
			throw new NullPointerException("pk");
		}
	}


	/**
	* Returns the pk of the Product of which this Asset is an instance.
	*/
	public ProductPK getProductPK() {
		return mProductPK;
	}


	public String getDescription() {
		return mDescription;
	}


	public String getSerialNumber() {
		return mSerialNumber;
	}


	public String toString() {
		StringBuffer sb = new StringBuffer(0);

		sb.append("Asset(");

		sb.append(getPK()).append(",");
		sb.append(getProductPK()).append(",");
		sb.append(getDescription()).append(",");
		sb.append("sn=").append(getSerialNumber());
		sb.append(")");

		return sb.toString();

	}
}
