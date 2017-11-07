package com.iobeam.portal.model.prototype.subscription;


import com.iobeam.portal.util.Duration;
import com.iobeam.portal.model.subscription.*;
import com.iobeam.portal.util.Money;


public class CustomerSupportOperatorSubscriptionPrototype extends SubscriptionPrototype {


	CustomerSupportOperatorSubscriptionPrototype(
			String description,
			boolean isAvailable,
			boolean isCommissionable) {

		super(SubscriptionType.CUSTOMER_SUPPORT_OPERATOR,
				description, isAvailable,
				new Money(0), null, 0, 
				false,
				false,
				0d, Duration.CONTINUOUS, false);
	}


	CustomerSupportOperatorSubscriptionPrototype(SubscriptionPrototypePK pk,
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
