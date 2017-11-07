package com.iobeam.portal.util;


import java.util.Currency;
import java.util.Locale;
import java.text.DecimalFormat;


public class Money implements java.io.Serializable {

	private double mAmount;
	private Currency mCurrency;

	private static DecimalFormat cDecimalFormat =
			new DecimalFormat("#,##0.00");

	private static DecimalFormat cCleanDecimalFormat =
			new DecimalFormat("###0.00");

	private static Currency USD = Currency.getInstance(Locale.US);


	public Money(double amount, Currency currency) {
		mAmount = amount;

		mCurrency = currency;
		if (currency == null) {
			throw new NullPointerException("currency");
		}
	}


	public Money(double amount) {
		this(amount, USD);
	}
	

	public double getAmount() {
		return mAmount;
	}


	public Currency getCurrency() {
		return mCurrency;
	}


	public String getStringAmount() {
		return cCleanDecimalFormat.format(getAmount());
	}

	public String getFormattedStringAmount() {
		return cDecimalFormat.format(getAmount());
	}

	

	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("Money(");
		sb.append(cDecimalFormat.format(getAmount())).append(",");
		sb.append(getCurrency().toString());
		sb.append(")");

		return sb.toString();
	}
}
