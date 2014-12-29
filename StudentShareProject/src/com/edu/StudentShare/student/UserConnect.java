package com.edu.StudentShare.student;

import java.io.IOException;
import java.net.URL;

import com.edu.StudentShare.LogFilter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UserConnect
 */
public class UserConnect extends HttpServlet {
	@Override
	public void destroy() {
		LogFilter.log.dispose();
	}

	private static final long serialVersionUID = 1L;
       UserDataJDBC userJdbc;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserConnect() {
        super();
        
    }
    public void init()  throws ServletException
    {
    	userJdbc = new UserDataJDBC("users");
    	 System.out.println("bookDataJDBCTemplate="+userJdbc);
    	 userJdbc.createTable();
    	System.out.println("table created");
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		try {
			//String url = request.getRequestURL().toString();
			//String fileNameWithoutExtn = url.substring(url.lastIndexOf('/')+1);
			String option1= request.getParameter("pWidth");

			switch (option1){
				case "UserOptions":
					String oldPassword= request.getParameter("password");
					String newPassword = request.getParameter("username");
					String confirmPassword = request.getParameter("username");
					if (newPassword == confirmPassword)
					{
					LogFilter.log.info("changed password="+oldPassword+", password="+newPassword);
					if (oldPassword !=null && newPassword !=null && confirmPassword !=null)
						userJdbc.changePassword(2, oldPassword, newPassword);
					}
					break;
				case "UserConnect":
					String user = request.getParameter("username");
					String pwd= request.getParameter("password");
					LogFilter.log.info("connect with user="+user+", password="+pwd);
					if (user !=null && pwd !=null)
						userJdbc.connect(user, pwd);
					
			}
		} catch (Exception e) {
			LogFilter.log.error("Failed at user" + e );
		}
	
				
	}

}
