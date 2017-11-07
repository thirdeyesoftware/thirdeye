package com.iobeam.portal.model.billing;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.NumberFormat;

import com.iobeam.portal.util.DocumentFormat;
import com.iobeam.portal.util.DocumentFormatException;

public class StatementFormat implements DocumentFormat {

	private int mType;

	public StatementFormat(int type) {
		mType = type;
	}

	/**
	 * returns a formatted String for the specified statement
	 * object.
	 */
	public String format(Object o) 
			throws DocumentFormatException {
		Statement stmt;

		if (o instanceof Statement) {
			stmt = (Statement)o;
		}
		else {
			throw new DocumentFormatException("unsupported target.");
		}

		if (mType == EMAIL) {
			return formatEmail(stmt);
		}
		else {
			return formatPrint(stmt);
		}
	}

	private String formatEmail(Statement stmt) 
			throws DocumentFormatException {
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		StringBuffer message = new StringBuffer(0);
		try {
			message.append("Statement for " + 
				"account:\t").append(stmt.getAccountPK().getID());
			message.append("\n");
			message.append("\n");
			message.append("Invoice:\t").append(
				stmt.getInvoice().getInvoiceNumber().getInvoiceNumber());
			message.append("\t\t").append(
				NumberFormat.getCurrencyInstance().format(
					stmt.getInvoice().getTotalDue().getAmount()));
		}
		catch (Exception e) {
			throw new DocumentFormatException(
				"error while formatting statement.",e);
		}
		return message.toString();
	}

	private String formatPrint(Statement stmt) 
			throws DocumentFormatException {
		throw new UnsupportedOperationException("no impl yet.");
	}

}

