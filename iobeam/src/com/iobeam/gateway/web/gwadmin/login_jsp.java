package com.iobeam.gateway.web.gwadmin;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.apache.jasper.runtime.*;

public class login_jsp extends HttpJspBase {


  private static java.util.Vector _jspx_includes;

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

      out.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"\n\"http://www.w3.org/TR/html4/loose.dtd\">\n");
      out.write("<html>\n");
      out.write("<head>\n");
      out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"/gwadmin/iobeam.css\"/>\n");
      out.write("<title>iobeam gateway administration");
      out.write("</title>\n");
      out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">\n");
      out.write("</head>\n\n");
      out.write("<body marginheight=0 leftmargin=0 topmargin=0 bgcolor=\"#FFFFFF\">\n");
      out.write("<table border=0 cellspacing=0 cellpadding=0 width=\"760\">\n");
      out.write("<tr>\n\t");
      out.write("<td align=\"left\" colspan=\"2\">");
      out.write("<img src=\"/gwadmin/images/inside_01.jpg\">\n\t");
      out.write("</td>\n");
      out.write("</tr>\n");
      out.write("<tr>\n\t");
      out.write("<td align=\"left\" colspan=\"2\">&nbsp;\n\t");
      out.write("</td>\n");
      out.write("</tr>\n");
      out.write("<tr>\n\t");
      out.write("<td align=\"center colspan=\"2\">\n\t\t");
      out.write("<table border=0 width=\"50%\" align=\"center\" cellpadding=\"2\" cellspacing=\"2\">\n\t\t\t");
      out.write("<form action=\"/gwadmin/logon\" method=\"post\">\n\t\t\t");
      out.write("<tr>\n\t\t\t\t");
      out.write("<td align=\"left\" colspan=\"2\">\t\n\t\t\t\t\t");
      out.write("<font class=\"newsbody\">Please enter username and password to enter the administration console.");
      out.write("</font>\n\t\t\t\t");
      out.write("</td>\n\t\t\t");
      out.write("</tr>\n\t\t\t");
      out.write("<tr> \n\t\t\t\t");
      out.write("<td align=\"left\">");
      out.write("<font class=\"newsbody\">User:");
      out.write("</font>\n\t\t\t\t");
      out.write("</td>\n\t\t\t\t");
      out.write("<td align=\"left\" valign=\"middle\">\n\t\t\t\t\t");
      out.write("<input type=\"text\" size=\"15\" maxlength=\"15\" name=\"username\">\n\t\t\t\t");
      out.write("</td>\n\t\t\t");
      out.write("</tr>\n\t\t\t");
      out.write("<tr>\n\t\t\t\t");
      out.write("<td align=\"left\">");
      out.write("<font class=\"newsbody\">Password:");
      out.write("</font>\n\t\t\t\t");
      out.write("</td>\n\t\t\t\t");
      out.write("<td align=\"left\" valign=\"middle\">\n\t\t\t\t\t");
      out.write("<input type=\"password\" name=\"password\" size=\"15\" maxlength=\"15\">\n\t\t\t\t");
      out.write("</td>\n\t\t\t");
      out.write("</tr>\n\t\t\t");
      out.write("<tr>\n\t\t\t\t");
      out.write("<td align=\"center\" colspan=\"2\">\n\t\t\t\t\t");
      out.write("<input type=\"submit\" value=\"  Login  \">\n\t\t\t\t");
      out.write("</td>\n\t\t\t");
      out.write("</tr>\n\t\t\t");
      out.write("</form>\n\t\t");
      out.write("</table>\n\t");
      out.write("</td>\n");
      out.write("</tr>\n");
      out.write("</table>\n");
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
