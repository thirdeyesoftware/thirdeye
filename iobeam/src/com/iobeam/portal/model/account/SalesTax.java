package com.iobeam.portal.model.account;


import java.util.Date;
import com.iobeam.portal.util.Money;

public class SalesTax extends AccountEntry implements Comparable {

	public SalesTax (long id, Money amount, Date postDate) {
		super(id, AccountEntryType.SALES_TAX, amount, postDate);
	}

	public String toString() {
		return "SalesTax:" + super.toString();
	}

	public int compareTo(Object o) {
		return super.compareTo(o);
	}
	
}

