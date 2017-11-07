package com.iobeam.portal.model.account;


import java.util.Date;
import com.iobeam.portal.util.Money;
import com.iobeam.portal.model.product.ProductPK;
import com.iobeam.portal.model.subscription.SubscriptionPK;

public class Purchase extends AccountEntry implements Comparable {

	public Purchase(long id, Money amount, Date postDate, ProductPK pk) {
		super(id, AccountEntryType.PURCHASE, amount, postDate);
		mProductPK = pk;
	}

	public Purchase(long id, Money amount, Date postDate, SubscriptionPK pk) {
		super(id, AccountEntryType.PURCHASE, amount, postDate);
		mSubscriptionPK = pk;
	}

	public boolean equals(Object o) {
		if (o instanceof Purchase) {
			Purchase p = (Purchase)o;
			if (p.getPostDate().equals(getPostDate()) &&
					p.getAmount().equals(getAmount()) &&
					getProductPK().equals(p.getProductPK()) &&
					getSubscriptionPK().equals(p.getSubscriptionPK()))
				return true;
			
		}
		return false;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("Purchase:");
		sb.append(getPostDate().toString()).append("\n");
		sb.append(getAmount()).append("\n");
		if (getProductPK() != null) {
			sb.append(getProductPK().toString());
		}
		else if (getSubscriptionPK() != null) {
			sb.append(getSubscriptionPK().toString());
		}
		return sb.toString();
	}

	public int compareTo(Object o) {
		return super.compareTo(o);
	}


}

