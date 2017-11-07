<%@page import="java.lang.Math,
com.iobeam.portal.task.actor.user.usersession.*,
com.iobeam.portal.ui.web.user.UserSessionHelper,
com.iobeam.portal.ui.web.user.signup.SignupUserForm,
com.iobeam.portal.model.venue.*,
com.iobeam.portal.util.*,
com.iobeam.portal.task.customer.setupuser.*,
com.iobeam.portal.model.prototype.subscription.*,
javax.ejb.*,
javax.naming.*,
java.rmi.*,
java.util.*"%>

<%
	InitialContext ic = new InitialContext();
	SetupUserHome home = (SetupUserHome)ic.lookup(SetupUserHome.JNDI_NAME);
	SetupUser su = home.create();
	Collection protos = su.getPublicMemberSubscriptionPrototypes();
	Vector v = new Vector();
	for (Iterator it = protos.iterator(); it.hasNext();) {
		SubscriptionPrototype sp = 
			(SubscriptionPrototype)it.next();
		String label = sp.getDescription() + " - $" +
			sp.getCostPerBillingCycle().getStringAmount();
		String value = String.valueOf(sp.getPK().getID());
		v.addElement( new Pair(label, value) );
	}

	SignupUserForm form = (SignupUserForm)request.getAttribute("SignupUserForm");
	if (form == null) {
		form = new SignupUserForm();
	}

	request.setAttribute("protos",v);

%>

<%@include file="../includes/usersession.html"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%
	request.setAttribute("months",JSPUtil.getMonths());
	request.setAttribute("years", JSPUtil.getYears());
	request.setAttribute("countries", JSPUtil.getCountries());

%>

<html>

	<head>
		<title>iobeam : wireless where you need it :</title>
		<link rel="stylesheet" href="/iobeam.css" type="text/css"/>
		<%@include file="/js/functions.js"%>
		<meta http-equiv="content-type" content="text/html;charset=ISO-8859-1">

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
				<td valign="top" align="left"><img src="/images/inside/headlineSignUp.jpg" width="597" height="36" border="0"><br>
					<table border="0" cellmargin="0" cellspacing="0" width="98%">
						<tr>
							<td width="25"></td>
							<td>
								<p><BR>
								<span class="newsbody">
								Enter the following pieces of information.  When you are
								finished, click <B>Subscribe</B>.  If you any questions about
								this sign up form, please email <a 
								href="mailto:sales@iobeam.com"><font class="linktext">sales@iobeam.com</font></a>.
								<BR><BR>
							
							</td>
						</tr>


						<tr>
							<td width="25">&nbsp;</td>
							<td align="left" bgcolor="#dbdcde"/>
								<table bgcolor="#FFFFFF" border=0 cellpadding="4"
								cellspacing="2">
									<tr>
										<td align="left" colspan="2">
											<font class="fontinstructions">
											<html:errors/>
											</font>
										</td>
									</tr>
									<html:form action="/user/signupuseraction.do" method="post">
									
									<tr>
										<td colspan="2" align="left">
											<font class="forminstructions">
											If you have a subscription card from a retailer or a private venue,
											enter the 15-digit subscription number on the back of the card.
											Otherwise, choose a subscription from the list available.
										</td>
									</tr>
									<tr>
										<td align="left" bgcolor="#dbdcde">
											<font class="formlabel">Subscription Number</font>
										</td>
										<td align="left" bgcolor="#dbdcde">
											<html:text property="secureId" size="20"/>
										</td>
									</tr>
