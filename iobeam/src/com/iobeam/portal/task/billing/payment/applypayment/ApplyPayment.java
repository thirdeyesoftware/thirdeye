package com.iobeam.portal.task.billing.payment.applypayment;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

import com.iobeam.portal.model.account.AccountPK;
import com.iobeam.portal.model.billing.PaymentInstrument;
import com.iobeam.portal.model.billing.BillingException;
import com.iobeam.portal.util.Money;

public interface ApplyPayment extends EJBObject {
	
	public void applyPayment(AccountPK pk, PaymentInstrument pi, Money amount, 
			String memo) throws BillingException, RemoteException;

	public void applyPayment(AccountPK pk, PaymentInstrument pi, Money amount)
			throws BillingException, RemoteException;
}

