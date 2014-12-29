package com.edu.StudentShare.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.edu.StudentShare.LogFilter;

public class ProductDataJDBC implements ProductDataDAO{

	
	private String tablenName = null;
	Connection conn = null;
	
	@Override
	public int createNewProduct(ProductData prod) {
		try{
	String	SQL = "INSERT INTO" + tablenName + " (`productName`, `price`, `sellerId`, `quantity`, `soldCount`, `description`, `image_url`, `dateTime"
			+ " VALUES (?,?,?,?,?,?,?,?);";
	PreparedStatement pre= conn.prepareStatement(SQL);
	
	ResultSet set = pre.executeQuery();
		}
		catch( Exception ex)
		{
			LogFilter.log.error("Failed at createNewProduct with " + ex.toString());
		}
		
	return 0;
				
	}

	@Override
	public Boolean deleteProduct(int productId) {
		try {
			String SQL = "DELETE from " + tablenName + "WHERE id = ?";

			PreparedStatement pre = conn.prepareStatement(SQL);
			pre.setInt(1, productId);
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return null;
	}

	@Override
	public Boolean buyProduct(int sellerID, int product_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean updateProduct(int productId, ProductData prod) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public void createTable(String tableName) {
		try {

			Statement st = conn.createStatement();
			st.executeUpdate("create database if not exists my_db");
			st.executeUpdate("use my_db");
			String SQL = "CREATE TABLE IF NOT EXISTS "
					+ tableName
					+ "(id int NOT NULL AUTO_INCREMENT,`productName` VARCHAR(100) NOT NULL,`price` double NOT NULL,`sellerId` int NOT NULL,`quantity` int	,`soldCount`  int NOT NULL,`description`  VARCHAR(1000) NOT NULL,`image_url` VARCHAR(100) NOT NULL DEFAULT 'default image link',`description` VARCHAR(100) NOT NULL DEFAULT 'empty',`dateTime` DATE NOT NULL,  primary key (id)) ENGINE = InnoDB CHARACTER SET utf8 COLLATE utf8_bin;";
			st.execute(SQL);
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}
		System.out.println("Created the table=" + tableName);
		return;
		
	}

}
