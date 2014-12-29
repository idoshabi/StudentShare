package com.edu.StudentShare.WishList;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.edu.StudentShare.LogFilter;

public class WishDataJDBC implements WishDataDAO {

	private String tablenName = null;
	Connection conn = null;
	
	public void createTable(String tableName) {
		
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
				+tableName+""
				+ "(id int NOT NULL AUTO_INCREMENT,`user_id` int NOT NULL,`product_id` int NOT NULL,`date_time` DATE	,primary key (id)) ENGINE = InnoDB CHARACTER SET utf8 COLLATE utf8_bin;";

			st.executeUpdate(SQL);

		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}
		System.out.println("Created the table=" + tableName);
		return;
	}

	@Override
	public int createNewWish(WishData data) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Boolean deleteNewWish(int wishId) {
		// TODO Auto-generated method stub
		return null;
	}

}
