package com.iobeam.portal.model.prototype.subscription;


import com.iobeam.portal.util.Duration;
import com.iobeam.portal.model.subscription.*;
import com.iobeam.portal.util.Money;


public class VenueStaffSubscriptionPrototype extends SubscriptionPrototype {

	private int mMaxGenerationCount;


	VenueStaffSubscriptionPrototype(
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
			int maxGenerationCount) {

		super(SubscriptionType.VENUE_STAFF,
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
	}

	VenueStaffSubscriptionPrototype(SubscriptionPrototypePK pk,
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
			int maxGenerationCount) {

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


		mMaxGenerationCount = maxGenerationCount;
	}


	public int getMaxGenerationCount() {
		return mMaxGenerationCount;
	}
}
