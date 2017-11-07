<%@page info="Iobeam conf" import="java.text.*, java.util.*"%>
<%
	Hashtable table = new Hashtable();
	
	table.put("108eam", "vc_testcenter");
	table.put("newyork", "newyork");
	table.put("losangeles", "losangeles");
	table.put("atlanta", "atlanta");
	
	String room = request.getParameter("room");
	
	if (room == null || room.trim().equals("")) {
		response.sendRedirect("index.jsp");
	}
	String password = request.getParameter("password");

	if (table.containsKey(password)) {
		room = (String)table.get(password);
	}
	else {
		response.sendRedirect("index.jsp");
	}
	String app = "vc_testcenter";

%>

<HTML>
<HEAD>
<meta http-equiv=Content-Type content="text/html;  charset=ISO-8859-1">
<TITLE>Iobeam Conference Service Test Center</TITLE>
</HEAD>
<BODY bgcolor="#FFFFFF">
<!-- URL's used in the movie-->
<!-- text used in the movie-->
<!--<P ALIGN="LEFT"></P>HelloText Color--><OBJECT classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
 codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,0,0"
 WIDTH="670" HEIGHT="570" id="tisvc" ALIGN="">
<PARAM NAME="FlashVars"
value="app=<%=app%>&uname=<%=request.getParameter("handle")%>&room=<%=room%>"> <PARAM
NAME=movie VALUE="testcenter.swf"> <PARAM NAME=quality VALUE=high> <PARAM
NAME=bgcolor VALUE=#FFFFFF> <EMBED src="testcenter.swf" quality=high bgcolor=#FFFFFF  WIDTH="670" HEIGHT="570" NAME="tisvc" ALIGN=""
 TYPE="application/x-shockwave-flash"
 FlashVars="uname=<%=request.getParameter("handle")%>&room=<%=room%>&app=<%=app%>" PLUGINSPAGE="http://www.macromedia.com/go/getflashplayer"></EMBED>
</OBJECT>
</BODY>
</HTML>
