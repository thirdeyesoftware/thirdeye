package com.iobeam.gateway.util;

import java.util.Hashtable;

public class GatewayTimeZone {

	private static final String cPathPrefix =
			"/usr/share/zoneinfo";

	private static Hashtable cHash = new Hashtable();
	private String mKey;
	private String mLink;

	public static final GatewayTimeZone ZONE_GMT = 
			new GatewayTimeZone("GMT", cPathPrefix + "/GMT");

	public static final GatewayTimeZone ZONE_EST = 
			new GatewayTimeZone("EST", cPathPrefix + "/US/Eastern");
	
	public static final GatewayTimeZone ZONE_CST = 
			new GatewayTimeZone("CST", cPathPrefix + "/US/Central");

	public static final GatewayTimeZone ZONE_MST = 
			new GatewayTimeZone("MST", cPathPrefix + "/US/Mountain");

	public static final GatewayTimeZone ZONE_PST = 
			new GatewayTimeZone("PST", cPathPrefix + "/US/Pacific");


	public GatewayTimeZone(String key, String link) {
		mKey = key;
		mLink = link;
		cHash.put(key, this);
	}

	public String getKey() {
		return mKey;
	}

	public String getLink() {
		return mLink;
	}

	public int hashCode() {
		return mKey.hashCode();
	}

	public boolean equals(Object o) {
		if (o instanceof GatewayTimeZone) {
			GatewayTimeZone z = (GatewayTimeZone)o;
			if (z.getLink().equals(getLink()) &&
					z.getKey().equals(getKey())) {
				return true;
			}
		}
		return false;
	}

	public static GatewayTimeZone getInstance(String key) {
		return (GatewayTimeZone)cHash.get(key);
	}

}

