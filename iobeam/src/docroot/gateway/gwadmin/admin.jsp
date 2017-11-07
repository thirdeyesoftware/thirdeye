<%@page import="java.text.SimpleDateFormat,
							  java.util.Date, java.util.Calendar,  
								java.util.GregorianCalendar, java.util.TimeZone,
								com.iobeam.gateway.router.*,
								com.iobeam.gateway.web.admin.*,
								com.iobeam.gateway.util.GatewayConfiguration" info=""%>

<%
	GatewayConfiguration gconfig = GatewayConfiguration.getInstance();

	if (!AdminLogon.checkCredentials(request)) {
		response.sendRedirect("login.jsp");
	}

	String msg = (String)request.getAttribute("msg");
	if (msg == null) msg = "";

	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss z");
	sdf.setTimeZone(
		TimeZone.getTimeZone(gconfig.getProperty("iobeam.gateway.timezone")));

	String localtime = sdf.format(new Date());

%>

<html>
<head>
<link rel="stylesheet" type="text/css" href="/gwadmin/iobeam.css"/>
<title>
	Venue <%=gconfig.getProperty("iobeam.gateway.venue.id")%>
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
	<td  align="left">
	<B><font face="verdana" size=1 color="blue"><%=msg%></font></B>
	<table border="0" cellpadding=2 cellspacing=2 width="90%">

	<tr>
		<td align="center" colspan=4>
			<font face="verdana" size=2>
			<B>Service Settings</B>
			<br>
			<font face="verdana" size=1" color="333333">Note:  Changing any of these
			settings will force all current and active users of this service to
			reauthenticate.</font>
			</font>
		</td>
	</tr>

	<tr>
		<td align="left" width="50">
		&nbsp;
		</td>
		<td align="left">
			&nbsp;
		</td>
		<td align="left">
			&nbsp;
		</td>
		<td align="left">
			&nbsp;
		</td>
		<td align="left" width="30">
			&nbsp;
		</td>
	</tr>
	</tr>
	<form action="/gwadmin/reset" method="post">
	<input type="hidden" value="reset" name="type">
	<tr>
		<td align="left">
			<font class="newsbody">
			Reset Configuration
			</font>
		</td>
		<td align="left">
			<input type="submit" value="  Reset  ">
		</td>
		<td align="left" colspan="3">
			<font face="verdana" size=1>
			This will reset the configuration back to factory defaults.  You
			<B>Must</B> power-cycle the device after resetting the configuration.
			</font>
		</td>
	</tr>
	</form>
	<form action="/gwadmin/changepassword" method="post">
	<tr>
		<td align="left" colspan=5>
			<font class="newsbody">
			<B>Change Password</B>
			</font>
		</td>
	</tr>
	<tr>
		<td align="left">
			<font class="newsbody">
			Current Password
			</font>
		</td>
		<td align="left" colspan="4">
			<input type="password" size="10" maxlength="15" 
					name="iobeam.gateway.admin.password">
		</td>
	</tr>
	<tr>
		<td align="left">
			<font class="newsbody">
			New Password
			</font>
		</td>
		<td align="left" colspan="4">
			<input type="password" size="10" maxlength="15" 
					name="iobeam.gateway.admin.password.new">
		</td>
	</tr>
	<tr>
		<td align="left">
			<font class="newsbody">
			Confirm Password
			</font>
		</td>
		<td align="left" colspan="4">
			<input type="password" size="10" maxlength="15" 
					name="iobeam.gateway.admin.password.new.confirm">
		</td>
	</tr>
	<tr>
		<td align="center" colspan=2>
			<input type="submit" value="  Change Password  ">
		</td>
		<td align="left" colspan=3>&nbsp;</td>
	</tr>
	</form>
	<tr>
		<td align="left" colspan="5">&nbsp;</td>
	</tr>

	<tr>
		<td align="center" colspan="5">
			<font class="newsbody">
			<B>Gateway Configuration</B>
		</td>
	</tr>

	<form action="/gwadmin/update" method="post">
	<tr>
		<td align="left">
			<font class="newsbody">
			Venue ID
			</font>
		</td>
		<td align="left" colspan="4">
			<input type="text"
			value="<%=gconfig.getProperty("iobeam.gateway.venue.id")%>"
				size=3 maxlength=3 name="iobeam.gateway.venue.id">
			<font face="verdana" size="1">&nbsp;Do not change this value</font>
		</td>
	</tr>
	<tr>
		<td align="left">
			<font class="newsbody">
			Time Zone
			</font>
		</td>
		<td align="left" colspan="4">
			<SELECT name="iobeam.gateway.timezone" size=1>
				<option value="GMT" 
					<%=(gconfig.getProperty("iobeam.gateway.timezone").equals("GMT") ?
					"selected" : "")%>
				>GMT</option>
				<option value="EST" 
					<%=(gconfig.getProperty("iobeam.gateway.timezone").equals("EST") ?
					"selected" : "")%>
				>EST</option>
				<option value="CST" 
					<%=(gconfig.getProperty("iobeam.gateway.timezone").equals("CST") ?
					"selected" : "")%>
				>CST</option>
				<option value="MST" 
					<%=(gconfig.getProperty("iobeam.gateway.timezone").equals("MST") ?
					"selected" : "")%>
				>MST</option>
				<option value="PST" 
					<%=(gconfig.getProperty("iobeam.gateway.timezone").equals("PST") ?
					"selected" : "")%>
				>PST</option>
			</select>
			&nbsp;<font face="verdana" size=1><%=localtime%></font>
		</td>
	</tr>
	<tr>
		<td align="left" colspan="5">
		&nbsp;
		</td>
	</tr>
	<tr>
		<td align="center" colspan="5">
			<font class="newsbody">
			<B>Splash Page</B>
		</td>
	</tr>
	<tr>
		<td align="left"colspan="5">
			<font face="verdana" size="1">
			<B>Splash Page URL</B> is the url (starting with http:// or https:// that 
			you want users redirected to when they open their web browsers and use
			this wireless service for the first time.
			</font>
		</td>
	</tr>
	<tr>
		<td align="left">
			<font class="newsbody">
			Splash Page URL
			</font>
		</td>
		<td align="left" colspan="4">
			<input type="text"
			value="<%=gconfig.getProperty("iobeam.gateway.anonymous.redirect.url")%>"
			name="iobeam.gateway.anonymous.redirect.url" size=50>
		</td>
	</tr>
	<tr>
		<td align="left" colspan="5">
		&nbsp;
		</td>
	</tr>
	<tr>
		<td align="center" colspan="5">
			<font class="newsbody">
			<B>Access Control Schedule</B>
		</td>
	</tr>
	<tr>
		<td align="left"colspan="5">
			<font face="verdana" size="1">
			<B>Start Access</B> and <B>Stop Access</B> define the schedule when
			wireless service is available.  Setting <B>Enable Access</B> to "NO" will
			disallow wireless access regardless of the schedule.
			</font>
		</td>
	</tr>
	<tr>
		<td align="left">
			<font class="newsbody">
			Start Access
			</font>
		</td>
		<td align="left" colspan="4">
			<input type="text"
			value="<%=gconfig.getProperty("iobeam.gateway.schedule.start")%>"
			name="iobeam.gateway.schedule.start" size=10>
		</td>
	</tr>
	<tr>
		<td align="left">
			<font class="newsbody">
			Stop Access
			</font>
		</td>
		<td align="left" colspan="4">
			<input type="text"
			value="<%=gconfig.getProperty("iobeam.gateway.schedule.stop")%>"
			name="iobeam.gateway.schedule.stop" size=10>
		</td>
	</tr>
	<tr>
		<td align="left">
			<font class="newsbody">
			Enable Access
			</font>
		</td>
		<td align="left" colspan="4">
		<% boolean allowaccess = "true".equals(gconfig.getProperty(
				"iobeam.gateway.access.enabled"));
		%>
			<input <%=allowaccess ? "checked" : ""%> type="radio" value="true"
			name="iobeam.gateway.access.enabled"><font face="verdana" size=1><B>Yes</B></font>&nbsp;&nbsp;
			<input <%=allowaccess ? "" : "checked"%> type="radio" value="false"
			name="iobeam.gateway.access.enabled"><font size=1 face="verdana"><B>No</B></font>
		</td>
	</tr>
	<tr>
		<td align="left" colspan="5">&nbsp;</td>
	</tr>
	<tr>
		<td align="center" colspan="5">
			<font class="newsbody">
			<B>Content Filtering</B>
			</font>
		</td>
	</tr>
	<tr>
		<td align="left">
			<font class="newsbody">
			Enable Content Filtering
			</font>
		</td>
		<%
			 boolean filtering = "true".equals(gconfig.getProperty(
			 	"iobeam.gateway.filtering"));

		%>
		<td align="left" colspan="4">
			<input <%=filtering ? "checked" : ""%> type="radio" value="true"
			name="iobeam.gateway.filtering"><font face="verdana" size=1><B>On</B></font>&nbsp;&nbsp;
			<input <%=filtering ? "" : "checked"%> type="radio" value="false"
			name="iobeam.gateway.filtering"><font size=1 face="verdana"><B>Off</B></font>
		</td>
	</tr>
	<tr>
		<td align="left" colspan="5">&nbsp;</td>
	</tr>
	<tr>
		<td align="center" colspan="5">
			<font class="newsbody">
			<B>WLAN Network Settings</B>
			</font>
		</td>
	</tr>
	<tr>
		<td align="left">
			<font class="newsbody">
			Radio Identifier (SSID)
			</font>
		</td>
		<td align="left" colspan="4">
			<input type="text"
			value="<%=gconfig.getProperty("iobeam.gateway.radio.ssid")%>"
				size=10 maxlength=15 name="iobeam.gateway.radio.ssid">
		</td>
	</tr>
	<tr>
		<td align="left">
			<font class="newsbody">
			Radio Channel (1-12)
			</font>
		</td>
		<td align="left" colspan="4">
			<input type="text"
			value="<%=gconfig.getProperty("iobeam.gateway.radio.channel")%>"
				size=3 maxlength=3 name="iobeam.gateway.radio.channel">
		</td>
	</tr>
	<tr>
		<td align="left">
			<font class="newsbody">
			Radio WEP Key
			</font>
		</td>
		<td align="left" colspan="4">
			<input type="text"
			value="<%=gconfig.getProperty("iobeam.gateway.radio.wep.key")%>"
				size=15 maxlength=20 name="iobeam.gateway.radio.wep.key">
				&nbsp;<font face="verdana" size=1>Leave blank for no encryption</font>
		</td>
	</tr>
	<tr>
		<td></td>
		<td colspan="4" align="left">
		<font face="verdana" size=1>Use a 5 digit key for 40-bit or a 
		13 digit key for 104-bit encryption.</font>
	<tr>
		<td align="left" colspan="5">&nbsp;</td>
	</tr>
	<tr>
		<td align="left">
			<font class="newsbody">
			Private Address
			</font>
		</td>
		<td align="left" colspan="4">
			<input type="text"
			value="<%=gconfig.getProperty("iobeam.gateway.address.internal.1")%>"
			size="16" maxlength="16" name="iobeam.gateway.address.internal.1">
		</td>
	</tr>
	<tr>
		<td align="left">
			<font class="newsbody">
			Private Subnet
			</font>
		</td>
		<td align="left" colspan="4">
			<input type="text"
			value="<%=gconfig.getProperty("iobeam.gateway.subnet.internal.1")%>"
			size="16" maxlength="16" name="iobeam.gateway.subnet.internal.1">&nbsp;
			<font face="verdana" size="1">(for example 172.16.1.0/24)</font>
		</td>
	</tr>
	<tr>
		<td align="left">
			<font class="newsbody">
			Private Netmask
			</font>
		</td>
		<td align="left" colspan="4">
			<input type="text"
			value="<%=gconfig.getProperty("iobeam.gateway.netmask.internal.1")%>"
			size="16" maxlength="16" name="iobeam.gateway.netmask.internal.1">
		</td>
	</tr>
	<tr>
		<td align="left">
			<font class="newsbody">
			Private Broadcast
			</font>
		</td>
		<td align="left" colspan="4">
			<input type="text"
			value="<%=gconfig.getProperty("iobeam.gateway.broadcast.internal.1")%>"
			size="12" maxlength="15" name="iobeam.gateway.broadcast.internal.1">
		</td>
	</tr>
	<tr>
		<td align="left">
			<font class="newsbody">
			DHCP Lease Duration
			</font>
		</td>
		<td align="left" colspan="4">
			<input type="text"
			value="<%=gconfig.getProperty("iobeam.gateway.routelease.internal.1.duration")%>"
			size="3" maxlength="2" name="iobeam.gateway.routelease.internal.1.duration">
			&nbsp;<font face="verdana" size=1><B>Hours</B></font>
		</td>
	</tr>	
	<tr>
		<td align="left">
			<font class="newsbody">
			DHCP Address Range
			</font>
		</td>
		<td align="left" colspan="4">
			<input type="text"
			value="<%=gconfig.getProperty("iobeam.gateway.routelease.internal.1.start")%>"
			size="15" maxlength="15" name="iobeam.gateway.routelease.internal.1.start">
			&nbsp;<font face="verdana" size=1><B>to</B></font>
			<input type="text"
			value="<%=gconfig.getProperty("iobeam.gateway.routelease.internal.1.stop")%>"
			size="15" maxlength="15" name="iobeam.gateway.routelease.internal.1.stop">
		</td>
	</tr>	
	<tr>
		<td align="left" colspan="5">&nbsp;</td>
	</tr>
	<tr>
		<td align="left" colspan="5">&nbsp;</td>
	</tr>
	<tr>
		<td align="center" colspan="5">
			<font class="newsbody">
			<B>ETH 1 Network Settings</B>
			</font>
		</td>
	</tr>
	<tr>
		<td align="left">
			<font class="newsbody">
			Private Address
			</font>
		</td>
		<td align="left" colspan="4">
			<input type="text"
			value="<%=gconfig.getProperty("iobeam.gateway.address.internal.2")%>"
			size="16" maxlength="16" name="iobeam.gateway.address.internal.2">
		</td>
	</tr>
	<tr>
		<td align="left">
			<font class="newsbody">
			Private Subnet
			</font>
		</td>
		<td align="left" colspan="4">
			<input type="text"
			value="<%=gconfig.getProperty("iobeam.gateway.subnet.internal.2")%>"
			size="16" maxlength="16" name="iobeam.gateway.subnet.internal.2">&nbsp;
			<font face="verdana" size="1">(for example 172.16.1.0/24)</font>
		</td>
	</tr>
	<tr>
		<td align="left">
			<font class="newsbody">
			Private Netmask
			</font>
		</td>
		<td align="left" colspan="4">
			<input type="text"
			value="<%=gconfig.getProperty("iobeam.gateway.netmask.internal.2")%>"
			size="16" maxlength="16" name="iobeam.gateway.netmask.internal.2">
		</td>
	</tr>
	<tr>
		<td align="left">
			<font class="newsbody">
			Private Broadcast
			</font>
		</td>
		<td align="left" colspan="4">
			<input type="text"
			value="<%=gconfig.getProperty("iobeam.gateway.broadcast.internal.2")%>"
			size="12" maxlength="15" name="iobeam.gateway.broadcast.internal.2">
		</td>
	</tr>
	<tr>
		<td align="left" colspan="5">&nbsp;</td>
	</tr>
	<tr>
		<td align="left">
			<font class="newsbody">
			DHCP Address Range
			</font>
		</td>
		<td align="left" colspan="4">
			<input type="text"
			value="<%=gconfig.getProperty("iobeam.gateway.routelease.internal.2.start")%>"
			size="15" maxlength="15" name="iobeam.gateway.routelease.internal.2.start">
			&nbsp;<font face="verdana" size=1><B>to</B></font>
			<input type="text"
			value="<%=gconfig.getProperty("iobeam.gateway.routelease.internal.2.stop")%>"
			size="15" maxlength="15" name="iobeam.gateway.routelease.internal.2.stop">
		</td>
	</tr>	
	<tr>
		<td align="left">
			<font class="newsbody">
			DHCP Lease Duration
			</font>
		</td>
		<td align="left" colspan="4">
			<input type="text"
			value="<%=gconfig.getProperty("iobeam.gateway.routelease.internal.2.duration")%>"
			size="3" maxlength="2" name="iobeam.gateway.routelease.internal.2.duration">
			&nbsp;<font face="verdana" size=1><B>Hours</B></font>
		</td>
	</tr>	
	<tr>
		<td align="left" colspan="5">&nbsp;</td>
	</tr>
	<tr>
		<td align="left">
			<font class="newsbody">
			DNS Servers
			</font>
		</td>
		<td align="left" colspan="4">
			<input type="text"
			value="<%=gconfig.getProperty("iobeam.gateway.address.dns.1")%>"
			size="16" maxlength="16" name="iobeam.gateway.address.dns.1">
		</td>
	</tr>	
	<tr>
		<td align="left">
		</td>
		<td align="left" colspan="4">
			<input type="text"
			value="<%=gconfig.getProperty("iobeam.gateway.address.dns.2")%>"
			size="16" maxlength="16" name="iobeam.gateway.address.dns.2">
		</td>
	</tr>

	<tr>
		<td align="left" colspan="5">&nbsp;</td>
	</tr>
	<tr>
		<td align="center" colspan="5">
			<font class="newsbody">
			<B>Public Network Settings</B>
			</font>
		</td>
	</tr>
	<tr>
		<td align="left">
			<font class="newsbody">
			Public Type
			</font>
		</td>
		<td align="left">
			<% String type = gconfig.getProperty("iobeam.gateway.external.type"); %>

			<select name="iobeam.gateway.external.type" size=1>
				<option value="dhcp" <%="dhcp".equals(type) ? "selected" : ""%>>dhcp</option>
				<option value="static" <%="static".equals(type) ? "selected" :
				""%>>static</option>
			</select>
		</td>
		<td align="left" colspan="3">
			<font face="verdana" size="1">If <B>dhcp</B> leave the <B>address</B>,
			<b>netmask</B> and <b>broadcast</B> blank.</font>
		</td>
	</tr>
	<tr>
		<td align="left">
			<font class="newsbody">
			Public Address
			</font>
		</td>
		<td align="left" colspan="4">
			<input type="text"
			value="<%=gconfig.getProperty("iobeam.gateway.address.external")%>"
			size="16" maxlength="16" name="iobeam.gateway.address.external">
		</td>
	</tr>
	<tr>
		<td align="left">
			<font class="newsbody">
			Public Netmask
			</font>
		</td>
		<td align="left" colspan="4">
			<input type="text"
			value="<%=gconfig.getProperty("iobeam.gateway.netmask.external")%>"
			size="16" maxlength="16" name="iobeam.gateway.netmask.external">
		</td>
	</tr>
	<tr>
		<td align="left">
			<font class="newsbody">
			Public Broadcast Address
			</font>
		</td>
		<td align="left" colspan="4">
			<input type="text"
			value="<%=gconfig.getProperty("iobeam.gateway.broadcast.external")%>"
			size="16" maxlength="16" name="iobeam.gateway.broadcast.external">
		</td>
	</tr>
	<tr>
		<td align="left">
			<font class="newsbody">
			Default Gateway Address
			</font>
		</td>
		<td align="left" colspan="4">
			<input type="text"
			value="<%=gconfig.getProperty("iobeam.gateway.defaultgateway.external")%>"
			size="16" maxlength="16" name="iobeam.gateway.defaultgateway.external">
		</td>
	</tr>

	<tr>
		<td align="center" colspan="5">
			<input type="submit" value="  Set  ">
		</td>
	</form>
	</tr>
	</table>
</td>
</tr>
</table>

</body>
</html>


