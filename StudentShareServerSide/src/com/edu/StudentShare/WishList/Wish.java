package com.edu.StudentShare.WishList;

import java.sql.Date;
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

import com.edu.StudentShare.DBHelper;
import com.edu.StudentShare.AuthFilter;
import com.edu.StudentShare.Utils;
import com.edu.StudentShare.Product.Prod;

@Path("/Wish")
public class Wish {
	static WishDataJDBC wishJdbc;
	static int user_id;

	public Wish() {

		init();
	}

	public void init() {
		wishJdbc = new WishDataJDBC("wish");
		wishJdbc.createTable();
		System.out.println("table created");
	}

	@POST
	@Path("/add")
	public Response createWish(@FormParam("product_id") int product_id,
			@Context HttpServletRequest req) {
		WishData data = null;
		int id = 0;
		String returnString = null;
		try {
			id = DBHelper.retiveUserId(req);
			java.sql.Date sqlDate = new java.sql.Date(
					System.currentTimeMillis());
			returnString = AddWishView(product_id);
			
			data = new WishData(id, product_id, sqlDate);
			wishJdbc.createNewWish(data);

		} catch (Exception e) {
			AuthFilter.log.error("Failed at user" + e);
		}

		return Response.status(201).entity(returnString).build();

	}

	private String AddWishView(int product_id) {
		String returnString = null;
		
		try {
			if (Prod.productJdbc.checkIfProductExist(product_id))
			{
				 returnString = "Added successfully to Wishlist";
			}
			else{
				 returnString = "This product does not exist! ";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return returnString;
	}
	
	@GET
	@Path("/show")
	public String showProducts(
			@Context HttpServletRequest req) {
		List<WishData> data = null;
		
		int id = 0;

		try {
			id = DBHelper.retiveUserId(req);

			data = wishJdbc.getAllWishById(id);

		} catch (Exception e) {
			AuthFilter.log.error("Failed at user" + e);
		}
		String returnString = "Your id is: " + id;

		return Utils.toJson(data);

	}

	@POST
	@Path("/delete")
	public Response deleteMesseage(
	@FormParam("wish_id") int messeageId, @Context HttpServletRequest req) {
		int id = 0;
		try {
			id = DBHelper.retiveUserId(req);
			wishJdbc.deleteWish(id, messeageId);

		} catch (Exception e) {
			AuthFilter.log.error("Failed at user" + e);
		}

		String returnString = "Your id is: " + messeageId;

		return Response.status(201).entity(returnString).build();

	}

}
