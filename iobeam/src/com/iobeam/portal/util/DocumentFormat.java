package com.iobeam.portal.util;

public interface DocumentFormat {
	public static final int EMAIL = 1;
	public static final int PRINT	=	2;

	public static final int LEFT_JUSTIFY = 100;
	public static final int RIGHT_JUSTIFY = 101;


	public String format(Object o) 
			throws DocumentFormatException;

}

