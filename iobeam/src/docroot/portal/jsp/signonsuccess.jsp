<%@page import="java.lang.Math,
com.iobeam.portal.task.actor.user.usersession.*,
com.iobeam.portal.ui.web.user.UserSessionHelper,
com.iobeam.portal.model.venue.*,
javax.ejb.*,
javax.naming.*,
java.rmi.*, 
java.net.URLDecoder"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@include file="../includes/usersession.html"%>
<%
	String customerName = 
		usersession.getCustomer().getCustomerContact().getContactName().getFirstName() + " " +
		usersession.getCustomer().getCustomerContact().getContactName().getLastName();
	
	String encodedRedirectURL = (String)request.getSession().getAttribute("redirecturl");

	String redirect = null;
	try {
		if (encodedRedirectURL != null && !encodedRedirectURL.trim().equals("")) {
			redirect = URLDecoder.decode(encodedRedirectURL, "UTF-8");
		}
	}
	catch (Exception e) {
	}
%>
	
<html>

	<head>
		<meta http-equiv="content-type" content="text/html;charset=ISO-8859-1">
		<% if (redirect != null) { %>
		<meta http-equiv="refresh" content="10;URL=<%=redirect%>">
		<% } %>
		<title>iobeam : wireless where you need it :</title>
		<%@include file="../js/functions.js"%>
		<link rel="stylesheet" href="/iobeam.css">
		<style media="screen" type="text/css"><!--
#layer1 { position: absolute; top: 291px; left: 192px; width: 553px; height: 100px; visibility: visible }
--></style>
	</head>

	<body bgcolor="#ffffff" leftmargin="0" marginwidth="0" topmargin="0" marginheight="0" background="/images/inside/insideBkgd.jpg" onload="CSScriptInit();">
		<table border="0" cellpadding="0" cellspacing="0" width="760">
			<tr>
				<td colspan="2"><img src="/images/inside/inside_01.jpg" width="760" height="89" border="0" usemap="#inside_01bad41e41"></td>
			</tr>
			<tr>
				<td width="162" valign="top">
				<%@include file="/includes/nav.jsp"%>
				</td>
				<td valign="top" align="left"><img src="/images/inside/userLoginTOP.jpg" width="598" height="197" border="0"><br>
					<br><BR>
				</td>
			</tr>
			<tr height="30">
				<td width="162" height="30"></td>
				<td valign="bottom" height="30" align="center">
					<table border="0" cellpadding="0" cellspacing="0" width="100%"
					align="center">
						<tr>
							<td width="50"></td>
							<td><%@include file="../includes/footer.html"%></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<map name="inside_01bad41e41"><area shape="rect" coords="8,7,146,65" href="/index.jsp"></map>
		<div id="layer1">
			<span class="newsbody"><i><b>Welcome to your Freedom. Unplug and have wireless where you need it.</b></i></span>
			<p><span class="newsbody">
			<BR>
			<%@include file="../includes/portalmenu.html"%><BR><BR>
				Welcome <B><%=customerName%></B>, to the i<i>o</i>beam service.
				You have successfully logged in.  You may now use this 
				wireless internet connection in the same manner you use 
				your internet connection at home or at the office.  
				<% if (redirect != null) { %>
				<BR><BR>You should be redirected in a few
				seconds to the page you originally requested.  If you are not
				redirected, click <a href="<%=redirect%>"><font class="linkfont">here</font></a>.
				<% } %>
				</span></p>
		</div>
	</body>

</html>
