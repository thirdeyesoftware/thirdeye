package com.iobeam.boot;


/**
* Command object for the BootService to execute.
*/
public interface Bootable {
	public void boot(BootContext bootContext) throws BootException;
}
