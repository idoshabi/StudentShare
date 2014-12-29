package com.edu.StudentShare.student;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.edu.StudentShare.LogFilter;
import com.edu.StudentShare.Utils;
/**
 * Servlet implementation class UserRegister
 */
public class UserRegister extends HttpServlet {
	private static final long serialVersionUID = 1L;
    UserDataJDBC userJdbc;
    public void init()  throws ServletException
    {
    	userJdbc = new UserDataJDBC("users");
    	 System.out.println("bookDataJDBCTemplate="+userJdbc);
    	 userJdbc.createTable();
    	System.out.println("table created");
    	LogFilter.log = new Utils("c:\\log\\test.log");
    }
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserRegister() {

        super();
        // TODO Auto-generated constructor stub
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
		// TODO Auto-generated method stub
		String user = request.getParameter("username");
		String first_Name = request.getParameter("first_name");
		String last_Name = request.getParameter("last_name");
		String email = request.getParameter("email");
		String birthday1 = request.getParameter("birthday");
		String password= request.getParameter("password");
		String confirm_pwd= request.getParameter("confirm_pwd");
		Date birthday = null;
		UserData userData = new UserData(user, password, email, birthday, first_Name, last_Name);


		//BookDataJDBCTemplate.u.info("New user="+user+", password="+password);
		if (user !=null && password !=null)
			userJdbc.createNewUser(userData);
		
		
	}

}
