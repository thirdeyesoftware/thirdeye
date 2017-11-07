package com.iobeam.portal.model.billing;

/**
 * Describes a product or subscription, or any other abstraction that can be
 * purchased via a generalized purchasing procedure.
 */

import com.iobeam.portal.util.Money;

public interface Vendible {

	/** 
	* Returns true if Vendible can result in a charge to the 
	* containing Account for the specified BillingPeriod.
	*/
	public boolean isBillable(BillingPeriod period);

	/**
	 * returns Money reflecting the amount this Vendible will be charged to
	 * billable party.
	 */
	public Money getPrice();

	/**
	 * description used as line item description on bill presented to 
	 * billable party.
	 */
	public String getDescription();

	/**
	 * returns true if Vendible is taxable.
	 */
	public boolean isTaxable();

}

