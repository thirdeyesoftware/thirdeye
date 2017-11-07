<%@page import="java.lang.Math,
com.iobeam.portal.task.actor.user.usersession.*,
com.iobeam.portal.ui.web.user.UserSessionHelper,
com.iobeam.portal.model.venue.*,
com.iobeam.portal.model.account.*,
com.iobeam.portal.model.customer.*,
com.iobeam.portal.model.customercontact.*,
com.iobeam.portal.model.invoice.*,
com.iobeam.portal.model.billing.*,
com.iobeam.portal.util.*,
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
	Vector accounts = new Vector(customer.getAccounts());
	Collection invoices = (Collection)request.getAttribute("invoices");
	AccountData accountData = (AccountData)request.getAttribute("accountdata");
	CustomerContact contact = customer.getCustomerContact();
	BillingPeriod currentBillingPeriod = 
		BillingPeriod.getInstanceFor(new Date());
	
%>

<html><head>
		<meta http-equiv="content-type" content="text/html;charset=ISO-8859-1"><title>iobeam : wireless where you need it :</title>
		<meta http-equiv="Expires" content="Sun, June 10, 1972 14:59:00 GMT">
		<meta http-equiv="Pragma" content="no-cache">
		
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
							<td>
								<table border="0" cellpadding="4" cellspacing="0" width="560">
									<tbody><tr>
										<td bgcolor="#2d4470" valign="middle"><span class="adminheadline"><B>
											<%=contact.getContactName().getFirstName() + " "+
												contact.getContactName().getLastName()%></B><BR>
											</span><span class="adminreversebold"> <B>iobeam&#8482; member since:
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
									<TR height="5">
										<td height="5">
											<%@include file="../includes/portalmenu.html"%>
										</td>
										<td align="left" height="5">
										</td>
										<td align="left" height="5">
										</td>
									</tr>
									<tr height="20">
										<td rowspan="2" valign="top" bgcolor="#f5f5f5"><span class="newsbody">
													<table border=0 cellpadding=2 cellspacing=2
															width="100%">
														<tr>
															<td align="left" colspan="4">
																<font class="bodyheading">
																	Billing History
																</font>
															</tr>
														</tr>

														<% if (accounts.size() > 1) { %>
														<tr>
															<td align="left" colspan="4">
																<font class="formlabel">
																	Change Account
																</font>
															</td>
														</tr>
														<tr>
															<td align="left" colspan="4">
																<form
																	action="<%=response.encodeURL("/billing/getinvoicesaction.do")%>"
																	method="post">
																<select name="aid" size="1">
																<% for (Iterator iter = accounts.iterator();
																			iter.hasNext(); ) { 
																		AccountData data = (AccountData)iter.next();
																%>
																<option value="<%=data.getPK().getID()%>"
																	name="aid"><%=data.getAccountNumber().getAccountNumberString()%></option>
																<% } %>
																</select>
																&nbsp;
																<input type="submit" value="-Go-">
															</td>
														</tr>
														<% } else { %>
														<tr>
															<td colspan="4">&nbsp;</td>
														</tr>

														<% } %>

														<tr>
															<td align="left">
																<font class="formlabel">
																	Account
																</font>
															</td>
															<td align="left" colspan="3">
																<font class="blackdataelement">
																	<%=accountData.getAccountNumber().getAccountNumberString()%>
																</font>
															</td>
														</tr>
														<tr>
															<td colspan="4">
															&nbsp;
															</td>
														</tr>
														<tr>
															<td align="left">
																<font class="formlabel">
																	Date
																</font>
															</td>
															<td align="left">
																<font class="formlabel">
																	Invoice Number	
																</font>
															</td>
															<td align="left">
																<font class="formlabel">
																	Amount 
																</font>
															</td>
															<td align="left">
																<font class="formlabel">
																	Status
																</font>
															</td>
														</tr>
														<% for (Iterator it = invoices.iterator();
																	it.hasNext(); ) { 
																Invoice invoice = (Invoice)it.next();
																String url =
																	response.encodeURL("/billing/getinvoicedetailaction.do?iid=" +
																		invoice.getPK().getID() + "&aid=" +
																		accountData.getPK().getID());
														%>
														<tr>
															<td align="left">
																<font class="blackdataelement">
																	<a href="<%=url%>" class="menulink"><%=sdf.format(invoice.getInvoiceDate())%></a>
																</font>
															</td>
															<td align="left">
																<font class="blackdataelement">
																	<a class="menulink" href="<%=url%>"><%=invoice.getInvoiceNumber()%></a>
																</font>
															</td>
															<td align="left">
																<font class="blackdataelement">
																	<a class="menulink" href="<%=url%>"><%=invoice.getTotalDue().getFormattedStringAmount()%></a>
																</font>
															</td>
															<td align="left">
																<font class="blackdataelement">
																	<a class="menulink" href="<%=url%>"><%=invoice.getInvoiceStatus().getName()%></a>
																</font>
															</td>
														</tr>
														<% } %>

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