<% if (form.getSecureId() == null || form.getSecureId().trim().equals("")) { %>								
									<!-- BEGIN SUBSCRIPTION TYPE -->
									<tr>
										<td align="left" bgcolor="#dbdcde">
											<font class="formlabel">Subscription Type</font>
										</td>
										<td align="left" bgcolor="#dbdcde">
											<table border="0" cellpadding=1 cellspacing=1
													bgcolor="#dbdcde">
												<% for (Iterator it = v.iterator();it.hasNext();)
												{ 
														Pair p = (Pair)it.next();
												%>
													<td align="left">
														<input type="radio" value="<%=p.getValue()%>"
															name="subscriptionPrototypeId">
														<font class="forminstructions">
														<%=p.getLabel()%>
														</font>
													</td>
												</tr>
												<% } %>
											</table>
										</td>
									</tr>
<% } %>											
									<tr>
										<td align="left" colspan="2">
											<font class="forminstructions">
											Please enter a user name and password.  Your password
											must be at least 6 characters in length.  i<i>o</i>beam
											suggests you use a password that contains numbers and
											letters.  Items with an <font class="bodyheadingsmall">*</font>
											are required fields.
											</font>
										</td>
									</tr>
									
									<tr>
										<td align="left" width="150" bgcolor="#dbdcde">
											<font class="formlabel">
											User Name *
											</font>
										</td>
										<td align="left" bgcolor="#dbdcde">
											<html:text property="username" size="20" maxlength="30"/>
										</td>
									</tr>

									<tr>
										<td align="left" width="150" bgcolor="#dbdcde">
											<font class="formlabel">
												Password *
											</font>
										</td>
										<td align="left" bgcolor="dbdcde">
											<html:password property="password" size="20" maxlength="30"/>
										</td>
									</tr>

									<tr>
										<td align="left" width="150" bgcolor="#dbdcde">
											<font class="formlabel">
												Password (confirm) *
											</font>
										</td>
										<td align="left" bgcolor="#dbdcde">
											<html:password property="passwordConfirm" size="20"
												maxlength="30"/>
										</td>
									</tr>
									<tr>
										<td align="left" width="150" bgcolor="#dbdcde">
											<font class="formlabel">
												What city were you born in? *
											</font>
										</td>
										<td align="left" bgcolor="#dbdcde">
											<html:text property="passwordReminderAnswer" size="20"
												maxlength="30"/>
										</td>
									</tr>
									<tr>
										<td align="left" width="150" bgcolor="#dbdcde">
											<font class="formlabel">
												First Name *
											</font>
										</td>
										<td align="left" bgcolor="#dbdcde">
											<html:text property="firstName" size="20"
											maxlength="20"/>
										</td>
									</tr>
									
									<tr>
										<td align="left" width="150" bgcolor="#dbdcde">
											<font class="formlabel">
												Last Name *
											</font>
										</td>
										<td align="left" bgcolor="#dbdcde">
											<html:text property="lastName" size="20"
											maxlength="20"/>
										</td>
									</tr>
									
									<tr>
										<td align="left" width="150" bgcolor="#dbdcde">
											<font class="formlabel">
												Phone Number *
											</font>
										</td>
										<td align="left" bgcolor="#dbdcde">
											<html:text property="phoneNumber" size="20"
											maxlength="13"/>
										</td>
									</tr>
									
									<tr>
										<td align="left" width="150" bgcolor="#dbdcde">
											<font class="formlabel">
												Fax Number
											</font>
										</td>
										<td align="left" bgcolor="#dbdcde">
											<html:text property="faxNumber" size="20"
											maxlength="13"/>
										</td>
									</tr>
									
									<tr>
										<td align="left" width="150" bgcolor="#dbdcde">
											<font class="formlabel">
												Email Address	*
											</font>
										</td>
										<td align="left" bgcolor="#dbdcde">
											<html:text property="emailAddress" size="30"
											maxlength="50"/>
										</td>
									</tr>
									
									<tr>
										<td align="left" width="150" bgcolor="#dbdcde">
											<font class="formlabel">
											Address *
											</font>
										</td>
										<td align="left" bgcolor="#dbdcde">
											<html:text property="address1" size="20" maxlength="30"/>
										</td>
									</tr>

									<tr>
										<td align="left" width="150" bgcolor="#dbdcde">
											<font class="formlabel">
											&nbsp;	
											</font>
										</td>
										<td align="left" bgcolor="#dbdcde">
											<html:text property="address2" size="20" maxlength="30"/>
										</td>
									</tr>

									<tr>
										<td align="left" width="150" bgcolor="#dbdcde">
											<font class="formlabel">
											City, State Zipcode *
											</font>
										</td>
										<td align="left" bgcolor="#dbdcde">
											<html:text property="city" size="20"
											maxlength="30"/>&nbsp;<html:text property="state"
											size="3" maxlength="2"/>&nbsp;<html:text
											property="zipcode" size="8" maxlength="11"/>
										</td>
									</tr>
									
									<tr>
										<td align="left" width="150" bgcolor="#dbdcde">
											<font class="formlabel">
											Country
											</font>
										</td>
										<td align="left" bgcolor="#dbdcde">
											<html:select property="countryId" size="1">
												<html:options collection="countries" property="value" labelProperty="label"/>
											</html:select>
										</td>
									</tr>
