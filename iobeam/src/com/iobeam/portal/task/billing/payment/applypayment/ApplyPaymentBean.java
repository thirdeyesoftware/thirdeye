package com.iobeam.portal.task.billing.payment.applypayment;

import javax.ejb.*;
import javax.naming.*;

import com.iobeam.portal.model.account.*;
import com.iobeam.portal.model.billing.PaymentInstrument;
import com.iobeam.portal.model.billing.BillingException;
import com.iobeam.portal.util.Money;

public class ApplyPaymentBean implements SessionBean {

	private SessionContext mContext;

	public void setSessionContext(SessionContext ctx) {
		mContext = ctx;
	}

	public void ejbCreate() throws CreateException {

	}

	public void ejbPostCreate() {
	}
	public void ejbRemove() {
	}
	public void ejbActivate() {
	}
	public void ejbPassivate() {
	}

	/**
	 * apply payment to account with specified payment instrument and amount.
	 */
	public void applyPayment(AccountPK pk, PaymentInstrument pi, Money amount) 
			throws BillingException {
		applyPayment(pk, pi, amount, null);
	}

	
	/**
	 * apply payment to account with specified payment instrument and amount.
	 */
	public void applyPayment(AccountPK pk, PaymentInstrument pi, Money amount,
			String memo) throws BillingException {

		try {
			InitialContext ic = new InitialContext();
			AccountHome home = (AccountHome)ic.lookup(AccountHome.JNDI_NAME);
			Account account = home.findByPrimaryKey(pk);
			account.applyPayment(pi, amount, memo);
		}
		catch (Exception ee) {
			getSessionContext().setRollbackOnly();
			throw new BillingException(
				"could not apply payment.", ee);
		}
	}
	private SessionContext getSessionContext() {
		return mContext;
	}

}

