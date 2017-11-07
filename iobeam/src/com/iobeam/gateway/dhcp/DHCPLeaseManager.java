package com.iobeam.gateway.dhcp;


import java.net.InetAddress;
import java.io.*;
import com.iobeam.util.MACAddress;
import com.iobeam.gateway.util.GatewayConfiguration;


public class DHCPLeaseManager {

	public static final String LEASE_FILE_PROP =
			"iobeam.gateway.dhcpdLeasesPath";

	/**
	* Returns the current DHCPLease for the specified InetAddress,
	* or null if there is none.
	*/
	public static DHCPLease getLease(InetAddress inetAddress)
			throws IOException, DHCPLeaseParseException {

		FileInputStream fis = new FileInputStream(
				GatewayConfiguration.getInstance().getProperty(LEASE_FILE_PROP));

		DHCPLeaseInputStream lis = new DHCPLeaseInputStream(fis);

		DHCPLease l;
		DHCPLease lease = null;

		while ((l = lis.readLease()) != null) {
			if (l.getInetAddress().equals(inetAddress)) {
				lease = l;
			}
		}

		return lease;
	}

	/**
	* Returns the current DHCPLease for the specified MACAddress,
	* or null if there is none.
	*/
	public static DHCPLease getLease(MACAddress macAddress)
			throws IOException, DHCPLeaseParseException {

		FileInputStream fis = new FileInputStream(
				GatewayConfiguration.getInstance().getProperty(LEASE_FILE_PROP));

		DHCPLeaseInputStream lis = new DHCPLeaseInputStream(fis);

		DHCPLease l;
		DHCPLease lease = null;

		while ((l = lis.readLease()) != null) {
			if (macAddress.equals(l.getMACAddress())) {
				lease = l;
			}
		}

		return lease;
	}
}
