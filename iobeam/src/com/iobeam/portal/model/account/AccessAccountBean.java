package com.iobeam.portal.model.account;

import javax.ejb.*;
import javax.naming.*;
import javax.rmi.*;
import com.iobeam.portal.util.DataAccessException;
import com.iobeam.portal.model.customer.CustomerPK;
import com.iobeam.portal.model.billablecustomer.BillableCustomerPK;
import java.util.Collection;

public class AccessAccountBean implements SessionBean {
	private SessionContext mSessionContext;
	
	public void ejbCreate() throws CreateException {

	}

	public void ejbRemove() {

	}

	public void setSessionContext(SessionContext sc) {
		mSessionContext = sc;
	}

	public void unsetSessionContext() {
		mSessionContext = null;
	}

	public void ejbActivate() {

	}

	public void ejbPassivate() {

	}


	public Collection findAllOpenAccounts() 
			throws FinderException {
		Collection c;
		try {
			c = AccessAccountDAO.selectAllOpenAccounts();
		}
		catch (DataAccessException dae) {
			throw new FinderException(dae.toString());
		}
		return c;

	}


	public Collection findByCustomerPK(CustomerPK pk) 
			throws FinderException {
		Collection c;
		try {
			c = AccessAccountDAO.selectByCustomerPK(pk);
		}
		catch (DataAccessException dae) {
			throw new FinderException(dae.toString());
		}
		return c;

	}

	public Collection findByBillableCustomerPK(BillableCustomerPK pk) 
			throws FinderException {
		Collection c;
		try {
			c = AccessAccountDAO.selectByBillableCustomerPK(pk);
		}
		catch (DataAccessException dae) {
			throw new FinderException(dae.toString());
		}
		return c;

	}

	public AccountEntry findAccountEntry(long acctEntryID)
			throws FinderException {
		AccountEntry entry = null;
		try {
			entry = AccountDAO.selectAccountEntry(
					acctEntryID);
		}
		catch (DataAccessException dae) {
			throw new FinderException(dae.toString());
		}
		return entry;
	}


	

	
	
	
}
