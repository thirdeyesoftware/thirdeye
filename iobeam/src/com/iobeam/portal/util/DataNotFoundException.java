package com.iobeam.portal.util;



/**
* Indicates an update or other Data Access operation that could not
* complete because the record to be retrieved or modified was
* unexpectedly not found in the database.
*
* Unlike a generalized DataAccessException, this Exception may not be
* caused by a SQL error, and may only indicate a no-rows-found or
* no-rows-updated condition.
*/
public class DataNotFoundException extends DataAccessException {

	public DataNotFoundException(String msg) {
		super(msg);
	}

	public DataNotFoundException(String msg, String sql) {
		super(msg, sql);
	}
	
	public DataNotFoundException(String msg, String sql, Object dataObject) {
		super(msg, sql, dataObject);
	}
}
