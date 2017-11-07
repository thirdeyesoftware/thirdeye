package com.iobeam.portal.util.sequence;

import javax.ejb.*;
import java.rmi.*;

public interface SequenceHome extends EJBHome {

	public static final String JNDI_NAME = "iobeam.portal.SequenceHome";

	Sequence create() throws CreateException, RemoteException;

}

