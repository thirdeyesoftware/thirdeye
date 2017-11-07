package com.iobeam.portal.model.subscription;


import java.util.Date;
import com.iobeam.portal.model.account.AccountPK;
import com.iobeam.portal.model.venue.VenuePK;
import com.iobeam.portal.model.prototype.subscription.*;
import com.iobeam.portal.util.Money;
import com.iobeam.portal.security.SecureID;


public class WispOperatorSubscription extends Subscription {

	WispOperatorSubscription(WispOperatorSubscriptionPrototype proto,
			Date startDate,
			Date expirationDate,
			
			BillingCycle billingCycle,
			int billingCycleCount,
			Money rate,
			boolean isCommissionable,
			double defaultCommissionRate,
			SubscriptionPK parentSubscriptionPK,

			boolean requiresRegistration) {

		super(SubscriptionType.WISP_OPERATOR, proto.getPK(),
				startDate, expirationDate,
				
				billingCycle,
				billingCycleCount,
				rate,
				isCommissionable,
				defaultCommissionRate,
				parentSubscriptionPK,

				requiresRegistration);
	}


	WispOperatorSubscription(SubscriptionPK subscriptionPK,
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
			String channelCode) {

		super(subscriptionPK, secureID,
				accountPK, subscriptionType,
				subscriptionPrototypePK,
				subscriptionStatus, startDate, expirationDate,
				billingCycle, billingCycleCount,
				rate, currentBillingCycleCount,
				isCommissionable, defaultCommissionRate,
				parentSubscriptionPK, isRegistered, requiresRegistration,
				channelCode);


	}

	/**
	* @see com.iobeam.portal.model.billing.Billable
	*/
	public boolean isTaxable() {
		return false;
	}


	public String toString() {
		StringBuffer sb = toStringBuffer();

		sb.insert(0, "WispOperatorSubscription(");

		sb.append(")");

		return sb.toString();
	}
}
