package com.iobeam.portal.util.sequence;

import javax.ejb.*;
import java.rmi.*;

public interface Sequence extends EJBObject {

	public static final String DB_TYPE_ORACLE = "oracle";
	public static final String DB_TYPE_PGSQL = "pgsql";
	public static final String DB_TYPE_MYSQL = "mysql";

	public long getNextSequenceNumber(String sequenceTarget) throws RemoteException;

}
