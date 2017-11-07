<%@page import="java.lang.Math,
com.iobeam.portal.task.actor.user.usersession.*,
com.iobeam.portal.ui.web.user.UserSessionHelper,
com.iobeam.portal.model.venue.*,
com.iobeam.portal.model.account.*,
com.iobeam.portal.model.customer.*,
com.iobeam.portal.model.customercontact.*,
com.iobeam.portal.model.billing.*,
com.iobeam.portal.model.subscription.*,
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
<%@ taglib uri="/WEB-INF/app.tld" prefix="app"%>

<%@include file="../includes/usersession.html"%>

<% /**
    * this jsp is the view result of a forward from ui controller
		* GetSubscriptionSummaryAction
		*/
	
	SimpleDateFormat sdf= new SimpleDateFormat("MM/dd/yyyy");
		
	Customer customer = usersession.getCustomer();
	
	ActionErrors errors = new ActionErrors();
	
	BillingPeriod currentBillingPeriod = 
		BillingPeriod.getInstanceFor(new Date());
	
	Subscription subscription;
	Account account;

	try {

		subscription =
			(Subscription)request.getAttribute("subscription");
		account = (Account)request.getAttribute("account");
		

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

<div id="tooltip" style="position:absolute;visibility:hidden"></div>

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
							<td width="30" ></td>
							<td align="center">
							</td>
						</tr>
						<tr>
							<td width="30"></td>
							<td>
								<table border="0" cellpadding="4" cellspacing="0" width="560">
									<tbody><tr>
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
										<td height="5" align="left">
											<%@include file="../includes/portalmenu.html"%>
										</td>
										<td align="left" height="5"></td>
										<td align="left" height="5"></td>
									</tr>
									<tr height="20">
										<td rowspan="2" valign="top" bgcolor="#f5f5f5"><span class="newsbody">
												<font class="bodyheading">Subscription Detail</font><BR><BR>
												<table border=0 cellpadding=4 cellspacing=0 width="100%">
													<tr>
														<td align="left" colspan="2">
														</td>
													</tr>
													<tr>
														<td align="left" bgcolor="#F5F5F5">
														<font class="formlabel">
															Account
														</font>
														</td>
														<td align="right" bgcolor="#F5F5F%">
															<font class="blackdataelement">
																<%=account.getData().getAccountNumber().getAccountNumberString()%>
															</font>
														</td>
													</tr>
													<tr>
														<td align="left" bgcolor="#FFFFFF">
														<font class="formlabel">
															Subscription	
														</font>
														</td>
														<td align="right" bgcolor="#FFFFFF">
															<font class="blackdataelement">
																<%=subscription.getSubscriptionType().getName()%>
															</font>
														</td>
													</tr>
													<tr>
														<td align="left" bgcolor="#F5F5F5">
														<font class="formlabel">
															Start Date	
														</font>
														</td>
														<td align="right" bgcolor="#F5F5F5">
															<font class="blackdataelement">
																<%=sdf.format(subscription.getStartDate())%>
															</font>
														</td>
													</tr>
													<tr>
														<td align="left" bgcolor="#FFFFFF">
														<font class="formlabel">
															Expiration Date	
														</font>
														</td>
														<td align="right" bgcolor="#FFFFFF">
															<font class="blackdataelement">
																<% if (subscription.getExpirationDate() == null) { 
																			out.print("perpetual");
																	 } else {
																	 		out.print(sdf.format(subscription.getExpirationDate()));
																	 }
																%>
															</font>
														</td>
													</tr>
													<% if (subscription.getExpirationDate() != null) { %>
													<tr>
														<td align="left" bgcolor="#F5F5F5">
														<font class="formlabel">
															# Billing Cycles Remaining	
														</font>
														</td>
														<td align="right" bgcolor="#F5F5F5">
															<font class="blackdataelement">
																<%=subscription.getCurrentBillingCycleCount()%>
															</font>
														</td>
													</tr>
													<% } %>
													<tr>
														<td align="left" bgcolor="#F5F5F5">
														<font class="formlabel">
															Amount	
														</font>
														</td>
														<td align="right" bgcolor="#F5F5F5">
															<font class="blackdataelement">
																$<%=subscription.getCostPerBillingCycle().
																	getStringAmount()%>
															</font>
														</td>
													</tr>
												</table>
										</td>
										<td width="5" height="20"></td>
										<td width="150" height="20" bgcolor="#2d4470"><span class="adminreversebold">NOTICES:</span></td>
									</tr>
									<tr>
										<td width="5"></td>
										<td width="150" bgcolor="#f5f5f5" valign="top">
											<app:CustomerNoticeTag/>
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
