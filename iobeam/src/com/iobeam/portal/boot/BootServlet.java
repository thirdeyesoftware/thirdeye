package com.iobeam.portal.boot;


import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.iobeam.boot.*;


public class BootServlet extends HttpServlet {

	public void doGet(HttpServletRequest request,
			HttpServletResponse response) {

		throw new UnsupportedOperationException("Not intended for use.");
	}


	public void doPost(HttpServletRequest request,
			HttpServletResponse response) {
		doGet(request, response);
	}


	public void init(ServletConfig config) throws ServletException {
		System.out.println("BootServlet.init:");

		try {
			(new PortalBootService()).boot();

		}
		catch (BootException be) {
			be.printStackTrace();
			throw new ServletException(be);
		}

		config.getServletContext().log("BootServlet.init: done");
	}
}

