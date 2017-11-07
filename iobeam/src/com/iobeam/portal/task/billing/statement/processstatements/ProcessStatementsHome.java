package com.iobeam.portal.task.billing.statement.processstatements;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import java.rmi.RemoteException;

public interface ProcessStatementsHome extends EJBHome {

	public static final String JNDI_NAME = 	
			"iobeam.portal.ProcessStatementsHome";

	public ProcessStatements create() 
			throws CreateException, RemoteException;
}

