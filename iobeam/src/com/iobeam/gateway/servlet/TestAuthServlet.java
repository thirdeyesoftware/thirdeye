
package com.iobeam.gateway.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.net.InetAddress;
import java.util.Date;

import com.iobeam.gateway.router.*;
import com.iobeam.util.MACAddress;

public class TestAuthServlet extends HttpServlet {


	public void doGet(HttpServletRequest request,
			HttpServletResponse response) {
		
		String ipaddress = request.getParameter("ipaddress");
		long contactID = Long.parseLong(request.getParameter("cid"));

		if (ipaddress == null) {
			System.out.println("missing ip.");
		}

		try {
			DefaultRouter router = (DefaultRouter)
					RouterFactory.getRouterFactory().getRouter();

			RouteLease lease = new RouteLease(ClientState.MEMBER_UNRESTRICTED,
				new Date(System.currentTimeMillis() + 60000),
				InetAddress.getByName(ipaddress),
				new MACAddress("00:00:00:00:00:00"), contactID);
			router.setRouteLease(lease);
			
		}
		catch (Exception ee) {
			System.out.println(ee.toString());
		}
		
			
	}


	public void doPost(HttpServletRequest request,
			HttpServletResponse response) {

		doGet(request, response);
	}

}

