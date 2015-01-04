package com.edu.StudentShare;

//Import required java libraries
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import com.edu.StudentShare.Message.Mess;
import com.edu.StudentShare.Product.Prod;
import com.edu.StudentShare.Product.ProductData;
import com.edu.StudentShare.Redis.ConnectionPool;
import com.edu.StudentShare.Redis.OnlineUsers;
import com.edu.StudentShare.Redis.Products;
import com.edu.StudentShare.Redis.ShopptingCart;
import com.edu.StudentShare.Redis.VisitorsCounter;
import com.edu.StudentShare.Transaction.Transac;
import com.edu.StudentShare.User.User;
import com.edu.StudentShare.WishList.Wish;

import java.util.*;

import jersey.repackaged.org.objectweb.*;

//Implements Filter class
public class LogFilter implements Filter {
	public static Utils log;

	public void init(FilterConfig config) throws ServletException {
		// Get init parameter
		ConnectionPool pool = new ConnectionPool();

		String logPath = config.getInitParameter("log_path");

		log = new Utils(logPath);
		User user = new User();
		Prod pro = new Prod();
		Mess mess = new Mess();
		Transac transac = new Transac();
		Wish wish = new Wish();

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws java.io.IOException, ServletException {

		int id = Utils.retiveUserId((HttpServletRequest) request);
		StringBuffer requestURL = ((HttpServletRequest) request)
				.getRequestURL();
		String URL = requestURL.toString();

		if (!IsAllowedUrl(URL) && id == 0) {
			PrintWriter out = response.getWriter();
			out.println("Not connected!, please login or register First..");
			return;
		}

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
		if (page.equals("login.html") || page.equals("Register.html")
				|| page.equals("Connect") || page.equals("Register")) {
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
