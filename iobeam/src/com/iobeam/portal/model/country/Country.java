package com.iobeam.portal.model.country;


public class Country implements java.io.Serializable {

	private CountryPK mPK;
	private String mCountryName;
	

	Country(CountryPK pk, String countryName) {
		mPK = pk;

		if (pk == null) {
			throw new NullPointerException("pk");
		}

		mCountryName = countryName;
		if (countryName == null) {
			throw new NullPointerException("countryName");
		}
	}


	public String getCountryName() {
		return mCountryName;
	}


	public CountryPK getPK() {
		return mPK;
	}


	public String toString() {
		return getCountryName();
	}
}
