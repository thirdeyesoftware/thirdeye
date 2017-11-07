<%@page import="java.util.*,
				com.iobeam.gateway.router.*, 
				com.iobeam.gateway.util.*,
				com.iobeam.gateway.web.admin.AdminLogon"%>

<%
	if (!AdminLogon.checkCredentials(request)) {
		response.sendRedirect("login.jsp");
	}
	Collection leases = (Collection)request.getAttribute("leases");
%>

<html>
<head>
<link rel="stylesheet" type="text/css" href="/gwadmin/iobeam.css">
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
	<td align="center" width="180" valign="top">
		<%@include file="/gwadmin/includes/menu.html"%>
	</td>
	<td align="left">

<table border="0" cellpadding=2 cellspacing=2 width="500">
<tr>
	<td align="center" colspan="3">
		<font class="newsbody"><B>Active Leases</B></font>
	</td>
</tr>

<tr>
	<td align="center" colspan="3">
	&nbsp;
	</td>
</tr>
<tr>
	<td align="left">
		<font face="verdana" size="1">
		<B>MAC Address</B>
		</font>
	</td>
	<td align="left">
		<font face="verdana" size="1">
		<B>IP Address</B>
		</font>
	</td>
	<td align="left">
		<font face="verdana" size="1">
		<B>Client State</B>
		</font>
	</td>
</tr>
<% for (Iterator it = leases.iterator(); it.hasNext();) {
		RouteLease lease = (RouteLease)it.next();
%>
<tr>
	<td align="left">
		<font face="verdana" size="1">
		<%=lease.getMACAddress().toString()%>
		</font>
	</td>
	<td align="left">
		<font face="verdana" size="1">
		<%=lease.getInetAddress().getHostAddress()%>
		</font>
	</td>
	<td align="left">
		<font face="verdana" size="1">
		<%=lease.getClientState().getID()%>:<%=lease.getClientState().getName()%>
		</font>
	</td>
</tr>
<% } %>
</table>
	</td>
</tr>
</table>
<%@include file="/gwadmin/includes/footer.html"%>

</body>
</html>

