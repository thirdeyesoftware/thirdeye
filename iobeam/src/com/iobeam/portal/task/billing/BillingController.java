package com.iobeam.portal.task.billing;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;
import java.util.Date;

import com.iobeam.portal.model.billing.BillingPeriod;
import com.iobeam.portal.model.billing.BillingException;

public interface BillingController extends EJBObject {

	/**
	 * billing process step that creates account entries for 
	 * billable subscription during the specified billing period.
	 */
	public static final int PROCESS_ACCOUNTS		= 	1;

	/**
	 * billing process step that creates invoices for 
	 * account activity during the specified billing period.
	 */
	public static final int CREATE_INVOICES			= 	2;

	/**
	 * billing process step that delivers invoices to billable
	 * parties.  this step sends an email or printed
	 * bill depending on billable party's preferences.
	 */
	public static final int DELIVER_INVOICES			=		3;
	
	/**
	 * billing process step that processes invoices for accounts
	 * with an automatic payment instrument.
	 */
	public static final int PROCESS_INVOICES		= 	4;

	/**
	 * billing process step that creates statements and delivers them
	 * to billable party.
	 */
	public static final int PROCESS_STATEMENTS	=		5;


	/**
	 * executes the specefied billing step for the 
	 * specified period.
	 */
	public void run(int step, BillingPeriod period) 
			throws BillingException, RemoteException;


	/**
	 * run all the billing steps for the specified
	 * billing period.
	 */
	public void run(BillingPeriod period) 
			throws BillingException, RemoteException;

	/**
	 * run the specified billing step for the billing period
	 * containing the specified target date.
	 */
	public void run(int step, Date targetDate)
			throws BillingException, RemoteException;

}

