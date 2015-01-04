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

import com.edu.StudentShare.DBHelper;
import com.edu.StudentShare.LogFilter;
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
	public String Addproduct(
			@FormParam("title") String title,
			@FormParam("contant") String contant,
			@FormParam("recipientId") int recipientId
			, @Context HttpServletRequest req) {
		int id = 0;
		int messeage_id = 0;
		try {
			id = DBHelper.retiveUserId(req);
			if (id != 0) {
				java.sql.Date sqlDate = new java.sql.Date(
						System.currentTimeMillis());
				MessageData data = new MessageData(title, contant, id, recipientId, sqlDate);
				messeage_id = messageJdbc.newMesseage(data);

			} else {
				return "Not connected";
			}

		} catch (Exception e) {
			LogFilter.log.error("Failed at user" + e);
		}
		return "<html> " + "<title>" + "Your id is" + "</title>" + "<body><h1>"
				+ "Your id is:" + messeage_id + "</body></h1>" + "</html> ";

	}

	
	@GET
	@Path("/SentMesseages")
	public String SentMesseages(
	@Context HttpServletRequest req) {
		int id = 0;
		List<MessageData> list= null;
		try {
			id = DBHelper.retiveUserId(req);
			if (id != 0) {
				
				list = messageJdbc.getSentMessagesByUser(id);

			} else {
				return "Not connected";
			}
		} catch (Exception e) {
			LogFilter.log.error("Failed at user" + e);
		}

		return list.toString();
	}
	
	@GET
	@Path("/RecivedMesseages")
	public String RecivedMesseages(
	@Context HttpServletRequest req) {
		int id = 0;
		List<MessageData> list= null;
		try {
			id = DBHelper.retiveUserId(req);
			if (id != 0) {
				
				list = messageJdbc.getRecivedMessagesByUser(id);

			} else {
				return "Not connected";
			}
		} catch (Exception e) {
			LogFilter.log.error("Failed at user" + e);
		}

		return list.toString();
	}
	@POST
	@Path("/delete")
	public String deleteMesseage(

	@FormParam("messeageId") int messeageId,
	@Context HttpServletRequest req) {
		int id = 0;
		try {
			id = DBHelper.retiveUserId(req);
			if (id != 0) {
				messageJdbc.deleteMesseage(id, messeageId);

			} else {
				return "Not connected";
			}
		} catch (Exception e) {
			LogFilter.log.error("Failed at user" + e);
		}

		return "Deleted";
	}


}
