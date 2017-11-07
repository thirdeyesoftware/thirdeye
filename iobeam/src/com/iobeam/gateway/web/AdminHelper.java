package com.iobeam.gateway.web;

import javax.servlet.*;
import javax.servlet.http.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.*;
import java.net.URL;
import java.net.URLConnection;
import java.io.*;

import com.iobeam.gateway.scheduler.*;
import com.iobeam.gateway.router.*;
import com.iobeam.gateway.util.*;
import com.iobeam.util.*;

public class AdminHelper {

	public static boolean hasInternetConnectivity() {
		boolean ret = false;
		try {
			URL pingURL = new URL(Portal.getPingURL());

			URLConnection conn = pingURL.openConnection();
			
			if (conn.getContentLength() >= 0)  {
				ret = true;
			}
				
		}
		catch (IOException ioe) {
			ret = false;
		}
		return ret;
	}

	public static long getServerTime() {
		SimpleDateFormat format = new 
				SimpleDateFormat("MM/dd/yyyy HH:mm:ss z");

		try {
			URL url = new URL(Portal.getTimeServiceURL());
			URLConnection conn = url.openConnection();
			String tm = null;
			StringBuffer time = new StringBuffer(0);
			if (conn.getContentLength() > 0) {
				InputStream is = conn.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				while ((tm = br.readLine()) != null) {
					time.append(tm.trim());
				}
				br.close();
				System.out.println("time from server =  " + time);
				return format.parse(time.toString()).getTime();
			}
			else {
				return System.currentTimeMillis();
			}
		}
		catch (Exception ioe) {
			System.err.println("could not get server time.\n" + ioe.toString());
			return -1;
		}
	}
	
	public static void main(String[] args) {

		System.out.println("internet is " + 
			(hasInternetConnectivity() ? "on" : "off"));

	}


}



