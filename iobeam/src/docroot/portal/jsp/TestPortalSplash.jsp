<%@ page import="java.util.*" %>
<%@ page import="com.iobeam.portal.service.contact.*" %>


<html>
<head>
<meta http-equiv='pragma' content='no-cache'></meta>
<title>iobeam TestPortalSplash</title>
</head>

<body>
<h1>iobeam TestPortalSplash</h1>

<p>

<%
	for (Enumeration en = request.getParameterNames();
			en.hasMoreElements(); ) {
		String n = (String) en.nextElement();

		out.println(n + " = " + request.getParameter(n));
		out.println("<br>");
	}
	out.println("<p>");


	long contactID = Long.parseLong(request.getParameter("cid"));

	out.println("<a href='AnonSignon.jsp?cid=" + contactID +
			"'>Sign On</a>");
%>


</body>

</html>
