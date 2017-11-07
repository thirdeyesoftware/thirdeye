package com.iobeam.gateway.router;


import java.net.InetAddress;
import java.util.Iterator;

import java.util.logging.Logger;
import java.util.*;
import java.io.IOException;

import com.iobeam.util.MACAddress;
import com.iobeam.gateway.util.*;
import com.iobeam.gateway.dhcp.*;
import com.iobeam.gateway.router.rule.*;
import com.iobeam.gateway.scheduler.*;


/**
* @author jblau
*/

public class DefaultRouter implements Router {
	public static final String CONFIG_PROP =
			"iobeam.gateway.routerConfig";
	public static final String CONFIG_FILTER_PROP = 
			"iobeam.gateway.routerConfig.filtering";
	public static final String FILTERING_PROP = 
			"iobeam.gateway.filtering";

	private RouterConfiguration mConfiguration;
	private Hashtable mActiveLeases = new Hashtable();
	private Hashtable mScheduledEvents = new Hashtable();
	

	DefaultRouter() throws RouterException {
		init();
	}


	/**
	* Sets the specified RouteLease so long as the mac does not
	* already have a lease.
	*/
	public synchronized void initializeRouteLease(RouteLease routeLease)
			throws RouterException {
		System.out.println("DefaultRouter.initializeRouteLease: " +
				routeLease);

		RouteLease existingLease = getActiveLease(routeLease.getMACAddress());
		if (existingLease == null) {
			setRouteLease(routeLease);
		} else 
		if (existingLease.getInetAddress().equals(
				routeLease.getInetAddress())) {	

			System.out.println("DefaultRouter.initializeRouteLease: " +
					"ignoring " + routeLease.getClientState() +
					" in favor of " + existingLease.getClientState());
		} else {
			System.out.println("DefaultRouter.initializeRouteLease: " + 
					"ip addr change, replacing " + existingLease +
					" with " + routeLease);
			setRouteLease(routeLease);
		}
	}


	/**
	* @see com.iobeam.gateway.router.Router#setRouteLease(RouteLease)
	*/
	public synchronized void setRouteLease(RouteLease routeLease)
			throws RouterException {

		System.out.println("DefaultRouter.setRouteLease: " + routeLease);

		int markType = 0;
		ClientState clientState = routeLease.getClientState();
		

		try {
		
			Mark mark = null;
			Match match = new Match();
			Rule rule = null;
			MACAddress macAddress = routeLease.getMACAddress();

			if (getActiveLease(macAddress) != null) {
				removeRouteLease(macAddress);
			}

			if (clientState.equals(ClientState.ALIEN_RESTRICTED)) {

				// Caller is asking to return this mac to the default rule,
				// so we do nothing else after removing
				// the mac-specific rule for this mac.

				return;
			}


			match.setMACAddress(macAddress);	
			Address addr = null;
	
			addr = new Address(routeLease.getInetAddress().getHostAddress(),
					false);
			

			if (clientState.equals(ClientState.MEMBER_UNRESTRICTED)) {
				// add inbound filter rule for unrestricted	
				rule = new Rule(null, addr, "filter",
						Target.getDefaultInstanceFor("ACCEPT"),
						"iobeam_inbound", match);

				RuleExerciser.applyRule(RuleAction.ACTION_APPEND,
						Table.FILTER, new Chain("iobeam_inbound",false), rule);

			} 
		
			mark = Mark.getMark(clientState);
	
			rule = new Rule(addr, null, "mangle", mark, "iobeam", match);

			RuleExerciser.applyRule(RuleAction.ACTION_APPEND, Table.MANGLE,
					new Chain("iobeam",false), rule);

			addActiveLease(macAddress, routeLease);
		}
		catch (ExerciserException ee) {
			ee.printStackTrace();
			throw new RouterException(ee.toString());
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new RouterException(e);
		}
	}


	/**
	 * @see com.iobeam.gateway.router.Router#removeRouteLease(InetAddress)
	 */

	public synchronized void removeRouteLease(InetAddress inetAddress)
			throws RouterException {

		MACAddress macAddress;
		
		try {
			DHCPLease lease = DHCPLeaseManager.getLease(inetAddress);
			if (lease == null || lease.getMACAddress() == null) {
				throw new RouterException(
					"Could not remove lease: missing MAC address.");
			}
			macAddress = lease.getMACAddress();
			removeRouteLease(macAddress);
		}
		catch (Exception e) {
			throw new RouterException(e.toString());
		}
	}


	/**
	 * @see com.iobeam.gateway.router.Router#removeRouteLease(MACAddress)
	 */
	public synchronized void removeRouteLease(MACAddress macAddress)
			throws RouterException {
		System.out.println("DefaultRouter.removeRouteLease: mac=" +
				macAddress);

		RouteLease lease = (RouteLease) mActiveLeases.get(macAddress);

		removeRouteLease(lease);
	}

