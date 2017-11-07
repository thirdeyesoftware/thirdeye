<%@page import="java.lang.Math,
com.iobeam.portal.task.actor.user.usersession.*,
com.iobeam.portal.ui.web.user.UserSessionHelper,
com.iobeam.portal.model.venue.*,
javax.ejb.*,
javax.naming.*,
java.rmi.*, 
java.net.URLDecoder"%>

<%@include file="../includes/usersession.html"%>
<%
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
		<link rel="stylesheet" href="/iobeam.css" type="text/css"/>
		<%@include file="/js/functions.js"%>
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
				<td valign="top" align="left">
					<table border="0" cellpadding="0" cellspacing="0" width="98%">
						<tr>
							<td width="50"></td>
							<td>
								<font class="newsbody">
								<BR>
								<BR>
								Welcome to the iobeam service portal brought to you by our friends at
								<B><%=usersession.getVenue().getVenueName() %></B>.  Experience your
								wireless freedom.</font>
								<BR>
								<BR>
								You may now use this wireless internet connection in the 
								same manner you use your internet connection at home or at the office.  
								<BR>
								<BR>
								<% if (redirect != null) { %>
								You should be redirected in a few
								seconds to the page you originally requested.  If you are not
								redirected, click <a href="<%=redirect%>">here</a>.
								<% } %>
								<BR>
								<BR>
								<BR>
								<BR>
								<BR>
								<BR>
								<BR>
								<BR>
								<BR>
								<BR>
								<BR>
								<BR>
								<BR>
								
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr height="30">
				<td width="162" height="30"></td>
				<td valign="bottom" height="30">
					<%@include file="/includes/footer.html"%>
				</td>
			</tr>
		</table>
		<map name="inside_01bad41e41"><area shape="rect" coords="8,7,146,65" href="/index.jsp"></map>
	</body>

</html>
