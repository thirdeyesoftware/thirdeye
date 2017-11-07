package com.iobeam.gateway.dhcp;


import java.io.*;
import java.util.*;
import java.net.InetAddress;


public class UnitTest {
	public static void main(String[] args) throws Exception {

		Properties p = System.getProperties();
		p.load(UnitTest.class.getResourceAsStream("/etc/gateway.properties"));
		System.setProperties(p);


		FileInputStream fis = new FileInputStream(
				System.getProperty(DHCPLeaseManager.LEASE_FILE_PROP));

		DHCPLeaseInputStream lis = new DHCPLeaseInputStream(fis);

		DHCPLease lease;
		List leases = new ArrayList();

		while ((lease = lis.readLease()) != null) {
			leases.add(lease);
		}

		for (Iterator it = leases.iterator(); it.hasNext(); ) {
			System.out.println(it.next());
		}


		System.out.println();

		InetAddress ia = InetAddress.getByName(args[0]);
		System.out.println(ia);

		System.out.println(DHCPLeaseManager.getLease(ia));
	}
}
