package com.iobeam.portal.task.actor.gateway.install;

import javax.ejb.*;
import javax.naming.*;
import java.rmi.RemoteException;
import com.iobeam.portal.model.gateway.*;
import com.iobeam.portal.security.*;
import com.iobeam.portal.model.gateway.Gateway;
import com.iobeam.portal.model.venue.*;

public class InstallGatewayBean implements SessionBean {

	private SessionContext mContext;

	public void ejbCreate() throws CreateException {


	}

	public void ejbPostCreate() {

	}

	public void ejbPassivate() {

	}

	public void ejbActivate() {

	}

	public void ejbRemove() {

	}

	public void setSessionContext(SessionContext ctx) {
		mContext = ctx;
	}


	/**
	* Activates the specified Gateway as the Gateway for its Venue.
	* This is a one-time operation for a new Gateway being installed
	* at a venue.
	*
	* @throws NoSuchVenueException if the Gateway's venuePK is not
	* known to the system.
	*/
	public void install(Gateway gateway) throws NoSuchVenueException {

		InitialContext ic = null;
		try {
			ic = new InitialContext();
			AccessVenueHome avHome = 
				(AccessVenueHome)ic.lookup(AccessVenueHome.JNDI_NAME);

			AccessVenue av = avHome.create();
			av.installGateway(gateway);
		}
		catch (CreateException ce) {
			throw new EJBException(ce.toString());
		}
		catch (NamingException ne) {
			throw new EJBException(ne.toString());
		}
		catch (RemoteException re) {
			throw new EJBException(re.toString());
		}
	}
}

