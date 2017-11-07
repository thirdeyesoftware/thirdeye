package com.iobeam.gateway.router;


import com.iobeam.gateway.scheduler.*;


public class RouteLeaseRemovalTask implements ScheduledTask {

	private RouteLease mRouteLease;


	public RouteLeaseRemovalTask(RouteLease routeLease) {
		mRouteLease = routeLease;

		if (routeLease == null) {
			throw new NullPointerException("routeLease");
		}
	}


	public void doTask() throws SchedulerException {
		try {
			RouterFactory.getRouter().removeRouteLease(
					mRouteLease.getMACAddress());
		}
		catch (RouterException re) {
			throw new SchedulerException(re);
		}
	}


	/**
	 * returns a reference to the lease.
	 */
	public RouteLease getRouteLease() {
		return mRouteLease;
	}

	public String toString() {
		return "RouteLeaseRemovalTask(" + mRouteLease + ")";
	}
}
