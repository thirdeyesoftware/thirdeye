package com.iobeam.gateway.util;

import java.io.*;
import java.util.*;

public class LexTemplate implements ConfigurationTemplate {

	private static final String DHCPD_CONFIG_PATH = "/etc";
	private static final String DHCPD_CONFIG_FILE = 
			"dhcpd.conf";

	private static final String RESOLVE_CONFIG_PATH = "/etc";
	private static final String RESOLVE_CONFIG_FILE = "resolv.conf";

	private static final String INTERFACES_PATH = "/etc/network";
	private static final String INTERFACES_FILE = 
			"interfaces";
	
	private static final String IP_PATH = "/var/dnscache/env";
	private static final String IP_FILE = "IP";

	private static final String DNS_LISTEN_PATH = 
			"/ro/var/dnscache/root/ip";

	private static final String HOSTAP_PATH = "/etc/pcmcia";
	private static final String HOSTAP_FILE =
			"hostap_cs.conf";

	private static final String TEMPLATE_PATH = 
			"/etc/templates";

	public void apply(Properties properties) throws Exception {
		String templatesPath = TEMPLATE_PATH + "/" +
				properties.getProperty("iobeam.gateway.version");

		Properties props = new Properties(properties);
		String subnet = properties.getProperty("iobeam.gateway.subnet.internal.1");
		String dnsshort = null;

		int pos = subnet.indexOf("/");
		if (pos > 0) {
			subnet = subnet.substring(0, pos);
			pos = subnet.lastIndexOf(".");
			dnsshort = subnet.substring(0, pos);
		}
		String dnsshort2 = null;
		String subnet2 =
			properties.getProperty("iobeam.gateway.subnet.internal.2");
		pos = subnet2.indexOf("/");
		if (pos > 0) {
			subnet2 = subnet2.substring(0, pos);
			pos = subnet2.lastIndexOf(".");
			dnsshort2 = subnet2.substring(0, pos);
		}
		
		props.setProperty("iobeam.gateway.subnet.internal.1.short", subnet);
		props.setProperty("iobeam.gateway.address.internal.dns.short", dnsshort);
		
		if (dnsshort2 != null) {
			props.setProperty("iobeam.gateway.address.internal.2.dns.short", 
					dnsshort2);
			props.setProperty("iobeam.gateway.subnet.internal.2.short", subnet2);
		}

		
		String key = props.getProperty("iobeam.gateway.radio.wep.key");
		if (key != null && !key.trim().equals("")) {
			props.setProperty("iobeam.gateway.radio.wep.key.long","key " + key);
		}
		else {
			props.setProperty("iobeam.gateway.radio.wep.key.long","");
		}

		String type = props.getProperty("iobeam.gateway.external.type");

		if ("dhcp".equals(type)) {
			props.setProperty("iobeam.gateway.address.external.long","");
			props.setProperty("iobeam.gateway.netmask.external.long","");
			props.setProperty("iobeam.gateway.broadcast.external.long","");
			props.setProperty("iobeam.gateway.defaultgateway.external.long","");
		}
		else {
			props.setProperty("iobeam.gateway.address.external.long",
				"address " + props.getProperty("iobeam.gateway.address.external"));

			props.setProperty("iobeam.gateway.netmask.external.long",
				"netmask " + props.getProperty("iobeam.gateway.netmask.external"));

			props.setProperty("iobeam.gateway.broadcast.external.long",
				"broadcast " + props.getProperty("iobeam.gateway.broadcast.external"));

			props.setProperty("iobeam.gateway.defaultgateway.external.long",
				"gateway " + 
				props.getProperty("iobeam.gateway.defaultgateway.external"));
		}

		try {
			apply(props, templatesPath + "/" + DHCPD_CONFIG_FILE + ".template",
				DHCPD_CONFIG_PATH + "/" + DHCPD_CONFIG_FILE);
		
			apply(props, templatesPath + "/" + INTERFACES_FILE + ".template",
				INTERFACES_PATH + "/" + INTERFACES_FILE);
		
			apply(props, templatesPath + "/" + IP_FILE + ".template",
				IP_PATH + "/" + IP_FILE);

			apply(props, templatesPath + "/" + HOSTAP_FILE + ".template",
				HOSTAP_PATH + "/" + HOSTAP_FILE);

			if ("static".equals(type)) {
				apply(props, templatesPath + "/" + RESOLVE_CONFIG_FILE + ".template",
					RESOLVE_CONFIG_PATH + "/" + RESOLVE_CONFIG_FILE);
			}
			createDNSListenFile(props);
		}
		catch (Exception e) {
			restoreBackup(DHCPD_CONFIG_PATH + "/" + DHCPD_CONFIG_FILE);
			restoreBackup(INTERFACES_PATH + "/" + INTERFACES_FILE);
			restoreBackup(IP_PATH + "/" + IP_FILE);
			restoreBackup(HOSTAP_PATH + "/" + HOSTAP_FILE);
			restoreBackup(RESOLVE_CONFIG_PATH + "/" + RESOLVE_CONFIG_FILE);
			throw e;
		}
	}

