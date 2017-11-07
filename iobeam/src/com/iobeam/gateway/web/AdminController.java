package com.iobeam.gateway.web;

import javax.servlet.http.*;
import javax.servlet.*;
import java.util.*;
import java.util.logging.*;
import java.io.*;

import com.iobeam.gateway.router.*;
import com.iobeam.gateway.scheduler.*;

public class AdminController extends HttpServlet {

	private static final Logger cLogger = Logger.getLogger(
		"com.iobeam.gateway.web");

	private static Hashtable cActions = new Hashtable();
	private static final String ACTIVE_LEASES_URL = "/gwadmin/leases.jsp";
	private static final String SCHEDULED_TASKS_URL = "/gwadmin/tasks.jsp";
	private static final String ADMIN_URL = "/gwadmin/admin.jsp";
	private static final String HISTORY_URL = "/gwadmin/history.jsp";
	private static final String INDEX_URL = "/gwadmin/index.jsp";
	private static final String ERROR_URL = "/gwadmin/error.jsp";
	
	private static final String HISTORY_LOG_FILE_PROP =
			"iobeam.gateway.lease.history.file";

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		System.out.println("AdminController.<init>");
		cActions.put("leases",ACTIVE_LEASES_URL);
		cActions.put("tasks",SCHEDULED_TASKS_URL);
		cActions.put("admin",ADMIN_URL);
		cActions.put("index", INDEX_URL);
		cActions.put("error", ERROR_URL);
	}

	public void doGet(HttpServletRequest request, 
			HttpServletResponse response) {

		doPost(request, response);
	
	}

	public void doPost(HttpServletRequest request, 
			HttpServletResponse response) {

		DefaultRouter router = (DefaultRouter)RouterFactory.getRouter();
		

		RequestDispatcher dispatcher;
		String key = request.getParameter("type");
	
		if ("tasks".equals(key)) {
		
	  	Collection tasks = Scheduler.getScheduler().getEvents();
			request.setAttribute("tasks", tasks);
			dispatcher = request.getRequestDispatcher(getUrlForKey(key));

		}
		else if ("leases".equals(key)) {
			
			Collection leases = router.getActiveLeases();
			request.setAttribute("leases", leases);
			dispatcher = request.getRequestDispatcher(getUrlForKey(key));

		}
		else if ("admin".equals(key)) {
			dispatcher = request.getRequestDispatcher(getUrlForKey("admin"));
		}
		else if ("history".equals(key)) {
			try {
				ServletOutputStream os = response.getOutputStream();
				FileInputStream is = new FileInputStream(
						System.getProperty(HISTORY_LOG_FILE_PROP));
				int n;
				byte[] b = new byte[4096];

				while ((n = is.read(b)) >= 0) {
					if (n > 0) {
						os.write(b, 0, n);
					}
				}
				os.flush();
				os.close();
				is.close();
			}
			catch (Exception ez) {
				cLogger.throwing(AdminController.class.getName(), "doPost",
					(Throwable)ez);
			}
			return;	
		}
		else if (key == null || key.trim().equals("")) {
			dispatcher = request.getRequestDispatcher(getUrlForKey("index"));
		}
		else {
			dispatcher = request.getRequestDispatcher(getUrlForKey("error"));
		}

		try {
			dispatcher.forward(request, response);
		}
		catch (Exception ee) {
			cLogger.throwing(AdminController.class.getName(), "doPost",
				(Throwable)ee);
		}

	}

	private String getUrlForKey(String key) {
		if (cActions.containsKey(key)) {
			return (String)cActions.get(key);
		}
		else {
			return (String)cActions.get("error");
		}
	}
		
}




		
