package com.iobeam.portal.model.subscription;


import java.util.Date;
import com.iobeam.portal.model.account.AccountPK;
import com.iobeam.portal.model.venue.VenuePK;
import com.iobeam.portal.model.prototype.subscription.*;
import com.iobeam.portal.util.Money;
import com.iobeam.portal.security.SecureID;


public class PrivateVenueSubscription extends Subscription
		implements SubscriptionGenerator {

	private int mMaxGenerationCount;
	private int mCurrentGenerationCount;
	private boolean mAllowStandAlone;


	PrivateVenueSubscription(PrivateVenueSubscriptionPrototype proto,
			Date startDate,
			Date expirationDate,

			BillingCycle billingCycle,
			int billingCycleCount,
			Money rate,
			boolean isCommissionable,
			double defaultCommissionRate,
			SubscriptionPK parentSubscriptionPK,

			boolean requiresRegistration,

			int maxGenerationCount,
			boolean allowstandalone) {

		super(SubscriptionType.PRIVATE_VENUE, proto.getPK(),
				startDate, expirationDate,

				billingCycle,
				billingCycleCount,
				rate,
				isCommissionable,
				defaultCommissionRate,
				parentSubscriptionPK,

				requiresRegistration);

		mMaxGenerationCount = maxGenerationCount;
		mCurrentGenerationCount = 0;
		mAllowStandAlone = allowstandalone;
	}


	PrivateVenueSubscription(SubscriptionPK subscriptionPK,
			SecureID secureID,
			AccountPK accountPK,
			SubscriptionType subscriptionType,
			SubscriptionPrototypePK subscriptionPrototypePK,
			SubscriptionStatus subscriptionStatus,
			Date startDate,
			Date expirationDate,
			BillingCycle billingCycle,
			int billingCycleCount,
			Money rate,
			int currentBillingCycleCount,
			boolean isCommissionable,
			double defaultCommissionRate,
			SubscriptionPK parentSubscriptionPK,
			boolean isRegistered,
			boolean requiresRegistration,
			String channelCode,

			int maxGenerationCount,
			int currentGenerationCount,
			boolean allowstandalone) {
			

		super(subscriptionPK, secureID,
				accountPK, subscriptionType,
				subscriptionPrototypePK,
				subscriptionStatus, startDate, expirationDate,
				billingCycle, billingCycleCount,
				rate, currentBillingCycleCount,
				isCommissionable, defaultCommissionRate,
				parentSubscriptionPK, isRegistered, requiresRegistration,
				channelCode);


		mMaxGenerationCount = maxGenerationCount;
		mCurrentGenerationCount = currentGenerationCount;
		mAllowStandAlone = allowstandalone;
	}

	public boolean allowStandAlone() {
		return mAllowStandAlone;
	}

	/**
	* Returns the maximum number of PrivateMemberSubscriptions that may be
	* generated under this Subscription.
	*/
	public int getMaxGenerationCount() {
		return mMaxGenerationCount;
	}


	public int getCurrentGenerationCount() {
		return mCurrentGenerationCount;
	}


	public void setCurrentGenerationCount(int currentGenerationCount) {
		if (currentGenerationCount > getMaxGenerationCount()) {
			throw new IllegalArgumentException("currentGenerationCount " +
					currentGenerationCount + " > max " +
					getMaxGenerationCount());
		}
		mCurrentGenerationCount = currentGenerationCount;
	}


	/**
	* @see com.iobeam.portal.model.billing.Billable
	*/
	public boolean isTaxable() {
		return false;
	}


	public String toString() {
		StringBuffer sb = toStringBuffer();

		sb.insert(0, "PrivateVenueSubscription(");

		sb.append(",");
		sb.append("maxgen=").append(getMaxGenerationCount()).append(",");
		sb.append("curgen=").append(getCurrentGenerationCount());

		sb.append(")");

		return sb.toString();
	}
}
