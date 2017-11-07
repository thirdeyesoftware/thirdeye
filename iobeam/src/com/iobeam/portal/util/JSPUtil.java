package com.iobeam.portal.util;

import java.util.*;
import com.iobeam.portal.model.country.*;
import javax.ejb.*;
import javax.naming.*;
import java.rmi.*;

public class JSPUtil {

	/**
	 * returns a collection of pair objects corresponding
	 * to the months of the year.
	 */
	public static Collection getMonths() {
		Vector v = new Vector();
		String[] months = {
			"January",
			"February",
			"March",
			"April",
			"May",
			"June",
			"July",
			"August",
			"September",
			"October",
			"November",
			"December"};
		
		for (int i = 0; i < months.length; i++) {
			String value = String.valueOf(i+1);
			if (value.length() < 2) {
				value = "0" + value;
			}
			v.addElement(
				new Pair(months[i], value));

		}

		return v;
	}

	public static Collection getYears() {
		String[] years = {
			"2003",
			"2004",
			"2005",
			"2006",
			"2007"};
		Vector v = new Vector();
		for (int i = 0; i < years.length; i++) {
			v.addElement(
				new Pair(years[i], years[i]));
		}
		return v;
	}

	public static Collection getCountries() {
		Vector v = new Vector();
		try {
			InitialContext ic = new InitialContext();
			AccessCountryHome achome = (AccessCountryHome)
				ic.lookup(AccessCountryHome.JNDI_NAME);
			AccessCountry ac = achome.create();
			for (Iterator it = ac.findAll().iterator(); it.hasNext();) {
				Country c = (Country)it.next();
				v.addElement(new Pair(
					c.getCountryName(), String.valueOf(c.getPK().getID())));

			}				
		}
		catch (Exception ee) {
			ee.printStackTrace();
		}
		return v;
	}

	public static String fixNull(String in) {
		if (in == null) return "";
		else return in;
	}

}
		
