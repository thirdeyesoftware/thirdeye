package com.iobeam.portal.model.customernotice;

import javax.ejb.*;
import javax.naming.*;
import javax.rmi.*;
import com.iobeam.portal.util.DataAccessException;
import java.util.*;

public class AccessCustomerNoticeBean implements SessionBean {
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

	public Collection findAll() throws FinderException {
		try {
			return CustomerNoticeDAO.selectAll();
		}
		catch (DataAccessException dae) {
			throw new FinderException(dae.toString());
		}
	}

	public Collection findActiveAllVenues() 
			throws FinderException {
		return findActiveByVenueId(AccessCustomerNotice.ALL_VENUES);
	}

	public Collection findActiveByVenueId(long venueid) 
			throws FinderException {
		try {
			return CustomerNoticeDAO.selectActiveByVenueId(venueid);
		}
		catch (DataAccessException dae) {
			throw new FinderException(dae.toString());
		}
	}

	public Collection findAllActive() throws FinderException {
		
		try {
			return CustomerNoticeDAO.selectAllActive();
		}
		catch (DataAccessException dae) {
			throw new FinderException(dae.toString());
		}
	}

	public CustomerNotice findByPrimaryKey(long key) 
			throws FinderException {
		try {
			return CustomerNoticeDAO.selectById(key);
		}
		catch (DataAccessException dae) {
			throw new FinderException(dae.toString());
		}
	}	
}
