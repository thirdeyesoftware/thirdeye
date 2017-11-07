package com.iobeam.portal.util;

/**
 * validates specified credit card number against 
 * Credit Card  validation routines.
 */

 public class CreditCardValidator {
 
 	private static final String MASTERCARD_PREFIX = "5";
	private static final String VISA_PREFIX = "4";
	private static final String AMEX_PREFIX = "3";
	private static final String DISCOVER_PREFIX = "6011";
	
	public static boolean validate(String ccnum) {
	

		if (ccnum == null || ccnum.trim().equals("")) return false;
		
		System.out.println("num len=" + ccnum.length());

		if (ccnum.length()< 15) return false;

		if (!ccnum.startsWith(MASTERCARD_PREFIX) &&
				!ccnum.startsWith(VISA_PREFIX) && 
				!ccnum.startsWith(DISCOVER_PREFIX) &&
				!ccnum.startsWith(AMEX_PREFIX)) {
			return false;
		}


		return checkLUHN(ccnum);
	}

	private static boolean checkLUHN(String ccnumber) {
		int sum = 0;
		char[] num = ccnumber.toCharArray();

		// double the alternate digit starting from 1-length
		int len = num.length -2;

		int i = 0;
		int digitSum = 0;
		StringBuffer  temp =  new StringBuffer(0);

		for (i = len; i >=  0; i = i  - 2) {
			temp.append(String.valueOf((2 * Character.getNumericValue(num[i]))));
		}

		char[] digits = temp.toString().toCharArray();
		for (int z = 0; z < digits.length; z++) {
			sum += Character.getNumericValue(digits[z]);
		}

		// add the sums of the products above to the alternate digits
		// not used in the above procedure.
		int total = 0;
		for (i = 1; i < num.length; i = i + 2) {
			total += Character.getNumericValue(num[i]);
		}
		total += sum;

		return (total % 10 == 0);
	}

	public static void main(String args[]) {
		if (args.length < 1) {
			return;
		}
		System.out.println("testing " + args[0]);
		if (validate(args[0])) {
			System.out.println("number is valid.");
		}
		else System.out.println("INVALID #");
	
	}

}
