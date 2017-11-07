package com.iobeam.portal.task.billing.payment.applypayment;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;
import java.rmi.RemoteException;

public interface ApplyPaymentHome extends EJBHome {

	public static final String JNDI_NAME = 
		"iobeam.portal.ApplyPayment";

	public ApplyPayment create() throws CreateException, RemoteException;
}

