package com.iobeam.boot;

import java.util.Properties;

/**
* Mechanism for passing any pertinent data to Bootables and BootClients.
*/
public interface BootContext {
	/**
	* Returns the Class of the BootService implementation
	* that is using this BootContext.
	*
	* The Class must be a BootService.
	*/
	public Class getServiceClass();

	public Properties getProperties();

}
