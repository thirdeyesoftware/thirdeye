<%@ page info="conference test"%>

<html>
<head>
<title> Iobeam Conference TestCenter </title>
</head>
<body bgcolor="#ffffff">
<font face="verdana" size=2>
<B>
<center>
iobeam Conference Service Test Center
</center>
</B>
</font>
<BR><BR>
<font face="verdana" size="1">
This service requires Flash Player version 6 step 69 or above.  
</font>
<BR><BR>
<BR>
<form action="testcenter.jsp" method="post">
<input type="hidden" value="039fhtw" name="pwd">
<table border=0 cellpadding=1 cellspacing=1>
<tr>
	<td><B><font face="verdana">Room</font></B></td>
	<td>
		<select name="room" size=1>
			<option value="testcenter">Test Center</option>
			<option value="newyork">New York</option>
			<option value="losangeles">Los Angeles</option>
			<option value="atlanta">Atlanta</option>
		</select>
	</td>
</tr>
<tr>
	<td><B><font face="verdana">User Name</font></B></td>
	<td>
		<input type="text" name="handle" value="">
	</td>
</tr>
<tr>
	<td><B><font face="verdana">Password</font></B></td>
	<td align="left">
		<input type="password" name="password" value="">
	</td>
</tr>
<tr>
	<td colspan=2 align="center">
	&nbsp;&nbsp;<input type="submit" value="Let's Conference">
	</td>
</tr>
</table>
</form>

</body>
</html>

