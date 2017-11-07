package com.iobeam.portal.model.account;

import com.iobeam.portal.model.subscription.SubscriptionPK;
import com.iobeam.portal.model.product.ProductPK;
import com.iobeam.portal.util.Money;

import java.util.Date;
import java.text.*;


/**
* Describes any transaction moving money into or out of an Account.
*/
public abstract class AccountEntry implements Comparable, java.io.Serializable {

	private static final DateFormat cDateFormat =
			new SimpleDateFormat("MM/dd/yy HH:mm");

	private AccountEntryType mEntryType;
	private Money mAmount;
	private Date mPostDate;
	private long mID;
	private String mMemo;

	protected SubscriptionPK mSubscriptionPK;
	protected ProductPK mProductPK;
	
	public AccountEntry(long id, AccountEntryType entryType,
			Money amount, Date postDate) {
			
		mID = id;
		
		if (entryType == null) {
			throw new NullPointerException("entryType");
		}
		mEntryType = entryType;

		if (amount == null) {
			throw new NullPointerException("amount");
		}
		mAmount = amount;

		if (postDate == null) {
			throw new NullPointerException("postDate");
		}
		mPostDate = postDate;
	}

	
	public long getID() {
		return mID;
	}

	public AccountEntryType getAccountEntryType() {
		return mEntryType;
	}

	public void setProductPK(ProductPK pk) {
		mProductPK = pk;
	}

	public void setSubscriptionPK(SubscriptionPK pk) {
		mSubscriptionPK = pk;
	}
	
	public SubscriptionPK getSubscriptionPK() {
		return mSubscriptionPK;
	}

	public ProductPK getProductPK() {
		return mProductPK;
	}


	public AccountEntryType getEntryType() {
		return mEntryType;
	}


	public Money getAmount() {
		return mAmount;
	}


	public Date getPostDate() {
		return mPostDate;
	}

	public void setMemo(String memo) {
		mMemo = memo;
	}

	public String getMemo() {
		return mMemo;
	}

	public String toString() {
		return "AccountEntry(" + getEntryType() + "," +
				cDateFormat.format(getPostDate()) + "," +
				getAmount() + ")";
	}

	public int hashCode() {
		return (int)mID;
	}

	public boolean equals(Object o) {
		if (o instanceof AccountEntry) {
			AccountEntry entry = (AccountEntry)o;
			if (entry.getID() == getID() &&
					entry.getAmount().equals(getAmount()) &&
					entry.getPostDate().equals(getPostDate()) &&
					entry.getAccountEntryType().equals(getAccountEntryType()) &&
					getProductPK().equals(entry.getProductPK()) &&
					getSubscriptionPK().equals(entry.getSubscriptionPK())) {
				return true;
			}
		}
		return false;
	}

	public int compareTo(Object o) {
		AccountEntry entry = (AccountEntry)o;
		
		Long id = new Long(entry.getID());
		return id.compareTo(new Long(getID()));


	}

}
