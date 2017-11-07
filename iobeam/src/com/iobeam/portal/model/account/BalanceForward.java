package com.iobeam.portal.model.account;


import java.util.Date;
import com.iobeam.portal.util.Money;


public class BalanceForward extends AccountEntry implements Comparable  {

	public BalanceForward(long id, Money amount, Date postDate) {
		super(id, AccountEntryType.BALANCE_FORWARD, amount, postDate);
	}

	public String toString() {
		return "BalanceForward:" + super.toString();
	}

	public int compareTo(Object o) {
		return super.compareTo(o);
	}

}

