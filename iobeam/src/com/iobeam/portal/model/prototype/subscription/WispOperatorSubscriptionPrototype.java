package com.iobeam.portal.model.prototype.subscription;


import com.iobeam.portal.util.Duration;
import com.iobeam.portal.model.subscription.*;
import com.iobeam.portal.util.Money;


public class WispOperatorSubscriptionPrototype extends SubscriptionPrototype {


	WispOperatorSubscriptionPrototype(
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

		super(SubscriptionType.WISP_OPERATOR,
				description, isAvailable,
				costPerBillingCycle,
				billingCycle, billingCycleCount,
				isTaxable,
				isCommissionable,
				defaultCommissionRate,
				duration,
				requiresRegistration);
	}


	WispOperatorSubscriptionPrototype(SubscriptionPrototypePK pk,
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

		super(pk, subscriptionType,
				description, isAvailable,
				costPerBillingCycle,
				billingCycle, billingCycleCount,
				isTaxable,
				isCommissionable,
				defaultCommissionRate,
				duration,
				requiresRegistration);
	}
}
