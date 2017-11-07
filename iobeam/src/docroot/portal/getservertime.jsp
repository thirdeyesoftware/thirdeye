<%@page info="no service page" import="java.util.Date,
						java.text.SimpleDateFormat" %>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss Z");
%>
<%=sdf.format(new Date())%>

