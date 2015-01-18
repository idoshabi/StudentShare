package com.edu.StudentShare.Message;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.edu.StudentShare.DBConn;
import com.edu.StudentShare.AuthFilter;

public class MessageDataJDBC implements MessageDataDAO {
	private String tablenName = null;
	Connection conn = null;

	public MessageDataJDBC(String string) {
		conn = DBConn.getConnection();

		tablenName = string;

	}

	private MessageData getMesseageFromResultSet(ResultSet rSet) {
		MessageData data = null;

		try {

			String title = rSet.getString("title");
			String contant = rSet.getString("contant");
			int recipientId = rSet.getInt("recipientId");
			int senderId = rSet.getInt("senderId");
			Date date = rSet.getDate("dateTime");

			data = new MessageData(title, contant, senderId, recipientId, date);
			return data;

		} catch (SQLException e) {
			System.err.println(e.getMessage());

		}
		return data;

	}

	public List<MessageData> getSentMessagesByUser(int user_id) {
		List<MessageData> dataList = new ArrayList<MessageData>();
		try {
			String SQL = "Select * from " + tablenName
					+ " Where senderId = ? ;";

			PreparedStatement stmnt = conn.prepareStatement(SQL);
			stmnt.setInt(1, user_id);
			ResultSet rSet = stmnt.executeQuery();

			while (rSet.next()) {
				dataList.add(getMesseageFromResultSet(rSet));
			}

			return dataList;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return dataList;
	}

	public List<MessageData> getRecivedMessagesByUser(int user_id) {
		List<MessageData> dataList = new ArrayList<MessageData>();
		try {
			String SQL = "Select * FROM " + tablenName
					+ " Where recipientId = ? ;";

			PreparedStatement stmnt = conn.prepareStatement(SQL);
			stmnt.setInt(1, user_id);
			ResultSet rSet = stmnt.executeQuery();

			while (rSet.next()) {
				dataList.add(getMesseageFromResultSet(rSet));
			}

			return dataList;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return dataList;
	}

	@Override
	public void createTable() {
		try {

			Statement st = conn.createStatement();
			st.executeUpdate("create database if not exists my_db");
			st.executeUpdate("use my_db");
			String SQL = "CREATE TABLE IF NOT EXISTS "
					+ tablenName
					+ "(id int NOT NULL AUTO_INCREMENT,`title` VARCHAR(100) NOT NULL,`contant` VARCHAR(100) NOT NULL,`recipientId` int NOT NULL ,`senderId` int NOT NULL, `dateTime` DATE NOT NULL,  primary key (id)) ENGINE = InnoDB CHARACTER SET utf8 COLLATE utf8_bin;";
			st.execute(SQL);
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}
		System.out.println("Created the table=" + tablenName);
		return;

	}

	@Override
	public int newMesseage(MessageData data) {
		int id = 0;
		try {
			String SQL = "INSERT INTO "
					+ tablenName
					+ "  (`title`, `contant`, `recipientId`, `senderId`, `dateTime`) "
					+ " VALUES (?,?,?,?,?);";
			PreparedStatement pre = conn.prepareStatement(SQL);

			pre.setString(1, data.getTitle());
			pre.setString(2, data.getContant());
			pre.setInt(3, data.getReciverId());
			pre.setInt(4, data.getSenderId());
			pre.setDate(5, data.getDataTime());
			pre.executeUpdate();
			Statement st = conn.createStatement();

			ResultSet rs = st
					.executeQuery("SELECT LAST_INSERT_ID() as last_id;");
			while (rs.next()) {
				id = Integer.parseInt(rs.getString("last_id"));
			}
		} catch (Exception ex) {
			AuthFilter.log.error("Failed at createNewProduct with "
					+ ex.toString());
		}

		return id;

	}

	@Override
	public Boolean deleteMesseage(int userid, int messeageId) {
		try {
			int senderId = 0;

			String checkIfBelong = "SELECT senderId FROM " + tablenName
					+ " WHERE id = " + messeageId;
			Statement stmnt = conn.createStatement();
			ResultSet rSet = stmnt.executeQuery(checkIfBelong);
			while (rSet.next()) {
				senderId = rSet.getInt("senderId");

			}
			if (userid != senderId) {
				AuthFilter.log
						.error("Alert! userid try to delete someone else product");
				return false;
			}
			String SQL = "DELETE from " + tablenName + " WHERE id = ?";
			// TODO: check if it the id of the user is the buyer

			PreparedStatement pre = conn.prepareStatement(SQL);
			pre.setInt(1, messeageId);
			pre.executeUpdate();

		} catch (Exception ex) {
			System.out.println(ex);
			return false;
		}
		return true;
	}

}
