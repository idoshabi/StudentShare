package com.edu.StudentShare.student;

import java.sql.Date;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.edu.StudentShare.LogFilter;
import com.edu.StudentShare.Utils;

/**
 * Servlet implementation class UserRegister
 */
@Path("/User")
public class User {
	static UserDataJDBC userJdbc;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public User() {
		init();
	}

	public void init() {
		userJdbc = new UserDataJDBC("users");
		System.out.println("bookDataJDBCTemplate=" + userJdbc);
		userJdbc.createTable();
		System.out.println("table created");
		LogFilter.log = new Utils("c:\\log\\test.log");
	}

	@POST
	@Path("/Connect")
	@Produces(MediaType.TEXT_HTML)
	public String UserConnect(@FormParam("username") String user,
							  @FormParam("password") String password) {
		int id = 0;
		try {
			
			
			LogFilter.log.info("connect with user=" + user + ", password="
					+ password);
			
			if (user != null && password != null)
				 id = userJdbc.connect(user, password);
			
			
		} catch (Exception e) {
			LogFilter.log.error("Failed at user" + e);
		}
		return "<html> " + "<title>" + "Your id is" + "</title>"
				+ "<body><h1>" + "Your id is:"+ id + "</body></h1>"
				+ "</html> ";

	}

	@POST
	@Path("/ChangePassword")
	@Produces(MediaType.TEXT_HTML)
	public String ChangePassword(@FormParam("username") String user,
							  @FormParam("password") String password) {
		try {
			int id;
			userJdbc.changePassword(userId, oldPwd, newPwd)
			LogFilter.log.info("connect with user=" + user + ", password="
					+ password);
			
			if (user != null && password != null)
				 id = userJdbc.connect(user, password);
			
			
		} catch (Exception e) {
			LogFilter.log.error("Failed at user" + e);
		}
		return null;

	}
	@POST
	@Path("/Register")
	@Produces(MediaType.TEXT_HTML)
	public String UserRegister(@FormParam("username") String user,
			@FormParam("first_Name") String first_Name,
			@FormParam("last_name") String last_Name,
			@FormParam("email") String email,
			@FormParam("birthday") String birthday1,
			@FormParam("password") String password,
			@FormParam("confirm_pwd") String confirm_pwd) {
		int id = 0;
		try {
			Date birthday = null;

			// BookDataJDBCTemplate.u.info("New user="+user+", password="+password);
			if (user != null && password != null){
				if (!password.equals (confirm_pwd))
					return "password is diffrent";
				UserData userData = new UserData(user, password, email, birthday,
						first_Name, last_Name);
				id = userJdbc.createNewUser(userData);

			}
			return ("<html> " + "<title>" + "Your id is" + "</title>"
					+ "<body><h1>" + "Your id is:"+ id + "</body></h1>"
					+ "</html> ");
		} catch (Exception e) {
			LogFilter.log.error("Failed at user" + e);
		}
		return null;

	}

	
}
