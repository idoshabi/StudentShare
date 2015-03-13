package com.edu.StudentShare.Message;

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

/**
 * Servlet implementation class UserRegister
 */
@Path("/Message")
public class Mess {
	static MessageDataJDBC messageJdbc;
	static int user_id;

	public Mess() {
		init();
	}

	public void init() {
		messageJdbc = new MessageDataJDBC("message");
		messageJdbc.createTable();
		System.out.println("table created");
	}

	@POST
	@Path("/send")
	
	public Response Addproduct(@FormParam("title") String title,
			@FormParam("contant") String contant,
			@FormParam("recipientId") int recipientId,
			@Context HttpServletRequest req) {
		int id = 0;
		int messeage_id = 0;
		try {
			id = DBHelper.retiveUserId(req);
			
				java.sql.Date sqlDate = new java.sql.Date(
						System.currentTimeMillis());
				MessageData data = new MessageData(title, contant, id,
						recipientId, sqlDate);
				messeage_id = messageJdbc.newMesseage(data);

			

		} catch (Exception e) {
			AuthFilter.log.error("Failed at user" + e);
			return Response.status(400).entity(e.toString()).build();

		}
		String returnString =  "Successfully sent messeage with id:" +messeage_id ;

		return Response.status(201).entity(returnString).build();


	}

	@GET
	@Path("/SentMesseages")
	public String SentMesseages(@Context HttpServletRequest req) {
		int id = 0;
		List<MessageData> list = null;
		try {
			id = DBHelper.retiveUserId(req);

			list = messageJdbc.getSentMessagesByUser(id);

		} catch (Exception e) {
			AuthFilter.log.error("Failed at user" + e);
		}
		
		return Utils.toJson(list);
	}

	@GET
	@Path("/RecivedMesseages")
	public String RecivedMesseages(@Context HttpServletRequest req) {
		int id = 0;
		List<MessageData> list = null;
		try {
			id = DBHelper.retiveUserId(req);

			list = messageJdbc.getRecivedMessagesByUser(id);

		} catch (Exception e) {
			AuthFilter.log.error("Failed at user" + e);
		}

		return Utils.toJson(list);
	}

	@POST
	@Path("/delete")
	public Response deleteMesseage(

	@FormParam("messeageId") int messeageId, @Context HttpServletRequest req) {
		int id = 0;
		try {
			id = DBHelper.retiveUserId(req);
			
				if (!messageJdbc.deleteMesseage(id, messeageId)){
					return Response.status(403).entity("You cant delete it!").build();

				}
			
		} catch (Exception e) {
			AuthFilter.log.error("Failed at user" + e);
			return Response.status(400).entity(e.toString()).build();

		}
		String returnString =  "Messeage deleted: " +messeageId ;

		return Response.status(201).entity(returnString).build();

	}

}
