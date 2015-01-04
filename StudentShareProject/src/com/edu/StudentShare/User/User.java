package com.edu.StudentShare.User;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.edu.StudentShare.LogFilter;
import com.edu.StudentShare.Utils;
import com.edu.StudentShare.Product.ProductData;
import com.edu.StudentShare.Redis.*;

/**
 * Servlet implementation class UserRegister
 */
@Path("/User")
public class User {
	public static UserDataJDBC userJdbc;
	static int user_id;

	public User() {
		init();
	}

	public void init() {
		userJdbc = new UserDataJDBC("users");
		System.out.println("bookDataJDBCTemplate=" + userJdbc);
		userJdbc.createTable();
		System.out.println("table created");
	}

	@POST
	@Path("/Connect")
	@Produces(MediaType.TEXT_HTML)
	public String UserConnect(@FormParam("username") String user,
			@FormParam("password") String password,
			@Context HttpServletRequest req) {
		int id = 0;
		try {
			// Get init parameter
			ConnectionPool pool = new ConnectionPool();
			Users.setUser(12, new UserData("moshe", "first", "first", null,
					"idan", "first", 41, 41, "first"));

			UserData data1 = Users.getUserById(10);

			ArrayList<UserData> x = Users.getTopUsers(2);
			Users.removeUser(12);

			ArrayList<ProductData> data = Products.getTopProducts(2);

			ArrayList<UserData> x1 = Users.getTopUsers(2);

			LogFilter.log.info("connect with user=" + user + ", password="
					+ password);

			if (user != null && password != null) {
				id = userJdbc.connect(user, password);
				saveSession(req, id);
				OnlineUsers.addUser(id);
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
	@Path("/showCart")
	public String showCart(@Context HttpServletRequest req) {
		HttpSession session = req.getSession(true);
		Object userId = session.getAttribute("user_id");
		List<ProductData> data = null;
		if (userId != null) {
			data = ShopptingCart.getItemFromCart((int) userId);

			return data.toString();
		} else {
			return "Not connected";
		}
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
	@Path("/deleteItemFromCart")
	public String deleteItemFromCart(@Context HttpServletRequest req,
			@QueryParam("product_id") int productId) {

		HttpSession session = req.getSession(true);
		Object userId = session.getAttribute("user_id");
		if (userId != null) {
			ShopptingCart.deleteItemFromCart((int) userId, productId);
		} else {
			return "Not connected";
		}

		return "Deleted";
	}
	@GET
	@Path("/getOnlineUsers")
	public String getOnlineUsers(@Context HttpServletRequest req,
			@QueryParam("intervalTime")int  intervalTime,
			@QueryParam("maxResults") int maxResults) {
		List<UserData> list = null;
		HttpSession session = req.getSession(true);
		Object userId = session.getAttribute("user_id");
		if (userId != null) {
			list = OnlineUsers.getOnlineUserByTime(maxResults, intervalTime);
			
		} else {
			return "Not connected";
		}

		return list.toString();
	}
	@GET
	@Path("/deleteCart")
	public String deleteCart(@Context HttpServletRequest req) {

		HttpSession session = req.getSession(true);
		Object userId = session.getAttribute("user_id");
		if (userId != null) {
			ShopptingCart.deleteCartForUser((int) userId);
		} else {
			return "Not connected";
		}

		return "Deleted";
	}

	@GET
	@Path("/CheckoutCart")
	public String CheckoutCart(@Context HttpServletRequest req) {
		List<String> data = null;
		HttpSession session = req.getSession(true);
		Object userId = session.getAttribute("user_id");

		if (userId != null) {
			data = ShopptingCart.CheckoutPurchase((int) userId);

		} else {
			return "Not connected";
		}
		return data.toString();

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
	@Path("/addToCart")
	@Produces(MediaType.TEXT_HTML)
	public String addToCart(@FormParam("product_id") int product_id,

	@Context HttpServletRequest req) {
		try {

			HttpSession session = req.getSession(true);
			Object userId = session.getAttribute("user_id");
			if (userId != null) {
				ShopptingCart.addItemToCart((int) userId, product_id);

			} else {
				return "Not connected";
			}

		} catch (Exception e) {
			LogFilter.log.error("Failed at user" + e);
		}
		return "Added to cart";

	}

	@POST
	@Path("/IncrementUser")
	@Produces(MediaType.TEXT_HTML)
	public String ChangePassword(@FormParam("points") int points,
			@FormParam("userToIncrement") int userToIncrement,
			@Context HttpServletRequest req) {
		try {

			HttpSession session = req.getSession(true);
			Object userId = session.getAttribute("user_id");
			if (userId != null) {
				Users.increaseUserRank(userToIncrement, points);
			} else {
				return "Not connected";
			}

		} catch (Exception e) {
			LogFilter.log.error("Failed at user" + e);
		}
		return null;

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
			@FormParam("image_url") String image_url,

			@Context HttpServletRequest req) {
		int id = 0;
		try {
			Date birthday = null;

			// BookDataJDBCTemplate.u.info("New user="+user+", password="+password);
			if (user != null && password != null) {
				if (!password.equals(confirm_pwd))
					return "password is diffrent";
				UserData userData = new UserData(user, password, email,
						birthday, first_Name, last_Name, 0, 100, image_url);
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
