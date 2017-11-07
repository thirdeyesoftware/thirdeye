package com.iobeam.gateway.web.admin;

import javax.servlet.*;
import javax.servlet.http.*;

import com.iobeam.gateway.util.GatewayConfiguration;
import com.iobeam.util.PasswordHelper;


public class AdminReset extends HttpServlet {

	private static final String ADMIN_URL = "/gwadmin/admin.jsp";
	private static final String REBOOT_ACK_URL = "/gwadmin/rebootack.jsp";
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		throw new UnsupportedOperationException("no impl");
	}

	public void doPost(HttpServletRequest request, 
			HttpServletResponse response) {
		String msg = null;
		String redirect = null;

		if ("reboot".equals(request.getParameter("type"))) {
			doReboot();
			msg = "This device will reboot in less than 10 seconds.";
			redirect = REBOOT_ACK_URL;
		}
		else {
			doReset();
			msg = "Configuration reset to default.";
			redirect = ADMIN_URL;
		}

		try {
			RequestDispatcher  disp = 
					request.getRequestDispatcher(redirect);
			request.setAttribute("msg", msg);
			disp.forward(request, response);
		}
		catch (Exception e) {
			System.out.println(" could not redirect to ADMIN_URL");
		}
		
		
	}

	private void doReboot() {

		try {
			Runtime.getRuntime().exec("/usr/bin/reboot.sh");
		}
		catch (Exception e) {
			System.out.println(" could not reboot\n" + e.toString());
		}

	}

	private void doReset() {
		try {
			GatewayConfiguration config = GatewayConfiguration.getInstance();
			config.reset();
		}
		catch (Exception e) {
			System.out.println(" could not reset\n" + e.toString());
		}
	}

			
}

