package com.edu.StudentShare.student;

import java.sql.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.edu.StudentShare.LogFilter;
import com.edu.StudentShare.Utils;

/**
 * Servlet implementation class UserRegister
 */
@Path("/User")
public class User {
	static UserDataJDBC userJdbc;
	static int user_id;

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
			@FormParam("password") String password,
			@Context HttpServletRequest req) {
		int id = 0;
		try {

			LogFilter.log.info("connect with user=" + user + ", password="
					+ password);

			if (user != null && password != null) {
				id = userJdbc.connect(user, password);
				saveSession(req, id);
			}

		} catch (Exception e) {
			LogFilter.log.error("Failed at user Connect" + e);
		}
		return "<html> " + "<title>" + "Your id is" + "</title>" + "<body><h1>"
				+ "Your id is:" + id + "</body></h1>" + "</html> ";

	}

	public int saveSession(HttpServletRequest req, int id) {

		HttpSession session = req.getSession(true);
		session.setAttribute("user_id", id);

		return id;

	}

	@GET
	@Path("/delete")
	public String deleteUser(@Context HttpServletRequest req) {

		HttpSession session = req.getSession(true);
		Object userId = session.getAttribute("user_id");
		if (userId != null) {
			userJdbc.deleteUser((int) userId);
		} else {
			return "Not connected";
		}

		return "Deleted";
	}
	@GET
	@Path("/show")
	public String showUser(@Context HttpServletRequest req) {
		UserData data = null;
		HttpSession session = req.getSession(true);
		Object userId = session.getAttribute("user_id");
		
		if (userId != null) {
			 data = userJdbc.showUserInfo((int) userId);
			
		} else { 
			return "Not connected";
		}
		return data.toString();

	}


	@POST
	@Path("/ChangePassword")
	@Produces(MediaType.TEXT_HTML)
	public String ChangePassword(@FormParam("oldDassword") String oldDassword,
			@FormParam("newPassword") String newPassword,
			@FormParam("confirmPassword") String confirmPassword,
			@Context HttpServletRequest req) {
		try {

			HttpSession session = req.getSession(true);
			Object userId = session.getAttribute("user_id");
			if (userId != null) {
				if (!confirmPassword.equals(newPassword))
					return "password is diffrent";
				System.out.println(userId.toString());
			} else {
				return "Not connected";
			}
			int id = (int) userId;
			Boolean status = userJdbc.changePassword(id, oldDassword,
					newPassword);
			LogFilter.log.info("connect with user=" + id + ", password="
					+ newPassword);

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
			@FormParam("confirm_pwd") String confirm_pwd,
			@Context HttpServletRequest req) {
		int id = 0;
		try {
			Date birthday = null;

			// BookDataJDBCTemplate.u.info("New user="+user+", password="+password);
			if (user != null && password != null) {
				if (!password.equals(confirm_pwd))
					return "password is diffrent";
				UserData userData = new UserData(user, password, email,
						birthday, first_Name, last_Name);
				id = userJdbc.createNewUser(userData);
				// Save the user_id to the session
				saveSession(req, id);
			}
			return ("<html> " + "<title>" + "Your id is" + "</title>"
					+ "<body><h1>" + "Your id is:" + id + "</body></h1>" + "</html> ");
		} catch (Exception e) {
			LogFilter.log.error("Failed at user" + e);
		}
		return null;

	}

}
