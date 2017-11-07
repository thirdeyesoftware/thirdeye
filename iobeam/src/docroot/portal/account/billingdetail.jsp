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

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<% /**
    * this jsp is the result of a forward from ui controller
		* GetAccountSummaryAction
		*/
	
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		
	Customer customer = usersession.getCustomer();
	AccountData accountData;
	BillableParty billableParty;
	CustomerContact billingContact;
	PaymentInstrument paymentInstrument;
	ActionErrors errors = new ActionErrors();
	Account account;
	BillingPeriod currentBillingPeriod = 
		BillingPeriod.getInstanceFor(new Date());
	
	request.setAttribute("months",JSPUtil.getMonths());
	request.setAttribute("years", JSPUtil.getYears());
	request.setAttribute("countries", JSPUtil.getCountries());

	try {

		customer = usersession.getCustomer();
		account = (Account)request.getAttribute("account");
		billableParty = account.getBillableParty();
		billingContact = billableParty.getBillingCustomerContact();
		paymentInstrument = billableParty.getPaymentInstrument();

	} 
	catch (Exception ee) {
		ee.printStackTrace();
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
												<font class="bodyheading">Billing Detail</font><BR><BR>
												<table border=0 cellpadding=2 cellspacing=0 width="100%">
													<tr>
														<td align="left">
															<font class="formlabel">
																For Account
															</font>
														</td>
														<td align="left">
															<font class="blackdataelement">
																<%=account.getData().getAccountNumber().getAccountNumberString()%>
															</font>
														</td>
													</tr>
													<html:form
													action="/account/updatebillablepartydetailaction.do"
													method="post">
													<input type="hidden"
													value="<%=account.getData().getPK().getID()%>"
													name="aid">
													<tr>
														<td align="left" BGCOLOR="#FFFFFF">
														<font class="formlabel" >
															First Name	
														</font>
														</td>
														<td align="left" bgcolor="#FFFFFF">
															<html:text property="firstName" size="20"
															maxlength="30"/>
														</td>
													</tr>
													<tr>
														<td align="left" valign="top" bgcolor="#f5f5f5">
														<font class="formlabel">
															Last Name	
														</font>
														</td>
														<td align="left" bgcolor="#f5f5f5" valign="top">
															<html:text property="lastName" size="20"
															maxlength="30"/>
														</td>
													</tr>
													<tr>
														<td align="left" valign="top" bgcolor="#FFFFFF">
														<font class="formlabel">
															Address
														</font>
														</td>
														<td align="left" bgcolor="#FFFFFF" valign="top">
															<html:text property="address1" size="30"
															maxlength="30"/>
														</td>
													</tr>
													<tr>
														<td align="left" valign="top" bgcolor="#FFFFFF">
														<font class="formlabel">
														</font>
														</td>
														<td align="left" bgcolor="#FFFFFF" valign="top">
															<html:text property="address2" size="30"
															maxlength="30"/>
														</td>
													</tr>
													<tr>
														<td align="left" valign="top" bgcolor="#f5f5f5">
														<font class="formlabel">
															City, State, Zipcode
														</font>
														</td>
														<td align="left" bgcolor="#f5f5f5" valign="top">
															<html:text property="city" size="20"
															maxlength="30"/>&nbsp;
															<html:text property="state" size="3"
															maxlength="2"/>&nbsp;
															<html:text property="zipcode" size="8"
															maxlength="10"/>
														</td>
													</tr>
													<tr>
														<td align="left" valign="top" bgcolor="#FFFFFF">
														<font class="formlabel">
														</font>
														</td>
														<td align="left" bgcolor="#FFFFFF" valign="top">
															<html:select property="countryId" size="1">
																<html:options collection="countries" 
																	property="value" labelProperty="label"/>
															</html:select>
														</td>
													</tr>
													<tr>
														<td align="left" valign="top" bgcolor="#f5f5f5">
														<font class="formlabel">
															Phone Number	
														</font>
														</td>
														<td align="left" bgcolor="#f5f5f5" valign="top">
															<html:text property="phoneNumber" size="20"
															maxlength="12"/>
														</td>
													</tr>
													<tr>
														<td align="left" valign="top" bgcolor="#FFFFFF">
														<font class="formlabel">
															Fax Number	
														</font>
														</td>
														<td align="left" bgcolor="#FFFFFF" valign="top">
															<html:text property="faxNumber" size="20"
															maxlength="12"/>
														</td>
													</tr>
													<tr>
														<td align="left" valign="top" bgcolor="#f5f5f5">
														<font class="formlabel">
															Email Address
														</font>
														</td>
														<td align="left" bgcolor="#f5f5f5" valign="top">
															<html:text property="emailAddress" size="20"
															maxlength="50"/>
														</td>
													</tr>
													<tr>
														<td align="left" valign="top" bgcolor="#FFFFFF">
														<font class="formlabel">
															Card Holder Name	
														</font>
														</td>
														<td align="left" bgcolor="#FFFFFF" valign="top">
															<html:text property="cardHolderName" size="30"
															maxlength="50"/>
														</td>
													</tr>
													<tr>
														<td align="left" valign="top" bgcolor="#f5f5f5">
														<font class="formlabel">
															Credit Card Number
														</font>
														</td>
														<td align="left" bgcolor="#f5f5f5" valign="top">
															<html:text property="creditCardNumber" size="20"
															maxlength="50"/>
														</td>
													</tr>
													<tr>
														<td align="left" valign="top" bgcolor="#FFFFFF">
														<font class="formlabel">
															Security Code
														</font>
														</td>
														<td align="left" bgcolor="#FFFFFF" valign="top">
															<html:text property="securityCode" size="4"
															maxlength="4"/>
														</td>
													</tr>
													<tr>
														<td align="left" valign="top" bgcolor="#f5f5f5">
														<font class="formlabel">
															Expiration Date
														</font>
														</td>
														<td align="left" bgcolor="#f5f5f5" valign="top">
															<html:select property="expirationMonth" size="1">
																<html:options collection="months" property="value"
																	labelProperty="label"/>
															</html:select>
															&nbsp;	
															<html:select property="expirationYear" size="1">
																<html:options collection="years" property="value"
																	labelProperty="label"/>
															</html:select>
														</td>
													</tr>
													<tr>
														<td align="center" colspan="2">
															<input type="submit" value="--Update--">
														</td>
													</tr>
													</html:form>
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
