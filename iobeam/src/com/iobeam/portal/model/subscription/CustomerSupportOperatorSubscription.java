package com.iobeam.portal.model.subscription;


import java.util.Date;
import com.iobeam.portal.model.account.AccountPK;
import com.iobeam.portal.model.venue.VenuePK;
import com.iobeam.portal.model.prototype.subscription.*;
import com.iobeam.portal.util.Money;
import com.iobeam.portal.security.SecureID;


public class CustomerSupportOperatorSubscription extends Subscription {

	CustomerSupportOperatorSubscription(CustomerSupportOperatorSubscriptionPrototype proto,
			Date startDate,
			Date expirationDate) {

		super(SubscriptionType.CUSTOMER_SUPPORT_OPERATOR, proto.getPK(),
				startDate, expirationDate,
				null, 0, new Money(0), false, 0d, null, false);
	}

	/**
	* @see com.iobeam.portal.model.billing.Billable
	*/
	public boolean isTaxable() {
		return false;
	}


	public String toString() {
		StringBuffer sb = toStringBuffer();

		sb.insert(0, "CustomerSupportOperatorSubscription(");

		sb.append(")");

		return sb.toString();
	}
}
