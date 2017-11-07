<%@page import="java.util.*,java.text.SimpleDateFormat,
	com.iobeam.gateway.scheduler.*,
	com.iobeam.gateway.router.*, 
	com.iobeam.gateway.util.*,
	com.iobeam.gateway.web.admin.*"%>

<%
	if (!AdminLogon.checkCredentials(request)) {
		response.sendRedirect("login.jsp");
	}
	Collection events = (Collection)request.getAttribute("tasks");
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

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
<TR>
	<td align="center" colspan="4">
		<font class="newsbody"><B>Active Tasks</B>
		</font>
	</td>
</tr>
<tr>
	<td align="center" colspan="4">&nbsp;</td>
</tr>
<tr>
	<td align="left">
		<font face="verdana" size="1">
		<B>Date/Time</B>
		</font>
	</td>
	<td align="left">
		<font face="verdana" size="1">
		<B>MACAddress</B>
		</font>
	</td>
	<td align="left">
		<font face="verdana" size="1">
		<B>IPAddress</B>
		</font>
	</td>
	<td align="left">
		<font face="verdana" size="1">
		<B>Client State</B>
		</font>
	</td>

</tr>
<% for (Iterator it = events.iterator(); it.hasNext();) {
		ScheduledEvent event = (ScheduledEvent)it.next();
		String datetime = sdf.format(event.getTriggerTime());
		RouteLeaseRemovalTask task = null;
		if (!(event.getTask() instanceof RouteLeaseRemovalTask)) {
			continue;
		}

		task = (RouteLeaseRemovalTask)event.getTask();
		RouteLease lease = task.getRouteLease();
%>

<tr>
	<td align="left">
		<font face="verdana" size="1">
		<%=datetime%>
		</font>
	</td>
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

