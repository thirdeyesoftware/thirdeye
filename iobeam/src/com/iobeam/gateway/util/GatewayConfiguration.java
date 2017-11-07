package com.iobeam.gateway.util;

import java.util.*;
import java.text.SimpleDateFormat;
import java.io.*;
import com.iobeam.boot.*;

public class GatewayConfiguration implements BootClient {


	private static GatewayConfiguration cInstance;
	private static Properties cProperties;
	
	private static final String CONFIG_FILE =
			"/usr/local/iobeam/gateway.properties";

	private static final String DEFAULT_CONFIG_FILE = 
			"/etc/gateway.properties";

	public static GatewayConfiguration getInstance() {
		if (cInstance == null) {
			try {
				cInstance = new GatewayConfiguration();
			}
			catch (Exception e) {
				throw new Error("could not instantiate Gateway Configuration");
			}
		}
		return cInstance;
	}

	private GatewayConfiguration() throws Exception {
		System.out.println("GatewayConfiguration.<init>");
		
		InputStream is = getInputStream();

		cProperties = System.getProperties();
		cProperties.load(is);

		//for compatibility
		System.setProperties(cProperties);

		for (Enumeration en = cProperties.keys();en.hasMoreElements();) {
			String key = (String)en.nextElement();
			System.out.println(key + "=" + cProperties.getProperty(key));
		}

		try {
			is.close();
			is = null;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private InputStream getInputStream() {
		try {
			File f = new File(CONFIG_FILE);
			if (f.exists()) {
				System.out.println("config");
				return new FileInputStream(CONFIG_FILE);
			}
			else {
				System.out.println("default config");
				return BootService.class.getResourceAsStream(
						DEFAULT_CONFIG_FILE);
			}
		}
		catch (IOException ioe) {
			return BootService.class.getResourceAsStream(
				DEFAULT_CONFIG_FILE);
		}
	}

	public String getProperty(String key) {
		return cProperties.getProperty(key);
	}

	public void reset() throws Exception {
		File file = new File(CONFIG_FILE);
		if (file.exists()) {
			file.delete();
		}
		InputStream is = getInputStream();
		cProperties = System.getProperties();
		cProperties.load(is);
		apply();

	}

	public void setProperty(String key, String value) {
		cProperties.setProperty(key, value);
	}

	public void save(String configName) throws Exception {
		if (configName == null) configName = "";
		remount(true);
		String location = CONFIG_FILE;
		FileOutputStream fos = new FileOutputStream(location);
		cProperties.store(fos, "SAVED BY GatewayConfiguration" + 
				configName);
		remount(false);
	}

	private void remount(boolean rw) {
		try {
			if (rw) {
				Runtime.getRuntime().exec("/usr/local/sbin/remountrw");
			}
			else {
				Runtime.getRuntime().exec("/usr/local/sbin/remountro");
			}
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	public Properties getProperties() {
		return cProperties;
	}

	/**
	 * this method applies the settings to the necessary files on the fs
	 */
	public void apply() throws Exception {
		ConfigurationTemplate template = (ConfigurationTemplate)Class.forName(
				cProperties.getProperty(
						"iobeam.gateway.configuration.template.class")).newInstance();
		remount(true);
		template.apply(cProperties);
		remount(false);

		setTimeZone();

	}

	private static void setTimeZone() {
		String zone = cProperties.getProperty("iobeam.gateway.timezone");
		System.err.println("zone = " + zone);
		GatewayTimeZone gtz = GatewayTimeZone.getInstance(zone);
		try {
			String[] cli = new String[2];
			cli[0] = "changezone";
			cli[1] = gtz.getLink();

			Runtime.getRuntime().exec(cli);
		}
		catch (Exception ee) {
			System.err.println(ee.toString());
		}
	}

	public static Bootable getBootable() {
			return new GatewayConfigurationBootable();
	}

	private static class GatewayConfigurationBootable implements Bootable {

		public void boot(BootContext bootContext) 
				throws BootException {
			try {
				cInstance = new GatewayConfiguration();
			}
			catch (Exception e) {
				e.printStackTrace();
				throw new BootException(e);
			}
		}
	}

	public static void setSystemTime(long time) {

			try {
					SimpleDateFormat sdf = new SimpleDateFormat(
						"MM/dd/yyyy HH:mm:ss Z");
					Date dt = new Date(time);
					String cli[] = new String[2];
					cli[0] = "/usr/bin/changedate.sh";
					cli[1] = sdf.format(dt);

					System.err.println(cli[0] + " " + cli[1]);

					Process p = Runtime.getRuntime().exec(cli);

			}
			catch (Exception e) { 
					System.out.println(e.toString());
					System.out.println("could not set time. " + time);
			}
	}
	

}

