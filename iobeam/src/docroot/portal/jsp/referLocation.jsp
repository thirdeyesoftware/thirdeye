<%@page import="java.lang.Math,
com.iobeam.portal.task.actor.user.usersession.*,
com.iobeam.portal.ui.web.user.UserSessionHelper,
com.iobeam.portal.model.venue.*,
javax.ejb.*,
javax.naming.*,
java.rmi.*"%>

<%@include file="../includes/usersession.html"%>
<html>

	<head>
		<meta http-equiv="content-type" content="text/html;charset=ISO-8859-1">
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
				<td valign="top" align="left"><img src="/images/inside/headlineReferLocation.jpg" width="597" height="36" border="0"><br>
					<table border="0" cellpadding="0" cellspacing="0" width="98%">
						<tr>
							<td width="50"></td>
							<td>
								<p><span class="newsbody">
								<BR>
								Soon, you'll be able to use iobeam's Wireless network where you need it most!  If you would like to suggest a location that is convenient
								to you, where you'd like to see our service, send an email to <a href="mailto:locations@iobeam.com"><font class="linkfont">locations@iobeam.com</font></A>.
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
