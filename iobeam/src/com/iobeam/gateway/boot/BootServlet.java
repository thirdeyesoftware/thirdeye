package com.iobeam.gateway.boot;


import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

import com.iobeam.boot.*;
import com.iobeam.gateway.router.*;


public class BootServlet extends HttpServlet {

	public void doGet(HttpServletRequest request,
			HttpServletResponse response) {

		throw new UnsupportedOperationException("Not intended for use.");
	}


	public void doPost(HttpServletRequest request,
			HttpServletResponse response) {
		doGet(request, response);
	}


	public void init(final ServletConfig config) throws ServletException {
		//config.getServletContext().log("BootServlet.init:");
		System.out.println("BootServlet.init:");
		try {
			BootContext context = new BootContext() {
				public Class getServiceClass() {
					return GatewayBootService.class;
				}
				public Properties getProperties() {
					throw new UnsupportedOperationException("no impl yet.");
				}
			};

			(new GatewayBootService(context)).boot();
		}
		catch (BootException be) {
			be.printStackTrace();
			throw new ServletException(be);
		}

		config.getServletContext().log("BootServlet.init: done");
	}
}

