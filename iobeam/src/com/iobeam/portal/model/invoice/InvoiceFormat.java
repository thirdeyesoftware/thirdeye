package com.iobeam.portal.model.invoice;

import java.text.SimpleDateFormat;
import java.text.NumberFormat;
import java.text.DecimalFormat;

import java.util.*;
import java.util.logging.Logger;
import javax.naming.*;
import javax.ejb.*;
import com.iobeam.portal.util.DocumentFormat;
import com.iobeam.portal.util.DocumentFormatException;
import com.iobeam.portal.util.Money;

import com.iobeam.portal.model.account.*;
import com.iobeam.portal.model.billing.*;
import com.iobeam.portal.model.product.*;

/** 
 * InvoiceFormat formats an invoice for printing or email delivery.
 * @type - DocumentFormat type 
 */

public class InvoiceFormat implements DocumentFormat {

	private int mType;
	private static final DecimalFormat cDecimalFormat = 
			new DecimalFormat("#0.00");

	public InvoiceFormat(int type) {
		mType = type;
	}

	/**
	 * returns a formatted String for the specified invoice 
	 * object.
	 */
	public String format(Object o) 
			throws DocumentFormatException {
		Invoice invoice;

		if (o instanceof Invoice) {
			invoice = (Invoice)o;
		}
		else {
			throw new DocumentFormatException("unsupported target.");
		}

		if (mType == EMAIL) {
			return formatEmail(invoice);
		}
		else {
			return formatPrint(invoice);
		}
	}

	private String formatEmail(Invoice invoice) 
			throws DocumentFormatException {
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		StringBuffer message = new StringBuffer(0);
		try {
			message.append(pad("Account Number:", 20, RIGHT_JUSTIFY));
			message.append("\t").append(
				getAccountNumber(invoice.getAccountPK()).getAccountNumberString());
			message.append("\n");
			message.append(pad("Invoice Number:", 20, RIGHT_JUSTIFY));
			message.append("\t").append(
				invoice.getInvoiceNumber().getInvoiceNumber());
			message.append("\n");
			message.append(pad("Invoice Date:", 20, RIGHT_JUSTIFY));
			message.append("\t").append(
				sdf.format(invoice.getInvoiceDate()));
			message.append("\n");
			message.append(pad("Total Due:", 20, RIGHT_JUSTIFY));
			message.append("\t").append(
				NumberFormat.getCurrencyInstance().format(
					invoice.getTotalDue().getAmount()));
			message.append("\n\n\n");
			message.append("Details:\n\n");
			message.append(pad("Item",20, LEFT_JUSTIFY));
			message.append("\t\t");
			message.append(pad("Date",20, LEFT_JUSTIFY));
			message.append("\t\t");
			message.append(pad("Amount",20, RIGHT_JUSTIFY));
			message.append("\n");

			for (Iterator it =
					invoice.getInvoiceLineItems().iterator();it.hasNext();) {
				InvoiceLineItem item = (InvoiceLineItem)it.next();
				AccountEntry entry = item.getAccountEntry();

				Logger.getLogger("com.iobeam.portal.model.invoice").
					info("invoice line item id = " + item.getID());
				Logger.getLogger("com.iobeam.portal.model.invoice").
					info("entry = " + entry.toString());

				message.append(
					pad(entry.getAccountEntryType().getName(), 20, LEFT_JUSTIFY));

				message.append("\t\t");

				message.append(
					pad(sdf.format(entry.getPostDate()),20, LEFT_JUSTIFY));

				message.append("\t\t");
				double amt = entry.getAmount().getAmount();
				if (entry.getAccountEntryType().equals(AccountEntryType.PAYMENT)) {
					amt = invertDouble(amt);
				}
				message.append( 
					pad(NumberFormat.getCurrencyInstance().format(
						amt), 20, RIGHT_JUSTIFY));
				message.append("\n");
			}

		}
		catch (Exception e) {
			e.printStackTrace();
			throw new DocumentFormatException(
				"error while formatting invoice.",e);
		}
		return message.toString();
	}

	private String formatPrint(Invoice invoice) 
			throws DocumentFormatException {
		throw new UnsupportedOperationException("no impl yet.");
	}


	private String formatDouble(double d) {
		return cDecimalFormat.format(d);
	}

	private double invertDouble(double d) {
		return (-1 * d);
	}

	private String pad(String token, int width, int justification) {
		if (token.length() >= width) return token;

		int numberToAdd = width - token.length();
		StringBuffer s = new StringBuffer(0);
		for (int i = 0; i < numberToAdd; i++) {
			s.append(" ");
		}
		if (justification == RIGHT_JUSTIFY) {
			return (s.append(token)).toString();
		} 
		else {
			return token + s.toString();
		}
	}

	public static void main(String args[]) throws Exception  {
		Invoice invoice = new Invoice(
			new InvoicePK(1), new InvoiceNumber("1000101"),
			new Date(), new AccountPK(1), new Money(100),
			InvoiceStatus.INVOICE_OPEN);
		Vector v = new Vector();
		Purchase purchase = new Purchase(
			0, new Money(100), new Date(), new ProductPK(1));
		v.addElement(new InvoiceLineItem(0, purchase));
		Payment payment = new Payment(0, new Money(10), new Date(),
			PaymentInstrumentType.CHECK, "memo");
		v.addElement(new InvoiceLineItem(1,payment));
		invoice.setInvoiceLineItems(v);
		InvoiceFormat format = new InvoiceFormat(EMAIL);
		System.out.println(format.format(invoice));
	}


	private AccountNumber getAccountNumber(AccountPK pk)
			throws Exception {
	
		AccountHome home = (AccountHome)
			(new InitialContext()).lookup(AccountHome.JNDI_NAME);
		Account account = home.findByPrimaryKey(pk);
		return account.getData().getAccountNumber();
	}


}

