package com.iobeam.portal.model.prototype.subscription;


import com.iobeam.portal.util.Duration;
import com.iobeam.portal.model.subscription.*;
import com.iobeam.portal.util.Money;


public abstract class SubscriptionPrototype implements java.io.Serializable {

	private SubscriptionPrototypePK mPK = null;
	private SubscriptionType mSubscriptionType;
	private String mDescription;
	private boolean mIsAvailable;
	private BillingCycle mBillingCycle;
	private int mBillingCycleCount;
	private Money mCostPerBillingCycle;
	private boolean mIsTaxable;
	private boolean mIsCommissionable;
	private double mDefaultCommissionRate;
	private Duration mDuration;
	private boolean mRequiresRegistration;


	SubscriptionPrototype(
			SubscriptionType subscriptionType,
			String description,
			boolean isAvailable,
			Money costPerBillingCycle,
			BillingCycle billingCycle,
			int billingCycleCount,
			boolean isTaxable,
			boolean isCommissionable,
			double defaultCommissionRate,
			Duration duration,
			boolean requiresRegistration) {

		mSubscriptionType = subscriptionType;
		if (subscriptionType == null) {
			throw new NullPointerException("subscriptionType");
		}

		mDescription = description;

		mIsAvailable = isAvailable;

		mCostPerBillingCycle = costPerBillingCycle;
		if (costPerBillingCycle == null) {
			throw new NullPointerException("costPerBillingCycle");
		}

		mBillingCycle = billingCycle;
		if (billingCycle == null) {
			throw new NullPointerException("billingCycle");
		}

		mBillingCycleCount = billingCycleCount;

		mIsTaxable = isTaxable;
		mIsCommissionable = isCommissionable;
		mDefaultCommissionRate = defaultCommissionRate;

		mDuration = duration;
		if (duration == null) {
			throw new NullPointerException("duration");
		}

		mRequiresRegistration = requiresRegistration;
	}


	SubscriptionPrototype(SubscriptionPrototypePK pk,
			SubscriptionType subscriptionType,
			String description,
			boolean isAvailable,
			Money costPerBillingCycle,
			BillingCycle billingCycle,
			int billingCycleCount,
			boolean isTaxable,
			boolean isCommissionable,
			double defaultCommissionRate,
			Duration duration,
			boolean requiresRegistration) {

		mPK = pk;
		if (pk == null) {
			throw new NullPointerException("pk");
		}

		mSubscriptionType = subscriptionType;
		if (subscriptionType == null) {
			throw new NullPointerException("subscriptionType");
		}

		mDescription = description;

		mIsAvailable = isAvailable;

		mCostPerBillingCycle = costPerBillingCycle;
		if (costPerBillingCycle == null) {
			throw new NullPointerException("costPerBillingCycle");
		}

		mBillingCycle = billingCycle;
		if (billingCycle == null) {
			throw new NullPointerException("billingCycle");
		}

		mBillingCycleCount = billingCycleCount;

		mIsTaxable = isTaxable;
		mIsCommissionable = isCommissionable;
		mDefaultCommissionRate = defaultCommissionRate;

		mDuration = duration;
		if (duration == null) {
			throw new NullPointerException("duration");
		}

		mRequiresRegistration = requiresRegistration;
	}


	public SubscriptionPrototypePK getPK() {
		return mPK;
	}

	void setPK(SubscriptionPrototypePK pk) {
		mPK = pk;
		if (pk == null) {
			throw new NullPointerException("pk");
		}
	}


	/**
	* Returns the SubscriptionType of Subscriptions built from this
	* SubscriptionPrototype.
	*/
	public SubscriptionType getSubscriptionType() {
		return mSubscriptionType;
	}


	/**
	* Returns a text description of the SubscriptionPrototype,
	* suitable for accounting line item (AccountEntry).
	*/
	public String getDescription() {
		return mDescription;
	}


	/**
	* Returns true if this SubscriptionPrototype is available for
	* selection by users.
	*/
	public boolean isAvailable() {
		return mIsAvailable;
	}


	public Money getCostPerBillingCycle() {
		return mCostPerBillingCycle;
	}


	/**
	* Returns the BillingCycle on which Subscriptions built on this
	* SubscriptionPrototype will be billed.
	*/
	public BillingCycle getBillingCycle() {
		return mBillingCycle;
	}


	/**
	* Returns the number of BillingCycles in the billable lifespan of any
	* Subscription built from this SubscriptionPrototype.
	*
	* This is the total number of BillingCycles in which the
	* corresponding Subscription
	* is billed to the Customer, before the Subscription is no longer billable. 
	*
	* A value of 0 indicates the Subscription has an open-ended
	* lifespan.
	*/
	public int getBillingCycleCount() {
		return mBillingCycleCount;
	}


	/**
	* Indicates Subscroptions built from this prototype are
	* subject to sales tax.
	*/
	public boolean isTaxable() {
		return mIsTaxable;
	}

	public boolean isCommissionable() {
		return mIsCommissionable;
	}

	public double getDefaultCommissionRate() {
		return mDefaultCommissionRate;
	}


	/**
	* Returns the time duration of generated Subscriptions.  This
	* value indicates that the Subscription is a time limited
	* or an open-ended Subscription (Duration.CONTINUOUS).
	*
	* The endDate of the resulting Subscription is determined
	* by the Duration.  A CONTINUOUS Duration is associated
	* with a null endDate.
	*/
	public Duration getDuration() {
		return mDuration;
	}


	/**
	* Returns true if Subscriptions built from this prototype
	* must be registered to the account holder prior to use.
	*/
	public boolean requiresRegistration() {
		return mRequiresRegistration;
	}


	public String toString() {
		StringBuffer sb = new StringBuffer("SubscriptionPrototype(");

		sb.append(getPK()).append(",");
		sb.append(getSubscriptionType()).append(",");
		sb.append(getDescription()).append(",");
		sb.append(getDuration());

		sb.append(")");

		return sb.toString();
	}
}
