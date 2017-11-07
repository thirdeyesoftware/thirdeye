package com.iobeam.portal.model.prototype.subscription;


import com.iobeam.portal.util.Duration;
import com.iobeam.portal.model.subscription.*;
import com.iobeam.portal.util.Money;


public class PrivateVenueSubscriptionPrototype extends SubscriptionPrototype {

	private int mMaxGenerationCount;

	private boolean mAllowStandAlone = false;


	PrivateVenueSubscriptionPrototype(
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
			int maxGenerationCount,
			boolean allowstandalone) {

		super(SubscriptionType.PRIVATE_VENUE,
				description, isAvailable,
				costPerBillingCycle,
				billingCycle,
				billingCycleCount,
				isTaxable,
				isCommissionable,
				defaultCommissionRate,
				duration,
				requiresRegistration);

		mMaxGenerationCount = maxGenerationCount;
		mAllowStandAlone = allowstandalone;
	}


	PrivateVenueSubscriptionPrototype(SubscriptionPrototypePK pk,
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

		mAllowStandAlone = allowstandalone;
		mMaxGenerationCount = maxGenerationCount;
	}


	public int getMaxGenerationCount() {
		return mMaxGenerationCount;
	}

	public boolean allowStandAlone() {
		return mAllowStandAlone;
	}
}

