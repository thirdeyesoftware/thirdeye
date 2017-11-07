package com.iobeam.gateway.router;

import java.util.Collection;
import com.iobeam.gateway.router.rule.Address;

public interface Configuration  {

	public String getName();

	public Address getHostAddress();

	public Address getLocalSubnet();

	public Collection getNameServers();

	public Collection getTables();
	
	
	
}