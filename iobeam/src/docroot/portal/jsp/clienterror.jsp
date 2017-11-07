<%@page language="java" import="java.util.*"%>

<%
	String msg = (String)request.getSession(false).getAttribute("errormsg");
	request.getSession(false).removeAttribute("errormsg");
%>

<html>
<head>
<title>
	iobeam Portal Services - Client Error
</title>
<body bgcolor="#ffffff">
<center>
<font color="blue" size="2"><B>An error occured while attempting to process
your request.
</font>
</center>

<br>
<font face="verdana" size="2" color="#000000">
<%=msg
%>


</font>

</body>
</html>

