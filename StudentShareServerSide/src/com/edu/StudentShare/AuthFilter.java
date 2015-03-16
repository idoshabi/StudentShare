package com.edu.StudentShare;

//Import required java libraries
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import com.edu.StudentShare.Message.Mess;
import com.edu.StudentShare.Product.Prod;
import com.edu.StudentShare.Redis.ConnectionPool;
import com.edu.StudentShare.Transaction.Transac;
import com.edu.StudentShare.User.User;
import com.edu.StudentShare.WishList.Wish;

import java.sql.SQLException;
import java.util.*;


//Implements Filter class
public class AuthFilter implements Filter {
	public static Utils log;

	public void init(FilterConfig config) throws ServletException {
		// Get init parameter
		ConnectionPool pool = new ConnectionPool();
		
		String logPath = config.getInitParameter("log_path");
		log = new Utils(logPath);

		StaticInit();

	}

	private void StaticInit() {
		DBConn db = new DBConn();
		User user = new User();
		Prod pro = new Prod();
		Mess mess = new Mess();
		Transac transac = new Transac();
		Wish wish = new Wish();
	}

	public void doFilter(ServletRequest request, ServletResponse resp,
			FilterChain chain) throws java.io.IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) resp;
		response.setHeader("Access-Control-Allow-Origin", "http://localhost:63342");
		
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");

		
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
		
		int id = Utils.retiveUserId((HttpServletRequest) request);
		StringBuffer requestURL = ((HttpServletRequest) request)
				.getRequestURL();
		String URL = requestURL.toString();/*
		if (!IsAllowedUrl(URL) && id == 0) {
			PrintWriter out = response.getWriter();
			response.sendError(response.SC_FORBIDDEN , "Not connected!, please login or register First..");
			
			return;
		}*/
		try {
			if (DBConn.conn.isClosed() || !DBConn.conn.isValid(0)) {
				StaticInit();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// FixIt

		// Get the IP address of client machine.
		String ipAddress = request.getRemoteAddr();
		// VisitorsCounter.NewVisit(ipAddress);
		// Log the IP address and current timestamp.
		System.out.println("IP " + ipAddress + ", Time "
				+ new Date().toString());

		// Pass request back down the filter chain
		chain.doFilter(request, response);

	}
	private Boolean IsAllowedUrl(String url) {
		String[] bits = url.split("/");
		String page = bits[bits.length - 1];
		if (page.equals("index.html") ||page.equals("login.html") || page.equals("Register.html")
				|| page.equals("Connect") || page.equals("Register")|| page.equals("getIdByUsername")
				|| page.equals("StudentShareProject")|| page.equals("Login")
				|| page.equals("StudentShare")) {
			return true;
		}
		String extension = page.substring(page.lastIndexOf("."));

		if (!extension.equals(".html")&& !extension.equals(""))
		{
			return true;
		}
		return false;
	}

	public void destroy() {
		/*
		 * Called before the Filter instance is removed from service by the web
		 * container
		 */
	}
}
