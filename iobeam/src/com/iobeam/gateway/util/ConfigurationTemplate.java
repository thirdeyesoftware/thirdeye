package com.iobeam.gateway.util;

import java.util.Properties;

public interface ConfigurationTemplate {

	public void apply(Properties p) throws Exception;

	
}

