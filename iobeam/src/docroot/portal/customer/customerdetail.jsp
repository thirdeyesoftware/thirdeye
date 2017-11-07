<%@page import="java.lang.Math,
com.iobeam.portal.task.actor.user.usersession.*,
com.iobeam.portal.ui.web.user.UserSessionHelper,
com.iobeam.portal.ui.web.customer.CustomerForm,
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

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

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

	BillingPeriod currentBillingPeriod = 
		BillingPeriod.getInstanceFor(new Date());

	request.setAttribute("countries", JSPUtil.getCountries());
	
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
										<td rowspan="2" align="left" valign="top" bgcolor="#f5f5f5"><span class="newsbody">
												<font class="bodyheading">My Information</font>
												<table border=0 cellpadding=4 cellspacing=2 width="100%" align="center">
													<tr>
														<td align="left">
															<html:errors/>
														</td>
													</tr>
													<html:form action="/customer/updatecustomeraction.do" method="post">
													<tr>
														<td align="left" valign="top" bgcolor="#f5f5f5">
															<font class="formlabel">
																Change Password
															</font>
														</td>
														<td align="center" bgcolor="#f5f5f5">
															<a href="/user/changepassword.jsp"
															class="formlink">Click Here</a>
														</td>
													</tr>
													<tr>
														<td valign="middle" align="left" valign="top" bgcolor="#f5f5f5">
														<font class="formlabel">
															First Name	
														</font>
														</td>
														<td align="left" bgcolor="#f5f5f5">
															<html:text  size="25" property="firstName"/>
														</td>
													</tr>
													
													<tr>
														<td align="left" valign="top" bgcolor="#FFFFFF"
														valign="middle">
														<font class="formlabel">
															Last Name	
														</font>
														</td>
														<td align="left" bgcolor="#ffffff">
															<html:text  size="25" property="lastName"/>
														</td>
													</tr>
													<tr>
														<td align="left" valign="top" bgcolor="#f5f5f5"
														valign="middle">
														<font class="formlabel">
															Address
														</font>
														</td>
														<td align="left" bgcolor="#f5f5f5">
															<html:text  size="25" property="address1"/>
														</td>
													</tr>
													<tr>
														<td align="left" valign="top" bgcolor="#FFFFFF"
														valign="middle">
														<font class="formlabel">
															
														</font>
														</td>
														<td align="left" bgcolor="#ffffff">
															<html:text  size="25" property="address2"/>
														</td>
													</tr>
													<tr>
														<td align="left" valign="top" bgcolor="#f5f5f5"
														valign="middle">
														<font class="formlabel">
															City, State
														</font>
														</td>
														<td align="left" bgcolor="f5f5f5">
															<html:text size="15" maxlength="30" property="city"/>&nbsp;
															<html:text size="4" maxlength="2" property="state"/>&nbsp;
														</td>
													</tr>
													<tr>
														<td align="left" valign="top" bgcolor="#ffffff"
														valign="middle">
														<font class="formlabel">
															Zipcode
														</font>
														</td>
														<td align="left" bgcolor="#ffffff">
															<html:text size="8" maxlength="11" property="zipcode"/>
														</td>
													</tr>

														</td>
													</tr>
													<tr>
														<td align="left" width="150" bgcolor="#f5f5f5"
														valign="middle">
															<font class="formlabel">
																Country
															</font>
														</td>
														<td align="left" bgcolor="#f5f5f5">
															<html:select property="countryId" size="1">
																<html:options collection="countries" 
																	property="value" labelProperty="label"/>
															</html:select>
														</td>
													</tr>
													
													<tr>
														<td align="left" valign="top" bgcolor="#FFFFFF"
														valign="middle">
														<font class="formlabel">
															Phone Number
														</font>
														</td>
														<td align="left" bgcolor="#ffffff">
															<html:text  size="25" property="phoneNumber"/>
														</td>
													</tr>
													
													<tr>
														<td align="left" valign="top" bgcolor="#f5f5f5"
														valign="middle">
														<font class="formlabel">
															Fax Number
														</font>
														</td>
														<td align="left" bgcolor="#f5f5f5">
															<html:text  size="25" property="faxNumber"/>
														</td>
													</tr>
													
													<tr>
														<td align="left" valign="top" bgcolor="#FFFFFF"
														valign="middle">
														<font class="formlabel">
															Email Address
														</font>
														</td>
														<td align="left" bgcolor="#ffffff">
															<html:text  size="30" property="emailAddress"/>
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
