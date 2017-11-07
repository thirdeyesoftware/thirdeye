<%@ page import="java.util.*" %>
<%@ page import="com.iobeam.portal.service.contact.*" %>


<html>
<head>
<meta http-equiv='pragma' content='no-cache'></meta>
<title>iobeam TestLeaseNotify</title>
</head>

<body>
<h1>iobeam TestLeaseNotify</h1>

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


	Contact c = ContactService.getContact(contactID);

	if (c != null) {
		StringTokenizer st = new StringTokenizer(c.toString(), "\n");
		while (st.hasMoreTokens()) {
			out.println(st.nextToken());
			out.println("<br>\n");
		}

		ContactService.enableContact(c);
	} else {
		out.println("No such contact: " + contactID + "<br>");
	}

%>


</body>

</html>
