package com.iobeam.gateway.scheduler;


/**
* A task to be run by the Scheduler.  ScheduledTasks are associated with
* a ScheduledEvents, which are known to the Scheduler.
*/
public interface ScheduledTask {

	public void doTask() throws SchedulerException;
}
