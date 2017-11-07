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
    * this jsp is the result of a forward from ui controller
		* GetAccountSummaryAction
		*/
	
	SimpleDateFormat sdf= new SimpleDateFormat("MM/dd/yyyy");
		
	Customer customer = usersession.getCustomer();
	Collection subscriptions;
	AccountData accountData;
	Collection accounts;
	BillableParty billableParty = null;
	CustomerContact billingContact = null;
	ActionErrors errors = new ActionErrors();
	Account account;
	BillingPeriod currentBillingPeriod = 
		BillingPeriod.getInstanceFor(new Date());

	try {

		customer = usersession.getCustomer();
		account= (Account)request.getAttribute("account");
		subscriptions = (Collection)request.getAttribute("subscriptions");
		accounts = customer.getAccounts();	

		if (request.getAttribute("billableparty") != null) { 
			billableParty = (BillableParty)request.getAttribute("billableparty");
			billingContact = billableParty.getBillingCustomerContact();
		}

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
										<td height="5">
											<%@include file="../includes/portalmenu.html"%>
										</td>
										<td align="left"></td>
										<td align="left"></td>
									</tr>
									<tr height="20">
										<td rowspan="2" valign="top" bgcolor="#f5f5f5"><span class="newsbody">
												<font class="bodyheading">Account Detail</font><BR><BR>
												<table border=0 cellpadding=2 cellspacing=0 width="100%">
													<tr>
														<td align="left" colspan="2">
															<form
															action="<%=response.encodeURL("/account/getaccountdetailaction.do")%>"
															method="post">
															<select name="aid" size="1">
															<% for (Iterator it = accounts.iterator();
															it.hasNext(); ) { 
																		AccountData data = (AccountData)it.next();
															%>
															<option value="<%=data.getPK().getID()%>"
															name="aid"><%=data.getAccountNumber().getAccountNumberString()%></option>
															<% } %>
															</select>
															&nbsp;
															<input type="submit" value="-Go-">

															</form>
														</td>
													</tr>
													<tr>
														<td align="left">
														<font class="formlabel">
															Account
														</font>
														</td>
														<td align="right">
															<font class="blackdataelement">
																<%=account.getData().getAccountNumber().getAccountNumberString()%>
															</font>
														</td>
													</tr>
													<tr>
														<td align="left" valign="top" bgcolor="#FFFFFF">
														<font class="formlabel">
															Billing Information
														</font>
														</td>
														<td align="right" bgcolor="#FFFFFF" valign="top">
															<font class="blackdataelement">
																<% if (billingContact == null) { %>
																	private
																<% } else { %>
																<%=billingContact.getContactName().getFirstName() +
																" " + billingContact.getContactName().getLastName()%>
																<BR>
																<%=billingContact.getAddress().getMailingAddress().getLine1()%><BR>
																<% if (billingContact.getAddress().getMailingAddress().getLine2() != null) { 
																	out.print(billingContact.getAddress().getMailingAddress().getLine2() + "<BR>");
																} %>
																<%=billingContact.getAddress().getMailingAddress().getCity() + ", "
																+ billingContact.getAddress().getMailingAddress().getState().toUpperCase()
																+ " " + billingContact.getAddress().getMailingAddress().getZipcode()%><BR>
																<%=billingContact.getPhoneNumber()%><BR>
																<a href="<%=response.encodeURL(
																	"/account/getbillablepartydetailaction.do?aid=" + account.getData().getPK().getID())%>"
																class="menulink" border="0">&lt;edit&gt;</a><BR>
																<a href="<%=response.encodeURL(
																	"/billing/getinvoicesaction.do?aid=" + account.getData().getPK().getID())%>"
																class="menulink" border="0">&lt;View Billing History&gt;</a>
															<% } %>
															</font>
														</td>
													</tr>
													<tr>
														<td align="left">
														<font class="formlabel">
															Billing Notification
														</font>
														</td>
														<td align="right">
															<font class="blackdataelement">
																<% if (billingContact == null) { %>
																private
																<% } else { %>
																<%=billingContact.getBillingDeliveryType().getName()%>
																<% } %>
															</font>
														</td>
													</tr>

													<tr>
														<td align="left" bgcolor="#FFFFFF">
														<font class="formlabel">
															Account Status	
														</font>
														</td>
														<td align="right" bgcolor="#FFFFFF">
															<font class="blackdataelement">
																<%=account.getData().getAccountStatus().getStatus()%>
															</font>
														</td>
													</tr>
													<tr>
														<td align="left">
														<font class="formlabel">
															Current Balance
														</font>
														</td>
														<td align="right">
															<font class="blackdataelement">
																<B>$<%=account.getCurrentBalance().getStringAmount()%></B>
															</font>
														</td>
													</tr>
													<tr>
														<td align="left" colspan="2" height="10" bgcolor="#FFFFFF">&nbsp;</td>
													<tr>
														<td align="left" colspan="2">
														<font class="bodyheading">
															Subscriptions:<BR><BR>
														</font>&nbsp;<font class="blackdataelement">Click
														on the subscription name to view/edit.</font>
														</td>
													</tr>
													<tr>
														<td align="left" colspan="2">
														<BR>
															<table border=0 cellpadding="2" cellspacing="0"  width="100%">
																<tr>
																	<td align="center">
																	&nbsp;
																	</td>
																	<td align="center">
																		<font class="blackformlabel">
																		Start
																		</font>
																	</td>
																	<td align="center">
																		<font class="blackformlabel">
																		Expires
																		</font>
																	</td>
																	<td align="center">
																		<font class="blackformlabel">
																		Billed
																		</font>
																	</td>
																	<td>&nbsp;</td>
																</tr>
																<% 
																	String colors[] = {"#FFFFFF", "#F5F5F5"};
																	int color = 0;
																	for (Iterator it = subscriptions.iterator();it.hasNext();) {
																		Subscription subscription =
																			(Subscription)it.next();
																%>
																<tr>
																	<td align="left" bgcolor="<%=colors[color]%>">
																		<a
																		href="<%=response.encodeURL("/account/getsubscriptiondetailaction.do?aid=" + 
																			account.getData().getPK().getID() + 
																			"&sid=" + 
																			subscription.getPK().getID())%>" class="menulink">
																			<%=subscription.getSubscriptionType().getName()%>
																		</a>
																	</td>
																	<td align="right" bgcolor="<%=colors[color]%>">
																		<font class="blackdataelement">
																		<%=sdf.format(subscription.getStartDate())%>
																		</font>
																	</td>
																	<td align="right" bgcolor="<%=colors[color]%>">
																		<font class="blackdataelement">
																		<% if (subscription.getExpirationDate() == null) { 
																					out.print("perpetual");
																			 } else {
																			 		out.print(sdf.format(subscription.getExpirationDate()));
																			 }
																		 %>
																		</font>
																	</td>
																	<td align="right" bgcolor="<%=colors[color]%>">
																		<font class="blackdataelement">
																		<%=subscription.getBillingCycle().getName()%>
																		</font>
																	</td>
																</tr>
																<% color = (color == 1) ? 0 : 1;
																} %>	
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
