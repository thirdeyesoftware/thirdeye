package com.iobeam.portal.security;


import com.iobeam.portal.model.venue.VenuePK;


public class NoSuchVenueException extends Exception {
	private VenuePK mVenuePK;

	public NoSuchVenueException(VenuePK venuePK) {
		super(venuePK + " is unknown");

		mVenuePK = venuePK;
	}


	public NoSuchVenueException(VenuePK venuePK, Throwable cause) {
		super(venuePK + " is unknown", cause);

		mVenuePK = venuePK;
	}


	public VenuePK getVenuePK() {
		return mVenuePK;
	}
}
