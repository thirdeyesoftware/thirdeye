package com.iobeam.gateway.router;


import java.net.InetAddress;
import java.util.Iterator;
import com.iobeam.util.MACAddress;;


public interface Router {


	/**
	* Router instance lifecycle management.
	*/
	public void init() 
			throws RouterException;


	/**
	* Removes all RouteLeases from the Router.
	*
	* This method is typically only used at system boot, or when
	* the routing tables are reset during a gateway update initiated
	* by the portal.
	*/
	public void flush() 
			throws RouterException;

	/**
	* Sets the specified RouteLease so long as the mac does not
	* already have a lease.
	*/
	public void initializeRouteLease(RouteLease routeLease)
			throws RouterException;
	
	/**
	* Sets the specified RouteLease in the Router, and removes
	* any existing lease for the associated mac.
	*/
	public void setRouteLease(RouteLease routeLease) 
			throws RouterException;


	/**
	* Returns the RouteLease associated with the specified Mac,
	* or null if there is none.
	*/
	public RouteLease getActiveLease(MACAddress mac);


	public void removeAllLeases() throws RouterException;

	/**
	* Removes the RouteLease for the specified client ip address.
	*/
	public void removeRouteLease(InetAddress inetAddress) 
			throws RouterException;

	
	/**
	* Removes the RouteLease for the specified client mac address.
	*/
	public void removeRouteLease(MACAddress madAddress) 
			throws RouterException;


	/**
	* Returns an Iterator over all RouteLeases known to the system.
	*/
	public Iterator getRouteLeaseIterator() 
			throws RouterException;


	/**
	 * returns whether the state transition is allowed.
	 */
	public boolean isAllowableStateTransition(RouteLease lease);


	public RouterConfiguration getConfiguration();
	
}
