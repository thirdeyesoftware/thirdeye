package com.iobeam.gateway.servlet;

import javax.servlet.*;
import javax.servlet.http.*;

import com.iobeam.gateway.router.*;

public class RouterBootServlet extends HttpServlet {

	// this brings the router singleton into existence...
	private DefaultRouter mRouter =  
		(DefaultRouter)RouterFactory.getRouterFactory().getRouter();

	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		
		// start here...
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
	{
		doGet(request, response);
	}

	public static void main(String[] args) {
		RouterBootServlet rb = new RouterBootServlet();
	}
}

