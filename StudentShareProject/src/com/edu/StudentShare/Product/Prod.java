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

import com.edu.StudentShare.LogFilter;
import com.edu.StudentShare.Utils;
import com.edu.StudentShare.Redis.Products;
import com.sun.research.ws.wadl.Response;

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
	public String Addproduct(@FormParam("productName") String productName,
			@FormParam("price") double price,
			@FormParam("description") String description,
			@FormParam("quntity") int quntity, @Context HttpServletRequest req) {
		int id = 0;
		int product_id = 0;
		try {
			id = Utils.retiveUserId(req);
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


	
	@GET
	@Path("/getSellers")
	public String getSellers
			(@Context HttpServletRequest req) {
		int id = 0;
		try {
			id = Utils.retiveUserId(req);
			if (id != 0) {
				List<String> list= productJdbc.getSellers();
				
				return list.toString();
			} else {
				return "Not connected";
			}
		} catch (Exception e) {
			LogFilter.log.error("Failed at user" + e);
		}

		return "Deleted";
	}
	
	@POST
	@Path("/delete")
	public String deleteProduct(
			@FormParam("product_id") int productId,
			@Context HttpServletRequest req) {
		int id = 0;
		try {
			id = Utils.retiveUserId(req);
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

	@POST
	@Path("/buy")
	public String buyProduct(
			@FormParam("product_id") int productId,
			@Context HttpServletRequest req) {
		int id = 0;
		try {
			id = Utils.retiveUserId(req);
			if (id != 0) {
				productJdbc.buyProduct(id, productId);

			} else {
				return "Not connected";
			}
		} catch (Exception e) {
			LogFilter.log.error("Failed at user" + e);
		}

		return "Successfully purchased ";
	}
	@GET
	@Path("/getTop")
	public String getTop(
			@QueryParam("max") int max,
			@Context HttpServletRequest req) {
		int id = 0;
		try {
			id = Utils.retiveUserId(req);
			if (id != 0) {
				ArrayList<ProductData> list = Products.getTopProducts( max);
				return list.toString();
			} else {
				return "Not connected";
			}
		} catch (Exception e) {
			LogFilter.log.error("Failed at user" + e);
		}

		return "Successfully purchased ";
	}
	@GET
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)

	public ProductData SearchProduct(
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
			LogFilter.log.error("Failed at user" + e);
		}
		return listdata.get(0);

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
			int id = Utils.retiveUserId(req);
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
	public String myProducts(@Context HttpServletRequest req) {

		List<ProductData> data = null;
		try {
			int id = Utils.retiveUserId(req);
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
