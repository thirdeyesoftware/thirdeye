package com.iobeam.portal.model.subscription;


import java.util.Date;
import java.text.*;
import com.iobeam.portal.model.billing.*;
import com.iobeam.portal.model.account.AccountPK;
import com.iobeam.portal.model.prototype.subscription.*;
import com.iobeam.portal.model.venue.VenuePK;
import com.iobeam.portal.util.Money;
import com.iobeam.portal.security.SecureID;



public abstract class Subscription 
		implements Commissionable, Vendible, java.io.Serializable {


	private static DateFormat cDateFormat =
			new SimpleDateFormat("MM/dd/yy HH:mm");

	private SubscriptionPK mSubscriptionPK = null;
	private SecureID mSecureID = null;

	private AccountPK mAccountPK;
	private SubscriptionType mSubscriptionType;
	private SubscriptionPrototypePK mSubscriptionPrototypePK;
	private SubscriptionStatus mSubscriptionStatus;

	private Date mStartDate;
	private Date mExpirationDate;

	private BillingCycle mBillingCycle;
	private int mBillingCycleCount;
	private Money mCostPerBillingCycle;
	private int mCurrentBillingCycleCount;

	private boolean mIsCommissionable;
	private double mDefaultCommissionRate;

	private SubscriptionPK mParentSubscriptionPK = null;
	private boolean mIsRegistered = false;
	private boolean mRequiresRegistration = false;

	private String mDescription = null;
	private String mChannelCode = null;

	

	Subscription(SubscriptionType subscriptionType,
			SubscriptionPrototypePK protoPK,
			Date startDate,
			Date expirationDate,

			BillingCycle billingCycle,
			int billingCycleCount,
			Money costPerBillingCycle,
			boolean isCommissionable,
			double defaultCommissionRate,
			SubscriptionPK parentSubscriptionPK,

			boolean requiresRegistration) {

		mSubscriptionType = subscriptionType;
		if (subscriptionType == null) {
			throw new NullPointerException("subscriptiontType");
		}

		mSubscriptionPrototypePK = protoPK;
		if (protoPK == null) {
			throw new NullPointerException("protoPK");
		}

		mStartDate = startDate;
		mExpirationDate = expirationDate;

		mBillingCycle = billingCycle;
		if (billingCycle == null) {
			throw new NullPointerException("billingCycle");
		}

		mBillingCycleCount = billingCycleCount;

		mCostPerBillingCycle = costPerBillingCycle;
		if (costPerBillingCycle == null) {
			throw new NullPointerException("costPerBillingCycle");
		}

		mIsCommissionable = isCommissionable;
		mDefaultCommissionRate = defaultCommissionRate;
		mParentSubscriptionPK = parentSubscriptionPK;

		if (startDate == null) {
			mSubscriptionStatus = SubscriptionStatus.CREATED;
		} else {
			mSubscriptionStatus = SubscriptionStatus.ACTIVE;
		}

		mRequiresRegistration = requiresRegistration;
	}



	Subscription(
			SubscriptionPK subscriptionPK,
			SecureID secureID,
			AccountPK accountPK,
			SubscriptionType subscriptionType,
			SubscriptionPrototypePK subscriptionPrototypePK,
			SubscriptionStatus subscriptionStatus,
			Date startDate,
			Date expirationDate,
			BillingCycle billingCycle,
			int billingCycleCount,
			Money costPerBillingCycle,
			int currentBillingCycleCount,
			boolean isCommissionable,
			double defaultCommissionRate,
			SubscriptionPK parentSubscriptionPK,
			boolean isRegistered,
			boolean requiresRegistration,
			String channelCode) {


		mSubscriptionPK = subscriptionPK;
		if (subscriptionPK == null) {
			throw new NullPointerException("subscriptionPK");
		}

		mSecureID = secureID;

		mAccountPK = accountPK;
		if (accountPK == null) {
			throw new NullPointerException("accountPK");
		}

		mSubscriptionType = subscriptionType;
		if (subscriptionType == null) {
			throw new NullPointerException("subscriptionType");
		}
		mSubscriptionPrototypePK = subscriptionPrototypePK;
		if (subscriptionPrototypePK == null) {
			throw new NullPointerException("subscriptionPrototypePK");
		}
		mSubscriptionStatus = subscriptionStatus;
		mStartDate = startDate;
		mExpirationDate = expirationDate;


		mBillingCycle = billingCycle;
		if (billingCycle == null) {
			throw new NullPointerException("billingCycle");
		}

		mBillingCycleCount = billingCycleCount;

		mCostPerBillingCycle = costPerBillingCycle;
		if (costPerBillingCycle == null) {
			throw new NullPointerException("costPerBillingCycle");
		}

		mCurrentBillingCycleCount = currentBillingCycleCount;
		mIsCommissionable = isCommissionable;
		mDefaultCommissionRate = defaultCommissionRate;

		mParentSubscriptionPK = parentSubscriptionPK;
		mIsRegistered = isRegistered;
		mRequiresRegistration = requiresRegistration;

		mChannelCode = channelCode;
	}


	public SubscriptionStatus getSubscriptionStatus() {
		return mSubscriptionStatus;
	}

	public SubscriptionType getSubscriptionType() {
		return mSubscriptionType;
	}

	void setDefaultCommissionRate(double d) {
		mDefaultCommissionRate = d;
	}

	public double getDefaultCommissionRate() {
		return mDefaultCommissionRate;
	}


	void setIsCommissionable(boolean b) {
		mIsCommissionable = b;
	}

	public boolean isCommissionable() {
		return mIsCommissionable;
	}
	

	public AccountPK getAccountPK() {
		return mAccountPK;
	}

	public void setAccountPK(AccountPK pk) {
		mAccountPK = pk;
	}
	
	public boolean isActive() {
		return mSubscriptionStatus.equals(SubscriptionStatus.ACTIVE);
	}

	public void setSubscriptionStatus(SubscriptionStatus status) {
		mSubscriptionStatus = status;
	}



	/**
	* The BillingCycle on which this Subscription is billed.
	* This value is set via the BillingCycle property on the
	* Subscription's SubscriptionPrototype.
	*/
	public BillingCycle getBillingCycle() {
		return mBillingCycle;
	}

	/**
	* Returns the total number of BillingCycles in the billable
	* lifecycle of this Subscription, or 0 if this is an open-ended
	* Subscription.
	*
	* The Subscription is no longer
	* billable when the currentBillingCycleCount >= the billingCycleCount
	* for non-zero billingCycleCounts.
	*/
	public int getBillingCycleCount() {
		return mBillingCycleCount;
	}

	private void setBillingCycleCount(int t) {
		mBillingCycleCount = t;
	}

	/**
	* Number of BillingCycles that remain before this Subscription
	* is no longer billable.
	*/
	public int getCurrentBillingCycleCount() {
		return mCurrentBillingCycleCount;
	}

	public void setCurrentBillingCycleCount(int c) {
		mCurrentBillingCycleCount = c;
	}

	public Money getCostPerBillingCycle() {
		return mCostPerBillingCycle;
	}

	void setCostPerBillingCycle(Money m) {
		mCostPerBillingCycle = m;
	}

	
	public SubscriptionPK getPK() {
		return mSubscriptionPK;
	}


	void setPK(SubscriptionPK pk) {
		mSubscriptionPK = pk;
	}

	

	public SubscriptionPrototypePK getSubscriptionPrototypePK() {
		return mSubscriptionPrototypePK;
	}


	/**
	* Returns the Date and time when this Subscription starts
	* (and becomes usable) or null if the Subscription has no
	* assigned start date (and so has not yet started.)
	*/
	public Date getStartDate() {
		return mStartDate;
	}

	/**
	* Sets the start date and time if it is not yet set.
	*/
	public void setStartDate(Date startDate) {
		if (mStartDate == null) {
			mStartDate = startDate;
		} else {
			throw new IllegalStateException("start date already set");
		}
	}


	/**
	* Returns the Date and time when this Subscription expires, or null
	* if the Subscription does not expire.
	*/
	public Date getExpirationDate() {
		return mExpirationDate;
	}


	public void setExpirationDate(Date expirationDate) {
		mExpirationDate = expirationDate;
	}



	/**
	* Returns the SubscriptionPK of the parent (generator)
	* for this Subscription, or null if there is none.
	*
	* The parent Subscription is the Subscription responsible for
	* the generation of this Subscription.  Some SubscriptionTypes are
	* generative, and only those may be parents.
	*/
	public SubscriptionPK getParentSubscriptionPK() {
		return mParentSubscriptionPK;
	}


	/**
	* Returns the optional SecureID that is used to identify this
	* Subscription via SubscriptionCard or other physicial media.
	*/
	public SecureID getSecureID() {
		return mSecureID;
	}


	void setSecureID(SecureID secureID) {
		if (secureID == null) {
			throw new NullPointerException("secureID");
		}

		if (mSecureID != null) {
			throw new IllegalStateException("already has a secureID " +
					mSecureID);
		}

		mSecureID = secureID;
	}


	/**
	* Returns true if this Subscription is registered to the Customer
	* that holds the Account containing this Subscription.   Venues
	* holding Member Subscriptions for distribution are not the
	* registered holders of those Subscriptions.   Members who claim
	* a Subscription from a Venue pool of Member Subscriptions are
	* the registered holders of those Subscriptions.
	*/
	public boolean isRegistered() {
		return mIsRegistered;
	}


	public void setRegistered(boolean isRegistered) {
		mIsRegistered = isRegistered;
	}


	/**
	* Returns true if this Subscription requires that it be registered
	* to the Account holder prior to use.
	*/
	public boolean requiresRegistration() {
		return mRequiresRegistration;
	}


	public String toString() {
		StringBuffer sb = toStringBuffer();

		sb.insert(0, "Subscription(");

		sb.append(")");

		return sb.toString();
	}


	StringBuffer toStringBuffer() {
		StringBuffer sb = new StringBuffer();

		sb.append(getPK()).append(",");
		if (getDescription() != null) {
			sb.append(getDescription());
			sb.append(",");
		}
		sb.append(getSecureID()).append(",");
		sb.append(getAccountPK()).append(",");
		sb.append(getSubscriptionType()).append(",");
		sb.append(getSubscriptionStatus()).append(",");

		if (getStartDate() != null) {
			sb.append("start=").append(cDateFormat.format(getStartDate()));
			sb.append(",");
		}
		if (getExpirationDate() != null) {
			sb.append("end=").append(cDateFormat.format(getExpirationDate()));
			sb.append(",");
		}

		sb.append(requiresRegistration() ?
				"reg required" : "reg not required").append(",");
		sb.append(isRegistered() ? "registered" : "unregistered").append(",");
		if (getChannelCode() != null) {
			sb.append("channelCode=").append(getChannelCode()).append(",");
		}
		sb.append(getPrice());

		return sb;
	}




	/**
	* @see com.iobeam.portal.model.billing.Vendible
	*/
	public String getDescription() {
		return mDescription;
	}


	void setDescription(String description) {
		mDescription = description;
	}


	/**
	* Returns true if this Vendible can result in a
	* charge to the containing Account for the specified BillingPeriod.
	*
	* @see com.iobeam.portal.model.billing.Vendible
	*/
	public boolean isBillable(BillingPeriod period) {
		boolean p = true;

		// p &= getPrice().getAmount() != 0;

		p &= getSubscriptionStatus().equals(SubscriptionStatus.ACTIVE);

		if (getBillingCycleCount() > 0) {
			p &= getBillingCycleCount() > getCurrentBillingCycleCount();
		}


		// bikestudga: if billingcycle is immediate && status
		// is active == return true;
		// 
		// bikestudga: if billingcycle is monthly and status
		// is active return true
		//
		// bikestudga: if billingcycle is quarterly and billingperiod
		// lands on quarter (this requires definition), return true.

		return p;
	}


	/**
	* @see com.iobeam.portal.model.billing.Vendible
	*/
	public abstract boolean isTaxable();


	/**
	* @see com.iobeam.portal.model.billing.Vendible
	*/
	public Money getPrice() {
		return getCostPerBillingCycle();
	}


	public void setChannelCode(String channelCode) {
		if (mChannelCode == null) {
			mChannelCode = channelCode;
		} else {
			throw new IllegalStateException("channelCode already set");
		}
	}


	/**
	* Returns a String code used to identify the sales channel
	* responsible for the sale of this Subscription, or null if there
	* is none.
	*/
	public String getChannelCode() {
		return mChannelCode;
	}
}
