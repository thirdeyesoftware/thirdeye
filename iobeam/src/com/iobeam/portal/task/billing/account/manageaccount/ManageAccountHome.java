package com.iobeam.portal.task.billing.account.manageaccount;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;
import java.rmi.RemoteException;

public interface ManageAccountHome extends EJBHome {

	public ManageAccount ejbCreate() throws CreateException, RemoteException;

}

