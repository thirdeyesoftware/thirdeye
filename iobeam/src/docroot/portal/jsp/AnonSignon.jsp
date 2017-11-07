<%@ page import="java.util.*" %>
<%@ page import="javax.naming.*" %>
<%@ page import="com.iobeam.portal.model.gateway.usercontact.*" %>
<%@ page import="com.iobeam.portal.task.gateway.manageusercontact.*" %>
<%@ page import="com.iobeam.portal.task.gateway.leasenotify.*" %>


<html>
<head>
<meta http-equiv='pragma' content='no-cache'></meta>
<title>iobeam TestPortalSplash</title>
</head>

<body>
<h1>iobeam Anon Signon</h1>

<p>

<%
	for (Enumeration en = request.getParameterNames();
			en.hasMoreElements(); ) {
		String n = (String) en.nextElement();

		out.println(n + " = " + request.getParameter(n));
		out.println("<br>");
	}
	out.println("<p>");


	ContactID contactID = new ContactID(
			Long.parseLong(request.getParameter("cid")));

	InitialContext ic = new InitialContext();

	ManageUserContactHome much = (ManageUserContactHome)
			ic.lookup(ManageUserContactHome.JNDI_NAME);
	ManageUserContact muc = much.create();

	muc.bindAnonymous(contactID);

	NotifyGatewayHome ngh = (NotifyGatewayHome)
			ic.lookup(NotifyGatewayHome.JNDI_NAME);
	NotifyGateway ng = ngh.create();

	ng.enableAnonymousContact(contactID);
%>


</body>

</html>
