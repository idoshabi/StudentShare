package com.edu.StudentShare;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class BookDataJDBCTemplate implements BookDataDAO {

	Connection conn;
	String tableName = "books"; 

	public BookDataJDBCTemplate() {
		conn = DBConn.getConnection();
	}

	/**
	 * This is the method to be used to create a table for a specific location
	 * The name of the table is the name of the location
	 */
	public void createTable(String placeName) {
		
		/*
		 * Generete a table without data
		 */
		try {

			Statement st = conn.createStatement();
			st.executeUpdate("create database if not exists my_db");
			st.executeUpdate("use my_db");
			 String total;
				LogFilter.log.error(String.format("user %s password", tableName));
				String SQL = "CREATE TABLE IF NOT EXISTS "+tableName+" (`title` VARCHAR(100) NOT NULL, `author` VARCHAR(100) NOT NULL) ENGINE = InnoDB CHARACTER SET utf8 COLLATE utf8_bin;";

			st.executeUpdate(SQL);

		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}
		System.out.println("Created the table=" + tableName);
		return;
	}

	/**
	 * This is the method to be insert a row into the table
	 */
	public void insert(String title, String author) {
		
		
		// this is for selecting db incase we do insert first
		// ((DriverManagerDataSource)dataSource).setUrl("jdbc:mysql://localhost:3306/TestData");
		
		String SQL = "INSERT INTO "
				+ tableName
				+ " (`title`, `author`)"
					+ " VALUES (?,?);";
		System.out.println("insert SQL="+SQL);
		try {
			PreparedStatement preparedStatement = conn.prepareStatement(SQL);
			preparedStatement.setString(1, title);
			preparedStatement.setString(2, author);
			preparedStatement.executeUpdate();

			System.out.println("Created Record");
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}
		return;
	}

	/**
	 * This is the method to be used to list down all the records from the above
	 * table.
	 */
	public List<BookData> listBookDatas() {
		List<BookData> bookDataList = new ArrayList<BookData>();
		String SQL = "select * from " + tableName;
		try {
			Statement st = conn.createStatement();
			for (ResultSet rs = st.executeQuery(SQL); rs.next();) {
				bookDataList.add(mapRow(rs));
			}
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}

		return bookDataList;
	}

	private BookData mapRow(ResultSet rs) throws SQLException {
		BookData bookData = new BookData();
		bookData.setTitle(rs.getString("title"));
		bookData.setAuthor(rs.getString("author"));
		System.out.println("mapRow");

		return bookData;
	}

	/**
	 * This is the method to be used to delete a record from the above table
	 * corresponding to specific params
	 */
	public void delete(String title) {

		try {
			Statement st = conn.createStatement();

			String sql = "DELETE FROM " + tableName + " WHERE title = '"
					+ title + "\'";
			System.out.println("DELETE action, SQL=" + sql);
			int delete = st.executeUpdate(sql);
			if (delete == 1) {
				System.out.println("Row:title" + title
						+ " is deleted.");
			} else {
				System.out.println("Row:title" + title
						+ "is not deleted.");
			}
		} catch (SQLException e) {
			System.out.println("SQL statement: deleteing" + "title="
					+ title + "is not executed!");
			e.printStackTrace();
		}
	}

	
}
