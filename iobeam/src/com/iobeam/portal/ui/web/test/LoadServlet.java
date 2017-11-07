package com.iobeam.portal.ui.web.test;

import javax.servlet.*;
import javax.servlet.http.*;
import java.net.*;
import java.sql.*;
import javax.naming.*;
import javax.ejb.*;
import java.util.*;
import javax.rmi.*;
import java.rmi.*;

import com.iobeam.portal.task.actor.user.signon.*;
import com.iobeam.portal.task.actor.user.usersession.*;

public class LoadServlet extends HttpServlet {

	public void init(ServletConfig config) {
		System.out.println("LoadServlet.<init>");
	}

	public void doGet(HttpServletRequest request,
			HttpServletResponse response) {

	
			try {

				InitialContext ic = new InitialContext();
				UserSessionHome home =
					(UserSessionHome)ic.lookup(UserSessionHome.JNDI_NAME);
				UserSession us = home.create();
				SignonUserHome suhome = (SignonUserHome)
						ic.lookup(SignonUserHome.JNDI_NAME);
				SignonUser su = suhome.create();
				su.signOnPortalUser(us, "jeffblau","iobeam".toCharArray());

				System.out.println("finished ");
			}
			catch (Exception ee) {
				ee.printStackTrace();
			}
	}
}

		
