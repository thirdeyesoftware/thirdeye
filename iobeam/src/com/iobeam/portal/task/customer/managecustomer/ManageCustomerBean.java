package com.iobeam.portal.task.customer.managecustomer;

import javax.ejb.*;
import javax.naming.*;
import java.rmi.RemoteException;
import java.util.logging.Logger;

import com.iobeam.portal.model.customer.*;
import com.iobeam.portal.model.billablecustomer.*;
import com.iobeam.portal.model.customercontact.*;
import com.iobeam.portal.model.billing.PaymentInstrument;

public class ManageCustomerBean implements SessionBean {
	private SessionContext mContext;
	
	public void setSessionContext(SessionContext ctx) {
		mContext = ctx;
	}

	public void ejbCreate() throws CreateException {

	}
	public void ejbRemove() {
	}
	public void ejbActivate() {
	}
	public void ejbPassivate() {
	}

	public Customer createCustomer(CustomerContact contact)	
			throws CreateException {
		try {
			return getCustomerHome().create(contact);
		}
		catch (RemoteException re) {
			Logger l =
				Logger.getLogger(
						"com.iobeam.portal.task.customer.managecustomer");

			l.throwing(ManageCustomer.class.getName(), "createCustomer",
					(Throwable) re);

			throw new EJBException(re.toString());
		}

	}


	public BillableCustomer createBillableCustomer(Customer customer, 
			PaymentInstrument instrument) throws CreateException {
		try {
			return getBillableCustomerHome().create(customer, instrument);
		}
		catch (RemoteException re) {
			Logger l =
				Logger.getLogger(
						"com.iobeam.portal.task.customer.managecustomer");
			l.throwing(ManageCustomer.class.getName(),
					"createBillableCustomer", (Throwable) re);

			throw new EJBException(re.toString());
		}
	}


	/**
	* Removes the specified Customer and its unreferenced
	* dependencies from the system.
	*/
	public void removeCustomer(CustomerPK customerPK) {
		boolean isBillable = false;

		try {
			BillableCustomerPK pk = new BillableCustomerPK(customerPK);

			BillableCustomer bc =
					getBillableCustomerHome().findByPrimaryKey(pk);
			isBillable = true;

			bc.remove();
		}
		catch (FinderException fe) {
			// Not a BillableCustomer
		}
		catch (EJBException ejbe) {
			throw ejbe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		if (!isBillable) {
			try {
				Customer c = getCustomerHome().findByPrimaryKey(customerPK);

				c.remove();
			}
			catch (EJBException ejbe) {
				throw ejbe;
			}
			catch (Exception e) {
				throw new EJBException(e);
			}
		}


		// We should remove any Accounts for the customer, as well as
		// their contents.  The Account itself can be responsible
		// for removing its contents.
	}


	private CustomerHome getCustomerHome() {
		try {
			InitialContext ic = new InitialContext();
			return (CustomerHome)ic.lookup(CustomerHome.JNDI_NAME);
		}
		catch (Exception ee) {
			throw new EJBException("could not create customer home." +
			ee.toString());
		}
	}
	

	private BillableCustomerHome getBillableCustomerHome() {
		try {
			InitialContext ic = new InitialContext();
			return (BillableCustomerHome)ic.lookup(
					BillableCustomerHome.JNDI_NAME);
		}
		catch (Exception ee) {
			throw new EJBException("could not create billable customer home." + 
				ee.toString());
		}
	}
}

