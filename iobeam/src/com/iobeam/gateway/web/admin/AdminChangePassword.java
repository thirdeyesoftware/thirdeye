package com.iobeam.gateway.web.admin;

import javax.servlet.*;
import javax.servlet.http.*;

import com.iobeam.gateway.util.GatewayConfiguration;
import com.iobeam.util.PasswordHelper;

public class AdminChangePassword extends HttpServlet {

	private static final String ADMIN_URL = "/gwadmin/admin.jsp";
	private static final String ERROR_URL = "/gwadmin/error.jsp";
	private static final String LOGIN_URL = "/gwadmin/login.jsp";

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		throw new UnsupportedOperationException("no impl");
	}

	public void doPost(HttpServletRequest request, 
			HttpServletResponse response) {

		String password = request.getParameter(
				"iobeam.gateway.admin.password");
		String newpwd = request.getParameter(
				"iobeam.gateway.admin.password.new");
		String confirm = request.getParameter(
				"iobeam.gateway.admin.password.new.confirm");

		if (confirm == null ||
				newpwd == null ||
				confirm.trim().equals("") ||
				newpwd.trim().equals("") ||
				!newpwd.equals(confirm)) {

			redirect(request, response,
					ADMIN_URL, "Your passwords do not match.");
			return;

		}
		HttpSession session = request.getSession(false);
		if (session == null)  {
			redirect(request, response,
					ERROR_URL, "Your session has expired.");
			return;
		}

		String pwd = (String)session.getAttribute("pwd");

		newpwd = PasswordHelper.getHashString(newpwd);
		password = PasswordHelper.getHashString(password);
		
		if (!password.equals(pwd)) {
			redirect(request, response,
					ERROR_URL, "Your existing password is incorrect.");
			return;
		}
		if (newpwd.equals(pwd)) {
			redirect(request, response,
					ADMIN_URL, "Your new password must be different " + 
					"from your existing password.");
			return;
		}
		try {
			GatewayConfiguration config = 
					GatewayConfiguration.getInstance();
			config.setProperty("iobeam.gateway.admin.password", newpwd);

			//next line for compatibility
			System.setProperty("iobeam.gateway.admin.password", newpwd);

			config.save("pwd change");
		}
		catch (Exception e) {
			redirect(request, response, ERROR_URL,
					"You password was not changed successfully.");
		}

		GatewayConfiguration.getInstance().
				setProperty("iobeam.gateway.admin.password", password);

		redirect(request, response,
				ADMIN_URL, "Your password was changed successfully.");
	}

	private void redirect(HttpServletRequest request, 
			HttpServletResponse response, String url, String msg) {
		
		try {
			request.setAttribute("msg", msg);
			RequestDispatcher disp = request.getRequestDispatcher(url);
			disp.forward(request, response);
		}
		catch (Exception e) {
			System.out.println("could not redirect to " + url);
		}
	}

}

