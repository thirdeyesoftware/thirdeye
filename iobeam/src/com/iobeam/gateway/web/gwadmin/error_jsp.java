package com.iobeam.gateway.web.gwadmin;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.apache.jasper.runtime.*;
import java.util.*;
import com.iobeam.gateway.router.*;
import com.iobeam.gateway.util.*;
import com.iobeam.gateway.web.admin.AdminLogon;

public class error_jsp extends HttpJspBase {


  private static java.util.Vector _jspx_includes;

  static {
    _jspx_includes = new java.util.Vector(2);
    _jspx_includes.add("/gwadmin/includes/menu.html");
    _jspx_includes.add("/gwadmin/includes/footer.html");
  }

  public java.util.List getIncludes() {
    return _jspx_includes;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    JspFactory _jspxFactory = null;
    javax.servlet.jsp.PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;


    try {
      _jspxFactory = JspFactory.getDefaultFactory();
      response.setContentType("text/html;charset=ISO-8859-1");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\n");

	String msg = null;
	if (request.getAttribute("msg") == null) {
		msg = "";
	}
	else {
		msg = (String)request.getAttribute("msg");
	}

      out.write("\n\n");
      out.write("<html>\n");
      out.write("<head>\n");
      out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"/gwadmin/iobeam.css\">j\n");
      out.write("<title>\n\tVenue ");
      out.print(System.getProperty("iobeam.gateway.venue.id"));
      out.write("\n");
      out.write("</title>\n");
      out.write("</head>\n");
      out.write("<body marginheight=0 leftmargin=0 topmargin=0 bgcolor=\"#FFFFFF\"\nbackground=\"/gwadmin/images/insideBkgd.jpg\">\n");
      out.write("<table border=0 cellspacing=0 cellpadding=0 width=\"760\">\n");
      out.write("<tr>\n\t");
      out.write("<td align=\"left\" colspan=\"2\">");
      out.write("<img src=\"/gwadmin/images/inside_01.jpg\">\n\t");
      out.write("</td>\n");
      out.write("</tr>\n");
      out.write("<tr>\n\t");
      out.write("<td align=\"center\" width=\"180\">\n\t\t");
      out.write("<table border=0 cellpadding=0 cellspacing=2 width=\"70%\">\n");
      out.write("<tr>\n\t");
      out.write("<td align=\"left\">\n\t\t");
      out.write("<a href=\"/gwadmin/admin.jsp\">\n\t\t");
      out.write("<font class=\"linkfont\">Settings");
      out.write("</font>");
      out.write("</a>\n\t");
      out.write("</td>\n");
      out.write("</tr>\n");
      out.write("<tr>\n\t");
      out.write("<td align=\"left\">\n\t\t");
      out.write("<a href=\"/admincontrol?type=leases\">");
      out.write("<font class=\"linkfont\">\n\t\tActive Leases");
      out.write("</font>");
      out.write("</a>\n\t");
      out.write("</td>\n");
      out.write("</tr>\n\n");
      out.write("<tr>\n\t");
      out.write("<td align=\"left\">\n\t\t");
      out.write("<a href=\"/admincontrol?type=tasks\">");
      out.write("<font class=\"linkfont\">\n\t\tActive Tasks");
      out.write("</font>");
      out.write("</a>\n\t");
      out.write("</td>\n");
      out.write("</tr>\n\n");
      out.write("<tr>\n\t");
      out.write("<td align=\"left\">\n\t\t");
      out.write("<a href=\"/admincontrol?type=history\">");
      out.write("<font class=\"linkfont\">\n\t\tActivity Log");
      out.write("</font>");
      out.write("</a>\n\t");
      out.write("</td>\n");
      out.write("<tr>\n\t");
      out.write("<td align=\"left\">\n\t\t");
      out.write("<form action=\"/gwadmin/reset\" method=\"post\">\n\t\t");
      out.write("<input type=\"hidden\" name=\"type\" value=\"reboot\">\n\t\t");
      out.write("<input type=\"submit\" value=\"  Reboot Gateway  \">\n\t\t");
      out.write("</form>\n\t");
      out.write("</td>\n");
      out.write("</tr>\n");
      out.write("</table>\n");
      out.write("\n\t");
      out.write("</td>\n\t");
      out.write("<td valign=\"top\" align=\"left\">");
      out.write("<font face=\"verdana\" size=\"2\">\nAn error occured while attempting to provide you access to the iobeam wireless\nservice.  The administrator has been notified.  If you have any questions, you\nmay call us at (678)268-4095 or email us at customersupport@iobeam.com.\n");
      out.write("</font>");
      out.write("<BR>");
      out.write("<BR>\n");
      out.write("<font face=\"verdana\" size=2>");
      out.write("<B>Error:");
      out.write("</B>&nbsp;\n");
      out.print(msg);
      out.write("\n");
      out.write("</font>\n\n\n");
      out.write("</tr>\n");
      out.write("</table>\n");
      out.write("<BR>");
      out.write("<BR>");
      out.write("<BR>\n");
      out.write("<BR>\n");
      out.write("\t\t\t\t\t");
      out.write("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"\n\t\t\t\t\talign=\"center\">\n\t\t\t\t\t\t");
      out.write("<tr>\n\t\t\t\t\t\t\t");
      out.write("<td colspan=\"2\">\n\t\t\t\t\t\t\t");
      out.write("<BR>\n\t\t\t\t\t\t\t");
      out.write("</td>\n\t\t\t\t\t\t");
      out.write("</tr>\n\t\t\t\t\t\t");
      out.write("<tr>\n\t\t\t\t\t\t\t");
      out.write("<td width=\"50\">");
      out.write("</td>\n\t\t\t\t\t\t\t");
      out.write("<td align=\"center\">");
      out.write("<font face=\"verdana\" size=1>copyright&copy;2003-2004\n\t\t\t\t\t\t\tiobeam, Inc.");
      out.write("</font>");
      out.write("</td>\n\t\t\t\t\t\t");
      out.write("</tr>\n\t\t\t\t\t");
      out.write("</table>\n\n");
      out.write("\n");
      out.write("</body>\n");
      out.write("</html>\n\n");
    } catch (Throwable t) {
      out = _jspx_out;
      if (out != null && out.getBufferSize() != 0)
        out.clearBuffer();
      if (pageContext != null) pageContext.handlePageException(t);
    } finally {
      if (_jspxFactory != null) _jspxFactory.releasePageContext(pageContext);
    }
  }
}
