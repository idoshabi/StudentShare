package com.edu.StudentShare.Product;

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

import com.edu.StudentShare.LogFilter;
import com.edu.StudentShare.Utils;

/**
 * Servlet implementation class UserRegister
 */
@Path("/Product")
public class Prod {
	static ProductDataJDBC productJdbc;
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
	public String Addproduct(@FormParam("productName") String productName,
			@FormParam("price") double price,
			@FormParam("description") String description,
			@FormParam("quntity") int quntity, @Context HttpServletRequest req) {
		int id = 0;
		int product_id = 0;
		try {
			id = retiveUserId(req);
			if (id != 0) {
				java.sql.Date sqlDate = new java.sql.Date(
						System.currentTimeMillis());
				ProductData data = new ProductData(productName, price, id,
						quntity, sqlDate, 0, description, "url");
				product_id = productJdbc.createNewProduct(data);

			} else {
				return "Not connected";
			}

		} catch (Exception e) {
			LogFilter.log.error("Failed at user" + e);
		}
		return "<html> " + "<title>" + "Your id is" + "</title>" + "<body><h1>"
				+ "Your id is:" + product_id + "</body></h1>" + "</html> ";

	}

	public int retiveUserId(HttpServletRequest req) {
		HttpSession session = req.getSession(true);
		Object userId = session.getAttribute("user_id");

		if (userId != null) {

			int id = (int) userId;
			return id;

		} else {
			return 0;
		}
	}

	@POST
	@Path("/delete")
	public String deleteProduct(

	@FormParam("product_id") int productId, @Context HttpServletRequest req) {
		int id = 0;
		try {
			id = retiveUserId(req);
			if (id != 0) {
				productJdbc.deleteProduct(id, productId);

			} else {
				return "Not connected";
			}
		} catch (Exception e) {
			LogFilter.log.error("Failed at user" + e);
		}

		return "Deleted";
	}

	@GET
	@Path("/search")
	public String SearchProduct(
			@QueryParam("searchPattren") String searchPattren,
			@Context HttpServletRequest req) {
		String total = null;
		int id = 0;
		try {
			id = retiveUserId(req);
			if (id != 0) {

				List<Integer> list = productJdbc.searchProduct(searchPattren);
				for (Integer item : list) {
					ProductData data = productJdbc.getProductInfo(item);
					System.out.println(item);
					total += " " + data.toString();
				}
			} else {
				return "Not connected";
			}
		} catch (Exception e) {
			LogFilter.log.error("Failed at user" + e);
		}

		return total;
	}

	@POST
	@Path("/show")
	@Produces(MediaType.TEXT_HTML)
	public String showProduct(@FormParam("product_id") int productId,
			@Context HttpServletRequest req) {
		{
		}
		ProductData data = null;
		try {
			int id = retiveUserId(req);
			if (id != 0) {
				data = productJdbc.getProductInfo(productId);

			} else {
				return "Not connected";
			}
		} catch (Exception e) {
			LogFilter.log.error("Failed at user" + e);
		}

		return data.toString();
	}
	@GET
	@Path("/myProducts")
	public String myProducts(
			@Context HttpServletRequest req) {
		{
		}
		List<ProductData> data = null;
		try {
			int id = retiveUserId(req);
			if (id != 0) {
				data = productJdbc.getProductsByUser(id);

			} else {
				return "Not connected";
			}
		} catch (Exception e) {
			LogFilter.log.error("Failed at user" + e);
		}
		
		return data.toString();
	}
}
