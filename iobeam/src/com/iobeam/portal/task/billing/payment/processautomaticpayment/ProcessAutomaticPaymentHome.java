package com.iobeam.portal.task.billing.payment.processautomaticpayment;


import javax.ejb.EJBHome;
import javax.ejb.CreateException;
import java.rmi.RemoteException;


public interface ProcessAutomaticPaymentHome extends EJBHome {
	
	public static final String JNDI_NAME = 
			"iobeam.portal.ProcessAutomaticPayment";
	

	public ProcessAutomaticPayment create() 
			throws CreateException, RemoteException;
}



