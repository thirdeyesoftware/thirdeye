package com.iobeam.portal.util;
import java.sql.*;

public class DataAccessException extends Exception {

	private String mSQL;
	private Object mDataObject;
	
	public DataAccessException(String msg) {
		this(msg, null, null, null);
	}

	public DataAccessException(String msg, String sql) {
		this(msg, null, sql, null);
	}
	
	public DataAccessException(String msg, Throwable cause, String sql) {
		this(msg, cause, sql, null);
	}

	public DataAccessException(String msg, String sql, Object dataObject) {
		this(msg, null, sql, dataObject);
	}
	
	public DataAccessException(String msg, Throwable cause, String sql, 
			Object dataObject) {
		super(msg, cause);
		mSQL = sql;
		mDataObject = dataObject;
	}
	public String getSQL() {
		return mSQL;
	}

	public Object getDataObject() {
		return mDataObject;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(super.toString());
		sb.append("\n");
		sb.append(getSQL()).append("\n");
		if (getCause() instanceof SQLException) {
			sb.append("SQLException:" + getCause().toString());
			sb.append("\n");
		}	
		if (getDataObject() != null) {
			sb.append("data:").append(getDataObject());
		}
		return sb.toString();
	}
	
}
