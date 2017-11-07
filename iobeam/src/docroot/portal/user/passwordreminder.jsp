<%@page import="java.lang.Math,
com.iobeam.portal.task.actor.user.usersession.*,
com.iobeam.portal.ui.web.user.UserSessionHelper,
com.iobeam.portal.model.venue.*,
javax.ejb.*,
javax.naming.*,
java.rmi.*"%>

<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

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
				<td valign="top" align="left"><br>
					<table border="0" cellpadding="0" cellspacing="0" width="98%">
						<tr>
							<td width="50"></td>
							<td>
								<span class="bodyheading">
								Password Reminder
								</span>
								<p><BR>
								<span class="newsbody">
								Please answer the questions below.  When you are finished,
								click <B>Finished</B>.
								<BR>
								<html:errors/>
								<BR>
								<html:form action="/user/passwordreminderaction.do" method="post">
								<table border=0 cellpadding=0 cellspacing=0>
									<tr>
										<td align="left">
											<font class="fontlabel">
												Email Address
											</font>
										</td>
										<td align="left">
											<html:text size="30" property="emailAddress" maxlength="50"/>
										</td>
									</tr>
									<tr>
										<td align="left">
											<font class="fontlabel">
												City, State 
											</font>
										</td>
										<td align="left">
											<html:text size="20" property="city" maxlength="20"/>&nbsp;
											<html:text size="3" property="state" maxlength="2"/>
										</td>
									</tr>
									<tr>
										<td align="left">
											<font class="fontlabel">
												Zipcode
											</font>
										</td>
										<td align="left">
											<html:text size="8" property="zipcode" maxlength="7"/>
										</td>
									</tr>
									<tr>
										<td align="left">
											<font class="fontlabel">
												What city were you born in?
											</font>
										</td>
										<td align="left">
											<html:text size="20" property="reminderAnswer" maxlength="50"/>
										</td>
									</tr>
									<tr>
										<td align="center" colspan="2">
											<input type="submit" value="--Finished--">
										</td>
									</tr>
									</html:form>
								</table>		
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
