package com.iobeam.portal.task.billing.invoice.createinvoice;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import java.rmi.RemoteException;

public interface CreateInvoiceHome extends EJBHome {

	public static final String JNDI_NAME = "iobeam.portal.CreateInvoiceHome";

	public CreateInvoice create() throws CreateException, RemoteException;

}

