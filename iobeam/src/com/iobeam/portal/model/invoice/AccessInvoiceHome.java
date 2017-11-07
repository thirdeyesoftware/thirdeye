package com.iobeam.portal.model.invoice;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;
import java.rmi.RemoteException;

public interface AccessInvoiceHome extends EJBHome {

	public static final String JNDI_NAME = "iobeam.portal.AccessInvoiceHome";

	public AccessInvoice create() throws CreateException, RemoteException;
	
}