	private void removeRouteLease(RouteLease lease) throws RouterException {
		System.out.println("DefaultRouter.removeRouteLease: lease=" + lease);	
		
		if (lease == null) {
			throw new RouterException("Can not remove lease.  lease is null.");
		}

		ScheduledEvent se = null;
		synchronized (this) {
			se = (ScheduledEvent) mScheduledEvents.get(lease.getMACAddress());
		}
		Scheduler.getScheduler().removeEvent(se);

		synchronized (this) {
			try {
				MACAddress macAddress = lease.getMACAddress();
				Match match = new Match();
				Mark mark = Mark.getMark(lease.getClientState());
				match.setMACAddress(macAddress);
				Address addr = new Address(
						lease.getInetAddress().getHostAddress(), false);
	
				Rule rule = new Rule(null,addr, "filter",
						Target.getDefaultInstanceFor("ACCEPT"),
						"iobeam_inbound", match);

				RuleExerciser.applyRule(RuleAction.ACTION_DELETE,
						Table.FILTER, new Chain("iobeam_inbound",false), rule);
														
				rule = new Rule(addr, null, "mangle", mark, "iobeam", match);

				RuleExerciser.applyRule(RuleAction.ACTION_DELETE, Table.MANGLE, 
						new Chain("iobeam",false), rule);

				removeActiveLease(macAddress);
			}
			catch (ExerciserException ee) {
				throw new RouterException(ee.toString());
			}
		}
	}

	/**
	 * method to protect access to active leases hashtable.
	 */
	private synchronized void removeActiveLease(MACAddress macAddress) {
		RouteLease lease = (RouteLease) mActiveLeases.remove(macAddress);

		System.out.println("DefaultRouter.removeActiveLease: " +
				macAddress + " " + lease +
				"  count = " + mActiveLeases.size());

	}

	/**
	 * method to protect access to active leases hashtable.
	 */	
	private synchronized void addActiveLease(MACAddress mac, RouteLease lease) {
		
		mActiveLeases.put(mac, lease);
		scheduleRemoval(lease);
		System.out.println("DefaultRouter.addActiveLease: " +
				mac + " " + lease +
				"  count = " + mActiveLeases.size());

	}

	/** 
	 * returns a collection of active leases.
	 */
	public Collection getActiveLeases() {
		return mActiveLeases.values();
	}


	public void removeAllLeases() throws RouterException {
		for (Iterator it = getRouteLeaseIterator(); it.hasNext();) {
			RouteLease lease = (RouteLease)it.next();
			removeRouteLease(lease);
		}
	}

	/**
	 * method to protect access to active leases hashtable.
	 */
	public synchronized RouteLease getActiveLease(MACAddress mac) {
		return (RouteLease)mActiveLeases.get(mac);
	}

	/**
	 * @see com.iobeam.gateway.router.Router#getRouteLeaseIterator()
	 */
	public synchronized Iterator getRouteLeaseIterator() 
			throws RouterException {
		return mActiveLeases.values().iterator();
	}


	public boolean isAllowableStateTransition(RouteLease lease) {

		RouteLease existingLease = getActiveLease(lease.getMACAddress());
		if (existingLease == null) {
			// going from no lease to unrestricted is not allowed.
			if (!lease.getClientState().equals(
					ClientState.MEMBER_UNRESTRICTED)) {
				return true;
			}
		}
		else {
			ClientState current = existingLease.getClientState();
			// if lease exists and is not member unrestricted, allow transition
			if (!current.equals(ClientState.MEMBER_UNRESTRICTED)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public synchronized void flush() throws RouterException {

		try {
			
			RuleExerciser.flush(Table.NAT);
			RuleExerciser.flush(Table.MANGLE);
			RuleExerciser.flush(Table.FILTER);

		}
		catch (ExerciserException ee) {
			throw new RouterException(ee.toString());
		}
	}
	

	/**
	* executes all initialization code for this default 
	* router implementation.
	* useful for loading initial set of rules, etc.
	*/
	public synchronized void init() throws RouterException {

		// Possibly use config.xml for this purpose...
		try {
			flush();
			GatewayConfiguration config = GatewayConfiguration.getInstance();
			String filtering = config.getProperty(FILTERING_PROP);
			if ("false".equals(filtering)) {
				mConfiguration = RouterConfiguration.parse(
						config.getProperty(CONFIG_PROP));
			}
			else {
				mConfiguration = RouterConfiguration.parse(
						config.getProperty(CONFIG_FILTER_PROP));
			}
				

			RuleExerciser.applyConfiguration(mConfiguration);

		}

		catch (ConfigurationParsingException ce) {
			ce.printStackTrace();
			throw new RouterException(ce.getMessage());
		}
		catch (Exception ioe) {
			ioe.printStackTrace();
		}
	}

	public RouterConfiguration getConfiguration() {
		return mConfiguration;
	}

	private void scheduleRemoval(RouteLease routeLease) {
		ScheduledEvent se = new ScheduledEvent(
				routeLease.getExpirationDate(),
				new RouteLeaseRemovalTask(routeLease));
		Logger.getLogger("com.iobeam.gateway.router").info(
				"scheduleRemoval:" + se.getID() + "," + routeLease.toString());
		Scheduler.getScheduler().addEvent(se);
		mScheduledEvents.put(routeLease.getMACAddress(), se);
	}
}
