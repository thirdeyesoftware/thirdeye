package com.iobeam.portal.model.subscription;


import com.iobeam.portal.model.venue.*;


/**
* Indicates a Subscription could not be used at a Venue because
* it is intended for use at a different venue.
*/
public class VenueMismatchSubscriptionException extends SubscriptionException {

	private VenuePK mVenuePK;

	public VenueMismatchSubscriptionException(VenuePK venuePK,
			Subscription subscription) {

		super(subscription.getPK() + " cannot be used at " + venuePK,
				subscription);

		mVenuePK = venuePK;
	}


	public VenueMismatchSubscriptionException(Throwable cause,
			VenuePK venuePK, Subscription subscription) {

		super(subscription.getPK() + " cannot be used at " + venuePK,
				cause, subscription);
	}


	public VenuePK getVenuePK() {
		return mVenuePK;
	}
}
