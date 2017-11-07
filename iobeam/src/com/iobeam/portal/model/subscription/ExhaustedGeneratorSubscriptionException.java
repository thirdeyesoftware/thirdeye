package com.iobeam.portal.model.subscription;


import com.iobeam.portal.model.venue.*;


/**
* Indicates an attempt to generate a Subscription using a
* generative Subscription that is empty.
*/
public class ExhaustedGeneratorSubscriptionException
		extends SubscriptionException {

	public ExhaustedGeneratorSubscriptionException(Subscription subscription) {

		super(subscription.getPK() + " has no remaining generation capacity",
				subscription);
	}


	public ExhaustedGeneratorSubscriptionException(Throwable cause,
			Subscription subscription) {

		super(subscription.getPK() + " has no remaining generation capacity",
				cause, subscription);
	}
}
