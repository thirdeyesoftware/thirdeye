package com.iobeam.gateway.router;


import java.net.*;
import java.util.*;
import java.io.IOException;

import com.iobeam.gateway.dhcp.*;
import com.iobeam.util.MACAddress;



public class TestRouter implements Router {

	TestRouter() throws RouterException {
		System.out.println("TestRouter.<init>:");
	}


	public void init() {
		System.out.println("TestRouter.init:");
	}


	public void initializeRouteLease(RouteLease routeLease)
			throws RouterException {
		System.out.println("TestRouter.initializeRouteLease: " + routeLease);
	}

	public void setRouteLease(RouteLease routeLease)
			throws RouterException {
		System.out.println("TestRouter.setRouteLease: " + routeLease);
	}

	public void removeAllLeases() throws RouterException {
		throw new UnsupportedOperationException("no impl.");
	}

	public RouteLease getActiveLease(MACAddress mac) {
		System.out.println("TestRouter.getActiveLease: " + mac);
		return null;
	}


	public void removeRouteLease(InetAddress inetAddress)
			throws RouterException {

		System.out.println("TestRouter.removeRouteLease: " + inetAddress);
	}


	public void removeRouteLease(MACAddress macAddress)
			throws RouterException {

		System.out.println("TestRouter.removeRouteLease: " + macAddress);
	}


	public boolean isAllowableStateTransition(RouteLease lease) {
		System.out.println("TestRouter.isAllowableStateTransition: " + lease);
		return true;
	}
		

	public Iterator getRouteLeaseIterator() throws RouterException {
		System.out.println("TestRouter.getRouteLeaseIterator: ");

		return (new Vector()).iterator();
	}


	public void flush() throws RouterException {
		System.out.println("TestRouter.flush: ");
	}
	

	public RouterConfiguration getConfiguration() {
		System.out.println("TestRouter.getConfiguration: ");

		return null;
	}
	
}
