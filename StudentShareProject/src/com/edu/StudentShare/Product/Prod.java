package com.edu.StudentShare.Product;

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
import javax.ws.rs.core.Response;

import com.edu.StudentShare.AuthFilter;
import com.edu.StudentShare.Utils;
import com.edu.StudentShare.Redis.Products;
import com.edu.StudentShare.User.User;
import com.edu.StudentShare.User.UserData;
import com.edu.StudentShare.User.UserDataJDBC;

/**
 * Servlet implementation class UserRegister
 */
@Path("/Product")
public class Prod {
	static public ProductDataJDBC productJdbc;

	static int user_id;

	public Prod() {
		init();
	}

	public void init() {
		productJdbc = new ProductDataJDBC("product");
		productJdbc.createTable();
		System.out.println("table created");
	}

	@POST
	@Path("/add")
	public Response Addproduct(@FormParam("productName") String productName,
			@FormParam("price") double price,
			@FormParam("description") String description,
			@FormParam("quntity") int quntity, @Context HttpServletRequest req) {
		int id = 0;
		int product_id = 0;
		try {
			id = Utils.retiveUserId(req);

			java.sql.Date sqlDate = new java.sql.Date(
					System.currentTimeMillis());
			ProductData data = new ProductData(productName, price, id, quntity,
					sqlDate, 0, description, "url");
			product_id = productJdbc.createNewProduct(data);

		} catch (Exception e) {
			AuthFilter.log.error("Failed at user" + e);
			return Response.status(400).entity(e.toString()).build();

		}
		String returnString = "Your id is: " + product_id;

		return Response.status(201).entity(returnString).build();

	}

	@GET
	@Path("/getSellers")
	@Produces(MediaType.APPLICATION_JSON)
	public String getSellers(@Context HttpServletRequest req) {
		int id = 0;
		List<String> list = null;
		try {

			list = productJdbc.getSellers();

		} catch (Exception e) {
			AuthFilter.log.error("Failed at user" + e);
		}

		return Utils.toJson(list);
	}

	@POST
	@Path("/delete")
	public Response deleteProduct(@FormParam("product_id") int productId,
			@Context HttpServletRequest req) {
		int id = 0;
		String returnString = null;
		try {
			id = Utils.retiveUserId(req);
			Boolean result = productJdbc.deleteProduct(id, productId);
			returnString = DeleteProductView(productId, id, result);


		} catch (Exception e) {
			AuthFilter.log.error("Failed at user" + e);
			return Response.status(400).entity(e).build();

		}
		return Response.status(201).entity(returnString).build();

	}

	@POST
	@Path("/buy")
	public Response buyProduct(@FormParam("product_id") int productId,
			@Context HttpServletRequest req) {
		int id = 0;
		String returnString = null;

		try {
			id = Utils.retiveUserId(req);

			productJdbc.buyProduct(id, productId);
			returnString = BuyProductView(productId, id);

		} catch (Exception e) {
			AuthFilter.log.error("Failed at user" + e);
			return Response.status(400).entity(e).build();

		}

		return Response.status(201).entity(returnString).build();
	}

	private String DeleteProductView(int productId, int id, Boolean result) {
		String returnString = null;
		if (!productJdbc.checkIfProductExist(productId)) {
			returnString = "This product doesn't exist!";

		} else {
			if(!result)
				returnString = "You are not allowed to delete it!";
			else{
			returnString = "Success deleted: " + id;
			}
		}
		
		return returnString;
	}

	private String BuyProductView(int productId, int id) {
		String returnString = null;
		if (!productJdbc.checkIfProductExist(productId)) {
			returnString = "product doesnt exist!";
		} else {
			returnString = "Success purchased: " + id;
		}

		return returnString;
	}

	@GET
	@Path("/getTop")
	@Produces(MediaType.APPLICATION_JSON)
	public String getTop(@QueryParam("max") int max,
			@Context HttpServletRequest req) {
		int id = 0;
		ArrayList<ProductData> list = null;
		try {

			list = Products.getTopProducts(max);

		} catch (Exception e) {
			AuthFilter.log.error("Failed at user" + e);
		}

		return Utils.toJson(list);
	}

	@GET
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	public String SearchProduct(
			@QueryParam("searchPattren") String searchPattren,
			@Context HttpServletRequest req) {
		List<ProductData> listdata = null;
		String total = "Result: ";
		listdata = new ArrayList<ProductData>();
		int id = 0;
		try {

			List<Integer> list = productJdbc.searchProduct(searchPattren);
			for (Integer item : list) {
				listdata.add(productJdbc.getProductInfo(item));
				System.out.println(item);

			}
		} catch (Exception e) {
			AuthFilter.log.error("Failed at user" + e);
		}

		return Utils.toJson(listdata);

	}

	@POST
	@Path("/show")
	@Produces(MediaType.APPLICATION_JSON)
	public String showProduct(@FormParam("product_id") int productId,
			@Context HttpServletRequest req) {
		{
		}
		ProductData data = null;
		try {
			int id = Utils.retiveUserId(req);
			data = productJdbc.getProductInfo(productId);

		} catch (Exception e) {
			AuthFilter.log.error("Failed at user" + e);
		}

		return Utils.toJson(data);
	}

	@GET
	@Path("/myProducts")
	@Produces(MediaType.APPLICATION_JSON)
	public String myProducts(@Context HttpServletRequest req) {

		List<ProductData> data = null;
		try {
			int id = Utils.retiveUserId(req);

			data = productJdbc.getProductsByUser(id);

		} catch (Exception e) {
			AuthFilter.log.error("Failed at user" + e);
		}

		return Utils.toJson(data);
	}
}
