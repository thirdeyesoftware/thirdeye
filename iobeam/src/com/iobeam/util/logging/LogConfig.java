package com.iobeam.util.logging;


import java.util.*;
import java.util.logging.*;
import java.io.*;


public class LogConfig {
	public static final String PROP_ROOT = "com.iobeam";

	public LogConfig() throws Throwable {
		try {
			init();
		}
		catch (Throwable t) {
			Logger.getLogger("").log(Level.SEVERE, "LogConfig.init failed", t);
		}
	}


	private void init() throws Exception {
		LogManager lm = LogManager.getLogManager();

		String p = System.getProperty("java.util.logging.config.file");
		FileInputStream fis = new FileInputStream(p);
		lm.readConfiguration(fis);
		fis.close();


		Logger l = Logger.getLogger(PROP_ROOT);

		String handlerClass = lm.getProperty(PROP_ROOT + ".handler");
		String formatterClass = lm.getProperty(PROP_ROOT + ".formatter");

		if (handlerClass != null) {
			Handler h = (Handler) getClass().getClassLoader().loadClass(
					handlerClass).newInstance();

			l.setUseParentHandlers(false);
			l.addHandler(h);

			if (formatterClass != null) {
				java.util.logging.Formatter f = 
					(java.util.logging.Formatter) getClass().getClassLoader().loadClass(
						formatterClass).newInstance();
				h.setFormatter(f);
			}
		}

		l.info("initialization complete.");
	}
}
