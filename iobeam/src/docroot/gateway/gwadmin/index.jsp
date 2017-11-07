<%@page import="java.util.*,
				com.iobeam.gateway.router.*, 
				com.iobeam.gateway.util.*,
				com.iobeam.gateway.web.admin.AdminLogon,
				com.iobeam.gateway.web.AdminHelper"%>

<%

	boolean liveInternet = AdminHelper.hasInternetConnectivity();
	String internetMsg = "";
	String msg = "";

	if (liveInternet) {
		internetMsg = "The internet connection is configured and " + 
			"working properly.";
	}
	else {
		internetMsg = "The internet connection is not configured " + 
			"properly.";
	}

	if (!AdminLogon.checkCredentials(request)) {
		response.sendRedirect("login.jsp");
	}

	if ("true".equals(System.getProperty("iobeam.gateway.registered"))) {
		msg = "This device is working properly.";
	}
	else {
		msg = "This device is not configured properly.  Please check the " + 
			"settings page for errors.";
	}

%>

<html>
<head>
<link rel="stylesheet" type="text/css" href="/gwadmin/iobeam.css">j
<title>
	Venue <%=System.getProperty("iobeam.gateway.venue.id")%>
</title>
</head>
<body marginheight=0 leftmargin=0 topmargin=0 bgcolor="#FFFFFF"
background="/gwadmin/images/insideBkgd.jpg">
<table border=0 cellspacing=0 cellpadding=0 width="760">
<tr>
	<td align="left" colspan="2"><img src="/gwadmin/images/inside_01.jpg">
	</td>
</tr>
<tr>
	<td align="center" width="180">
		<%@include file="/gwadmin/includes/menu.html"%>
	</td>
	<td valign="top" align="left">
		<table cellpadding=0 cellspacing=0 border=0>
			<tr>
				<td align="left"><font face="verdana" size="2">
					<%=internetMsg%></font>
				</td>
			</tr>
			<tr>
				<td align="left"><font face="verdana" size="2"><%=msg%></font>
				</td>
			</tr>
		</table>
	</td>
</tr>
</table>
<BR><BR><BR>
<BR>
<%@include file="/gwadmin/includes/footer.html"%>
</body>
</html>

