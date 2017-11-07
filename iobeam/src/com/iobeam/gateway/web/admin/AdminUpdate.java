package com.iobeam.gateway.web.admin;

import javax.servlet.http.*;
import javax.servlet.*;
import java.util.*;
import com.iobeam.gateway.router.*;
import com.iobeam.util.PasswordHelper;
import com.iobeam.gateway.util.GatewayConfiguration;

public class AdminUpdate extends HttpServlet {
	private static final String ADMIN_URL = "/gwadmin/admin.jsp";
	private static final String ERROR_URL = "/gwadmin/error.jsp";

	private static final String[][] cRequiredFields = {
		{"iobeam.gateway.venue.id", "Venue Id"},
		{"iobeam.gateway.radio.ssid", "SSID"},
		{"iobeam.gateway.radio.channel", "Radio Channel"},
		{"iobeam.gateway.schedule.start", "Start Access"},
		{"iobeam.gateway.schedule.stop", "Stop Access"},
		{"iobeam.gateway.address.internal.1", "Private Address"},
		{"iobeam.gateway.subnet.internal.1", "Private Subnet"},
		{"iobeam.gateway.netmask.internal.1", "Private Netmask"},
		{"iobeam.gateway.broadcast.internal.1", "Private Broadcast"},
		{"iobeam.gateway.routelease.internal.1.duration", "Route Lease Duration"},
		{"iobeam.gateway.routelease.internal.1.start", "DHCP Range"},
		{"iobeam.gateway.routelease.internal.1.stop", "DHCP Range"},
		{"iobeam.gateway.address.dns.1", "DNS Server"},
		{"iobeam.gateway.address.dns.2", "DNS Server"},
	};

	private static final String[][] cStaticRequiredFields = {
		{"iobeam.gateway.address.external", "Public Address"},
		{"iobeam.gateway.netmask.external", "Public Netmask"},
		{"iobeam.gateway.broadcast.external", "Public Broadcast"},
		{"iobeam.gateway.defaultgateway.external", "Public Default Gateway"},
	};


	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	public void doPost(HttpServletRequest request, 
			HttpServletResponse response) {

		String redirect = null;
		String msg = "";

		msg = checkRequiredFields(request);
		if (!msg.equals("")) {
			redirect = ADMIN_URL;
		}
		else {
		
			if (doUpdate(request)) {
				redirect = ADMIN_URL;
				msg = "Update successful.  You must power-cycle this device for these " +
					"settings to take effect.";
				try {
					DefaultRouter router = (DefaultRouter)RouterFactory.getRouter();
					router.removeAllLeases();
				}
				catch (Exception ee) {
					System.out.println(ee.toString() + " encountered while trying " + 
						" to remove all leases.");
				}
			}
			else {
				msg = "Update Failed";
				redirect = ERROR_URL;
			}
		}

		RequestDispatcher dispatcher = 
				request.getRequestDispatcher(redirect);
		request.setAttribute("msg", msg);
		try {
			dispatcher.forward(request, response);
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}

	}
	private String checkRequiredFields(HttpServletRequest request) {
		String msg = "";

		for (int i = 0; i < cRequiredFields.length; i++) {
			String name = cRequiredFields[i][0];
			if (request.getParameter(name) == null ||
					request.getParameter(name).trim().equals("")) {
				msg += cRequiredFields[i][1] + " is required<BR>";
			}
		}

		if ("static".equals(request.getParameter("iobeam.gateway.external.type"))) {
			for (int i = 0; i < cStaticRequiredFields.length; i++) {
				String name = cStaticRequiredFields[i][0];
				if (request.getParameter(name) == null ||
						request.getParameter(name).trim().equals("")) {
					msg += cStaticRequiredFields[i][1] + " is required<BR>";
				}
			}
		}



		return msg;
	}



	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		doPost(req, resp);
	}

	private boolean doUpdate(HttpServletRequest request) {
		GatewayConfiguration config = 
			GatewayConfiguration.getInstance();
		Enumeration en = request.getParameterNames();
		while (en.hasMoreElements()) {
			String name = (String)en.nextElement();
			if (name.indexOf("obeam") > 0) {
				config.setProperty(name, 
					request.getParameter(name));
			}
		}
		try {
			config.apply();
			config.save(null);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}


}


