package com.iobeam.portal.model.product;

import com.iobeam.portal.util.Money;
import com.iobeam.portal.model.billing.Vendible;
import com.iobeam.portal.model.billing.Commissionable;
import com.iobeam.portal.model.billing.BillingPeriod;

public class Product 
		implements Vendible, Commissionable, java.io.Serializable {

	public static final long serialVersionUID = 200304180117L;

	private ProductPK mPK;
	private String mProductNumber;
	private boolean mIsCommissionable;
	private double mDefaultCommissionRate;
	private ProductCategory mCategory;
	private String mDescription;
	private boolean mIsTaxable;
	private Money mPrice;
	private int mBillingCycleCount;
	private boolean mIsActive;
	
	public Product(ProductPK pk, String productNumber, 
								Money Price, String desc, ProductCategory category,
								boolean isCommissionable, double commissionRate, 
								boolean isActive) {
		mPK = pk;
		mProductNumber = productNumber;
		mPrice = Price;
		mDescription = desc;
		mCategory = category;
		mIsCommissionable = isCommissionable;
		mDefaultCommissionRate = commissionRate;
		mIsActive = isActive;
	}

	public Product(ProductPK pk) {
		this(pk, null, null, null, null, false, 0, true);
	}

	public ProductPK getPK() {
		return mPK;
	}

	public String getProductNumber() {
		return mProductNumber;
	}

	public void setProductNumber(String s) {
		mProductNumber = s;
	}
	
	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String desc) {
		mDescription = desc;
	}

	public ProductCategory getProductCategory() {
		return mCategory;
	}

	public void setProductCategory(ProductCategory cat) {
		mCategory = cat;
	}

	public boolean isCommissionable() {
		return mIsCommissionable;
	}

	public void setIsCommissionable(boolean b) {
		mIsCommissionable = b;
	}

	public double getDefaultCommissionRate() {
		return mDefaultCommissionRate;
	}

	public void setDefaultCommissionRate(double d) {
		mDefaultCommissionRate = d;
	}

	public void setBillingCycleCount(int t) {
		mBillingCycleCount = t;
	}
	public int getBillingCycleCount() { 
		return mBillingCycleCount;
	}

	public void setIsTaxable(boolean b) {
		mIsTaxable = b;
	}

	public boolean isTaxable() {
		return mIsTaxable;
	}

	public boolean isBillable(BillingPeriod period) {
		return true;
	}

	
	public Money getPrice() {
		return mPrice;
	}

	public void setPrice(Money m) {
		mPrice = m;
	}

	public void setIsActive(boolean b) {
		mIsActive = b;
	}
	public boolean isActive() {
		return mIsActive;
	}
	
	public boolean equals(Object o) {
		if (o instanceof Product) {
			Product p = (Product)o;
			if (p.getPK().equals(getPK()) &&
					p.getPrice().equals(getPrice()) &&
					p.isCommissionable() == isCommissionable() &&
					p.getDefaultCommissionRate() == getDefaultCommissionRate() &&
					p.getProductCategory().equals(getProductCategory()))
				return true;

		}
		return false;
	}

	public String toString() {
	
		StringBuffer sb = new StringBuffer(0);
		sb.append("Product:");
		sb.append(mPK.toString()).append("\n");
		sb.append(mPrice.toString()).append("\n");
		sb.append(mDescription).append("\n");
		sb.append(mCategory.toString()).append("\n");
		sb.append("taxable? ").append(mIsTaxable).append("\n");
		sb.append("price:").append(mPrice).append("\n");
		sb.append("cycleCount:").append(mBillingCycleCount).append("\n");
		sb.append("commissionable:").append(mIsCommissionable).append("\n");
		sb.append("defCommRate:").append(mDefaultCommissionRate);
		
		return sb.toString();
	}
	
		
}

