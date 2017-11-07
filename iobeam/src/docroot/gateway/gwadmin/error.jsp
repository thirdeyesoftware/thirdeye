<%@page import="java.util.*,
				com.iobeam.gateway.router.*, 
				com.iobeam.gateway.util.*,
				com.iobeam.gateway.web.admin.AdminLogon"%>
<%
	String msg = null;
	if (request.getAttribute("msg") == null) {
		msg = "";
	}
	else {
		msg = (String)request.getAttribute("msg");
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
	<td valign="top" align="left"><font face="verdana" size="2">
An error occured while attempting to provide you access to the iobeam wireless
service.  The administrator has been notified.  If you have any questions, you
may call us at (678)268-4095 or email us at customersupport@iobeam.com.
</font><BR><BR>
<font face="verdana" size=2><B>Error:</B>&nbsp;
<%=msg%>
</font>


</tr>
</table>
<BR><BR><BR>
<BR>
<%@include file="/gwadmin/includes/footer.html"%>
</body>
</html>

