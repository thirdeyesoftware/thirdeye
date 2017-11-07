package com.iobeam.portal.task.actor.gateway.register;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import javax.ejb.*;
import javax.naming.*;
import java.util.logging.*;
import java.util.Date;
import java.rmi.RemoteException;
import java.net.InetAddress;
import com.iobeam.portal.model.gateway.*;
import com.iobeam.portal.model.venue.*;
import com.iobeam.portal.model.subscription.*;
import com.iobeam.util.MACAddress;

public class RegisterGatewayListener extends HttpServlet {

	public void doGet(HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException {

		doPost(request, response);
	}

	public void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException {

		Logger l = 	
			Logger.getLogger("com.iobeam.portal.task.actor.gateway");

		String venueid = request.getParameter("vid");
		String gatewayMAC = request.getParameter("mac");
		String publicIP = request.getRemoteAddr();
		int notifyPort = Integer.parseInt(request.getParameter("notifyPort"));
		String privateIP = request.getParameter("privateip");

		l.info("starting gateway registration doPost()");
		if (venueid == null || publicIP == null) {
			l.info("venueid is null || publicIP is null");
		}

		try {
			InitialContext ic = new InitialContext();

			RegisterGatewayHome rgHome = (RegisterGatewayHome)
					ic.lookup(RegisterGatewayHome.JNDI_NAME);

			RegisterGateway rg = rgHome.create();

			VenuePK venuePK = new VenuePK(Long.parseLong(venueid));
			MACAddress mac = new MACAddress(gatewayMAC);
			rg.register(venuePK,
					mac,
					InetAddress.getByName(publicIP),
					notifyPort,
					InetAddress.getByName(privateIP));

			AccessVenueHome avHome = (AccessVenueHome)
					ic.lookup(AccessVenueHome.JNDI_NAME);

			AccessVenue av = avHome.create();
			Venue venue = av.findByPrimaryKey(venuePK);

			Subscription sub = rg.getVenueSubscription(venuePK);

			StringBuffer result = new StringBuffer("success,");

			AccessGatewayHome agHome = (AccessGatewayHome)ic.lookup(
					AccessGatewayHome.JNDI_NAME);
			AccessGateway ag = agHome.create();
			Gateway gw = ag.findByMACAddress(mac);
			result.append(gw.getPK().getID());

			result.append(",");
			result.append(venue.getSiteKey()).append(",");

			result.append(",");
			result.append(sub.getSubscriptionType().getName());

			if (sub.getSubscriptionType().equals(
					SubscriptionType.PUBLIC_VENUE)) {
				PublicVenueSubscription pvs = (PublicVenueSubscription) sub;

				if (pvs.hasAnonymousAccess()) {
					result.append(",anonymous");
				}
			}


			response.getOutputStream().println(result.toString());
		}
		catch (Exception e) {

			l.throwing(RegisterGatewayListener.class.getName(),
					"doPost", e);

			throw new ServletException(e);
		}
	}
}

