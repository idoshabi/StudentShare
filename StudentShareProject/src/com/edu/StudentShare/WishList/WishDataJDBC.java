package com.edu.StudentShare.WishList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.edu.StudentShare.DBConn;
import com.edu.StudentShare.LogFilter;

public class WishDataJDBC implements WishDataDAO {

	public WishDataJDBC(String tablenName) {
		
		conn = DBConn.getConnection();
		this.tableName = tablenName;
	}

	private String tableName = null;
	Connection conn = null;

	public void createTable() {

		/*
		 * Generete a table without data
		 */
		try {

			Statement st = conn.createStatement();
			st.executeUpdate("create database if not exists my_db");
			st.executeUpdate("use my_db");
			String total;
			LogFilter.log.error(String.format("user %s password", tableName));
			String SQL = "CREATE TABLE IF NOT EXISTS "
					+ tableName
					+ ""
					+ "(id int NOT NULL AUTO_INCREMENT,`user_id` int NOT NULL,`product_id` int NOT NULL,`date_time` DATE	,primary key (id)) ENGINE = InnoDB CHARACTER SET utf8 COLLATE utf8_bin;";

			st.executeUpdate(SQL);

		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}
		System.out.println("Created the table=" + tableName);
		return;
	}

	public int createNewWish(WishData data) {
		int id = 0;
		try {
			String SQL = "INSERT INTO " + tableName
					+ "  (`user_id`, `product_id`, `date_time`) "
					+ " VALUES (?,?,?);";
			PreparedStatement pre = conn.prepareStatement(SQL);

			pre.setInt(1, data.getUserId());
			pre.setInt(2, data.getProductId());
			pre.setDate(3, data.getDataTime());

			pre.executeUpdate();
			Statement st = conn.createStatement();

			ResultSet rs = st
					.executeQuery("SELECT LAST_INSERT_ID() as last_id;");
			while (rs.next()) {
				id = Integer.parseInt(rs.getString("last_id"));
			}
		} catch (Exception ex) {
			LogFilter.log.error("Failed at createNewProduct with "
					+ ex.toString());
		}

		return id;

	}

	public Boolean deleteWish(int userId, int wishId) {
		try {
			int wishUserId = 0;

			String checkIfBelong = "SELECT user_id FROM " + tableName
					+ "  WHERE id = " + wishId;
			Statement stmnt = conn.createStatement();
			ResultSet rSet = stmnt.executeQuery(checkIfBelong);
			while (rSet.next()) {
				wishUserId = rSet.getInt("user_id");

			}
			if (userId != wishUserId) {
				LogFilter.log
						.error("Alert! userid try to delete someone else product");
				return false;
			}
			String SQL = "DELETE from " + tableName + " WHERE id = ?";

			PreparedStatement pre = conn.prepareStatement(SQL);
			pre.setInt(1, wishId);
			pre.executeUpdate();

		} catch (Exception ex) {
			System.out.println(ex);
			return false;
		}
		return true;
	}



}
