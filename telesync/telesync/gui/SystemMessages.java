package telesync.gui;

import java.util.*;

public class SystemMessages {
	private static final String PROP_FILE = "META-INF/messages.properties";
	private Hashtable mMessages;
	private static final SystemMessages mInstance = new SystemMessages();
	
	private SystemMessages() {
		mMessages = new Hashtable();
		load();
	}
	public static SystemMessages getInstance() {
		return mInstance;
	}
	
	private void load() {
		try {
			final Properties properties = new Properties();
			try {
			properties.load(getClass().getClassLoader().getResourceAsStream(PROP_FILE));
			}
			catch (Exception e) {
				throw new RuntimeException("cannot load " + PROP_FILE);
			}

			Enumeration en = properties.propertyNames();
			while (en.hasMoreElements()) {
				String key = (String)en.nextElement();
				mMessages.put(key,properties.getProperty(key));
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String get(String name) {
		return (String)mMessages.get(name);
	}
}


	