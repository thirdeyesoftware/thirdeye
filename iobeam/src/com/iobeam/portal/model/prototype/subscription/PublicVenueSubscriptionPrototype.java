package com.iobeam.portal.model.prototype.subscription;


import com.iobeam.portal.util.Duration;
import com.iobeam.portal.model.subscription.*;
import com.iobeam.portal.util.Money;


public class PublicVenueSubscriptionPrototype extends SubscriptionPrototype {

	private boolean mHasAnonymousAccess;
	private int mMaxGenerationCount;
	private boolean mAllowStandAlone;


	PublicVenueSubscriptionPrototype(
			String description,
			boolean isAvailable,
			Money costPerBillingCycle,
			BillingCycle billingCycle,
			int billingCycleCount,
			boolean isTaxable,
			boolean isCommissionable,
			double defaultCommissionRate,
			Duration duration,
			boolean requiresRegistration,
			boolean hasAnonymousAccess,
			int maxGenerationCount,
			boolean allowstandalone) {

		super(SubscriptionType.PUBLIC_VENUE,
				description, isAvailable,
				costPerBillingCycle,
				billingCycle,
				billingCycleCount,
				isTaxable,
				isCommissionable,
				defaultCommissionRate,
				duration,
				requiresRegistration);

		mHasAnonymousAccess = hasAnonymousAccess;
		mMaxGenerationCount = maxGenerationCount;
		mAllowStandAlone = allowstandalone;
	}

	PublicVenueSubscriptionPrototype(SubscriptionPrototypePK pk,
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
			boolean requiresRegistration,
			boolean hasAnonymousAccess,
			int maxGenerationCount,
			boolean allowstandalone) {

		super(pk, subscriptionType,
				description, isAvailable,
				costPerBillingCycle,
				billingCycle,
				billingCycleCount,
				isTaxable,
				isCommissionable,
				defaultCommissionRate,
				duration,
				requiresRegistration);

		mHasAnonymousAccess = hasAnonymousAccess;
		mMaxGenerationCount = maxGenerationCount;
		mAllowStandAlone = allowstandalone;
	}


	public boolean hasAnonymousAccess() {
		return mHasAnonymousAccess;
	}

	public boolean allowStandAlone() {
		return mAllowStandAlone;
	}

	public int getMaxGenerationCount() {
		return mMaxGenerationCount;
	}
}
