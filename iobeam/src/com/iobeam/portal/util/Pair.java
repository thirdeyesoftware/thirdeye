package com.iobeam.portal.util;

/**
 * an object for struts collections
 */
public class Pair {
	
	public String label;
	public String value;

	public Pair(String label, String value) {
		this.label= label;
		this.value = value;
	}

	public String getLabel() {
		return this.label;
	}
	public String getValue() {
		return this.value;
	}
}

