package com.edu.StudentShare;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ScopeServletContext
 */
@WebServlet("/ScopeServletContext")

public class ScopeServletContext extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ScopeServletContext() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 res.setContentType("text/plain");
		    PrintWriter out = res.getWriter();

		    ServletContext context = getServletContext();
		    String val = (String)context.getAttribute("key1");
		    if (val != null)
		    	out.println("context.getAttribute="+val);
		    else
		    	out.println("context.getAttribute=null");
		    out.println("req.getServerName(): " + req.getServerName());
		    out.println("req.getServerPort(): " + req.getServerPort());
		    out.println("context.getServerInfo(): " + context.getServerInfo());
		    out.println("getServerInfo() name: " + getServerInfoName(context.getServerInfo()));
		    out.println("getServerInfo() version: " + getServerInfoVersion(context.getServerInfo()));
		    out.println("context.getAttributeNames():");
		   Enumeration<String> e = context.getAttributeNames();
		    while (e.hasMoreElements()) {
		      String name = (String) e.nextElement();
		      out.println("  context.getAttribute(\"" + name + "\"): " + context.getAttribute(name));
		    }
		    context.setAttribute("key1", "val1");
	}

	 private String getServerInfoName(String serverInfo) {
		    int slash = serverInfo.indexOf('/');
		    if (slash == -1)
		      return serverInfo;
		    else
		      return serverInfo.substring(0, slash);
		  }

		  private String getServerInfoVersion(String serverInfo) {
		    // Version info is everything between the slash and the space
		    int slash = serverInfo.indexOf('/');
		    if (slash == -1)
		      return null;
		    int space = serverInfo.indexOf(' ', slash);
		    if (space == -1)
		      space = serverInfo.length();
		    return serverInfo.substring(slash + 1, space);
		  }
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