	private void apply(Properties p, String templ,
			String destinationfile) throws Exception {

		BufferedReader reader = new BufferedReader(
				new InputStreamReader(
					LexTemplate.class.getResourceAsStream(templ)));

		String line = null;
		File f = new File(destinationfile);

		if (f.exists()) {
			File bak = null;
			f.renameTo(bak = new File(destinationfile + ".bak"));
			bak.deleteOnExit();
		}
		try {

			FileOutputStream fos = new FileOutputStream(destinationfile);
			PrintWriter writer = new PrintWriter(fos);

			while ((line = reader.readLine()) != null) {
				String newline = parseLine(p, line);
				writer.println(newline);
			}
			writer.flush();
			writer.close();
			fos.flush();
			fos.close();
			reader.close();
			fos = null;
			reader = null;
		}
		catch (Exception ez) {
			System.out.println("could not create " + destinationfile);
			throw ez;
		}

	}

	private void restoreBackup(String destinationfile) {
		try {

			File f = new File(destinationfile + ".bak");
			if (f.exists()) {
				f.renameTo(new File(destinationfile));
			}
		}
		catch (Exception e) {
			System.out.println("could not restore " + destinationfile);
		}
	}

	private void createDNSListenFile(Properties p) throws Exception {
		
			File dir = new File(DNS_LISTEN_PATH);
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				files[i].delete();
			}
			File file = new File(DNS_LISTEN_PATH + "/127.0.0.1");
			file.createNewFile();
			
			file = new File(
					DNS_LISTEN_PATH + "/" +
					p.getProperty("iobeam.gateway.address.internal.dns.short"));
			file.createNewFile();

			if (
				p.getProperty("iobeam.gateway.address.internal.2.dns.short") == null || 
				!p.getProperty("iobeam.gateway.address.internal.2.dns.short").trim().
						equals("")) {
				file = new File(
						DNS_LISTEN_PATH + "/" +
						p.getProperty("iobeam.gateway.address.internal.2.dns.short"));
				file.createNewFile();
			}



	}

	/**
	 * returns either a literal or system property pointed to by
	 * specified variable name.
	 */
	private String parseLine(Properties props, String line) throws Exception {

		String variable_indicator = "$";
		String variable_start_bracket = "{";
		String variable_end_bracket = "}";
		String variableStart = variable_indicator + variable_start_bracket;

		if (line == null || line.trim().equals("")) return line;

		int pos = -1;
		int end = 0;
		String varName = null;

		while ((pos = line.indexOf(variableStart)) >= 0) {
			System.out.println("line = " + line);
			System.out.println("pos = " + pos);

			String value = null;
			end = line.indexOf(variable_end_bracket, pos);

			System.out.println("end = " + end);

			varName = line.substring(pos+2, end);

			System.out.println("varName=" + varName);

			value = props.getProperty(varName);
			if (value == null) throw new Exception("could not parse " + line);

			line = line.replaceFirst("\\" + variable_indicator + 
					"\\" + variable_start_bracket + varName + "\\" + 
					variable_end_bracket, value);
		
		}
	
		return line;

	}

}

