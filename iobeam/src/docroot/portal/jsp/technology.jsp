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
				<td valign="top" align="left"><img src="/images/inside/headlineTechnology.jpg" width="597" height="36" border="0"><br>

					<table border="0" cellpadding="0" cellspacing="0" width="98%">
						<tr>
							<td width="50"></td>
							<td><span class="bodyheading"><BR>
								i<i>o</i>beam&#8482; provides wireless access wherever you need it.</span>
								<BR><BR>
								<p><span class="newsbody">
								i<i>o</i>beam&#8482; is a leading wireless internet service provider offering connectivity
								where you need it.  i<i>o</i>beam provides complete, end-to-end hardware and
								subscription services for affordable deployment of wireless enabled
								networks.  
																
								<BR><BR>
								</span>
								<img src="/images/networkIllustration_small.jpg" align="right"
								valign="top" style="{padding: 3 3 3 3;}">
								<span class="bodyheading">How does it
								work?</span><BR><BR>
								<span class="newsbody">
								i<i>o</i>beam's network employs industry standard wireless communication
								between the network access point and client devices such as PDAs and laptops.
								The defacto standard and most commonly deployed wireless networks use
								the unlicensed 2.4ghz radio frequency and the 802.11b protocol,
								also called WiFi.  WiFi allows speeds up to 11 megabits per second, 10 times
								faster than DSL and 50 times faster than dialup.  
								Wireless reception distances can vary, with real world performance between 200 and 1000
								feet.  <BR><BR>Signal reception depends on many factors. 
								Environments free of radio
								noise and other electro-magnetic interference yield the strongest signal and
								longest ranges.
								<BR><BR>
								</span>
								<span class="bodyheading">
								What do I need?<BR><BR></span>
								<span class="bodyheadingsmallgrey">
								Mobile Networkers<BR><BR></span>
								<span class="newsbody">
								If you are looking for wireless connectivity,
								the i<i>o</i>beam service requires you to have a
								mobile device capable of communicating using
								802.11b (or 802.11g).  Click on </span><a
								href="/jsp/products.jsp">products</a><span
								class="newsbody"> to see
								suggestions and special offers for wireless
								cards that work with our service.  If you have
								a device with Intel Centrino&reg; technology,
								you do not need any additional hardware.
								<BR><BR></span>
								<span class="bodyheadingsmallgrey">
								Venue Operators<BR><BR></span>
								<span class="newsbody">
								If you are a venue operator wanting to deploy
								the <B>i<i>o</i>beam</B> Service at your
								location, you will need an i<i>o</i>beam Service
								Gateway and an upstream connection to the
								internet.  Although the i<i>o</i>beam service will
								work with almost any kind of connection,
								i<i>o</i>beam suggests using a connection with at
								least 256k of upstream bandwidth available.<BR><BR>
								The i<i>o</i>beam Service Gateway&#8482; (ISG) is a small, highly integrated  access device that 
								provides a connection  between the hot spot venue and iobeam's authentication and 
								usage accounting services.  Hot Spot operators connect the ISG to their ISDN, DSL or 
								Cable modems.  The ISG also has the capability to bridge the wireless network with an
								existing local area network.  Once the ISG is configured, the hot spot location is ready to accept wireless internet users. 
								</span></p>
								
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
