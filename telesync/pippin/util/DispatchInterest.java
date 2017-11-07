package pippin.util;

import java.util.*;
import java.io.*;

import pippin.pui.*;

/** 
 * Utility (singleton) to discern dispatch interest for Component Properties 
 */
 
public class DispatchInterest {
	
	private static final String PROP_FILE = "/META-INF/DispatchInterest.properties";
	private Properties mProperties;
	
	private static final DispatchInterest mInstance = new DispatchInterest();
	
	public DispatchInterest() {
		reload();	
	}
	
	public static DispatchInterest getInstance() {
		return mInstance;
	}
	
	public boolean hasInterest(String key) {
		/* if key's value is not literal "true" returns false */
		if (mProperties != null) {
			try {
				String value = mProperties.getProperty(key);
				return (new Boolean(value)).booleanValue();
			}
			catch (NullPointerException npe) {
				return false;
			}
			catch (MissingResourceException mre) {
				return false;
			}
		} 
		return true;
	}
	
	public boolean hasInterest(String componentName, String attribute) {
		String key = componentName + "." + attribute;
		return hasInterest(key);
	}
	
	public boolean hasInterest( String componentName, PMessage msg) {
		
		for( Enumeration eAttributes = msg.getAttributes(); eAttributes.hasMoreElements(); ) {
			String attr = ((PAttribute)eAttributes.nextElement()).getName();
			if (hasInterest(componentName, attr)) return true;
		}
		return false;
	}
	
			
			
	public void reload() {
		mProperties = new Properties();
		
		try {
			mProperties.load(getClass().getResourceAsStream(PROP_FILE));
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
			mProperties = null;
			throw new RuntimeException("cannot load " + PROP_FILE);
			// return;
		}
		
	
	}
	
}
