<%@page import="java.lang.Math,
com.iobeam.portal.task.actor.user.usersession.*,
com.iobeam.portal.ui.web.user.contact.UserContactForm,
com.iobeam.portal.ui.web.user.UserSessionHelper,
com.iobeam.portal.model.venue.*,
com.iobeam.portal.model.account.*,
com.iobeam.portal.model.customer.*,
com.iobeam.portal.model.customercontact.*,
com.iobeam.portal.model.billing.*,
com.iobeam.portal.model.subscription.*,
com.iobeam.portal.model.gateway.usercontact.*,
com.iobeam.portal.util.*,
org.apache.struts.action.*,
java.text.SimpleDateFormat,
java.util.*,
javax.ejb.*,
javax.naming.*,
java.rmi.*"%>

<%
	if (UserSessionHelper.getUserSession(request.getSession()) == null) {
		response.sendRedirect(response.encodeRedirectURL("/index.jsp"));
	}
%>

<%@include file="../includes/usersession.html"%>

<% /**
    * this jsp is the result of a forward from ui controller
		* GetAccountSummaryAction
		*/
	
	SimpleDateFormat sdf= new SimpleDateFormat("MM/dd/yyyy");
	SimpleDateFormat stf = new SimpleDateFormat("hh:mm:ss a");
	SimpleDateFormat fullformat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

	Customer customer = usersession.getCustomer();
	
	ActionErrors errors = new ActionErrors();

	BillingPeriod currentBillingPeriod = 
		BillingPeriod.getInstanceFor(new Date());
	Collection usage;
	Collection months;

	try {

		customer = usersession.getCustomer();
		usage = (Collection)request.getAttribute("usage");
		months = (Collection)request.getAttribute("months");

	} 
	catch (Exception ee) {
		errors.add("application", new ActionError("app.exception"));
		request.getRequestDispatcher("/jsp/error.jsp").
			forward(request, response);
		return;
	}
	
%>

<html><head>
		<meta http-equiv="content-type" content="text/html;charset=ISO-8859-1"><title>iobeam : wireless where you need it :</title>
		
		<link rel="stylesheet" href="/iobeam.css" type="text/css"/>
		<%@include file="/js/functions.js"%>
