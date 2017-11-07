package com.iobeam.portal.task.billing.payment.processautomaticpayment;


import javax.ejb.EJBObject;
import java.rmi.RemoteException;
import com.iobeam.portal.task.billing.payment.*;
import com.iobeam.portal.util.Money;
import com.iobeam.portal.model.billing.*;


public interface ProcessAutomaticPayment extends EJBObject {
	
	/**
	* Presents the specified PaymentInstrument and charge amount
	* to the appropriate payment gateway for authorization.
	*
	* Returns the ProcessPaymentResponse, containing transactionID and
	* approval code.
	*
	* @exception PaymentProcessingException the payement could not
	* be processed, with explanation.
	*/
	public ProcessPaymentResponse processAutomaticPayment(
			AutomaticPaymentInstrument instrument,
			Money amount) 
			throws PaymentProcessingException, RemoteException;


	/**
	* Returns the version String of the underlying
	* transaction processing client.
	*/
	public String getTransactionClientVersion()
			throws RemoteException;
}
