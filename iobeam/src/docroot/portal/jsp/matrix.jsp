<%@page import="java.lang.Math,
com.iobeam.portal.task.actor.user.usersession.*, com.iobeam.portal.ui.web.user.UserSessionHelper,
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
				<td valign="top" align="left"><img src="/images/inside/headlineServices.jpg" width="597" height="36" border="0"><br>
					<table border="0" cellpadding="0" cellspacing="0" width="98%">
						<tr>
							<td width="20"></td>
							<td>
								<p><BR>
								<span class="newsbody">We believe <B>i<i>o</i>beam</B> has what
								it takes to make your venue stand out as a competitive,
								revenue-generating hot spot.  Here is a comparison of our venue
								services along with the offerings of some of competitors.
								<BR>
								<table border=0 cellpadding=2 cellspacing=2 align="left"
								width="100%">
								<tr>
									<td align="center">
										<font class="blackformlabel">
											Venue Services
										</font>
									</td>
									<td align="center">
										<font class="blackformlabel">
											T-Mobile
										</font>
									</td>
									<td align="center">
										<font class="blackformlabel">
											Boingo
										</font>
									</td>
									<td align="center">
										<font class="blackformlabel">
											HotSpotzz
										</font>
									</td>
									<td align="center">
										<font class="bodyheading">
											i<i>o</i>beam
										</font>
									</td>
								</tr>
								<tr>
									<td align="left">
										<font class="newsbody">
											WiFi 802.11b Enabled
										</font>
									</td>
									<td align="center">
										<font class="newsbody">
											Yes
										</font>
									</td>
									<td align="center">
										<font class="newsbody">
											Yes
										</font>
									</td>
									<td align="center">
										<font class="newsbody">
											Yes
										</font>
									</td>
									<td align="center">
										<font class="bodyheading">
											Yes
										</font>
									</td>
								</tr>
								<tr>
									<td align="left">
										<font class="newsbody">
											Indoor / Outdoor Service
										</font>
									</td>
									<td align="center">
										<font class="newsbody">
											Yes
										</font>
									</td>
									<td align="center">
										<font class="newsbody">
											Yes
										</font>
									</td>
									<td align="center">
										<font class="newsbody">
											Yes
										</font>
									</td>
									<td align="center">
										<font class="bodyheading">
											Yes
										</font>
									</td>
								</tr>
								<tr>
									<td align="left">
										<font class="newsbody">
											Venue Hardware
										</font>
									</td>
									<td align="center">
										<font class="newsbody">
											N/A
										</font>
									</td>
									<td align="center">
										<font class="newsbody">
											$799.00
										</font>
									</td>
									<td align="center">
										<font class="newsbody">
											$500.00
										</font>
									</td>
									<td align="center">
										<font class="bodyheading">
											As low as $499.00
										</font>
									</td>
								</tr>
								<tr>
									<td align="left">
										<font class="newsbody">
											Lease Hardware
										</font>
									</td>
									<td align="center">
										<font class="newsbody">
											No
										</font>
									</td>
									<td align="center">
										<font class="newsbody">
											No
										</font>
									</td>
									<td align="center">
										<font class="newsbody">
											No
										</font>
									</td>
									<td align="center">
										<font class="bodyheading">
											Yes
										</font>
									</td>
								</tr>
								<tr>
									<td align="left">
										<font class="newsbody">
											Flexible Billing
										</font>
									</td>
									<td align="center">
										<font class="newsbody">
											No
										</font>
									</td>
									<td align="center">
										<font class="newsbody">
											No
										</font>
									</td>
									<td align="center">
										<font class="newsbody">
											No
										</font>
									</td>
									<td align="center">
										<font class="bodyheading">
											Yes
										</font>
									</td>
								</tr>
								<tr>
									<td align="left">
										<font class="newsbody">
											Payment Plans
										</font>
									</td>
									<td align="center">
										<font class="newsbody">
											N/A
										</font>
									</td>
									<td align="center">
										<font class="newsbody">
											No
										</font>
									</td>
									<td align="center">
										<font class="newsbody">
											No
										</font>
									</td>
									<td align="center">
										<font class="bodyheading">
											Yes
										</font>
									</td>
								</tr>
								<tr>
									<td align="left">
										<font class="newsbody">
											Revenue Share
										</font>
									</td>
									<td align="center">
										<font class="newsbody">
											N/A
										</font>
									</td>
									<td align="center">
										<font class="newsbody">
											Yes
										</font>
									</td>
									<td align="center">
										<font class="newsbody">
											Yes
										</font>
									</td>
									<td align="center">
										<font class="bodyheading">
											Yes
										</font>
									</td>
								</tr>
								<tr>
									<td align="left">
										<font class="newsbody">
											Allows Dynamic IP Address
										</font>
									</td>
									<td align="center">
										<font class="newsbody">
											No
										</font>
									</td>
									<td align="center">
										<font class="newsbody">
											No
										</font>
									</td>
									<td align="center">
										<font class="newsbody">
											No
										</font>
									</td>
									<td align="center">
										<font class="bodyheading">
											Yes
										</font>
									</td>
								</tr>
								<tr>
									<td align="left">
										<font class="newsbody">
											Marketing Assistance
										</font>
									</td>
									<td align="center">
										<font class="newsbody">
											Yes
										</font>
									</td>
									<td align="center">
										<font class="newsbody">
											No
										</font>
									</td>
									<td align="center">
										<font class="newsbody">
											Yes
										</font>
									</td>
									<td align="center">
										<font class="bodyheading">
											Yes
										</font>
									</td>
								</tr>
								<tr>
									<td align="left">
										<font class="newsheadingsmall">*</font>&nbsp;
										<font class="newsbody">
											<B>Customize Service by Day / Time </B>
										</font>
									</td>
									<td align="center">
										<font class="newsbody">
											No
										</font>
									</td>
									<td align="center">
										<font class="newsbody">
											No
										</font>
									</td>
									<td align="center">
										<font class="newsbody">
											No
										</font>
									</td>
									<td align="center">
										<font class="bodyheading">
											Yes
										</font>
									</td>
								</tr>
								<tr>
									<td align="left">
										<font class="newsheadingsmall">*</font>&nbsp;
										<font class="newsbody">
											<B>Allow Access to Preferred Content</B>
										</font>
									</td>
									<td align="center">
										<font class="newsbody">
											No
										</font>
									</td>
									<td align="center">
										<font class="newsbody">
											No
										</font>
									</td>
									<td align="center">
										<font class="newsbody">
											No
										</font>
									</td>
									<td align="center">
										<font class="bodyheading">
											Yes
										</font>
									</td>
								</tr>
								<tr>
									<td colspan="5">
										&nbsp;
									</td>
								</tr>
								<tr>
									<td colspan="5" align="left">
										<font class="newsheadingsmall">*</font>&nbsp;&nbsp;
										<font class="newsbody">
										These services require an enhanced i<i>o</i>beam venue
										subscription.  Ask your sales representative for details.
										</font>
									</td>
								</tr>
							</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr height="30">
				<td width="162" height="30"></td>
				<td valign="bottom" height="30" align="center">
					<%@include file="/includes/footer.html"%>

				</td>
			</tr>
		</table>
		<map name="inside_01bad41e41"><area shape="rect" coords="8,7,146,65" href="/index.jsp"></map>
	</body>

</html>
