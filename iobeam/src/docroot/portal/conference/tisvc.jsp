<%@page info="Iobeam conf" import="java.text.*, java.util.Date"%>
<%
	if (!"nexnet".equals(request.getParameter("password"))) {
		response.sendRedirect(response.encodeRedirectURL("conference.jsp"));
	}
%>

<HTML>
<HEAD>
<meta http-equiv=Content-Type content="text/html;  charset=ISO-8859-1">
<TITLE>Iobeam Conference Service Test Center</TITLE>
</HEAD>
<BODY bgcolor="#FFFFFF">
<!-- URL's used in the movie-->
<!-- text used in the movie-->
<!--<P ALIGN="LEFT"></P>HelloText Color-->
<OBJECT classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
 codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,0,0"
  WIDTH="550" HEIGHT="450" id="tisvc202" ALIGN="">
	 <PARAM NAME=FlashVars VALUE="uname=<%=request.getParameter("handle")%>&room=tisconf">
	  <PARAM NAME=movie VALUE="tisvc202.swf"> <PARAM NAME=quality VALUE=high>
		<PARAM NAME=bgcolor VALUE=#CCCCCC> <EMBED src="tisvc202.swf" quality=high
		bgcolor=#CCCCCC  WIDTH="550" HEIGHT="450" NAME="tisvc202" ALIGN=""
		 TYPE="application/x-shockwave-flash"
		 FLASHVARS="uname=<%=request.getParameter("handle")%>&room=tisconf"
		 PLUGINSPAGE="http://www.macromedia.com/go/getflashplayer"></EMBED>
		 </OBJECT>
</BODY>
</HTML>
