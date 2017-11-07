package com.iobeam.portal.task.billing.account.manageaccount;

import javax.ejb.*;
import javax.naming.*;
import java.util.logging.Logger;
import com.iobeam.portal.model.customer.*;
import com.iobeam.portal.model.billablecustomer.*;
import com.iobeam.portal.model.account.*;

public class ManageAccountBean implements SessionBean {

	SessionContext mContext;

	public void setSessionContext(SessionContext ctx) {
		mContext = ctx;
	}
	public void ejbCreate() throws CreateException {

	}

	public void ejbPostCreate() {
	}

	public void ejbActivate() {
	}
	
	public void ejbPassivate() {
	}

	public void ejbRemove() {
	}

	/**
	 * Create an account for the given customer and the given billable customer.
	 * @throws CreateException if creating the account fails.
	 */
	public Account createAccount(Customer customer, BillableCustomer
			billCustomer, AccountStatus status) throws CreateException {
		Account account;
		try {
			InitialContext ic = new InitialContext();
			AccountHome home = (AccountHome)ic.lookup(AccountHome.JNDI_NAME);
			account = home.create(customer, billCustomer);
			account.setAccountStatus(status);
		}
		catch (Exception e) {
			Logger.getLogger("com.iobeam.portal.task.billing.account.manageaccount").
					throwing(AccountBean.class.getName(), "createAccount", (Throwable)e);

			throw new CreateException(e.toString());
		}
		return account;

	}

}


