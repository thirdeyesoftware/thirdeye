<%@page import="java.lang.Math,
com.iobeam.portal.task.actor.user.usersession.*,
com.iobeam.portal.ui.web.user.UserSessionHelper,
com.iobeam.portal.model.venue.*,
javax.ejb.*,
javax.naming.*,
java.rmi.*"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@include file="../includes/usersession.html"%>

	
<html>

	<head>
		<meta http-equiv="content-type" content="text/html;charset=ISO-8859-1">
		<meta http-equiv="Expires" content="Sun, June 10, 1972 14:59:00 GMT">
		<meta http-equiv="Pragma" content="no-cache">

		<title>ioBeam : wireless where you need it :</title>
		<%@include file="../js/functions.js"%>
		<link rel="stylesheet" href="/iobeam.css">
		<style media="screen" type="text/css"><!--
#layer1 { position: absolute; top: 291px; left: 192px; width: 553px; height: 100px; visibility: visible }
#layer2 { position: absolute; top: 477px; left: 213px; width: 180px; height: 55px; visibility: visible }
#layer3 { position: absolute; top: 477px; left: 483px; width: 262px; height: 42px; visibility: visible }
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
					<br>
					<img src="/images/inside/userLoginMIDDLE.jpg" width="598" height="118" border="0"><br>
					<a href="https://www.iobeam.com/user/signup.jsp" border=0><img border=0 src="/images/inside/userLoginBOTTOM.jpg" width="598" height="155" border="0">
					</a>
				</td>
			</tr>
			<tr height="30">
				<td width="162" height="30"></td>
				<td valign="bottom" height="30" align="center">
					<table border="0" cellpadding="0" cellspacing="0" width="100%"
					align="center">
						<tr>
							<td width="50"></td>
							<td><%@include file="../includes/footer.html"%>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<map name="inside_01bad41e41"><area shape="rect" coords="8,7,146,65" href="/index.jsp"></map>
		<div id="layer1">
			<span class="newsbody"><i><b>Welcome to your Freedom. Unplug and have wireless where you need it.</b></i></span>
			<p><span class="newsbody">
				Please enter your username and password below.  If you have a
				subscription card, enter the 15-digit number on the back of the card.
				Click <B>Go</B> to continue.</span></p>
				<BR>

		</div>
		<div id="layer2">
			<table border="0" cellpadding="0" cellspacing="0" width="180">
			<html:form action="/user/signonuseraction" method="post">
				<html:hidden property="type" value="secureid"/>
				<tr>
					<td align="right">
					<html:text property="secureId" size="12"/></td>
					<td width="10"></td>
					<td><input type="image" src="/images/inside/userLoginSUBSCRIPT_Button.jpg"></td>
				</tr>
				<tr>
					<td colspan="3" align="center">&nbsp;</td>
				</tr>
				</html:form>
			</table>
		</div>
		<div id="layer3">
			<table border="0" cellpadding="0" cellspacing="0" width="262">
			<html:form action="/user/signonuseraction" method="post">
				<html:hidden property="type" value="credentials"/>
				<tr>
					<td><html:text property="username"  size="12"/></td>
					<td width="30"></td>
					<td><html:password property="password" size="12"/></td>
					<td><input type="image" src="/images/inside/userLoginUSER_Button.jpg"></td>
				</tr>
				</html:form>
				<tr>
					<td colspan="3" align="center"><a href="/user/passwordreminder.jsp"><span class="newsbody"><b>&lt; forgot your password? &gt;</b></span></a></td>
					<td></td>
				</tr>
			</table>
		</div>
	</body>

</html>