<% if (form.getSecureId() == null || form.getSecureId().trim().equals("")) { %>
						<script>
							function copybilling() {
								var form = document.forms["SignupUserForm"];
								if (form.copyBilling.value != "on") {
									return;
								}
								else {
									form.billingAddress1.value = 
									form.address1.value;
									form.billingAddress2.value = 	
									form.address2.value;
									form.billingCity.value = 
										form.city.value;
									form.billingState.value = 
										form.state.value;
									form.billingZipcode.value = 
										form.zipcode.value;
								}
							}
						</script>


								<!-- Start Billing Information -->	
									<tr>
										<td align="left" colspan="2" bgcolor="#ffffff">
											<font class="forminstructions">
												Billing Information
											</font>
										</td>
									</tr>
								
									<tr>
										<td align="left" width="150" bgcolor="#dbdcde">
											<font class="formlabel">
												Same as Above	
											</font>
										</td>
										<td align="left" bgcolor="#dbdcde">
											<input type="checkbox" value="on" name="copyBilling"
											onClick="copybilling()">
										</td>
									</tr>

									</tr>
									<tr>
										<td align="left" width="150" bgcolor="#dbdcde">
											<font class="formlabel">
												Address *
											</font>
										</td>
										<td align="left" bgcolor="#dbdcde">
											<html:text property="billingAddress1" size="20"
											maxlength="20"/>
										</td>
									</tr>
									<tr>
										<td align="left" width="150" bgcolor="#dbdcde">
											<font class="formlabel">
												
											</font>
										</td>
										<td align="left" bgcolor="#dbdcde">
											<html:text property="billingAddress2" size="20"
											maxlength="20"/>
										</td>
									</tr>
									
									<tr>
										<td align="left" width="150" bgcolor="#dbdcde">
											<font class="formlabel">
											City, State Zipcode *
											</font>
										</td>
										<td align="left" bgcolor="#dbdcde">
											<html:text property="billingCity" size="20"
											maxlength="30"/>&nbsp;<html:text property="billingState"
											size="3" maxlength="2"/>&nbsp;<html:text
											property="billingZipcode" size="8" maxlength="11"/>
										</td>
									</tr>

									<tr>
										<td align="left" width="150" bgcolor="#dbdcde">
											<font class="formlabel">
											Country
											</font>
										</td>
										<td align="left" bgcolor="#dbdcde">
											<html:select property="billingCountryId" size="1">
												<html:options collection="countries" property="value" labelProperty="label"/>
											</html:select>
										</td>
									</tr>
									
									<tr>
										<td align="left" colspan="2">
											<font class="forminstructions">
												Credit Card Information	
											</font>
										</td>
									</tr>
									<tr>
										<td align="left" width="150" bgcolor="#dbdcde">
											<font class="formlabel">
												Credit Card Number *
											</font>
										</td>
										<td align="left" bgcolor="#dbdcde">
											<html:text property="creditCardNumber" size="20"
											maxlength="16"/>
										</td>
									</tr>
									
									<tr>
										<td align="left" width="150" bgcolor="#dbdcde">
											<font class="formlabel">
												Security Code	*
											</font>
										</td>
										<td align="left" bgcolor="#dbdcde">
											<html:text property="securityCode" size="4"
											maxlength="4"/><font class="newsbody">&nbsp;3 digit code on
											back of card</font>
										</td>
									</tr>
									
									<tr>
										<td align="left" width="150" bgcolor="#dbdcde">
											<font class="formlabel">
												Card Holder Name *
											</font>
										</td>
										<td align="left" bgcolor="#dbdcde">
											<html:text property="cardHolderName" size="20"
											maxlength="16"/>
										</td>
									</tr>

									<tr>
										<td align="left" width="150" bgcolor="#dbdcde">
											<font class="formlabel">
												Expiration Date *
											</font>
										</td>
										<td align="left" bgcolor="#dbdcde">
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
										<td align="left" colspan="2" bgcolor="#ffffff"> &nbsp;
											<font class="forminstructions">
												You will not be billed until the end of the current
												billing cycle.
										</td>
									</tr>
								 <!-- END BILLING INFORMATION -->
<% } %>
									<tr>
									
									<tr>
										<td align="left" width="150" bgcolor="#dbdcde">
											<font class="forminstructions">
												I've read the <a href="terms.jsp" class="formlink" target="_NEW">Terms and Conditions</A>
											</font>
										</td>
										<td align="left" bgcolor="#dbdcde" align="left">
											<html:checkbox property="disclaimer"/>
										</td>
									</tr>
									<tr>
										<td colspan="2" bgcolor="#ffffff" align="center">
											<input type="submit" value="-- Subscribe --">
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
				<td valign="bottom" height="30" align="center">
					<%@include file="/includes/footer.html"%>

				</td>
			</tr>
		</table>
		<map name="inside_01bad41e41"><area shape="rect" coords="8,7,146,65" href="/index.jsp"></map>
	</body>

</html>
