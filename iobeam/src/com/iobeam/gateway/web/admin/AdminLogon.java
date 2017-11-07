package com.iobeam.gateway.web.admin;

import javax.servlet.http.*;
import javax.servlet.*;
import com.iobeam.util.PasswordHelper;
import com.iobeam.gateway.util.GatewayConfiguration;

public class AdminLogon extends HttpServlet {

	private static final String LOGIN_URL = "/gwadmin/login.jsp";
	private static final String INDEX_URL = "/gwadmin/index.jsp";

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	public void doPost(HttpServletRequest request, 
			HttpServletResponse response) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		GatewayConfiguration config = GatewayConfiguration.getInstance();

		String uname = config.getProperty("iobeam.gateway.admin.username");
		String pwd = config.getProperty("iobeam.gateway.admin.password");

		String redirect = null;

		if (username == null || password == null ||
				username.trim().equals("") || password.trim().equals("")) {
			redirect = LOGIN_URL;
		}
		else {
			if (uname.equals(username) && 
					pwd.equals(PasswordHelper.getHashString(password))) {
				addCredentials(request, uname, pwd);
				redirect = INDEX_URL;
			} else {
				redirect = LOGIN_URL;
			}	
		}
		try {
			response.sendRedirect(redirect);
		}
		catch (Exception e) { }

	}

	public void doGet(HttpServletRequest request, 
			HttpServletResponse response) {
		throw new UnsupportedOperationException("no imple");
	}

	private void addCredentials(HttpServletRequest request,
			String uname, String password) {
		HttpSession session = request.getSession(true);
		session.setAttribute("uname", uname);
		session.setAttribute("pwd", password);
	}

	public static boolean checkCredentials(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) return false;
		String name = null;
		String pwd = null;
		if (session.getAttribute("uname") != null) {
			name = (String)session.getAttribute("uname");
		}
		if (session.getAttribute("pwd") != null) {
			pwd = (String)session.getAttribute("pwd");
		}

		if (name == null || pwd == null) return false;
		GatewayConfiguration config = GatewayConfiguration.getInstance();
		String username = config.getProperty("iobeam.gateway.admin.username");
		String password = config.getProperty("iobeam.gateway.admin.password");
		return (name.equals(username) && pwd.equals(password));
	}

}


