package com.iobeam.portal.task.billing.notifybillableparty;

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface NotifyBillablePartyHome extends EJBHome {
	
	public static final String JNDI_NAME = 
		"iobeam.portal.NotifyBillablePartyHome";

	public NotifyBillableParty create() throws CreateException, RemoteException;

}

