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
				<td valign="top" align="left"><img src="/images/inside/headlineContact.jpg" width="597" height="36" border="0"><br>
					<table border="0" cellpadding="0" cellspacing="0" width="98%">
						<tr>
							<td width="50"></td>
							<td>
								<p><BR>
								<span class="newsbody">
								<BR>
								Iobeam is based in the Metro Atlanta Area.  
								<BR><BR>
								Our address is:<BR>
								Iobeam, inc.<BR>
								
								890-F Atlanta Street, Dept 175<BR>
								Roswell, GA 30075<BR>
								(678)268-4095<BR>
								<BR>
								Sales Inquiries:<BR>
								<a href="mailto:sales@iobeam.com"><font class="linkfont">sales@iobeam.com</font></A>
								<BR><BR>
								Employment Opportunities:<BR>
								<a href="mailto:jobs@iobeam.com"><font class="linkfont">jobs@iobeam.com</font></A>
								<BR><BR>
								Business Development:<BR>
								<a href="mailto:bizdev@iobeam.com"><font class="linkfont">bizdev@iobeam.com</font></A>
								<BR><BR>
								Professional Services:<BR>
								<a href="mailto:engineering@iobeam.com"><font class="linkfont">engineering@iobeam.com</font></A>
								
							
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
