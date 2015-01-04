package com.edu.StudentShare.Product;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.edu.StudentShare.DBConn;
import com.edu.StudentShare.LogFilter;
import com.edu.StudentShare.Redis.Products;
import com.edu.StudentShare.Transaction.Transac;
import com.edu.StudentShare.Transaction.TransactionData;
import com.edu.StudentShare.User.User;

public class ProductDataJDBC implements ProductDataDAO {

	private String tablenName = null;
	Connection conn = null;

	public ProductDataJDBC(String string) {
		conn = DBConn.getConnection();

		tablenName = string;

	}

	public List<Integer> searchProduct(String searchPattren) {
		List<Integer> list = null;
		try {

			list = new ArrayList<>();
			String SQL = "SELECT * FROM " + tablenName
					+ " WHERE productName LIKE '%" + searchPattren + "%' ;";
			Statement stmnt = conn.createStatement();
			ResultSet rSet = stmnt.executeQuery(SQL);

			// ResultSet rSet = stmnt.executeQuery();
			while (rSet.next()) {
				list.add(rSet.getInt("id"));
			}

		} catch (Exception ex) {
			LogFilter.log
					.error("Failed at searchProduct with " + ex.toString());
			return null;
		}
		return list;

	}

	public int createNewProduct(ProductData prod) {
		int id = 0;
		try {
			String SQL = "INSERT INTO "
					+ tablenName
					+ "  (`productName`, `price`, `sellerId`, `quantity`, `soldCount`, `description`, `image_url`, `dateTime`) "
					+ " VALUES (?,?,?,?,?,?,?,?);";
			PreparedStatement pre = conn.prepareStatement(SQL);

			pre.setString(1, prod.get_productName());
			pre.setDouble(2, prod.getPrice());
			pre.setInt(3, prod.get_seller_id());
			pre.setInt(4, prod.get_quntity());
			pre.setInt(5, prod.get_sold());
			pre.setString(6, prod.getDescription());
			pre.setString(7, prod.getImageUrl());
			pre.setDate(8, prod.getPostTime());
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

		Products.setProduct(id, prod);

		return id;

	}

	@Override
	public Boolean deleteProduct(int userid, int productId) {
		try {
			int sellerId = 0;
			String checkIfBelong = "SELECT sellerId FROM " + tablenName
					+ " WHERE id = " + productId;
			Statement stmnt = conn.createStatement();
			ResultSet rSet = stmnt.executeQuery(checkIfBelong);
			while (rSet.next()) {
				sellerId = rSet.getInt("sellerId");

			}
			if (userid != sellerId) {
				LogFilter.log
						.error("Alert! userid try to delete someone else product");
				return false;
			}
			String SQL = "DELETE from " + tablenName + " WHERE id = ?";
			// TODO: check if it the id of the user is the buyer

			PreparedStatement pre = conn.prepareStatement(SQL);
			pre.setInt(1, productId);
			pre.executeUpdate();

		} catch (Exception ex) {
			System.out.println(ex);
			return false;
		}
		return true;
	}

	private Boolean checkIfProductAvailableAndDescrease(int productId,
			ProductData ProdData) {
		try {
			if (ProdData.get_quntity() > 0) {
				String soldCountSql = "Update " + tablenName
						+ " SET  soldCount = soldCount + 1 Where id = ?";
				PreparedStatement stmntProduct = conn
						.prepareStatement(soldCountSql);
				stmntProduct.setInt(1, productId);
				stmntProduct.executeUpdate();
				String quntitySql = "Update " + tablenName
						+ " SET  quantity = quantity  - 1 Where id = ?";

				PreparedStatement quantitystmntProduct = conn
						.prepareStatement(quntitySql);
				quantitystmntProduct.setInt(1, productId);
				quantitystmntProduct.executeUpdate();

				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}

	@Override
	public int buyProduct(int buyerId, int product_id) {
		int transactionId = 0;
		try {
			String SQL = "Select * from " + tablenName + " where id = "
					+ product_id;
			Statement stmnt = conn.createStatement();
			ResultSet rSet = stmnt.executeQuery(SQL);
			ProductData ProdData = getProductFromResultSet(rSet);

			if (checkIfProductAvailableAndDescrease(product_id, ProdData)) {
				// Transaction part

				TransactionData TransData = new TransactionData(
						ProdData.get_seller_id(), buyerId, product_id,
						ProdData.getPrice(), ProdData.getPostTime());
				
				User.userJdbc.UpdateUserRank(buyerId, 0.1); 

				User.userJdbc.UpdateUserRank(ProdData.get_seller_id(), 1); 

				transactionId = Transac.transactionJdbc
						.makeTransaction(TransData);
				if (transactionId == 0) {
					System.out.println("Failed at product_id: " + product_id);
				}
				return transactionId;
			}

		} catch (Exception e) {
			System.out.println(e);
		}
		return 0;

	}

	@Override
	public Boolean updateProduct(int productId, ProductData prod) {
		String SQL = "";
		return null;
	}

	public void createTable() {
		try {

			Statement st = conn.createStatement();
			st.executeUpdate("create database if not exists my_db");
			st.executeUpdate("use my_db");
			String SQL = "CREATE TABLE IF NOT EXISTS "
					+ tablenName
					+ "(id int NOT NULL AUTO_INCREMENT,`productName` VARCHAR(100) NOT NULL,`price` double NOT NULL,`sellerId` int NOT NULL,`quantity` int	,`soldCount`  int NOT NULL,`description`  VARCHAR(1000) NOT NULL DEFAULT 'empty',`image_url` VARCHAR(100) NOT NULL DEFAULT 'default image link',`dateTime` DATE NOT NULL,  primary key (id)) ENGINE = InnoDB CHARACTER SET utf8 COLLATE utf8_bin;";
			st.execute(SQL);
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}
		System.out.println("Created the table=" + tablenName);
		return;

	}

	public ProductData getProductInfo(int id) {
		ProductData data = null;
		try {
			String SQL = "Select * FROM " + tablenName + " Where id = ? ;";
			PreparedStatement stmnt = conn.prepareStatement(SQL);
			stmnt.setInt(1, id);
			ResultSet rSet = stmnt.executeQuery();
			
				data = getProductFromResultSet(rSet);
			
		} catch (SQLException ex) {
			System.err.println( ex.getMessage());
		}
		return data;
	}

	private ProductData getProductFromResultSet(ResultSet rSet) {
		ProductData data = null;

		try {
			while (rSet.next()) {
				String productName = rSet.getString("productName");
				double price = rSet.getDouble("price");
				int soldCount = rSet.getInt("soldCount");
				String image_url = rSet.getString("image_url");
				Date date = rSet.getDate("dateTime");
				int sellerId = rSet.getInt("sellerId");
				String description = rSet.getString("description");
				int quantity = rSet.getInt("quantity");

				data = new ProductData(productName, price, sellerId, quantity,
						date, soldCount, description, image_url);
			}
			return data;

		} catch (SQLException e) {
			System.err.println(e.getMessage());

		}
		return data;

	}

	@Override
	public List<ProductData> getProductsByUser(int user_id) {

		List<ProductData> dataList = new ArrayList<ProductData>();

		try {
			String SQL = "Select * from " + tablenName
					+ " Where sellerId = ? ;";

			PreparedStatement stmnt = conn.prepareStatement(SQL);
			stmnt.setInt(1, user_id);
			ResultSet rSet = stmnt.executeQuery();

			dataList.add(getProductFromResultSet(rSet));

			return dataList;
		} catch (SQLException e) {
			System.err.println(e.getMessage());

		}
		return dataList;
	}

	public List<String> getSellers(){
		String SQL = "SELECT distinct users.username FROM users INNER JOIN product ON users.id=product.sellerId";
		
		List<String> dataList = null;
		try {
			dataList = new ArrayList<String>();
			Statement stmnt = conn.createStatement();

			ResultSet rSet = stmnt.executeQuery(SQL);
			while (rSet.next())
			{
				dataList.add(rSet.getString("username"));
			}
			
		} catch (SQLException err) {
			err.printStackTrace();
		}
		
		return dataList;
	}
}
