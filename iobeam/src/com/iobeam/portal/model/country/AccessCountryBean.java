package com.iobeam.portal.model.country;


import java.util.*;
import javax.ejb.*;
import com.iobeam.portal.util.DataNotFoundException;


public class AccessCountryBean implements SessionBean {

	private SessionContext mSessionContext;


	public void setSessionContext(SessionContext context) {
		mSessionContext = context;
	}


	public void ejbCreate() throws CreateException {
	}


	public void ejbActivate() {
	}


	public void ejbPassivate() {
	}


	public void ejbRemove() {
	}




	/**
	* Returns the Country instance for the specified pk.
	*/
	public Country findByPrimaryKey(CountryPK countryPK)
			throws FinderException {

		Country country = null;

		try {
			country = CountryDAO.select(countryPK);

			if (country == null) {
				throw new ObjectNotFoundException(countryPK.toString());
			}
		}
		catch (FinderException fe) {
			throw fe;
		}
		catch (Exception e) {
			throw new EJBException(e);
		}

		return country;
	}


	/**
	* Returns a Collection of all Country instances known to the system.
	*/
	public Collection findAll() {
		try {
			return CountryDAO.selectAll();
		}
		catch (Exception e) {
			throw new EJBException(e);
		}
	}
}
