package com.edu.StudentShare.User;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import javax.ws.rs.core.Response;

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
	public Response UserConnect(@FormParam("username") String user,
			@FormParam("password") String password,
			@Context HttpServletRequest req) {
		int id = 0;
		try {
			// Get init parameter
			ConnectionPool pool = new ConnectionPool();


			LogFilter.log.info("connect with user=" + user + ", password="
					+ password);

			if (user != null && password != null) {
				id = userJdbc.connect(user, password);
				saveSession(req, id);
				OnlineUsers.addUser(id);
			}
		} 
		catch (Exception e) {
			LogFilter.log.error("Failed at user" + e);
			return Response.status(400).entity(e.toString()).build();

		}
		String returnString =  "Your id is: " +id ;
		
		return Response.status(201).entity(returnString).build();
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
		
			data = ShopptingCart.getItemFromCart((int) userId);

			return Utils.toJson(data);
		
	}

	@GET
	@Path("/delete")
	public Response deleteUser(@Context HttpServletRequest req) {

		HttpSession session = req.getSession(true);
		Object userId = session.getAttribute("user_id");

		userJdbc.deleteUser((int) userId);
		
		String returnString = "Your id is: " + userId;

		return Response.status(201).entity(returnString).build();

	}

	@GET
	@Path("/deleteItemFromCart")
	public Response deleteItemFromCart(@Context HttpServletRequest req,
			@QueryParam("product_id") int productId) {

		HttpSession session = req.getSession(true);
		Object userId = session.getAttribute("user_id");
		
			ShopptingCart.deleteItemFromCart((int) userId, productId);
			String returnString = "Your id is: " + (int) userId;


			return Response.status(201).entity(returnString).build();
	}
	@GET
	@Path("/getOnlineUsers")
	public String getOnlineUsers(@Context HttpServletRequest req,
			@QueryParam("intervalTime")int  intervalTime,
			@QueryParam("maxResults") int maxResults) {
		List<UserData> list = null;
		HttpSession session = req.getSession(true);
		Object userId = session.getAttribute("user_id");
		
			list = OnlineUsers.getOnlineUserByTime(maxResults, intervalTime);
			
		

		return Utils.toJson(list);
	}
	@GET
	@Path("/deleteCart")
	public Response deleteCart(@Context HttpServletRequest req) {

		HttpSession session = req.getSession(true);
		Object userId = session.getAttribute("user_id");
		ShopptingCart.deleteCartForUser((int) userId);

		String returnString = "Your id is: " + (int) userId;

		return Response.status(201).entity(returnString).build();
	}

	@GET
	@Path("/CheckoutCart")
	public String CheckoutCart(@Context HttpServletRequest req) {
		List<String> data = null;
		HttpSession session = req.getSession(true);
		Object userId = session.getAttribute("user_id");

			data = ShopptingCart.CheckoutPurchase((int) userId);

		
		return Utils.toJson(data);

	}
	
	
	@GET
	@Path("/getTop")
	@Produces(MediaType.APPLICATION_JSON)

	public String getTop(@QueryParam("max") int max,
			@Context HttpServletRequest req) {
		int id = 0;
		ArrayList<UserData> list = null;
		try {

			list = Users.getTopUsers(max);

		} catch (Exception e) {
			LogFilter.log.error("Failed at user" + e);
		}

		return Utils.toJson(list);
	}
	
	@GET
	@Path("/show")
	public String showUser(@Context HttpServletRequest req) {
		UserData data = null;
		HttpSession session = req.getSession(true);
		Object userId = session.getAttribute("user_id");

		
			data = userJdbc.showUserInfo((int) userId);

		return Utils.toJson(data);


	}
	@POST
	@Path("/addToCart")
	@Produces(MediaType.TEXT_HTML)
	public Response addToCart(@FormParam("product_id") int product_id,

	@Context HttpServletRequest req) {
		String returnString = null;

		try {
			HttpSession session = req.getSession(true);
			Object userId = session.getAttribute("user_id");
			ShopptingCart.addItemToCart((int) userId, product_id);

			returnString = "Your id is: " + (int) userId;

		} catch (Exception e) {
			return Response.status(400).entity("Failed at user" + e).build();

		}

		return Response.status(201).entity(returnString).build();

	}

	@POST
	@Path("/IncrementUser")
	@Produces(MediaType.TEXT_HTML)
	public Response ChangePassword(@FormParam("points") int points,
			@FormParam("userToIncrement") int userToIncrement,
			@Context HttpServletRequest req) {
		try {

			HttpSession session = req.getSession(true);
			Object userId = session.getAttribute("user_id");
				Users.increaseUserRank(userToIncrement, points);
			
		} catch (Exception e) {
			return Response.status(400).entity("Failed at user" +e).build();
		}
		String returnString = "Your id is: " + (int) userToIncrement;

		return Response.status(201).entity(returnString).build();


	}

	@POST
	@Path("/ChangePassword")
	@Produces(MediaType.TEXT_HTML)
	public Response ChangePassword(@FormParam("oldDassword") String oldDassword,
			@FormParam("newPassword") String newPassword,
			@FormParam("confirmPassword") String confirmPassword,
			@Context HttpServletRequest req) {
		String returnString = null;
		try {

			HttpSession session = req.getSession(true);
			Object userId = session.getAttribute("user_id");
			 returnString = "Your id is: " + (int) userId;

				if (!confirmPassword.equals(newPassword)){
					String DiffrentPwd = "password is diffrent";
				return Response.status(401).entity(DiffrentPwd).build();
				}
				
			int id = (int) userId;
			Boolean status = userJdbc.changePassword(id, oldDassword,
					newPassword);
			LogFilter.log.info("connect with user=" + id + ", password="
					+ newPassword);

		} catch (Exception e) {
			LogFilter.log.error("Failed at user" + e);
		}

		return Response.status(201).entity(returnString).build();

	}
	  
	@POST
	@Path("/Register")
	@Produces(MediaType.TEXT_HTML)
	public Response UserRegister(@FormParam("username") String user,
			@FormParam("first_Name") String first_Name,
			@FormParam("last_name") String last_Name,
			@FormParam("email") String email,
			@FormParam("birthday") DateAdapter  birthday1,
			@FormParam("password") String password,
			@FormParam("confirm_pwd") String confirm_pwd,
			@FormParam("image_url") String image_url,

			@Context HttpServletRequest req) {
		int id = 0;
		try {
			Date birthday = birthday1.getDate();
			 java.sql.Date date = Utils.TosqlDate(birthday);
			 int initPoints = 100;
			 int initRank = 0;
			// BookDataJDBCTemplate.u.info("New user="+user+", password="+password);
			if (user != null && password != null) {
				if (!password.equals(confirm_pwd))
				{
					String DiffrentPwd = "password is diffrent!";
				return Response.status(401).entity(DiffrentPwd).build();
				}
				UserData userData = new UserData(user, password, email,
						date, first_Name, last_Name, initRank, initPoints, image_url);
				
				id = userJdbc.createNewUser(userData);
				// Save the user_id to the session
				saveSession(req, id);
				
				Users.setUser(id, userData);

			}
			String returnString =  "Your id is:" + id ;
			return Response.status(201).entity(returnString).build();

		} catch (Exception e) {
			LogFilter.log.error("Failed at user" + e);
		}
		return null;

	}
	public static class DateAdapter{
        private Date date;
        public DateAdapter(String date){
            try {
                this.date = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            } catch (Exception e) {
            		System.out.println(e);
            }
        }

        public Date getDate(){
            return this.date;
        }
    }

}