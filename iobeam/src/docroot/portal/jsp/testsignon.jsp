<%@page import="java.lang.Math,
com.iobeam.portal.task.actor.user.usersession.*,
com.iobeam.portal.ui.web.user.UserSessionHelper,
com.iobeam.portal.model.venue.*,
com.iobeam.portal.model.gateway.usercontact.*,
com.iobeam.portal.model.gateway.*,
java.net.InetAddress,javax.ejb.*,
javax.naming.*,
java.rmi.*"%>

<%
	UserSession usersession = null;
	InitialContext ic = new InitialContext();
	UserSessionHome home = (UserSessionHome)ic.lookup(UserSessionHome.JNDI_NAME);
	VenuePK pk = new VenuePK(1);

	usersession = home.create(new ContactID(226), pk);

	UserSessionHelper.setUserSession(request.getSession(true), usersession);

	//response.sendRedirect("/jsp/signon.jsp");
	AccessGatewayHome agHome = (AccessGatewayHome)(new InitialContext()).lookup(
		AccessGatewayHome.JNDI_NAME);
	AccessGateway ag = agHome.create();
	Gateway g = ag.findByIPAddress(InetAddress.getByName((request.getRemoteAddr())));
	out.print(g.toString());

%>



	