</head>

	<body bgcolor="#ffffff" leftmargin="0" marginwidth="0" topmargin="0" marginheight="0" background="/images/inside/insideBkgd.jpg" onload="CSScriptInit();">

		<table border="0" cellpadding="0" cellspacing="0" width="760">
			<TR>
				<td colspan="2">
					<img src="/images/inside/inside_01.jpg" width="760" height="89" border="0" usemap="#inside_01bad41e41"></td>
			</tr>
			<tr>
				<td width="162" valign="top">
					<%@include file="/includes/nav.jsp"%>
				</td>
				<td valign="top" align="left"><img src="/images/inside/headlineAccountSumma.jpg" width="598" height="88" border="0">
					<br>
					<table border="0" cellpadding="0" cellspacing="0" width="98%">
						<tbody>
						<tr height="20">
							<td height="20" colspan="2" align="center">
							</td>
						</tr>
						<tr>
							<td width="30"></td>
							<td align="center">
							</td>
						</tr>
						<tr>
							<td width="30"></td>
							<td>
								<table border="0" cellpadding="4" cellspacing="0" width="560">
									<tbody>
									<tr>
										<td bgcolor="#2d4470" valign="middle"><span class="adminheadline"><B>
											<%=customer.getCustomerContact().getContactName().getFirstName() + " "+
												customer.getCustomerContact().getContactName().getLastName()%></B><BR>
											</span><span class="adminreversebold">
												<B>iobeam&#8482; member since:
												<i><%=sdf.format(customer.getData().getCreateDate())%></i></B></span></td>
										<td width="5"></td>
										<td width="150" bgcolor="#2d4470" align="right" valign="middle">
											<table border="0" cellpadding="2" cellspacing="0">
												<tbody><tr>
													<td align="right"><span class="adminorangebold">privacy statement</span></td>
													<td align="center"><img src="/images/inside/privacyLock.jpg" width="18" height="25" border="0"></td>
												</tr>
											</tbody></table>
										</td>
									</tr>
									<tr height="5">
										<td align="left" height="5">
												<%@include file="../includes/portalmenu.html"%><BR>
										</td>
										<td height="5">&nbsp;</td>
										<td align="left" height="10">&nbsp;
										</td>
									</tr>
									<tr height="20">
										<td rowspan="2" valign="top" bgcolor="#f5f5f5"><span class="newsbody">
												<table border=0 cellpadding=0 cellspacing=0 width="100%">
													<tr>
														<td align="left">
														<font class="bodyheading">
															Usage	
														</font>
														</td>
														<td align="right">
															<font class="blackdataelement">
															<form action="<%=response.encodeURL("/user/getusageaction.do")%>"
															method="post">
															<input type="hidden"
																value="<%=usersession.getUser().getUserPK().getID()%>"
																name="userid">
															<select name="startdate">
															<% for (Iterator it = months.iterator();
															it.hasNext(); ) {
																	Date d = (Date)it.next();
															%>
																<option
																value="<%=sdf.format(d)%>"><%=sdf.format(d)%></optioN>
															<% } %>
															</select>
															<input type="submit" value="-Go-">

															</font>
															</form>
														</td>
													</tr>
													<tr height="5">
														<td colspan="4" height="5">&nbsp;</td>
													</tr>
													<tr>
														<td align="left" colspan="2">
														<table border=0 cellpadding=0 cellspacing=2
														width="100%">
															<tr>
																<td align="left">
																	<font class="blackformlabel">
																		Date
																	</font>
																</td>
																<td align="left">
																	<font class="blackformlabel">
																		Time	
																	</font>
																</td>
																<td align="left">
																	<font class="blackformlabel">
																		Venue	
																	</font>
																</td>
																<td align="left">
																	<font class="blackformlabel">
																		Duration
																	</font>
																</td>
															</tr>
															<% if (usage.size() == 0) { %>
															<tr>
																<td align="center" colspan="4">
																	<font class="newsbody">
																	You have not used the service during the  
																	current billing period.
																	</font>
																</td>
															</tr>
															<% } %>

													<% for (Iterator it = usage.iterator(); it.hasNext();) {
															UserContactForm contact = (UserContactForm)it.next();
													%>
															<tr>
																<td align="left">
																	<font class="blackdataelementsmall">
																	<%=sdf.format(fullformat.parse(contact.getDate()))%>
																	</font>
																</td>
																<td align="left">
																	<font class="blackdataelementsmall">
																	<%=stf.format(fullformat.parse(contact.getDate()))%>
																	</font>
																</td>
																<td align="left">
																	<font class="blackdataelementsmall">
																	<%=contact.getVenueName()%>
																	</font>
																</td>
																<td align="left">
																	<font class="blackdataelementsmall">
																	n/a
																	</font>
																</td>
															</tr>
														<% } %>
														</table>
														</td>
													</tr>
												</table>
										</td>
										<td width="5" height="20"></td>
										<td width="150" height="20" bgcolor="#2d4470"><span class="adminreversebold">NOTICES:</span></td>
									</tr>
									<tr>
										<td width="10"></td>
										<td width="150" bgcolor="#f5f5f5" valign="top">
												<span class="newsbody"><b>Billing Notice:<br>
												</b>The current billing period ends on
												<B><%=sdf.format(currentBillingPeriod.getEndDate())%></B>.&nbsp;&nbsp;
												If you secured your account with a credit card, it
												will be billed the day before the billing period ends.</span>
												<BR><BR><span class="newsbody"><b>Enhanced Web:<br>
												</b>i<i>o</i>beam will soon offer enhanced web access
												and discounts to preferred merchants on the net from
												selected venues.</span>
										</td>
									</tr>
								</tbody></table>
							</td>
						</tr>
					</tbody>
					</table>
				</td>
			</tr>
			<tr height="30">
				<td width="162"></td>
				<td valign="bottom" align="center" height="30">
					<%@include file="/includes/footer.html"%>
				</td>
			</tr>

		</tbody></table>
		<map name="inside_01bad41e41"><area shape="rect" coords="8,7,146,65" href="/index.jsp"></map>
	</body></html>
